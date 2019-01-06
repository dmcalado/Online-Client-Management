package pt.multicert.clientmanagement.unit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.EmptyNameException;
import pt.multicert.clientmanagement.exceptions.InvalidNIFException;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import java.util.List;

import static org.junit.Assert.*;

public class GetClientUnitTests extends BaseTests {

    @BeforeClass
    public static void createClients(){

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
    public void getClientByValidNIF(){

        DataLayer dbLayer = new DataLayer();
        ClientInfo client;

        try{
            client = dbLayer.getClient(NIF_OK);
            assertNotNull(client);
            assertEquals(CLIENT_NAME, client.getName()) ;
            assertEquals(NIF_OK, client.getNif());
            assertEquals(MORADA, client.getMorada());
            assertEquals(TELEFONE, client.getTelefone());

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }


    //TEST 2 - Get Client by an invalid NIF
    @Test(expected = InvalidNIFException.class)
    public void getClientByInvalidNIF() throws ClientManagementException{

        DataLayer dbLayer = new DataLayer();

        dbLayer.getClient(INVALID_NIF);

    }
    //TEST 3 - Get Client by its name
    @Test
    public void getClientByItsName(){

        DataLayer dbLayer = new DataLayer();
        List<ClientInfo> clients;
        ClientInfo client;

        try{
            clients = dbLayer.getClientsByName(CLIENT_NAME);

            assertNotNull(clients);
            assertEquals(2, clients.size());
            client = clients.get(0);
            assertEquals(CLIENT_NAME, client.getName()) ;
            assertEquals(NIF_OK, client.getNif());
            assertEquals(MORADA, client.getMorada());
            assertEquals(TELEFONE, client.getTelefone());

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }
    //TEST 4 - Get Client by an empty name
    @Test(expected = EmptyNameException.class)
    public void getClientByEmptyName()throws ClientManagementException{

        DataLayer dbLayer = new DataLayer();

        dbLayer.getClientsByName(EMPTY_NAME);

    }

    //TEST 5 - Get all clients
    @Test
    public void getAllClients(){

        DataLayer dbLayer = new DataLayer();
        List<ClientInfo> clients;
        ClientInfo client;

        try{
            clients = dbLayer.getAllClients();

            assertNotNull(clients);
            assertEquals(2, clients.size());
            client = clients.get(0);
            assertEquals(CLIENT_NAME, client.getName()) ;
            assertEquals(NIF_OK, client.getNif());
            assertEquals(MORADA, client.getMorada());
            assertEquals(TELEFONE, client.getTelefone());

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }

    }

    //TEST 6 - Client does not exist by name
    @Test
    public void getClientThatDoesnotExistByNIF(){

        DataLayer dbLayer = new DataLayer();
        ClientInfo client;

        try{
            client = dbLayer.getClient(NIF_OTHER2_OK);
            assertNull(client);

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }

    //TEST 7 - Client does not exist by name
    @Test
    public void getClientThatDoesnotExistByName(){

        DataLayer dbLayer = new DataLayer();
        List<ClientInfo> clients;

        try{
            clients = dbLayer.getClientsByName(CLIENT_NAME_DOES_NOT_EXIST);
            assertTrue(clients.size() == 0);

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }
}
