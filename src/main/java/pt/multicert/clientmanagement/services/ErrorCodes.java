package pt.multicert.clientmanagement.services;


public class ErrorCodes{
    public static final int INVALID_NIF = -10;
    public static final String INVALID_NIF_MESSAGE ="Invalid NIF.";

    public static final int EMPTY_NAME = -11;
    public static final String EMPTY_NAME_MESSAGE = "Empty Name";

    public static final int CLIENT_ALREADY_EXISTS = -17;
    public static final String CLIENT_ALREADY_EXISTS_MESSAGE = "Client already exists";

    //generic error codes
    public static final int ADD_CLIENT_FAILED = -12;
    public static final String ADD_CLIENT_FAILED_MESSAGE = "Cannot add client to system!";
    public static final int GET_CLIENT_BY_NIF_FAILED = -18;
    public static final String GET_CLIENT_BY_NIF_MESSAGE = "Cannot get client by its NIF!";
    public static final int GET_CLIENT_BY_NAME_FAILED = -13;
    public static final String GET_CLIENT_NY_NAME_MESSAGE = "Cannot get client by its name!";
    public static final int GET_ALL_CLIENTS_FAILED = -14;
    public static final String GET_ALL_CLIENT_FAILED_MESSAGE = "Cannot get all clients";
    public static final int DELETE_CLIENT_FAILED = -15;
    public static final String DELETE_CLIENT_FAILED_MESSAGE = "Failed to delete client!";

    public static final int CLIENT_NOT_FOUND = -16;
    public static final String CLIENT_NOT_FOUND_MESSAGE = "Client does not exist!";
    public static final int OK = 1;
}