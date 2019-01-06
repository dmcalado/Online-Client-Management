package pt.multicert.clientmanagement.unit;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientDoesntExistException;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.exceptions.InvalidNIFException;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import static org.junit.Assert.fail;
import static pt.multicert.clientmanagement.unit.BaseTests.*;

public class DeleteClientUnitTests extends BaseTests{

    @BeforeClass
    public static void createClient(){

        addClientSetup(CLIENT_NAME, NIF_OK, MORADA, TELEFONE);
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
