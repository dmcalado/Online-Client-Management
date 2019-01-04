package pt.multicert.clientmanagement.exceptions;

public class ClientDoesntExistException extends ClientManagementException {

    public ClientDoesntExistException(String message){
        super(message);
    }
}
