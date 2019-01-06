package pt.multicert.clientmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private DataLayer dbLayer;

    @Autowired
    public ServicesLayer(DataLayer dataLayer){
        this.dbLayer = dataLayer;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "addClientRequest")
    @ResponsePayload
    public AddClientResponse addClient(@RequestPayload AddClientRequest request){

        ClientInfo clientInfo = null;
        AddClientResponse response = new AddClientResponse();
        ExecResult result = new ExecResult();

        response.setResult(result);

        clientInfo = request.getClientInfo();


        try{
            this.dbLayer.addClient(clientInfo);
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage(ErrorCodes.OK_MESSAGE);
        }
        catch (ClientManagementException ex)
        {
            System.err.println("Failed to add a new client " + ex.getMessage());
            response.getResult().setErrorCode(ex.getErrorCode());
            response.getResult().setErrorMessage(ex.getErrorMessage());
        }
        //going to store client into database
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getClientByNameRequest")
    @ResponsePayload
    public GetClientByNameResponse getClientByName(@RequestPayload GetClientByNameRequest request){

        GetClientByNameResponse response = new GetClientByNameResponse();
        ExecResult result = new ExecResult();
        List<ClientInfo> clients = null;


        response.setResult(result);

        try{
            clients = this.dbLayer.getClientsByName(request.getName());
            for (ClientInfo client : clients){
                response.getClient().add(client);
            }
            if (clients.isEmpty()){
                response.getResult().setErrorCode(ErrorCodes.CLIENT_NOT_FOUND);
                response.getResult().setErrorMessage(ErrorCodes.CLIENT_NOT_FOUND_MESSAGE);
            }
            else{
                response.getResult().setErrorCode(ErrorCodes.OK);
                response.getResult().setErrorMessage(ErrorCodes.OK_MESSAGE);
            }

        }
        catch (ClientManagementException ex)
        {
            response.getResult().setErrorCode(ex.getErrorCode());
            response.getResult().setErrorMessage(ex.getErrorMessage());
        }
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "getClientByNIFRequest")
    @ResponsePayload
    public GetClientByNIFResponse getClientByNIF(@RequestPayload GetClientByNIFRequest request){

        GetClientByNIFResponse response = new GetClientByNIFResponse();
        ExecResult result = new ExecResult();
        ClientInfo client = null;

        response.setResult(result);

        try{
            client = this.dbLayer.getClient(request.getNif());
            response.setClient(client);
            if (client == null){
                response.getResult().setErrorCode(ErrorCodes.CLIENT_NOT_FOUND);
                response.getResult().setErrorMessage(ErrorCodes.CLIENT_NOT_FOUND_MESSAGE);
            }
            else{
                response.getResult().setErrorCode(ErrorCodes.OK);
                response.getResult().setErrorMessage(ErrorCodes.OK_MESSAGE);
            }
        }
        catch (ClientManagementException ex)
        {
            response.getResult().setErrorCode(ex.getErrorCode());
            response.getResult().setErrorMessage(ex.getErrorMessage());
        }
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "listClientsRequest")
    @ResponsePayload
    public ListClientsResponse getAllClients(@RequestPayload ListClientsRequest request){

        ListClientsResponse response = new ListClientsResponse();
        ExecResult result = new ExecResult();
        List<ClientInfo> clients = null;


        response.setResult(result);

        try{
            clients = this.dbLayer.getAllClients();
            for (ClientInfo client : clients){
                response.getClient().add(client);
            }
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage(ErrorCodes.OK_MESSAGE);
        }
        catch (GetAllClientsException ex)
        {
            System.err.println("Failed to get all clients ");
            response.getResult().setErrorCode(ex.getErrorCode());
            response.getResult().setErrorMessage(ex.getErrorMessage());
        }
        return  response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "removeClientRequest")
    @ResponsePayload
    public RemoveClientResponse removeClient(@RequestPayload RemoveClientRequest request){

        RemoveClientResponse response = new RemoveClientResponse();
        ExecResult result = new ExecResult();


        response.setResult(result);

        try{
            this.dbLayer.deleteClient(request.getNif());
            response.getResult().setErrorCode(ErrorCodes.OK);
            response.getResult().setErrorMessage(ErrorCodes.OK_MESSAGE);
        }
        catch (ClientManagementException ex){
            response.getResult().setErrorCode(ex.getErrorCode());
            response.getResult().setErrorMessage(ex.getErrorMessage());
        }
        return  response;
    }

    
}
