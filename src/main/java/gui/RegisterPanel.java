package gui;

import data_base.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clasa RegisterPanel.
 * Reprezintă scena de inregistrare, una dintre scenele afisate pe BaseFrame
 */

/**
 * <h1>Clasa RegisterPanel</h1>
 * Clasa RegisterPanel extinde clasa JPanel fiind scena pe care sunt puse componentele
 * pentru interfața de înregistrare
 * <p>
 * <b>Note:</b> Reprezintă una din interfețele ”desenete” pe BaseFrame
 * Aceasta are ca scop facilitarea procesului de înregistrare
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

class RegisterPanel extends JPanel {
    private JTextField fNameField;
    private JTextField lNameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    /**
     * Constructorul clasei RegisterPanel, metoda în care se adaugă elementele interfeței
     *
     * @param baseFrame Reprezinta scheletul pe care urmeaza sa fie „desenată” scena
     */

    public RegisterPanel(BaseFrame baseFrame) {
        setSize(1280, 720);
        setLayout(null);
        setBackground(Color.decode("#F2F2F2"));

        //Logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/test6.png")));
        Image newImage = logo.getImage().getScaledInstance(505, 528, Image.SCALE_DEFAULT);
        logo = new ImageIcon(newImage);
        JLabel imgLabel = new JLabel(logo);
        imgLabel.setBounds((int) (getWidth() * 0.55), 130, logo.getIconWidth(), logo.getIconHeight());
        add(imgLabel);

        //Title
        JLabel travelAppLabel = new JLabel("Travel Agency");
        travelAppLabel.setBounds(0, 20, super.getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);

        //First Name
        JLabel fNameLabel = new JLabel("First Name:");
        fNameLabel .setBounds(80, 130, getWidth() - 30, 24);
        fNameLabel .setFont(new Font("Dialog", Font.PLAIN, 20));
        add(fNameLabel);

        fNameField = new JTextField();
        fNameField.setBounds(80, 170, getWidth() / 3, 40);
        fNameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(fNameField);

        //Last Name
        JLabel lNameLabel = new JLabel("Last Name:");
        lNameLabel.setBounds(80, 230, getWidth() - 30, 24);
        lNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(lNameLabel);

        lNameField = new JTextField();
        lNameField.setBounds(80, 270, getWidth() / 3, 40);
        lNameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(lNameField);

        //Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(80, 330, getWidth() - 30, 24);
        emailLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(80, 370, getWidth() / 3, 40);
        emailField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(emailField);

        //Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 430, getWidth() - 30, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(80, 470, getWidth() / 3, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        //Buttons

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/registerButton2.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/hoverRegisterButton2.png")));

        JButton registerButton = new JButton();
        registerButton.setBounds(70, 520, 432, 102);
        registerButton.setCursor(cursor);
        registerButton.setIcon(imgButton);
        registerButton.setRolloverIcon(imgHover);
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.addActionListener(e -> {
            String first_name = fNameField.getText();
            String last_name = lNameField.getText();
            String email = emailField.getText();
            String password = String.valueOf(passwordField.getPassword());

            int validate = validateUserInput(first_name, last_name, email, password);
            switch (validate){
                case 0:
                    if(JDBC.register(first_name, last_name, email, password)) {
                        baseFrame.showLoginPanel();
                        JOptionPane.showMessageDialog(baseFrame, "Registered Account Succesfully!");
                    }
                    else{
                        JOptionPane.showMessageDialog(baseFrame, "Error: email already used");
                    }
                    break;
                case 1:
                    JOptionPane.showMessageDialog(baseFrame, "Error: Every field should be entered");
                    break;
                case 2:
                    JOptionPane.showMessageDialog(baseFrame, "Error: The password should be at least 8 characters long and at most 20 characters. \n " +
                            "From which: at least one digit \n" +
                            "at least one upper case letter \n" +
                            "at least one lower case letter \n" +
                            "at least one special character \n" +
                            "it doesnt contain any white spaces");
                    break;
                case 3:
                    JOptionPane.showMessageDialog(baseFrame, "Error: The email is not valid");
                    break;
                case 4:
                    JOptionPane.showMessageDialog(baseFrame, "Error: the first name should not contain any digits");
                    break;
                case 5:
                    JOptionPane.showMessageDialog(baseFrame, "Error: the last name should not contain any digits");
                    break;
            }
        });
        add(registerButton);

        JLabel loginLabel = new JLabel("<html><a href=\"#\">Already have an account? Login Here</a></html>");
        loginLabel.setBounds(70, 620, getWidth() / 3, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(cursor);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Switch back to the LoginPanel
                baseFrame.showLoginPanel();
            }
        });
        add(loginLabel);
    }

    private int validateUserInput(String first_name, String last_name, String email, String password){
        if(first_name.length() == 0 || last_name.length() == 0 || email.length() == 0 || password.length() == 0){
            return 1;
        }

        if(!validatePassword(password)){
            return 2;
        }

        if(!validateEmail(email)){
            return 3;
        }
        if(validateName(first_name)){
            return 4;
        }
        if(validateName(last_name)){
            return 5;
        }
        return 0;
    }
    public boolean validatePassword(String password){
        String regexPassword =  "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        Pattern p = Pattern.compile(regexPassword);

        Matcher m = p.matcher(password);

        return m.matches();
    }
    public boolean validateEmail(String email){
        String regexEmail = "^(.+)@(\\S+)$";
        Pattern p = Pattern.compile(regexEmail);
        Matcher m = p.matcher(email);

        return m.matches();
    }
    public boolean validateName(String name){
        return name.matches(".*\\d.*");
    }
}
