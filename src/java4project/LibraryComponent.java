
package java4project;

import java.io.Serializable;
import java.util.*;
import java.io.*;

public interface LibraryComponent {
    public void  acceptVisitor();
    
}

////////////////////////////////////// Specialiste /////////////////////////////////////////////////////////
class Specialiste implements LibraryComponent, Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Set<Matiere> matieres ;

    public Specialiste(String name) {
        this.name = name;
        matieres = new HashSet<Matiere>();
    }
    public boolean add(Matiere m ){
        return m.getParentSpecialite().equals(this.name)? matieres.add(m): false ;
    }

    public String getName() {
        return name;
    }
    
    

      @Override
    public void acceptVisitor() {    }
}

/////////////////////////////////////Matiere/////////////////////////////////////////////////

class Matiere implements LibraryComponent, Comparable<Matiere> , Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private Specialiste parentSpecialite;
    private Map<Integer, Media> medias;
    
    
    public Matiere(String Code, Specialiste parentSpecialite) {
        this.code = Code;
        this.parentSpecialite = parentSpecialite;
        this.medias = new HashMap<Integer, Media>();
    }

    public boolean add(Media m) {
        if (m.getRelatedMatiere().contains(this)) {
            medias.put(m.id, m);
            return true;
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public Specialiste getParentSpecialite() {
        return parentSpecialite;
    }

    public Map<Integer, Media> getMedias() {
        return medias;
    }
    @Override
    public String toString() {
        return this.code; // Or this.name if you want the full name
    }
    


    @Override
    public void acceptVisitor() {
    }

    public int compareTo(Matiere other) {
        return this.code.compareTo(other.code);
    }

}




class SpecialisteRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Specialiste> specialistes = new ArrayList<>();

    // Add a Specialiste
    public boolean add(Specialiste s) {
        if (!specialistes.contains(s)) {
            return specialistes.add(s);
        }
        return false;
    }

    // Remove by name
    public boolean remove(String name) {
        Iterator<Specialiste> it = specialistes.iterator();
        while (it.hasNext()) {
            Specialiste s = it.next();
            if (s.getName().equals(name)) {
                it.remove();
                return true; 
            }
        }
        return false;
    }
    

    // Find by name
    public Specialiste findByName(String name) {
        for (Specialiste s : specialistes) {
            if (s.getName().equals(name)) {
                return s; 
            }
        }
        return null;
    }

    // List all
    public List<Specialiste> findAll() {
        return specialistes;
    }

    // Save to binary file
    public void saveToFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("spec.bin");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(specialistes);
        out.close();
        fileOut.close();
    }

    // Load from binary file
    public void loadFromFile() throws IOException, ClassNotFoundException {
        File f = new File("spec.bin");
        if (!f.exists()) return;
        FileInputStream fileIn = new FileInputStream("spec.bin");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        this.specialistes = (List<Specialiste>) in.readObject();
        in.close();
        fileIn.close();
    }
}


class MatiereRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Matiere> matieres = new ArrayList<>();

    // Add Matiere
    public boolean add(Matiere m) {
        if (!matieres.contains(m)) {
            return matieres.add(m);
        }
        return false;
    }

    // Remove by code
    public boolean remove(String code) {
        //return matieres.removeIf(m -> m.getCode().equals(code));
        Iterator<Matiere> it = matieres.iterator();
        while (it.hasNext()) {
            Matiere s = it.next();
            if (s.getCode().equals(code)) {
                it.remove();
                return true; 
            }
        }
        return false;
    }

    // Find by code
    public Matiere findByCode(String code) {
         for (Matiere s : matieres) {
            if (s.getCode().equals(code)) {
                return s; 
            }
        }
        return null;
    }

    // Find by Specialiste
    public List<Matiere> findBySpecialiste(String specialisteName) {
        List<Matiere> result = new ArrayList<>();
        for (Matiere m : matieres) {
            if (m.getParentSpecialite().getName().equals(specialisteName)) {
                result.add(m);
            }
        }
        return result;
    }

    // List all
    public List<Matiere> findAll() {
        return matieres;
    }

    // Save to binary file
    public void saveToFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("matiere.bin");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(matieres);
        out.close();
        fileOut.close();
    }

    // Load from binary file
    public void loadFromFile() throws IOException, ClassNotFoundException {
        File f = new File("matiere.bin");
        if (!f.exists()) return;
        FileInputStream fileIn = new FileInputStream(f);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        this.matieres = (List<Matiere>) in.readObject();
        in.close();
        fileIn.close();
    }
}

