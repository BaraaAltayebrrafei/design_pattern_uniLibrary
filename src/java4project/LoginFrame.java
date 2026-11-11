package java4project;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFrame extends JFrame {

    private EtudiantRepository repo;
    private MediaRepository medRepo;
    public LoginFrame(EtudiantRepository repo, MediaRepository medRepo) {
        this.repo = repo;
        this.medRepo = medRepo;
        setTitle("Login");
        setSize(300,200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnLogin = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(3,2));
        panel.add(new JLabel("Username"));
        panel.add(txtUser);
        panel.add(new JLabel("Password"));
        panel.add(txtPass);
        panel.add(new JLabel());
        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            Object result = repo.authenticate(user, pass);

            if (result == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid credentials",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            } 
            else if (result.equals("ADMIN")) {
                //new AddMediaFrame();
                new AdminFrame();
                dispose();
            } 
            else {          
                
                System.out.println(((Etudiant) result).toString());
                try {
                    new EtudiantFrame((Etudiant) result);
                } catch (IOException ex) {
                    Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
            }
        });

        setVisible(true);
    }
}
