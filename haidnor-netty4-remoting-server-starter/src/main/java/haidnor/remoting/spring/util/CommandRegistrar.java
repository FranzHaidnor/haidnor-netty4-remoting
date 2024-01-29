package haidnor.remoting.spring.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 处理器接口路径注册器
 */
public class CommandRegistrar {

    private final Set<Integer> set = new HashSet<>();

    public void register(String command) {
        int hashCode = command.hashCode();
        boolean containsKey = set.contains(hashCode);
        if (containsKey) {
            throw new Error("already exists '" + command + "' command hashCode");
        }
        set.add(hashCode);
    }

}
