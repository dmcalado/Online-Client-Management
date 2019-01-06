package pt.multicert.clientmanagement.unit;

import pt.multicert.clientmanagement.data.DataLayer;
import pt.multicert.clientmanagement.exceptions.ClientManagementException;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import static org.junit.Assert.fail;

public class BaseTests {

    protected static final String CLIENT_NAME = "UNIT Exist";
    protected  static final String CLIENT_NAME_DOES_NOT_EXIST = "UNIT Does Not Exist";
    protected static final int NIF_OK = 123234242;
    protected static final int NIF_OTHER_OK = 123849284;
    protected static final int NIF_OTHER2_OK = 123847019;
    protected static final String MORADA = "Lisboa";
    protected static final int TELEFONE = 987654321;
    protected static final int INVALID_NIF = -123452;
    protected static final String EMPTY_NAME = "";

    public ClientInfo getClientInfo(String name, int nif, String morada, int telefone){
        ClientInfo client = new ClientInfo();

        client.setName(name);
        client.setNif(nif);
        client.setMorada(morada);
        client.setTelefone(telefone);
        return client;
    }

    public static void addClientSetup(String name, int nif, String morada, int telefone){
        DataLayer dbLayer = new DataLayer();
        ClientInfo client = null;

        try{
            //create client
            client = new ClientInfo();
            client.setName(name);
            client.setNif(nif);
            client.setMorada(morada);
            client.setTelefone(telefone);
            dbLayer.addClient(client);

        }catch (ClientManagementException ex){
            fail("Received exception! Fail!");
        }
    }

    public static void deleteClientSetup(int nif){
        DataLayer dbLayer = new DataLayer();

        try{
            dbLayer.deleteClient(nif);
        }catch (Exception ex){
            fail("Received exception! Fail!");
        }
    }
}
