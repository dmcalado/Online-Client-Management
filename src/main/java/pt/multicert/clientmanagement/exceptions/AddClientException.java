package pt.multicert.clientmanagement.exceptions;

public class AddClientException extends ClientManagementException{

    public AddClientException(int errorCode, String message){
        super(errorCode, message);
    }
}
