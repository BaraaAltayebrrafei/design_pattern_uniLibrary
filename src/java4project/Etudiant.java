package java4project;

import java.io.Serializable;
import java.util.*;

public class Etudiant implements Serializable { 
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private Specialiste specialite;
    private List<Matiere> matieres;

    public Etudiant(String username, String password, Specialiste specialite,List<Matiere> matieres) {
        this.username = username;
        this.password = password;
        this.specialite = specialite;
        //this.matieres = new ArrayList<>();
        this.matieres = matieres;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Specialiste getSpecialite() { return specialite; }
    public List<Matiere> getMatieres() {return matieres;}

    @Override
    public String toString() {
        return "Etudiant{" + "username=" + username + ", password=" + password + ", specialite=" + specialite + ", matieres=" + matieres.toString() + '}';
    }
    
    
    
}
