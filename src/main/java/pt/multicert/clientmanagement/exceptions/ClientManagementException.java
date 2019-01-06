package pt.multicert.clientmanagement.exceptions;

public class ClientManagementException extends Exception {

    private int errorCode = -1;
    private String errorMessage;

    public ClientManagementException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
