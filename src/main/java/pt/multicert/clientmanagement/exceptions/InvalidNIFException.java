package pt.multicert.clientmanagement.exceptions;

public class InvalidNIFException extends ClientManagementException {

    public InvalidNIFException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
