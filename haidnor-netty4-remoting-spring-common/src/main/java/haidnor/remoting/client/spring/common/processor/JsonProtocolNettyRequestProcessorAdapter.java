package haidnor.remoting.client.spring.common.processor;

import haidnor.remoting.core.processor.NettyRequestProcessor;
import haidnor.remoting.protocol.RemotingCommand;
import haidnor.remoting.protocol.RemotingSysResponseCode;
import haidnor.remoting.util.Jackson;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Common remoting command processor
 */
public abstract class JsonProtocolNettyRequestProcessorAdapter<T> implements NettyRequestProcessor {

    private Class<? extends T> paramType = null;

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
        if (paramType == null) {
            paramType = (Class<? extends T>) getTypeVariableByName(this.getClass(), "T");
        }
        try {
            Object result = processRequest(Jackson.toBean(request.getBody(), paramType));
            if (result == null) {
                return RemotingCommand.createResponse(RemotingSysResponseCode.SUCCESS);
            } else {
                return RemotingCommand.createJsonProtocolResponse(RemotingSysResponseCode.SUCCESS, result);
            }
        } catch (Exception exception) {
            return RemotingCommand.createJsonProtocolResponse(RemotingSysResponseCode.SYSTEM_ERROR, exception.getMessage());
        }
    }

    public abstract Object processRequest(T request);

    private static Class<?> getTypeVariableByName(Class<?> clazz, String name) {
        Map<TypeVariable, Type> typeVariableMap = GenericTypeResolver.getTypeVariableMap(clazz);
        for (TypeVariable typeVariable : typeVariableMap.keySet()) {
            Type type = typeVariableMap.get(typeVariable);
            if (typeVariable.getName().equals(name)) {
                return (Class<?>) type;
            }
        }
        throw new RuntimeException("not found generic Type");
    }


}
