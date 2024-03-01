package gui;

import data_base.JDBC;
import data_base.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Clasa LoginPanel.
 * Reprezintă scena de logare, una dintre scenele afisate pe BaseFrame
 */

/**
 * <h1>Clasa LoginPanel</h1>
 * Clasa LoginPanel extinde clasa JPanel fiind scena pe care sunt puse componentele
 * pentru interfața de login
 * <p>
 * <b>Note:</b> Reprezintă una din interfețele ”desenete” pe BaseFrame
 * Aceasta are ca scop facilitarea procesului de logare
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class LoginPanel extends JPanel{

    private JTextField emailField;
    private JPasswordField passwordField;

    /**
     * Constructorul clasei LoginPanel, metoda în care se adaugă elementele interfeței
     *
     * @param baseFrame Reprezinta scheletul pe care urmeaza sa fie „desenată” scena
     */

    public LoginPanel(BaseFrame baseFrame) {
        setSize(1280, 720);
        setLayout(null);
        //Title
        JLabel travelAppLabel = new JLabel("Travel Agency");
        travelAppLabel.setBounds(0, 20, getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);

        //Logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/test6.png")));
        Image newImage = logo.getImage().getScaledInstance(505, 528, Image.SCALE_DEFAULT);
        logo = new ImageIcon(newImage);
        JLabel imgLabel = new JLabel(logo);
        imgLabel.setBounds((int) (getWidth() * 0.55), (int) (getHeight() * 0.12), logo.getIconWidth(), logo.getIconHeight());
        add(imgLabel);

        //Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(80, 160, baseFrame.getWidth() - 30, 24);
        emailLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(80, 200, baseFrame.getWidth() / 3, 40);
        emailField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(emailField);

        //Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 260, baseFrame.getWidth() - 30, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(80, 300, baseFrame.getWidth() / 3, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        //Buttons

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/loginButton2.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/hoverLoginButton2.png")));

        JButton loginButton = new JButton();
        loginButton.setBounds(70, 350, 432, 102);
        loginButton.setCursor(cursor);
        loginButton.setIcon(imgButton);
        loginButton.setRolloverIcon(imgHover);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(e -> {
            String textField1 = emailField.getText();
            String textField2 = String.valueOf(passwordField.getPassword());

            User user = JDBC.validateLogin(textField1, textField2);

            if(user != null){
                if(JDBC.getRole(user) == 2) {
                    baseFrame.showProfilePanel(user);
                }
                else{
                    baseFrame.showAdminPanel(user);
                }
                JOptionPane.showMessageDialog(baseFrame, "Login Successfully!");
            }
            else{
                JOptionPane.showMessageDialog(baseFrame, "Login failed...");
            }
        });
        add(loginButton);

        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a></html>");
        registerLabel.setBounds(70, 450, baseFrame.getWidth() / 3, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(cursor);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Switch to the RegisterPanel
                baseFrame.showRegisterPanel();
            }
        });
        add(registerLabel);
    }
}
