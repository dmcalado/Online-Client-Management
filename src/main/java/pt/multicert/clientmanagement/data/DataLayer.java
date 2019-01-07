package pt.multicert.clientmanagement.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import pt.multicert.clientmanagement.exceptions.*;
import pt.multicert.clientmanagement.services.ErrorCodes;
import pt.multicert.clientmanagement.ws_objects.ClientInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Component
public class DataLayer {
    private static SessionFactory factory;

    public DataLayer(){
        Configuration config = null;
        try{
            config = new Configuration();
            config.addResource("Client.hbm.xml");
            this.factory = config.configure().buildSessionFactory();
            System.err.println("Factory built!");
        }
        catch (Exception ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
        }
    }

    //Add a new client
    public void addClient(ClientInfo clientInfo) throws ClientManagementException {

        Client client = null;
        try{
            //validating info before send to db
            validateNIF(clientInfo.getNif());
            validateName(clientInfo.getName());

            client = new Client(clientInfo.getNif(), clientInfo.getName(), clientInfo.getMorada(), clientInfo.getTelefone());
            if (getClientAux(client.getNif()) == null){
                //if the client does not exist, add it to DB.
                addClientAux(client);
                return;
            }
            throw new ClientAlreadyExistsException(ErrorCodes.CLIENT_ALREADY_EXISTS, ErrorCodes.CLIENT_ALREADY_EXISTS_MESSAGE);
        }
        catch (ClientAlreadyExistsException ex){
            throw ex;
        }
        catch (InvalidNIFException ex){
            throw ex;
        }
        catch (EmptyNameException ex){
            throw ex;
        }
        catch (ClientManagementException ex){
            System.err.println(ex.getMessage());
            throw new AddClientException(ex.getErrorCode(), ex.getErrorMessage());
        }

    }



    private void addClientAux(Client client) throws AddClientException{
        Session session = null;
        Transaction tx = null;

        try {
            System.out.println("Before open session!");
            session = factory.openSession();
            tx = session.beginTransaction();
            System.out.println("Before save object!");
            session.save(client);
            tx.commit();

        } catch (Exception ex) {
            if (tx!=null) tx.rollback();
            System.err.println("Failed to add a new client." + ex.getMessage());
            throw new AddClientException(ErrorCodes.ADD_CLIENT_FAILED, ErrorCodes.ADD_CLIENT_FAILED_MESSAGE);

        } finally {
            session.close();
        }
    }

    public ClientInfo getClient(int clientNif)throws ClientManagementException{
        ClientInfo client;

        client = getClientAux(clientNif);
        if (client == null)
            throw new ClientDoesntExistException(ErrorCodes.CLIENT_NOT_FOUND, ErrorCodes.CLIENT_NOT_FOUND_MESSAGE);

        return client;
    }

    private ClientInfo getClientAux(int client_nif) throws ClientManagementException {
        Session session = null;
        Client client = null;

        try {
            validateNIF(client_nif);
            session = factory.openSession();
            client = (Client)session.get(Client.class, client_nif);
            return getClientInfoFromClient(client);

        } catch (InvalidNIFException ex){
            throw ex;
        }
        catch (Exception ex) {
            System.err.println("Failed to get client info." + ex.getMessage());
            throw new GetClientException(ErrorCodes.GET_CLIENT_BY_NIF_FAILED, ErrorCodes.GET_CLIENT_BY_NIF_MESSAGE);
        } finally {
            if (session != null)
                session.close();
        }
    }

    public List<ClientInfo> getClientsByName(String name) throws ClientManagementException{
        Session session = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery<Client> queryCriteria = null;
        Root<Client> root = null;
        Query<Client> query = null;
        List<Client> dbClients;


        try {
            session = factory.openSession();
            //validate name
            validateName(name);
            //build query conditions
            criteriaBuilder = session.getCriteriaBuilder();
            queryCriteria = criteriaBuilder.createQuery(Client.class);
            root = queryCriteria.from(Client.class);
            queryCriteria.select(root).where(criteriaBuilder.like(root.get("name"), "%"+name+"%"));

            query = session.createQuery(queryCriteria);

            //adapt client to ClientInfo
            dbClients = query.getResultList();

            return getClientInfoFromListofClients(dbClients);

        } catch (EmptyNameException ex){
            throw ex;
        }
        catch (Exception ex){
            throw new GetClientException(ErrorCodes.GET_CLIENT_BY_NAME_FAILED, ErrorCodes.GET_CLIENT_NY_NAME_MESSAGE);
        }

        finally {
            session.close();
        }
    }

    public List<ClientInfo> getAllClients() throws GetAllClientsException {
        Session session = null;
        List<Client> clients;

        try {
            session = factory.openSession();
            clients = session.createQuery("FROM Client").list();
            return getClientInfoFromListofClients(clients);

        } catch (Exception ex) {
            System.err.println("Failed to get all client info." + ex);
            throw new GetAllClientsException(ErrorCodes.GET_ALL_CLIENTS_FAILED, ErrorCodes.GET_ALL_CLIENT_FAILED_MESSAGE);

        } finally {
            session.close();
        }
    }

    public void deleteClient(int client_nif) throws ClientManagementException {
        Session session = null;
        Transaction tx = null;
        Client client = null;

        try{
            validateNIF(client_nif);
            session = factory.openSession();
            tx = session.beginTransaction();
            client = session.get(Client.class, client_nif);
            if (client == null)
                throw new ClientDoesntExistException(ErrorCodes.CLIENT_NOT_FOUND, ErrorCodes.CLIENT_NOT_FOUND_MESSAGE);
            session.delete(client);
            tx.commit();

        }catch (InvalidNIFException ex){
            throw ex;
        }
        catch (ClientDoesntExistException ex){
            throw ex;
        }
        catch (Exception ex){
            if (tx != null) tx.rollback();
            throw  new DeleteClientException(ErrorCodes.DELETE_CLIENT_FAILED, ErrorCodes.DELETE_CLIENT_FAILED_MESSAGE);
        }finally {
            if (session != null)
                session.close();
        }
    }

    private List<ClientInfo> getClientInfoFromListofClients(List<Client> dbClients){
        ArrayList<ClientInfo> clients = new ArrayList<>();

        for (Client client: dbClients) {

            clients.add(getClientInfoFromClient(client));
        }
        return clients;
    }

    private ClientInfo getClientInfoFromClient(Client client){

        ClientInfo auxClient = null;

        if (client == null)
            return  null;
        auxClient = new ClientInfo();
        auxClient.setNif(client.getNif());
        auxClient.setName(client.getName());
        auxClient.setMorada(client.getMorada());
        auxClient.setTelefone(client.getTelefone());

        return auxClient;
    }

    private void validateNIF(int nif)throws InvalidNIFException{

        if (nif <= 0)
            throw new InvalidNIFException(ErrorCodes.INVALID_NIF, ErrorCodes.INVALID_NIF_MESSAGE);
    }


    private void validateName(String name)throws EmptyNameException {

        if (name == null || name.isEmpty())
            throw new EmptyNameException(ErrorCodes.EMPTY_NAME, ErrorCodes.EMPTY_NAME_MESSAGE);
    }

}
