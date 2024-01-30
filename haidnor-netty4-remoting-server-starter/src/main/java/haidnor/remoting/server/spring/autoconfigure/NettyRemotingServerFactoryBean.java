package haidnor.remoting.server.spring.autoconfigure;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.RPCHook;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingChannelEventListener;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingRPCHook;
import haidnor.remoting.client.spring.common.annotation.NettyRemotingRequestProcessor;
import haidnor.remoting.client.spring.common.util.CommandRegistrar;
import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.core.processor.NettyRequestProcessor;
import haidnor.remoting.server.spring.NettyServerStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class NettyRemotingServerFactoryBean implements FactoryBean<NettyRemotingServer> {

    private static final Logger log = LoggerFactory.getLogger(NettyServerStartup.class);

    @Autowired
    private ServerConfig config;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public NettyRemotingServer getObject() {
        NettyServerConfig nettyServerConfig = new NettyServerConfig();
        BeanUtils.copyProperties(config, nettyServerConfig);
        NettyRemotingServer server = new NettyRemotingServer(nettyServerConfig);

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
            server.registerProcessor(processor.value(), (NettyRequestProcessor) bean, executorService);
            log.debug("register netty remoting request processor {} , command {}", processor.value(), processor.value());
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
        rpcHookList = rpcHookList.stream()
                .sorted(Comparator.comparingInt(o -> Objects.requireNonNull(AnnotationUtils.findAnnotation(o.getClass(), NettyRemotingRPCHook.class)).order()))
                .collect(Collectors.toList());

        for (RPCHook rpcHook : rpcHookList) {
            server.registerRPCHook(rpcHook);
        }

        // ChannelEventListener ----------------------------------------------------------------------------------------
        String[] eventListenerBeanNames = applicationContext.getBeanNamesForAnnotation(NettyRemotingChannelEventListener.class);
        for (String beanName : eventListenerBeanNames) {
            Object bean = applicationContext.getBean(beanName);
            if (!(bean instanceof ChannelEventListener)) {
                throw new RuntimeException(beanName.getClass() + " is not of type ChannelEventListener");
            }
            if (server.getChannelEventListener() != null) {
                throw new RuntimeException("ChannelEventListener is already registered");
            }
            server.registerChannelEventListener((ChannelEventListener) bean);
        }

        return server;
    }

    @Override
    public Class<?> getObjectType() {
        return NettyRemotingServer.class;
    }

}
