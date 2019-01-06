package pt.multicert.clientmanagement.data;
// Generated 6/jan/2019 18:39:25 by Hibernate Tools 3.2.2.GA



/**
 *             This class contains the client detail.
 *         
 */
public class Client  implements java.io.Serializable {


     private int nif;
     private String name;
     private String morada;
     private int telefone;

    public Client() {
    }

	
    public Client(int nif) {
        this.nif = nif;
    }
    public Client(int nif, String name, String morada, int telefone) {
       this.nif = nif;
       this.name = name;
       this.morada = morada;
       this.telefone = telefone;
    }
   
    public int getNif() {
        return this.nif;
    }
    
    public void setNif(int nif) {
        this.nif = nif;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getMorada() {
        return this.morada;
    }
    
    public void setMorada(String morada) {
        this.morada = morada;
    }
    public int getTelefone() {
        return this.telefone;
    }
    
    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }




}


