package java4project;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddMediaFrame extends JFrame {

    private JComboBox<String> typeCombo;
    private JTextField txtTitre, txtAuteur, txtAnnee, txtDesc;
    private JTextField txtId;
// This is the "Specific" field (Changes meaning dynamically)
    private JLabel lblSpecific;
    private JTextField txtSpecific;
    private JComboBox<String> niveau;
    private JLabel lblNiveau;
    private JComboBox<String> cmbNiveau;
    private JList<String> listMatieres;
    MatiereRepository matRepo;
    MediaRepository medRepo;

    public AddMediaFrame(MatiereRepository matRepo, MediaRepository medRepo) {
        this.medRepo = medRepo;
        this.matRepo = matRepo;

        try {
            matRepo.loadFromFile();
        } catch (Exception e) {
            System.out.println("Aucune Matiere existante trouvée, démarrage vide.");
        }
        setTitle("Ajouter un Nouveau Média");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Don't close the whole app, just this window
        setLayout(new BorderLayout(10, 10));

        // --- Main Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Media Type Selector
        formPanel.add(new JLabel("Type de Média:"));

        typeCombo = new JComboBox<>(new String[]{"Document", "Video", "Quiz"});
        formPanel.add(typeCombo);

        formPanel.add(new JLabel("Matières Concernées:"));

        //String[] matieresData = {"Java", "Web Design", "Base de Données", "Algorithme", "Maths", "Anglais"};
        List<Matiere> allMat = matRepo.findAll();
        String[] items = new String[allMat.size()];
        for (int i = 0; i < allMat.size(); i++) {
            items[i] = String.valueOf(allMat.get(i).getCode());
        }
        listMatieres = new JList<>(items);
        listMatieres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listMatieres.setVisibleRowCount(3); // Show 3 rows at a time

        formPanel.add(new JScrollPane(listMatieres));
        formPanel.add(new JLabel("Titre:"));

        txtTitre = new JTextField();
        formPanel.add(txtTitre);

        formPanel.add(new JLabel("Auteur:"));
        txtAuteur = new JTextField();
        formPanel.add(txtAuteur);

        formPanel.add(new JLabel("Année:"));
        txtAnnee = new JTextField();
        formPanel.add(txtAnnee);

        formPanel.add(new JLabel("Description:"));
        txtDesc = new JTextField();
        formPanel.add(txtDesc);

        // ... previous code adding Description ...
        //  formPanel.add(new JLabel("Description:"));
        // txtDesc = new JTextField();
        //formPanel.add(txtDesc);
        lblSpecific = new JLabel("Nombre de pages:");
        txtSpecific = new JTextField();

        formPanel.add(lblSpecific);
        formPanel.add(txtSpecific);

        lblNiveau = new JLabel("Niveau: ");
        cmbNiveau = new JComboBox<>(new String[]{"Facile", "Intermediaire", "Difficile"});

        formPanel.add(lblNiveau);
        formPanel.add(cmbNiveau);
        lblNiveau.setVisible(false);
        cmbNiveau.setVisible(false);

        add(formPanel, BorderLayout.CENTER);

        // --- Buttons Panel ---
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- EVENTS ---
        // 1. Dynamic Label Change
        // When user picks "Video", change label to "Durée", etc.
        typeCombo.addActionListener(e -> {
            String selected = (String) typeCombo.getSelectedItem();
            switch (selected) {
                case "Document":
                    lblSpecific.setText("Nombre de pages:");
                    lblNiveau.setVisible(false);
                    cmbNiveau.setVisible(false);
                    break;
                case "Video":
                    lblSpecific.setText("Durée (minutes):");
                    lblNiveau.setVisible(false);
                    cmbNiveau.setVisible(false);
                    break;
                case "Quiz":
                    lblSpecific.setText("Durée (minutes):");
                    lblNiveau.setVisible(true);
                    cmbNiveau.setVisible(true);
                    break;
            }
        });
btnSave.addActionListener(e -> {
    // 1. Get the CURRENT selection at the moment of clicking save
    String selectedType = (String) typeCombo.getSelectedItem();
    
   // Media newMedia = null; // Initialized to avoid the "not initialized" error
    String titre = txtTitre.getText().trim();
    String auteur = txtAuteur.getText().trim();
    String strAnnee = txtAnnee.getText().trim();
    String strSpecific = txtSpecific.getText().trim();
    String desc = txtDesc.getText().trim();
    String niveauVal = (String) cmbNiveau.getSelectedItem();

    // 2. Validation: Check that fields are NOT empty
    if (!titre.isEmpty() && !auteur.isEmpty() && !strAnnee.isEmpty() && !strSpecific.isEmpty()) {
        try {
            int annee = Integer.parseInt(strAnnee);
            int specificAtt = Integer.parseInt(strSpecific);

            // 3. Duplicate Check
            SearchContext context = new SearchContext();
            context.setStrategy(new SearchByTitle());
            if (!context.executeStrategy(medRepo.findAll(), titre).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Un média avec ce titre existe déjà.");
                return;
            }

            // 4. Factory Creation
            MediaFactory factory = new MediaFactory();
            // Order: type, annee, titre, desc, auteur, specificAtt, niveau, specificAtt
            Media newMedia = factory.createMedia(selectedType, annee, titre, desc, auteur, niveauVal, specificAtt);

            if (newMedia != null) {
                // Link selected Matieres from the JList
                List<String> selectedMatCodes = listMatieres.getSelectedValuesList();
                for (String code : selectedMatCodes) {
                    Matiere m = matRepo.findByCode(code);
                    if (m != null) {
                        newMedia.getRelatedMatiere().add(m);
                        m.add(newMedia); // Bio-directional link
                    }
                }

                medRepo.add(newMedia);
                medRepo.saveToFile();

                JOptionPane.showMessageDialog(this,
                    "Média Ajouté ! \nType"+ selectedType +"\nID: " + newMedia.getId() + 
                    "\nTitre: " + newMedia.getTitre() + 
                    "\nMatières: " + newMedia.getRelatedMatiere().toString());
                dispose();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'année et la valeur spécifique doivent être des nombres !");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } else {
        JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.");
    }
});
        // 2. Save Button Logic
//        btnSave.addActionListener(e -> {
//            // String typeCombo = typeCombo.
//            String selectedType = (String) typeCombo.getSelectedItem();
//            Media newMedia = null;
//            String titre = txtTitre.getText();
//            String auteur = txtAuteur.getText();
//            String strAnnee = txtAnnee.getText().trim();
//            String strSpecific = txtSpecific.getText().trim();
//            String desc = txtDesc.getText();
//            String niveau = (String) cmbNiveau.getSelectedItem();
//            List<Media> allMedias = medRepo.findAll();
//            if (!selectedType.isEmpty() && !titre.isEmpty()
//                    && !auteur.isEmpty() && !strAnnee.isEmpty() 
//                    && !desc.isEmpty() && !strSpecific.isEmpty()){
//                int annee = Integer.parseInt(strAnnee);
//                int specificAtt = Integer.parseInt(strSpecific);
//                if (!selectedType.equals("Quiz")) {
//                    niveau = "";
//                } else if (!niveau.isEmpty()) {
//
//                    SearchContext context = new SearchContext();
//                    context.setStrategy(new SearchByTitle());
//
//                    List<Media> duplicates = context.executeStrategy(allMedias, txtTitre.getText().trim());
//                    if (!duplicates.isEmpty()) {
//                        JOptionPane.showMessageDialog(this, "Un média avec ce titre existe déjà.");
//                        return;
//                    }
//
//                    MediaFactory factory = new MediaFactory();
//                    newMedia = factory.createMedia(selectedType, annee, titre, desc, auteur, niveau, specificAtt);
//                    medRepo.add(newMedia);
//
//                    try {
//                        medRepo.saveToFile();
//                    } catch (Exception ex) {
//                        JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage(),
//                                "Erreur", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//                   
//
//                } 
//                JOptionPane.showMessageDialog(this,
//                            "Media Ajoutée !\nID: " + newMedia.id + "\nTitre: " + newMedia.titre + "\nMatire: " + newMedia.relatedMatiere);
//                    dispose();
//            }
//
//        });

        // 3. Cancel Button
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void saveMedia() {
        try {
            // A. Gather Data
            String type = (String) typeCombo.getSelectedItem();
            int id = Integer.parseInt(txtId.getText());
            String titre = txtTitre.getText();
            String auteur = txtAuteur.getText();
            int annee = Integer.parseInt(txtAnnee.getText());
            String desc = txtDesc.getText();

            // This is the "Specific Value"
            // We parse it as an INT, regardless of whether it represents pages, minutes, or questions.
            int specificValue = Integer.parseInt(txtSpecific.getText());

            // B. Use your Factory (Assuming you updated it for Quiz)
            // Media newMedia = MediaFactory.createMedia(type, id, annee, titre, desc, auteur, specificValue);
            // C. Show Success
            JOptionPane.showMessageDialog(this, "Média ajouté avec succès !\nType: " + type + "\nValeur Spécifique: " + specificValue);

            // D. Close window
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : ID, Année et la valeur spécifique doivent être des nombres !", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
}
