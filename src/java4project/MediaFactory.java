
package java4project;

public class MediaFactory {
    public Media createMedia(String type, int annee, String titre, 
            String description, String auteur, String niveau, int specAtt){
        
        String cleanType = type.trim().toLowerCase();
        
        if (type.equalsIgnoreCase("document"))
            return new DocumentMedia(annee, titre, description, auteur, specAtt);
        else if (type.equalsIgnoreCase("quiz"))
            return new Quiz(annee, titre, description, auteur, niveau,specAtt);
        else if (type.equalsIgnoreCase("video"))
            return new Video(annee, titre, description, auteur,specAtt);
        return null;
    }
}
