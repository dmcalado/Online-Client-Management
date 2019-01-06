package pt.multicert.clientmanagement.unit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.Client;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.EmptyNameException;
import pt.multicert.clientmanagement.exceptions.InvalidNIFException;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import java.util.List;

import static org.junit.Assert.*;
import static pt.multicert.clientmanagement.unit.UnitBaseTests.*;
import static pt.multicert.clientmanagement.unit.UnitBaseTests.TELEFONE;

public class GetClientUnitTests {

    @BeforeClass
    public static void createClient(){

        DataLayer dbLayer = new DataLayer();
        ClientInfo client = null;

        try{
            //create client
            client = new ClientInfo();
            client.setName(CLIENT_NAME);
            client.setNif(NIF_OK);
            client.setMorada(MORADA);
            client.setTelefone(TELEFONE);
            dbLayer.addClient(client);

            //create client
            client = new ClientInfo();
            client.setName(CLIENT_NAME);
            client.setNif(NIF_OTHER_OK);
            client.setMorada(MORADA);
            client.setTelefone(TELEFONE);
            dbLayer.addClient(client);
        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }

    @AfterClass
    public static void rollbackAddClient(){

        DataLayer dbLayer = new DataLayer();

        try{
            dbLayer.deleteClient(NIF_OK);
            dbLayer.deleteClient(NIF_OTHER_OK);
        }catch (Exception ex){
            fail("Received exception! Fail!");
        }
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

    @Test
    public void getClientThatDoesnotExist(){

        DataLayer dbLayer = new DataLayer();
        ClientInfo client;

        try{
            client = dbLayer.getClient(NIF_OTHER2_OK);
            assertNull(client);

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }
}
