package pt.multicert.clientmanagement.exceptions;


public class DeleteClientException extends ClientManagementException {

    public DeleteClientException(int errorCode, String message){
        super(errorCode, message);
    }
}
