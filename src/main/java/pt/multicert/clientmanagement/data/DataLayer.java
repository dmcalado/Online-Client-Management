package pt.multicert.clientmanagement.data;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import pt.multicert.clientmanagement.ws_objects.*;
import pt.multicert.clientmanagement.exceptions.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DataLayer {
    private static SessionFactory factory;

    public DataLayer(){
        Configuration config = null;
        try{
            config = new Configuration();
            //config.addClass(Client.class);
            config.addResource("Client.hbm.xml");
            this.factory = config.configure().buildSessionFactory();
            System.err.println("Factory built!");
        }
        catch (Exception ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
        }
    }

    //Add a new client
    public void AddClient(ClientInfo clientInfo) throws AddClientException {

        Client client = null;
        try{
            client = new Client(clientInfo.getNif(), clientInfo.getName(), clientInfo.getMorada(), clientInfo.getTelefone());
            if (GetClient(client.getNif()) == null){
                //if the client does not exist, add it to DB.
                AddClientAux(client);
                return;
            }
            throw new ClientAlreadyExistsException("The client alredy exists!");
        }
        catch (ClientManagementException ex){
            System.err.println(ex.getMessage());
            throw new AddClientException(ex.getMessage());
        }

    }


    private void AddClientAux(Client client) throws AddClientException{
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
            throw new AddClientException("Failed to add client to DB.");

        } finally {
            session.close();
        }
    }

    public ClientInfo GetClient(int client_nif) throws GetClientException {
        Session session = null;
        Client client = null;

        try {
            session = factory.openSession();
            client = (Client)session.get(Client.class, client_nif);
            return getClientInfoFromClient(client);

        } catch (Exception ex) {
            System.err.println("Failed to get client info." + ex.getMessage());
            throw new GetClientException("Failed to get Client Info!");
        } finally {
            session.close();
        }
    }

    public List<ClientInfo> GetClientsByName (String name) throws GetClientException{
        Session session = null;
        CriteriaBuilder criteriaBuilder = null;
        CriteriaQuery<Client> queryCriteria = null;
        Root<Client> root = null;
        Query<Client> query = null;
        List<Client> dbClients;


        try {
            session = factory.openSession();
            //validate name
            if (name == null || name.isEmpty()){
                return new ArrayList<>();
            }
            //build query conditions
            criteriaBuilder = session.getCriteriaBuilder();
            queryCriteria = criteriaBuilder.createQuery(Client.class);
            root = queryCriteria.from(Client.class);
            queryCriteria.select(root).where(criteriaBuilder.like(root.get("name"), "%"+name+"%"));

            query = session.createQuery(queryCriteria);

            //adapt client to ClientInfo
            dbClients = query.getResultList();

            return getClientInfoFromListofClients(dbClients);

        } catch (Exception ex){
            System.err.println("Failed to get client info." + ex);
            throw new GetClientException("The wild card is empty!");
        }

        finally {
            session.close();
        }
    }

    public List<ClientInfo> GetAllClients() throws GetAllClientsException {
        Session session = null;
        List<Client> clients;

        try {
            session = factory.openSession();
            clients = session.createQuery("FROM Client").list();
            return getClientInfoFromListofClients(clients);

        } catch (Exception ex) {
            System.err.println("Failed to get all client info." + ex);
            throw new GetAllClientsException("Failed to get all clients info.");

        } finally {
            session.close();
        }
    }

    public void DeleteClient (int client_nif) throws ClientDoesntExistException, DeleteClientException {
        Session session = null;
        Transaction tx = null;
        Client client = null;

        try{
            session = factory.openSession();
            tx = session.beginTransaction();
            client = session.get(Client.class, client_nif);
            if (client == null)
                throw new ClientDoesntExistException("Client with NIF [" + client_nif + "] does not exist");
            session.delete(client);
            tx.commit();

        }catch (ClientDoesntExistException ex){
            throw ex;
        }
        catch (Exception ex){
            if (tx != null) tx.rollback();
            throw  new DeleteClientException("Failed to delete client with NIF:[" + client_nif + "]");
        }finally {
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

}
