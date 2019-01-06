package pt.multicert.clientmanagement.unit;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientDoesntExistException;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.InvalidNIFException;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static pt.multicert.clientmanagement.unit.UnitBaseTests.*;
import static pt.multicert.clientmanagement.unit.UnitBaseTests.TELEFONE;

public class DeleteClientUnitTests {

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

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }

    @Test
    public void deleteClientOK(){

        DataLayer dbLayer = new DataLayer();

        try{
            dbLayer.deleteClient(NIF_OK);

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }

    @Test(expected = InvalidNIFException.class)
    public void deleteInvalidNIF() throws ClientManagementException{

        DataLayer dbLayer = new DataLayer();

        dbLayer.deleteClient(INVALID_NIF);


    }

    @Test(expected = ClientDoesntExistException.class)
    public void deleteNIFDoesNotExist() throws ClientManagementException{

        DataLayer dbLayer = new DataLayer();

        dbLayer.deleteClient(NIF_OTHER_OK);

    }
}
