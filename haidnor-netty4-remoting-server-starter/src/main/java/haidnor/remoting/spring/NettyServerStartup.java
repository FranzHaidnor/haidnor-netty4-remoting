package haidnor.remoting.spring;

import haidnor.remoting.core.NettyRemotingServer;
import haidnor.remoting.core.NettyServerConfig;
import haidnor.remoting.core.processor.NettyRequestProcessor;
import haidnor.remoting.spring.annotation.NettyRemotingRequestProcessor;
import haidnor.remoting.spring.autoconfigure.ServerConfig;
import haidnor.remoting.spring.util.CommandRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class NettyServerStartup implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(NettyServerStartup.class);

    @Autowired
    private ServerConfig config;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) {
        NettyServerConfig nettyConfig = new NettyServerConfig();
        BeanUtils.copyProperties(config, nettyConfig);
        NettyRemotingServer server = new NettyRemotingServer(nettyConfig);

        ExecutorService executorService = Executors.newFixedThreadPool(nettyConfig.getServerProcessorMaxThreads());

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
        }

        // 钩子
//        server.registerRPCHook();

        // 监听器
//        server.registerChannelEventListener();

        server.start();
    }

}