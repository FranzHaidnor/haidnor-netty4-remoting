package haidnor.remoting.client.spring.autoconfigure;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.RPCHook;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingChannelEventListener;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingRPCHook;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingRequestProcessor;
import haidnor.remoting.client.spring.common.util.CommandRegistrar;
import haidnor.remoting.core.NettyClientConfig;
import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.core.processor.NettyRequestProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class NettyRemotingClientFactoryBean implements FactoryBean<NettyRemotingClient> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ClientConfig config;

    @Override
    public NettyRemotingClient getObject() {
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        BeanUtils.copyProperties(config, nettyClientConfig);
        NettyRemotingClient client = new NettyRemotingClient(nettyClientConfig);
        ExecutorService executorService = Executors.newFixedThreadPool(config.getProcessorThreads());

        // NettyRequestProcessor ---------------------------------------------------------------------------------------
        String[] processorBeanNames = applicationContext.getBeanNamesForAnnotation(NettyRemotingRequestProcessor.class);
        CommandRegistrar commandRegistrar = new CommandRegistrar();
        for (String beanName : processorBeanNames) {
            Object bean = applicationContext.getBean(beanName);
            if (!(bean instanceof NettyRequestProcessor)) {
                throw new RuntimeException(beanName.getClass() + " is not of type NettyRequestProcessor");
            }
            NettyRemotingRequestProcessor processor = AnnotationUtils.findAnnotation(bean.getClass(), NettyRemotingRequestProcessor.class);

            assert processor != null;
            commandRegistrar.register(processor.value());
            client.registerProcessor(processor.value(), (NettyRequestProcessor) bean, executorService);
        }

        // RPCHook -----------------------------------------------------------------------------------------------------
        List<RPCHook> rpcHookList = new ArrayList<>();
        String[] hookBeanNames = applicationContext.getBeanNamesForAnnotation(NettyRemotingRPCHook.class);
        for (String beanName : hookBeanNames) {
            Object bean = applicationContext.getBean(beanName);
            if (!(bean instanceof RPCHook)) {
                throw new RuntimeException(beanName.getClass() + " is not of type RPCHook");
            }
            rpcHookList.add((RPCHook) bean);
        }

        rpcHookList.stream()
                .sorted(Comparator.comparingInt(o -> {
                    Order order = AnnotationUtils.findAnnotation(o.getClass(), Order.class);
                    return order != null ? order.value() : Integer.MAX_VALUE;
                }))
                .forEach(client::registerRPCHook);

        // ChannelEventListener ----------------------------------------------------------------------------------------
        String[] eventListenerBeanNames = applicationContext.getBeanNamesForAnnotation(NettyRemotingChannelEventListener.class);
        for (String beanName : eventListenerBeanNames) {
            Object bean = applicationContext.getBean(beanName);
            if (!(bean instanceof ChannelEventListener)) {
                throw new RuntimeException(beanName.getClass() + " is not of type ChannelEventListener");
            }
            if (client.getChannelEventListener() != null) {
                throw new RuntimeException("ChannelEventListener is already registered");
            }
            client.registerChannelEventListener((ChannelEventListener) bean);
        }

        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return NettyRemotingClient.class;
    }

}
