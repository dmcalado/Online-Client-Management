package pt.multicert.clientmanagement.exceptions;

public class EmptyNameException extends ClientManagementException {
    public EmptyNameException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
