package pt.multicert.clientmanagement.services;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.*;
import pt.multicert.clientmanagement.ws_objects.*;

import java.util.List;

@Endpoint
public class ServicesLayer {
    private static final String NAMESPACE = "http://multicert.pt/clientmanagement";

    private DataLayer dbLayer = new DataLayer();

    @PayloadRoot(namespace = NAMESPACE, localPart = "addClientRequest")
    @ResponsePayload
    public AddClientResponse AddClient(@RequestPayload AddClientRequest request){

        ClientInfo clientInfo = null;
        AddClientResponse response = new AddClientResponse();
        ExecResult result = new ExecResult();

        response.setResult(result);

        clientInfo = request.getClientInfo();

        //validating info before send to db
        if (clientInfo.getNif() <= 0){
            response.getResult().setErrorCode(ErrorCodes.INVALID_NIF);
            response.getResult().setErrorMessage("Nif inválido");
            return response;
        }

        if (clientInfo.getName() == null || (clientInfo.getName().isEmpty())){
            response.getResult().setErrorCode(ErrorCodes.EMPTY_NAME);
            response.getResult().setErrorMessage("Nome vazio");
            return response;
        }
        try{
            this.dbLayer.AddClient(clientInfo);
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage("Success!");
        }
        catch (AddClientException ex)
        {
            System.err.println("Failed to add a new client " + ex.getMessage());
            response.getResult().setErrorCode(ErrorCodes.ADD_CLIENT_FAILED);
            response.getResult().setErrorMessage(ex.getMessage());
        }
        //going to store client into database
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getClientByNameRequest")
    @ResponsePayload
    public GetClientByNameResponse GetClientByName(@RequestPayload GetClientByNameRequest request){

        GetClientByNameResponse response = new GetClientByNameResponse();
        ExecResult result = new ExecResult();
        List<ClientInfo> clients = null;


        response.setResult(result);

        //validate the input
        if (request.getName() == null || (request.getName().isEmpty())){
            response.getResult().setErrorCode(ErrorCodes.EMPTY_NAME);
            response.getResult().setErrorMessage("Nome vazio");
            return response;
        }
        try{
            clients = this.dbLayer.GetClientsByName(request.getName());
            for (ClientInfo client : clients){
                response.getClient().add(client);
            }
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage("Success!");
        }
        catch (GetClientException ex)
        {
            System.err.println("Failed to get client ");
            response.getResult().setErrorCode(ErrorCodes.GET_CLIENT_BY_NAME_FAILED);
            response.getResult().setErrorMessage(ex.getMessage());
        }
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getClientByNIFRequest")
    @ResponsePayload
    public GetClientByNIFResponse GetClientByNIF(@RequestPayload GetClientByNIFRequest request){

        GetClientByNIFResponse response = new GetClientByNIFResponse();
        ExecResult result = new ExecResult();
        ClientInfo client = null;

        response.setResult(result);

        //validate the input
        if (request.getNif() <= 0){
            //validating info before send it to db
            response.getResult().setErrorCode(ErrorCodes.INVALID_NIF);
            response.getResult().setErrorMessage("Invalid Nif.");
            return response;
        }
        try{
            client = this.dbLayer.GetClient(request.getNif());
            response.setClient(client);
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage("Success!");
        }
        catch (GetClientException ex)
        {
            System.err.println("Failed to get client ");
            response.getResult().setErrorCode(ErrorCodes.GET_CLIENT_BY_NAME_FAILED);
            response.getResult().setErrorMessage(ex.getMessage());
        }
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "listClientsRequest")
    @ResponsePayload
    public ListClientsResponse GetAllClients(@RequestPayload ListClientsRequest request){

        ListClientsResponse response = new ListClientsResponse();
        ExecResult result = new ExecResult();
        List<ClientInfo> clients = null;


        response.setResult(result);

        try{
            clients = this.dbLayer.GetAllClients();
            for (ClientInfo client : clients){
                response.getClient().add(client);
            }
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage("Success!");
        }
        catch (GetAllClientsException ex)
        {
            System.err.println("Failed to get all clients ");
            response.getResult().setErrorCode(ErrorCodes.GET_ALL_CLIENTS_FAILED);
            response.getResult().setErrorMessage(ex.getMessage());
        }
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "removeClientRequest")
    @ResponsePayload
    public RemoveClientResponse RemoveClient(@RequestPayload RemoveClientRequest request){

        RemoveClientResponse response = new RemoveClientResponse();
        ExecResult result = new ExecResult();


        response.setResult(result);

        try{
            if (request.getNif() <= 0){
                //validating info before send it to db
                response.getResult().setErrorCode(ErrorCodes.INVALID_NIF);
                response.getResult().setErrorMessage("Invalid Nif.");
                return response;
            }

            this.dbLayer.DeleteClient(request.getNif());
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage("Success!");
        }
        catch (ClientDoesntExistException ex){
            System.err.println("Client not found!");
            response.getResult().setErrorCode(ErrorCodes.CLIENT_NOT_FOUND);
            response.getResult().setErrorMessage(ex.getMessage());
        }
        catch (DeleteClientException ex){
            System.err.println("Failed to delete client ");
            response.getResult().setErrorCode(ErrorCodes.DELETE_CLIENT_FAILED);
            response.getResult().setErrorMessage(ex.getMessage());
        }
        return  response;
    }

    public class ErrorCodes{
        public static final int INVALID_NIF = -10;
        public static final int EMPTY_NAME = -11;
        public static final int ADD_CLIENT_FAILED = -12;
        public static final int GET_CLIENT_BY_NAME_FAILED = -13;
        public static final int GET_ALL_CLIENTS_FAILED = -14;
        public static final int DELETE_CLIENT_FAILED = -15;
        public static final int CLIENT_NOT_FOUND = -16;
        public static final int OK = 1;
    }
    
}
