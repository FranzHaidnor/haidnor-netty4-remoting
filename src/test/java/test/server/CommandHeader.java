package test.server;

import haidnor.remoting.CommandCustomHeader;
import haidnor.remoting.exception.RemotingCommandException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandHeader implements CommandCustomHeader {

    private String name;

    private Integer age;

    @Override
    public void checkFields() throws RemotingCommandException {

    }

}
