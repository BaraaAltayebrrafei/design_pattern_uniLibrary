package java4project;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class AdminFrame extends JFrame {
private SpecialisteRepository specRepo;
private MatiereRepository matRepo;
private MediaRepository medRepo;

    public AdminFrame() {
        specRepo = new SpecialisteRepository();
        matRepo = new MatiereRepository();
        medRepo = new MediaRepository();
        try {
            specRepo.loadFromFile();
            matRepo.loadFromFile();
            medRepo.loadFromFile();
        } catch (Exception e) {
            System.out.println("No existing binary data found, starting fresh.");
        }
        setTitle("Panneau d'Administration");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Header Title
        JLabel titleLabel = new JLabel("Tableau de Bord Admin", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Center Panel (Grid for Action Buttons)
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2 rows, 2 cols, gaps
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        
        JButton btnAddSpec = new JButton("Nouvelle Spécialité");
        btnAddSpec.addActionListener(e -> new AddSpecialiteFrame(specRepo));

        JButton btnAddMat = new JButton("Nouvelle Matière");
        btnAddMat.addActionListener(e -> new AddMatiereFrame(specRepo,matRepo));


        // -- Button 1: Recherche --
        JButton btnSearch = new JButton("Recherche");
        btnSearch.addActionListener(e -> {
            new RechercheFrame(medRepo);
        });

        // -- Button 2: Add Media --
        JButton btnAdd = new JButton("Ajouter Média");
        btnAdd.addActionListener(e -> {
            new AddMediaFrame(matRepo, medRepo); 
        });

        // -- Button 3: Statistique --
        JButton btnStats = new JButton("Statistiques");
        btnStats.addActionListener(e -> {
            new StatistiqueFrame();
        });

        // -- Button 4: Déconnecter --
        JButton btnLogout = new JButton("Se Déconnecter");
        btnLogout.addActionListener(e -> {
            dispose();
            EtudiantRepository repo = new EtudiantRepository("data/universite.xml");
            //new LoginFrame(repo);
        });

        // Add buttons to grid
        centerPanel.add(btnAddSpec);
        centerPanel.add(btnAddMat);
        centerPanel.add(btnSearch);
        centerPanel.add(btnAdd);
        centerPanel.add(btnStats);
        centerPanel.add(btnLogout);

        add(centerPanel, BorderLayout.CENTER);

        // 3. Footer (Optional)
        JLabel footerLabel = new JLabel("© 2024 University System", SwingConstants.CENTER);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footerLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Helper method to make buttons look nice and uniform
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}