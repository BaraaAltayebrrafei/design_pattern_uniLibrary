package java4project;

import java.io.*;
import java.io.Serializable;
import java.util.*;

abstract class Media implements LibraryComponent, Serializable {

    private static final long serialVersionUID = 1L;

    private static int nbr = 1;
    protected int id, annee;
    protected String titre, description, auteur;
    protected Set<Matiere> relatedMatiere;//to add media in only related matiere

    public Media(int annee, String titre, String description, String auteur) {
        this.id = nbr++;
        this.annee = annee;
        this.titre = titre;
        this.description = description;
        this.auteur = auteur;
        this.relatedMatiere = new HashSet<Matiere>();
    }

    public int getId() {
        return id;
    }

    public int getAnnee() {
        return annee;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getAuteur() {
        return auteur;
    }

    public Set<Matiere> getRelatedMatiere() {
        return relatedMatiere;
    }

    abstract void addObserver(Observer o);

    abstract void removeObserver(Observer o);

    abstract void notifyObservers();

    @Override
    public void acceptVisitor() {
    }

}

//////////////////////////////////////Quiz////////////////////////////////////////
class Quiz extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    private int duree;
    private String niveau;

    public Quiz(int annee, String titre, String description, String auteur,
            String niveau, int duree) {
        super(annee, titre, description, auteur);
        this.duree = duree;
        this.niveau = niveau;
    }

    @Override
    void addObserver(Observer o) {
    }

    @Override
    void removeObserver(Observer o) {
    }

    @Override
    void notifyObservers() {

    }

}
//////////////////////////////////////Video////////////////////////////////////////

class Video extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    private int duree;

    public Video(int annee, String titre, String description, String auteur, int duree) {
        super(annee, titre, description, auteur);
        this.duree = duree;
    }

    @Override
    void addObserver(java4project.Observer o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void removeObserver(java4project.Observer o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    void notifyObservers() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

//////////////////////////////////////DocumentMedia////////////////////////////////////////
class DocumentMedia extends Media implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageNb;

    public DocumentMedia(int annee, String titre, String description, String auteur, int pageNb) {
        super(annee, titre, description, auteur);
        this.pageNb = pageNb;
    }

    @Override
    void addObserver(Observer o) {
    }

    @Override
    void removeObserver(Observer o) {
    }

    @Override
    void notifyObservers() {
    }
}

class MediaRepository implements Serializable {

    private List<Media> medias = new ArrayList<>();

    public void add(Media media) {
        medias.add(media);
    }

    public List<Media> findAll() {
        return medias;
    }

    public void saveToFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("media.bin");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(medias);
        out.close();
        fileOut.close();
    }

    public void loadFromFile() throws IOException, ClassNotFoundException {
        File file = new File("media.bin");
        if (!file.exists()) {
            return; // first run
        }
        FileInputStream fileIn = new FileInputStream("media.bin");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        this.medias = (List<Media>) in.readObject();
        in.close();
        fileIn.close();
    }
}
