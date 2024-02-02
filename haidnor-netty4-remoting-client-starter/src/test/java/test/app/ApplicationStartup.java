package test.app;

import haidnor.remoting.core.NettyRemotingClient;
import haidnor.remoting.protocol.RemotingCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationRunner {

    @Autowired
    private NettyRemotingClient client;

    @Override
    public void run(ApplicationArguments args) {
//        RemotingCommand request = RemotingCommand.creatJsonProtocolRequest("/user/login", new User(1L, "888888ABC123456"));
//        RemotingCommand response = client.invokeSync("127.0.0.1:8080", request);
//        System.out.println(Jackson.toBean(response.getBody(),String.class));

        // 创建请求命令
        RemotingCommand request = RemotingCommand.creatRequest("/test/echo", "Hello World".getBytes());
        // 发送请求
        RemotingCommand response = client.invokeSync("127.0.0.1:8080", request);
        // 打印服务端响应消息
        System.out.println(new String(response.getBody()));


//        client.invokeOneway("127.0.0.1:8080", request);

    }

}
