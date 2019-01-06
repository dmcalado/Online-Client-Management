package pt.multicert.clientmanagement.exceptions;

public class ClientAlreadyExistsException extends AddClientException {

    public ClientAlreadyExistsException (int errorCode, String message){
        super(errorCode, message);
    }
}
