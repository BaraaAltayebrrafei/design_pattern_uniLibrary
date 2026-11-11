package java4project;

import org.jdom2.*;
//import java.io.File;
import java.io.*;
import java.util.*;
import org.jdom2.input.SAXBuilder;

public class EtudiantRepository {

    List<Etudiant> etudiants = new ArrayList<>();

    public EtudiantRepository(String xmlPath) {
        loadXML(xmlPath); // Parse XML into 'etudiants' list
    }

    private void loadXML(String path) {
        try {
            // 1. Initialize JDOM SAXBuilder
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new File(path));
            Element rootNode = document.getRootElement();

            // 2. Get all <specialite> tags
            List<Element> specs = rootNode.getChildren("specialite");

            for (Element spec : specs) {
                String nomSpec = spec.getAttributeValue("nom");
                Specialiste specialite = new Specialiste(nomSpec);

                // 3. Get all <etudiant> tags inside the specialty
                List<Element> etudiantsXML = spec.getChildren("etudiant");

                for (Element e : etudiantsXML) {
                    // Prepare the list of Matieres for this specific student
                    List<Matiere> matieresPourEtudiant = new ArrayList<>();
                    List<Element> matElements = e.getChildren("matiere");

                    for (Element matElem : matElements) {
                        // Extract the text (e.g., "NFP121") and create the object
                        String code = matElem.getText().trim();
                        matieresPourEtudiant.add(new Matiere(code, specialite));
                    }

                    // 4. Create the Etudiant object with the correct list
                    Etudiant etudiant = new Etudiant(
                            e.getAttributeValue("username"),
                            e.getAttributeValue("password"),
                            specialite,
                            matieresPourEtudiant
                    );

                    this.etudiants.add(etudiant);
                }
            }
            System.out.println("Chargement XML réussi. " + etudiants.size() + " étudiants chargés.");

            // --- DEBUG PRINT START ---
            System.out.println("\n========== DATA VERIFICATION ==========");
            for (Etudiant etu : etudiants) {
                System.out.println("Etudiant: " + etu.getUsername());
                System.out.println("  -> Spécialité: " + etu.getSpecialite().getName());
                System.out.print("  -> Matières: ");

                if (etu.getMatieres().isEmpty()) {
                    System.out.println("[AUCUNE MATIERE TROUVEE]");
                } else {
                    for (Matiere m : etu.getMatieres()) {
                        System.out.print("[" + m.getCode() + "] ");
                    }
                    System.out.println(); // New line
                }
                System.out.println("---------------------------------------");
            }
            System.out.println("=======================================\n");
// --- DEBUG PRINT END ---
        } catch (Exception ex) {
            System.err.println("Erreur JDOM: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Object authenticate(String user, String pass) {
        if ("admin".equalsIgnoreCase(user) && "admin".equals(pass)) {
            return "ADMIN";
        }

        for (Etudiant e : etudiants) {
            if (e.getUsername().equals(user) && e.getPassword().equals(pass)) {
                System.out.println(e.toString());
                return e;
            }
        }
        return null;
    }

//    public void loadWithJDOM(Element rootElement) {
//    // Get all <specialite> tags
//    List<Element> specs = rootElement.getChildren("specialite"); 
//    
//    for (Element spec : specs) {
//        String nomSpec = spec.getAttributeValue("nom");
//        Specialiste specialite = new Specialiste(nomSpec);
//
//        // Get all <etudiant> tags inside the specialty
//        List<Element> etudiantsXML = spec.getChildren("etudiant");
//        
//        for (Element e : etudiantsXML) {
//            List<Element> mat = e.getChildren("matiere");
//            Etudiant etudiant = new Etudiant(
//                e.getAttributeValue("username"),
//                e.getAttributeValue("password"),
//                specialite
//            );
//            for (Element matElem : e.getChildren("matiere")) {
//        etudiant.getMatieres().add(new Matiere(matElem.getText(), specialite));
//    }
//
//            // Get all <matiere> tags inside the student
//            List<Element> mats = e.getChildren("matiere");
//            for (Element m : mats) {
//                etudiant.getMatieres().add(new Matiere(m.getText(), specialite));
//            }
//            this.etudiants.add(etudiant);
//        }
//    }
//}
//    
    //    private void loadXML(String path) {
//        try {
//            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            Document doc = builder.parse(new File(path));
//            NodeList specs = doc.getElementsByTagName("specialite");
//
//            for (int i = 0; i < specs.getLength(); i++) {
//                Element spec = (Element) specs.item(i);
//                String nomSpec = spec.getAttribute("nom");
//                Specialiste specialite = new Specialiste(nomSpec);
//
//                NodeList etuNodes = spec.getElementsByTagName("etudiant");
//                for (int j = 0; j < etuNodes.getLength(); j++) {
//                    Element e = (Element) etuNodes.item(j);
//                    NodeList matieres = e.getChildNodes()
//                    Etudiant etudiant = new Etudiant(
//                            e.getAttribute("username"),
//                            e.getAttribute("password"),
//                            specialite
//                    );
//
//                    // Load matieres (either <matiere> or <valeur>)
//                    NodeList matNodes = e.getElementsByTagName("matiere"); // or "valeur"
//                    for (int k = 0; k < matNodes.getLength(); k++) {
//                        Element m = (Element) matNodes.item(k);
//                        etudiant.getMatieres().add(new Matiere(m.getTextContent().trim(), specialite));
//                    }
//
//                    etudiants.add(etudiant);
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void saveToBin() {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("etudiant.bin"))) {
//            oos.writeObject(this.etudiants);
//            System.out.println("Serialization réussie vers  --> etudiant.bin");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
