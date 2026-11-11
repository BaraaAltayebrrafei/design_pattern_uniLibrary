package java4project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Class;

class EtudiantFrame extends JFrame {

    MediaRepository medRepo;

    public EtudiantFrame(Etudiant student) throws IOException {
        this.medRepo = new MediaRepository();
        try {
            this.medRepo.loadFromFile();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EtudiantFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        setTitle("Espace Étudiant");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        System.out.println(student.toString());
        // =========================
        // 1. HEADER
        // =========================
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(new Color(60, 63, 65));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblUser = new JLabel("Utilisateur: " + student.getUsername());
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblSpec = new JLabel("Spécialité: " + student.getSpecialite().getName());
        lblSpec.setForeground(new Color(173, 216, 230));
        lblSpec.setFont(new Font("Arial", Font.ITALIC, 14));

        headerPanel.add(lblUser);
        headerPanel.add(lblSpec);
        add(headerPanel, BorderLayout.NORTH);

        // =========================
        // 2. CENTER: TREE OF MATIERES + MEDIA
        // =========================
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mes Matières");
        List<Matiere> EtudiantMat = student.getMatieres();
        for (Matiere m : EtudiantMat) {
            DefaultMutableTreeNode matiereNode = new DefaultMutableTreeNode(m.getCode());

            root.add(matiereNode);

            SearchContext context = new SearchContext();
            context.setStrategy(new SearchByMatiere());

            List<Media> results = context.executeStrategy(medRepo.findAll(), m.getCode());

            // if(results.isEmpty()){System.out.println("emptyy");}
            for (Media med : results) {
                DefaultMutableTreeNode mediaNode = new DefaultMutableTreeNode(med.getTitre());
                matiereNode.add(mediaNode);
            }
            //  root.add(matiereNode);
            //}
        }

        // Create the tree
        JTree tree = new JTree(root);
        tree.setRowHeight(25);

        // Optional: nicer icons
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setLeafIcon(UIManager.getIcon("FileView.fileIcon"));
        renderer.setClosedIcon(UIManager.getIcon("FileView.directoryIcon"));
        renderer.setOpenIcon(UIManager.getIcon("FileView.directoryIcon"));

        // Double-click opens media details
//        tree.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent e) {
//                int selRow = tree.getRowForLocation(e.getX(), e.getY());
//                if (selRow != -1 && e.getClickCount() == 2) {
//                    DefaultMutableTreeNode selectedNode
//                            = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//                    if (selectedNode != null && selectedNode.isLeaf()) {
//                        String mediaTitle = selectedNode.toString();
//                        //    new MediaDetailFrame(mediaTitle);
//                    }
//                }
//            }
//        });
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                if (selRow != -1 && e.getClickCount() == 2) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                    if (selectedNode != null && selectedNode.isLeaf()) {
                        String mediaTitle = selectedNode.toString();
                        SearchContext context = new SearchContext();
                        context.setStrategy(new SearchByTitle());

                        List<Media> results = context.executeStrategy(medRepo.findAll(), mediaTitle);

                        for(Media m :results){
                            JOptionPane.showMessageDialog(
                                EtudiantFrame.this,
                                "Media Type: " + m.getClass().getName()+"\n" +
                                "Title: "+mediaTitle+"\n" +
                                "Auteur: "+m.auteur+"\n" +
                                "Annee: "+ m.annee+"\n" +
                                "description: "+m.description,
                                "Détails du Média",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        }
                        // Show a simple information popup
                        
                    }
                }
            }
        });

        add(new JScrollPane(tree), BorderLayout.CENTER);

        // =========================
        // 3. LOGOUT
        // =========================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.addActionListener(e -> {
            dispose();
            //new LoginFrame(new EtudiantRepository("data/universite.xml",medRepo));
        });
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
