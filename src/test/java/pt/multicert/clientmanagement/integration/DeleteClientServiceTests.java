package pt.multicert.clientmanagement.integration;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.DeleteClientException;
import pt.multicert.clientmanagement.exceptions.GetAllClientsException;
import pt.multicert.clientmanagement.services.ErrorCodes;
import pt.multicert.clientmanagement.services.ServicesLayer;
import pt.multicert.clientmanagement.unit.BaseTests;
import pt.multicert.clientmanagement.ws_objects.ListClientsRequest;
import pt.multicert.clientmanagement.ws_objects.ListClientsResponse;
import pt.multicert.clientmanagement.ws_objects.RemoveClientRequest;
import pt.multicert.clientmanagement.ws_objects.RemoveClientResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class DeleteClientServiceTests extends BaseTests {

    @BeforeClass
    public static void createClient(){

        addClientSetup(CLIENT_NAME, NIF_OK, MORADA, TELEFONE);
    }

    //TEST 1 - delete client OK
    @Test
    public void deleteClient(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        RemoveClientRequest request = new RemoveClientRequest();
        RemoveClientResponse response;

        request.setNif(NIF_OK);
        response = service.removeClient(request);

        assertEquals(ErrorCodes.OK, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.OK_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 2 - delete invalid NIF
    @Test
    public void deleteClientWithInvalidNIF(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        RemoveClientRequest request = new RemoveClientRequest();
        RemoveClientResponse response;

        request.setNif(INVALID_NIF);
        response = service.removeClient(request);

        assertEquals(ErrorCodes.INVALID_NIF, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.INVALID_NIF_MESSAGE, response.getResult().getErrorMessage());
    }


    //TEST 3 - delete nif does not exist
    @Test
    public void deleteClientDoesNotExist(){
        ServicesLayer service = new ServicesLayer(new DataLayer());
        RemoveClientRequest request = new RemoveClientRequest();
        RemoveClientResponse response;

        request.setNif(NIF_OTHER2_OK);
        response = service.removeClient(request);

        assertEquals(ErrorCodes.CLIENT_NOT_FOUND, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.CLIENT_NOT_FOUND_MESSAGE, response.getResult().getErrorMessage());
    }

    //TEST 4 - simulate hibernate exception
    @Test
    public void deleteClientMockedDataLayer(){
        DataLayer dbLayer = mock(DataLayer.class);
        ServicesLayer service;
        RemoveClientRequest request = new RemoveClientRequest();
        RemoveClientResponse response;

        try {
            doThrow(new DeleteClientException(ErrorCodes.DELETE_CLIENT_FAILED, ErrorCodes.DELETE_CLIENT_FAILED_MESSAGE))
                    .when(dbLayer)
                    .deleteClient(NIF_OK);
        } catch (ClientManagementException ex) {
            fail("Received exception! Fail!");
        }

        service = new ServicesLayer(dbLayer);
        request.setNif(NIF_OK);
        response = service.removeClient(request);

        assertEquals(ErrorCodes.DELETE_CLIENT_FAILED, response.getResult().getErrorCode());
        assertEquals(ErrorCodes.DELETE_CLIENT_FAILED_MESSAGE, response.getResult().getErrorMessage());
    }
}
