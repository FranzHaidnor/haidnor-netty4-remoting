package haidnor.remoting;


import haidnor.remoting.exception.RemotingCommandException;

public interface CommandCustomHeader {

    void checkFields() throws RemotingCommandException;

}
