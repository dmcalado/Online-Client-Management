package pt.multicert.clientmanagement.exceptions;

public class ClientAlreadyExistsException extends AddClientException {

    public ClientAlreadyExistsException (String message){
        super(message);
    }
}
