package pt.multicert.clientmanagement.exceptions;

public class ClientDoesntExistException extends ClientManagementException {

    public ClientDoesntExistException(int errorCode, String message){
        super(errorCode, message);
    }
}
