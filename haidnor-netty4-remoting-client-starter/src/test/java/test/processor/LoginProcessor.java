package test.processor;

import haidnor.remoting.client.spring.common.annotation.NettyRemotingRequestProcessor;
import haidnor.remoting.client.spring.common.processor.JsonProtocolNettyRequestProcessorAdapter;
import org.springframework.stereotype.Component;
import test.model.User;

@NettyRemotingRequestProcessor("/user/login")
@Component
public class LoginProcessor extends JsonProtocolNettyRequestProcessorAdapter<User> {

    @Override
    public Object processRequest(User user) {
        System.out.println(user);
        return "OK";
    }

}
