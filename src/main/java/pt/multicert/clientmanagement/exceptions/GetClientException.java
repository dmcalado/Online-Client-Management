package pt.multicert.clientmanagement.exceptions;

public class GetClientException extends ClientManagementException {

    public GetClientException(int errorCode, String message){
        super(errorCode, message);
    }
}
