package pt.multicert.clientmanagement.integration;

import org.junit.AfterClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.AddClientException;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.services.ErrorCodes;
import pt.multicert.clientmanagement.services.ServicesLayer;
import pt.multicert.clientmanagement.unit.BaseTests;
import pt.multicert.clientmanagement.ws_objects.AddClientRequest;
import pt.multicert.clientmanagement.ws_objects.AddClientResponse;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class AddClientServiceTests extends BaseTests {

    @AfterClass
    public static void rollbackAddClient(){

        DataLayer dbLayer = new DataLayer();

        try{
            dbLayer.deleteClient(NIF_OK);
        }catch (Exception ex){
            fail("Received exception! Fail!");
        }
    }


    //Test 1 - Add a valid client
    @Test
    public void addClientSuccess(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        AddClientRequest request = new AddClientRequest();
        AddClientResponse response;
        ClientInfo clientInfo = null;

        clientInfo = getClientInfo(CLIENT_NAME, NIF_OK, MORADA, TELEFONE);

        request.setClientInfo(clientInfo);
        response = service.addClient(request);

        assertEquals(ErrorCodes.OK, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.OK_MESSAGE, response.getResult().getErrorMessage());

    }

    //Test 2 - Add an invalid client with empty name
    @Test
    public void addClientEmptyName(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        AddClientRequest request = new AddClientRequest();
        AddClientResponse response;
        ClientInfo clientInfo;

        clientInfo = getClientInfo(EMPTY_NAME, NIF_OK, MORADA, TELEFONE);

        request.setClientInfo(clientInfo);
        response = service.addClient(request);

        assertEquals(ErrorCodes.EMPTY_NAME, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.EMPTY_NAME_MESSAGE, response.getResult().getErrorMessage());

    }

    //Test 3 - Add an invalid client with an invalid NIF
    @Test
    public void addClientInvalidNIF(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        AddClientRequest request = new AddClientRequest();
        AddClientResponse response;
        ClientInfo clientInfo;

        clientInfo = getClientInfo(CLIENT_NAME, INVALID_NIF, MORADA, TELEFONE);

        request.setClientInfo(clientInfo);
        response = service.addClient(request);

        assertEquals(ErrorCodes.INVALID_NIF, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.INVALID_NIF_MESSAGE, response.getResult().getErrorMessage());

    }

    //Test 4 - Use a Data Layer mocked object
    @Test
    public void addClientMockedDataLayer(){

        DataLayer dbLayer = mock(DataLayer.class);
        ServicesLayer service;
        AddClientRequest request = new AddClientRequest();
        AddClientResponse response;
        ClientInfo clientInfo;

        clientInfo = getClientInfo(CLIENT_NAME, NIF_OK, MORADA, TELEFONE);

        try {
            doThrow(new AddClientException(ErrorCodes.ADD_CLIENT_FAILED, ErrorCodes.ADD_CLIENT_FAILED_MESSAGE))
                    .when(dbLayer)
                    .addClient(clientInfo);
        } catch (ClientManagementException ex) {
            fail("Received exception! Fail!");
        }

        service = new ServicesLayer(dbLayer);
        request.setClientInfo(clientInfo);

        response = service.addClient(request);

        assertEquals(ErrorCodes.ADD_CLIENT_FAILED, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.ADD_CLIENT_FAILED_MESSAGE, response.getResult().getErrorMessage());
    }
}
