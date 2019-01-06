package pt.multicert.clientmanagement.exceptions;

public class GetAllClientsException extends ClientManagementException {

    public GetAllClientsException(int errorCode, String message){
        super(errorCode, message);
    }
}
