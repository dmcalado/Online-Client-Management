package pt.multicert.clientmanagement.integration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.Client;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.AddClientException;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.GetAllClientsException;
import pt.multicert.clientmanagement.exceptions.GetClientException;
import pt.multicert.clientmanagement.services.ErrorCodes;
import pt.multicert.clientmanagement.services.ServicesLayer;
import pt.multicert.clientmanagement.unit.BaseTests;
import pt.multicert.clientmanagement.ws_objects.*;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class GetClientServiceTests extends BaseTests {

    @BeforeClass
    public static void createClient(){

        //create first client
        addClientSetup(CLIENT_NAME, NIF_OK, MORADA, TELEFONE);
        //create second client
        addClientSetup(CLIENT_NAME, NIF_OTHER_OK, MORADA, TELEFONE);
    }

    @AfterClass
    public static void rollbackAddClient(){

        //delete first client;
        deleteClientSetup(NIF_OK);
        //delete second client;
        deleteClientSetup(NIF_OTHER_OK);
    }

    //TEST 1 - Get Client its NIF

    @Test
    public void getClientByItsNIF(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        GetClientByNIFRequest request = new GetClientByNIFRequest();
        GetClientByNIFResponse response;
        ClientInfo client;

        request.setNif(NIF_OK);
        response = service.getClientByNIF(request);

        client = response.getClient();
        assertNotNull(client);
        assertEquals(NIF_OK, client.getNif());

        assertEquals(ErrorCodes.OK, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.OK_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 2 - Get Client by an invalid NIF
    @Test
    public void getClientUsingInvalidNIF(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        GetClientByNIFRequest request = new GetClientByNIFRequest();
        GetClientByNIFResponse response;

        request.setNif(INVALID_NIF);
        response = service.getClientByNIF(request);

        assertEquals(ErrorCodes.INVALID_NIF, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.INVALID_NIF_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 3 - Use a mocked Data Layer to simulate the hibernate exceptions
    @Test
    public void getClientByNIFMockedDataLayer(){
        DataLayer dbLayer = mock(DataLayer.class);
        ServicesLayer service;
        GetClientByNIFRequest request = new GetClientByNIFRequest();
        GetClientByNIFResponse response;

        try {
            doThrow(new GetClientException(ErrorCodes.GET_CLIENT_BY_NIF_FAILED, ErrorCodes.GET_CLIENT_BY_NIF_MESSAGE))
                    .when(dbLayer)
                    .getClient(NIF_OK);
        } catch (ClientManagementException ex) {
            fail("Received exception! Fail!");
        }

        service = new ServicesLayer(dbLayer);
        request.setNif(NIF_OK);

        response = service.getClientByNIF(request);

        assertEquals(ErrorCodes.GET_CLIENT_BY_NIF_FAILED, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.GET_CLIENT_BY_NIF_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 4 - Get Client by its name
    @Test
    public void getClientByItsName(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        GetClientByNameRequest request = new GetClientByNameRequest();
        GetClientByNameResponse response;
        ClientInfo client;

        request.setName(CLIENT_NAME);
        response = service.getClientByName(request);

        assertNotNull(response.getClient().get(0));
        client = response.getClient().get(0);
        assertTrue(client.getName().contains(CLIENT_NAME));

        assertEquals(ErrorCodes.OK, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.OK_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 5 - Get Client by an empty name
    @Test
    public void getClientByEmptyName(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        GetClientByNameRequest request = new GetClientByNameRequest();
        GetClientByNameResponse response;

        request.setName(EMPTY_NAME);
        response = service.getClientByName(request);

        assertEquals(ErrorCodes.EMPTY_NAME, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.EMPTY_NAME_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 6 - Use a mocked Data Layer to simulate the hibernate exceptions
    @Test
    public void getClientByNameMockedLayer(){
        DataLayer dbLayer = mock(DataLayer.class);
        ServicesLayer service;
        GetClientByNameRequest request = new GetClientByNameRequest();
        GetClientByNameResponse response;

        try {
            doThrow(new GetClientException(ErrorCodes.GET_CLIENT_BY_NAME_FAILED, ErrorCodes.GET_CLIENT_NY_NAME_MESSAGE))
                    .when(dbLayer)
                    .getClientsByName(CLIENT_NAME);
        } catch (ClientManagementException ex) {
            fail("Received exception! Fail!");
        }

        service = new ServicesLayer(dbLayer);
        request.setName(CLIENT_NAME);

        response = service.getClientByName(request);

        assertEquals(ErrorCodes.GET_CLIENT_BY_NAME_FAILED, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.GET_CLIENT_NY_NAME_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 7 - Get all clients
    @Test
    public void getAllClients(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        ListClientsRequest request = new ListClientsRequest();
        ListClientsResponse response;
        List<ClientInfo> clients;
        response = service.getAllClients(request);

        clients = response.getClient();

        assertNotNull(clients);
        assertTrue(clients.size() > 0);

        assertEquals(ErrorCodes.OK, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.OK_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 8 - Hibernate query failed for get all clients
    @Test
    public void getAllClientMockedDataLayer(){
        DataLayer dbLayer = mock(DataLayer.class);
        ServicesLayer service;
        ListClientsRequest request = new ListClientsRequest();
        ListClientsResponse response;

        try {
            doThrow(new GetAllClientsException(ErrorCodes.GET_ALL_CLIENTS_FAILED, ErrorCodes.GET_ALL_CLIENT_FAILED_MESSAGE))
                    .when(dbLayer)
                    .getAllClients();
        } catch (ClientManagementException ex) {
            fail("Received exception! Fail!");
        }

        service = new ServicesLayer(dbLayer);

        response = service.getAllClients(request);

        assertEquals(ErrorCodes.GET_ALL_CLIENTS_FAILED, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.GET_ALL_CLIENT_FAILED_MESSAGE, response.getResult().getErrorMessage());
    }
    //TEST 9 - Client does not exist by nif
    @Test
    public void getClientThatDoesnotExistByNIF(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        GetClientByNIFRequest request = new GetClientByNIFRequest();
        GetClientByNIFResponse response;
        ClientInfo client;

        request.setNif(NIF_OTHER2_OK);
        response = service.getClientByNIF(request);

        client = response.getClient();
        assertNull(client);

        assertEquals(ErrorCodes.CLIENT_NOT_FOUND, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.CLIENT_NOT_FOUND_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 10 - client does not exist by name
    @Test
    public void getClientThatDoesnotExistByName(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        GetClientByNameRequest request = new GetClientByNameRequest();
        GetClientByNameResponse response;

        request.setName(CLIENT_NAME_DOES_NOT_EXIST);
        response = service.getClientByName(request);

        assertNotNull(response.getClient());
        assertTrue(response.getClient().isEmpty());

        assertEquals(ErrorCodes.CLIENT_NOT_FOUND, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.CLIENT_NOT_FOUND_MESSAGE, response.getResult().getErrorMessage());
    }
}
