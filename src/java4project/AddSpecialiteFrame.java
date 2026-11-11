package java4project;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class AddSpecialiteFrame extends JFrame {

    private JTextField txtNomSpec;
    private SpecialisteRepository repository;  


    public AddSpecialiteFrame(SpecialisteRepository repository) {
        this.repository = repository;
        
        setTitle("Ajouter une Spécialité");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nom Spécialité:"));
        txtNomSpec = new JTextField();
        formPanel.add(txtNomSpec);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnSave = new JButton("Enregistrer");
        //JButton btnCancel = new JButton("Annuler");

        btnPanel.add(btnSave);
        //btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String nom = txtNomSpec.getText().trim();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //if spec exist
            if (repository.findByName(nom) != null) {
                JOptionPane.showMessageDialog(this, "Cette spécialité existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Specialiste newSpec = new Specialiste(nom);
            repository.add(newSpec);

            try {
                repository.saveToFile();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Spécialité ajoutée avec succès : " + nom);
            dispose();
        });

        setVisible(true);
    }
}

class AddMatiereFrame extends JFrame {

    private JTextField txtCode;
    private JTextField txtNom;
    private JComboBox<String> comboSpec; // To select the parent specialty
    //private StrategyExporter strategy;
    SpecialisteRepository specRepo ;
    MatiereRepository matRepo;
    
    public AddMatiereFrame(SpecialisteRepository specRepo, MatiereRepository matRepo) {
        // this.strategy = strategy;
        this.specRepo =specRepo;
        this.matRepo =  matRepo;
//        try {
//            specRepo.loadFromFile(); 
//        } catch (Exception e) {
//            System.out.println("Aucune spécialité existante trouvée, démarrage vide.");
//        }
        setTitle("Ajouter une Matière");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Code Matière:"));
        txtCode = new JTextField();
        formPanel.add(txtCode);

        formPanel.add(new JLabel("Spécialité Parente:"));
        List<Specialiste> allSpecs = specRepo.findAll();
       // List<String> specialiteList = loader.loadSpecialiteNames();
        String[] items = new String[allSpecs.size()];
        for (int i = 0; i < allSpecs.size(); i++) {
            items[i] = String.valueOf(allSpecs.get(i).getName());
        }
        comboSpec = new JComboBox<>(items);

        formPanel.add(comboSpec);

        add(formPanel, BorderLayout.CENTER);

        // --- Buttons ---
        JPanel btnPanel = new JPanel();
        JButton btnSave = new JButton("Enregistrer");
       // JButton btnCancel = new JButton("Annuler");

        btnPanel.add(btnSave);
       // btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // --- Actions ---
        btnSave.addActionListener(e -> {
            String code = txtCode.getText().trim();
            // String nom = txtNom.getText().trim();
            String selectedSpec = (String) comboSpec.getSelectedItem();

            
            if (!code.isEmpty() && selectedSpec != null) {
                if (matRepo.findByCode(code) != null) {
                    JOptionPane.showMessageDialog(this, "Cette Matiere existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                 }
                Specialiste spec = new Specialiste(selectedSpec);
                Matiere newMat = new Matiere(code, spec);
                
                matRepo.add(newMat);

                try {
                    matRepo.saveToFile();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }      
                JOptionPane.showMessageDialog(this,
                        "Matière Ajoutée !\nCode: " + code + "\nSpécialité: " + selectedSpec);
                dispose();                         
            }else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

       // btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

}
