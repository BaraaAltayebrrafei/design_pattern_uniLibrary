package java4project;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RechercheFrame extends JFrame {

    private MediaRepository medRepo;
    private SearchContext searchContext = new SearchContext();

    private JLabel searchLabel = new JLabel("Cherche par:");
    private JComboBox<String> comboSearch = new JComboBox<>(new String[]{"Specialiste", "Matiere", "Media"});

    private JLabel saisirLabel = new JLabel("Saisir un specialiste:");
    private JTextField saisirSearch = new JTextField(15);

    private JButton modifier = new JButton("Modifier");
    private JButton supprimer = new JButton("Supprimer");
    //private JButton sauvegarde = new JButton("Sauvegarder");
    JButton btnSearch = new JButton("Rechercher");
    private DefaultTableModel tableModel;
    private JTable dataTable;

    public RechercheFrame(MediaRepository mediaRepo) {
        this.medRepo = mediaRepo;
//        try {
//            mediaRepo.loadFromFile(); 
//        } catch (Exception e) {
//            System.out.println("Aucune spécialité existante trouvée, démarrage vide.");
//        }
        setTitle("Interface Professeur / Admin");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- TOP PANEL (Search + Actions) ---
        JPanel topContainer = new JPanel(new GridLayout(2, 1));

        // Row 1: Search Criteria
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchLabel);
        searchPanel.add(comboSearch);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(saisirLabel);
        searchPanel.add(saisirSearch);

        // Row 2: Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(modifier);
        actionPanel.add(supprimer);
        actionPanel.add(btnSearch);

        topContainer.add(searchPanel);
        topContainer.add(actionPanel);
        add(topContainer, BorderLayout.NORTH);

        // --- CENTER PANEL (Table) ---
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Titre");
        tableModel.addColumn("Auteur");
        tableModel.addColumn("Annee");
        tableModel.addColumn("Description");
        tableModel.addColumn("Specialiste");
        tableModel.addColumn("Matiere");

        dataTable = new JTable(tableModel);
        add(new JScrollPane(dataTable), BorderLayout.CENTER);

        // --- BOTTOM PANEL (Export + Logout) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnExport = new JButton("Exporter");
        //JButton btnLogout = new JButton("Déconnexion");

        bottomPanel.add(btnExport);
        //bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);
        List<Media> allMedia = medRepo.findAll();
        loadAllMediasToTable(allMedia, "", "ALL");

        // --- LOGIC: Dynamic Label Change ---
        comboSearch.addActionListener(e -> {
            String selected = (String) comboSearch.getSelectedItem();
            switch (selected) {
                case "Specialiste":
                    saisirLabel.setText("Saisir un specialiste:");
                    break;
                case "Matiere":
                    saisirLabel.setText("Saisir code matiere:");
                    break;
                case "Media":
                    saisirLabel.setText("Saisir Titre du media:");
                    break;
            }
        });
        btnSearch.addActionListener(e -> {
            String query = saisirSearch.getText().trim();
            if (query.isEmpty()) {
                loadAllMediasToTable(allMedia, "", "ALL");
                return;
            }

            // Choose strategy
            String selected = (String) comboSearch.getSelectedItem();
            switch (selected) {
                case "Specialiste":
                    searchContext.setStrategy(new SearchBySpecialite());
                    break;
                case "Matiere":
                    searchContext.setStrategy(new SearchByMatiere());
                    break;
                case "Media":
                    searchContext.setStrategy(new SearchByTitle());
                    break;
            }

            List<Media> result = searchContext.executeStrategy(medRepo.findAll(), query);

            loadAllMediasToTable(result, query, selected);
            //fillTableFromMedias(result);
        });

        // --- LOGIC: Export Popup Menu ---
        JPopupMenu exportMenu = new JPopupMenu();
        JMenuItem exportXml = new JMenuItem("Exporter en XML");
        JMenuItem exportCsv = new JMenuItem("Exporter en CSV");
        exportMenu.add(exportXml);
        exportMenu.add(exportCsv);

        btnExport.addActionListener(e -> {
            exportMenu.show(btnExport, 0, -exportMenu.getPreferredSize().height);
        });

        // Export Actions
        ExporterContext exporter = new ExporterContext();

        exportXml.addActionListener(e -> {
            exporter.setStrategy(new ExportXml());
            try {
                //if (dataTable.getRowCount() == 0) System.out.println("emptyyyy");
                exporter.exportData(dataTable, "data/medias.xml");
                JOptionPane.showMessageDialog(this, "Export XML réussi !");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'export XML !");
            }
        });

        exportCsv.addActionListener(e -> {
            exporter.setStrategy(new ExportCsv());
            try {
                exporter.exportData(dataTable, "data/medias.csv");
                JOptionPane.showMessageDialog(this, "Export CSV réussi !");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'export CSV !");
            }
        });

        // Logout Action
//       s
        setVisible(true);
    }

    private void loadAllMediasToTable(
            List<Media> medias,
            String query,
            String searchType
    ) {
        tableModel.setRowCount(0);

        for (Media m : medias) {
            for (Matiere mat : m.getRelatedMatiere()) {

                Specialiste spec = mat.getParentSpecialite();

                // 🔹 SHOW EVERYTHING
                if (searchType.equals("ALL")) {
                    addRow(m, mat, spec);
                    continue;
                }

                // 🔹 Filter by Matiere
                if (searchType.equals("Matiere")
                        && !mat.getCode().equalsIgnoreCase(query)) {
                    continue;
                }

                // 🔹 Filter by Specialiste
                if (searchType.equals("Specialiste")
                        && (spec == null || !spec.getName().equalsIgnoreCase(query))) {
                    continue;
                }

                // 🔹 Media search → show all rows
                addRow(m, mat, spec);
            }
        }
    }

    private void addRow(Media m, Matiere mat, Specialiste spec) {
        tableModel.addRow(new Object[]{
            m.getId(),
            m.getTitre(),
            m.getAuteur(),
            m.getAnnee(),
            m.getDescription(),
            spec != null ? spec.getName() : "-",
            mat.getCode()
        });
    }
}
