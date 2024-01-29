package haidnor.remoting.client.spring.autoconfigure;

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
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component("nettyRemotingClient")
public class NettyRemotingClientFactory implements FactoryBean<NettyRemotingClient> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ClientConfig clientConfig;

    @Override
    public NettyRemotingClient getObject() {
        NettyClientConfig config = new NettyClientConfig();
        BeanUtils.copyProperties(clientConfig, config);
        NettyRemotingClient client = new NettyRemotingClient(config);
        ExecutorService executorService = Executors.newFixedThreadPool(clientConfig.getProcessorThreads());

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

        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return NettyRemotingClient.class;
    }

}
