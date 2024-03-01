package gui;

import data_base.JDBC;
import data_base.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class ModifyUser extends JPanel {

    private User loggedInUser;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField firstNameField;
    private JTextField secondNameField;
    private JRadioButton clientButton;
    private JRadioButton adminButton;


    private void modifyButton(ActionEvent e, User user){
        String first_name = firstNameField.getText();
        String second_name = secondNameField.getText();
        String email = emailField.getText();
        String password = String.valueOf(passwordField.getPassword());
        int role;
        if(clientButton.isSelected()){
            role = 2;
        }
        else{
            role = 1;
        }
        if(!first_name.isEmpty()){
            JDBC.updateFirstName(user, first_name);
        }
        if(!second_name.isEmpty()){
            JDBC.updateLastName(user, second_name);
        }
        if(!email.isEmpty()){
            JDBC.updateEmail(user, email);
        }
        if(!password.isEmpty()){
            JDBC.updatePassword(user, password);
        }
        if(role != JDBC.getRole(user)){
            JDBC.updateRole(user, role);
        }
        UserData.refresh();

    }


    public ModifyUser(BaseFrame baseFrame, User user){
        setSize(600, 600);
        setLayout(null);
        // Title

        JLabel title = new JLabel();
        title.setText("Modify User");
        title.setBounds(0, 20, 600, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 35));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        // Modify Button


        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/modifyButton.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/modifyButtonHover.png")));


        JButton modifyButton = new JButton();
        modifyButton.setBounds(0, 430, 600, 102);
        modifyButton.setCursor(cursor);
        modifyButton.setIcon(imgButton);
        modifyButton.setRolloverIcon(imgHover);
        modifyButton.setFocusPainted(false);
        modifyButton.setContentAreaFilled(false);
        modifyButton.setBorderPainted(false);
        modifyButton.setHorizontalAlignment(SwingConstants.CENTER);
        modifyButton.addActionListener(e ->{
            modifyButton(e, user);
            baseFrame.dispose();
        });

        // first_name Label

        JLabel firstnameLabel = new JLabel();
        firstnameLabel.setText("First name:");
        firstnameLabel.setBounds(30, 80, baseFrame.getWidth() - 30, 30);
        firstnameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(firstnameLabel);

        // first_name textfield

        firstNameField = new JTextField();
        firstNameField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        firstNameField.setBounds(30, 110, baseFrame.getWidth() - 90, 40);
        add(firstNameField);

        // last_name Label

        JLabel lastnameLabel = new JLabel();
        lastnameLabel.setText("Last name:");
        lastnameLabel.setBounds(30, 155, baseFrame.getWidth() - 30, 30);
        lastnameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(lastnameLabel);

        // second_name textfield

        secondNameField = new JTextField();
        secondNameField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        secondNameField.setBounds(30, 185, baseFrame.getWidth() - 90, 40);
        add(secondNameField);

        // email Label

        JLabel emailLabel = new JLabel();
        emailLabel.setText("Email:");
        emailLabel.setBounds(30, 230, baseFrame.getWidth() - 30, 30);
        emailLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(emailLabel);

        // second_name textfield

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        emailField.setBounds(30, 260, baseFrame.getWidth() - 90, 40);
        add(emailField);

        // Pass Label

        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        passwordLabel.setBounds(30, 305, baseFrame.getWidth() - 30, 30);
        passwordLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(passwordLabel);

        // password field

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        passwordField.setBounds(30, 335, baseFrame.getWidth() - 90, 40);
        add(passwordField);

        // Button client

        clientButton = new JRadioButton();
        clientButton.setText("Client");
        clientButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN,25));
        clientButton.setBounds(30, 380, 100, 30);
        clientButton.setFocusPainted(false);
        clientButton.setBorderPainted(false);

        // Button admin

        adminButton = new JRadioButton();
        adminButton.setText("Admin");
        adminButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN,25));
        adminButton.setBounds(160, 380, baseFrame.getWidth() - 30, 30);
        adminButton.setFocusPainted(false);
        adminButton.setBorderPainted(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(clientButton);
        buttonGroup.add(adminButton);
        if(JDBC.getRole(user) == 1){
            adminButton.setSelected(true);
        }
        else{
            clientButton.setSelected(true);
        }
        add(clientButton);
        add(adminButton);
        add(modifyButton);
    }
}
