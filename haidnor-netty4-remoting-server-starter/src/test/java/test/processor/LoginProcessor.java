package test.processor;

import haidnor.remoting.spring.annotation.NettyRemotingRequestProcessor;
import haidnor.remoting.spring.processor.JsonProtocolNettyRequestProcessorAdapter;
import test.model.User;
import org.springframework.stereotype.Component;

@NettyRemotingRequestProcessor("/user/login")
@Component
public class LoginProcessor extends JsonProtocolNettyRequestProcessorAdapter<User> {

    @Override
    public Object processRequest(User user) {
        System.out.println(user);
        return "OK";
    }

}
