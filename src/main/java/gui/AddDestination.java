package gui;

import data_base.JDBC;
import data_base.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class AddDestination extends JPanel {
    private JTextField nameField;

    public void add(ActionEvent e){
        String name = nameField.getText();

        if(!name.isEmpty()){
            JDBC.addDestination(name);
        }
        UserData.refresh();
    }

    public AddDestination(BaseFrame baseFrame){
        setSize(600, 600);
        setLayout(null);

        JLabel title = new JLabel();
        title.setText("Add Destination");
        title.setBounds(0, 20, 600, 50);
        title.setFont(new Font("Segoe UI", Font.BOLD, 35));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/SmallAdd.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/SmallAddH.png")));


        JButton addButton = new JButton();
        addButton.setBounds(0, 430, 600, 102);
        addButton.setCursor(cursor);
        addButton.setIcon(imgButton);
        addButton.setRolloverIcon(imgHover);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setHorizontalAlignment(SwingConstants.CENTER);
        addButton.addActionListener(e ->{
            add(e);
            baseFrame.dispose();
        });
        add(addButton);

        // name label

        JLabel nameLabel = new JLabel();
        nameLabel.setBounds(30, 80, baseFrame.getWidth() - 30, 30);
        nameLabel.setText("Name:");
        nameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(nameLabel);

        // first_name textfield

        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        nameField.setBounds(30, 110, baseFrame.getWidth() - 90, 40);
        add(nameField);

    }
}
