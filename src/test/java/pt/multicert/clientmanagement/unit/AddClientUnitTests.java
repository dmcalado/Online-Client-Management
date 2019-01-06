package pt.multicert.clientmanagement.unit;

import org.junit.AfterClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientAlreadyExistsException;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.EmptyNameException;
import pt.multicert.clientmanagement.exceptions.InvalidNIFException;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import static org.junit.Assert.fail;
import static pt.multicert.clientmanagement.unit.BaseTests.*;

public class AddClientUnitTests {



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
    public void addClientSuccess() throws ClientManagementException{
        DataLayer dbLayer = new DataLayer();
        ClientInfo client = null;

        try{
            //create client1
            client = new ClientInfo();
            client.setName(CLIENT_NAME);
            client.setNif(NIF_OK);
            client.setMorada(MORADA);
            client.setTelefone(TELEFONE);
            dbLayer.addClient(client);
        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }

    //Test 2 - Add an invalid client with empty name
    @Test(expected= EmptyNameException.class)
    public void addClientEmptyName() throws ClientManagementException{
        DataLayer dbLayer = new DataLayer();
        ClientInfo client = null;

        //create client1
        client = new ClientInfo();
        client.setName(EMPTY_NAME);
        client.setNif(NIF_OTHER_OK);
        client.setMorada(MORADA);
        client.setTelefone(TELEFONE);
        dbLayer.addClient(client);

        fail("Expected AddClientException");

    }

    //Test 3 - Add an invalid client with an invalid NIF
    @Test(expected = InvalidNIFException.class)
    public void addClientInvalidNIF() throws ClientManagementException {
        DataLayer dbLayer = new DataLayer();
        ClientInfo client = null;

        //create client1
        client = new ClientInfo();
        client.setName(CLIENT_NAME);
        client.setNif(INVALID_NIF);
        client.setMorada(MORADA);
        client.setTelefone(TELEFONE);
        dbLayer.addClient(client);

        fail("Expected AddClientException");
    }

    //Test 4 - Add a duplicated client
    @Test(expected = ClientAlreadyExistsException.class)
    public void clientAlreadyExists() throws ClientManagementException{

        ClientInfo client;
        DataLayer dbLayer = new DataLayer();

        //create client1
        client = new ClientInfo();
        client.setName(CLIENT_NAME);
        client.setNif(NIF_OK);
        client.setMorada(MORADA);
        client.setTelefone(TELEFONE);
        dbLayer.addClient(client);
        fail("Expected ClientAlreadyExistsException!");

    }
}
