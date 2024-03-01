package gui;

import data_base.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class AddHotel extends JPanel {
    private JTextField roomTypeField;
    private JTextField priceField;
    private String destination;

    public void add(ActionEvent e){
        String type = roomTypeField.getText();
        double price = Double.valueOf(priceField.getText());
        int destinationId = JDBC.getIdDestination(destination);

        if(!type.isEmpty() && price > 0 && destinationId > 0){
            JDBC.addHotel(type, price, destinationId);
        }
        UserData.refresh();
    }

    public AddHotel(BaseFrame baseFrame){
        setSize(600, 600);

        setLayout(null);

        JLabel title = new JLabel();
        title.setText("Add Hotel");
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

        // type label

        JLabel nameLabel = new JLabel();
        nameLabel.setBounds(30, 80, baseFrame.getWidth() - 30, 30);
        nameLabel.setText("Type:");
        nameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(nameLabel);

        // first_name textfield

        roomTypeField = new JTextField();
        roomTypeField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        roomTypeField.setBounds(30, 110, baseFrame.getWidth() - 90, 40);
        add(roomTypeField);

        JLabel priceLabel = new JLabel();
        priceLabel.setText("Price:");
        priceLabel.setBounds(30, 155, baseFrame.getWidth() - 30, 30);
        priceLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(priceLabel);

        // second_name textfield

        priceField = new JTextField();
        priceField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        priceField.setBounds(30, 185, baseFrame.getWidth() - 90, 40);
        add(priceField);

        String[] destinations = JDBC.populateListDestinations();

        JLabel destinationsLabel = new JLabel();
        destinationsLabel.setText("Destination:");
        destinationsLabel.setBounds(30, 230, baseFrame.getWidth() - 30, 30);
        destinationsLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(destinationsLabel);

        JComboBox<String> destinationBox = new JComboBox<>(destinations);
        destinationBox.setSelectedIndex(0);
        destinationBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> fromBox = (JComboBox<String>) e.getSource();
                destination = (String) fromBox.getSelectedItem();
            }
        });
        destinationBox.setBounds(30, 260, baseFrame.getWidth() - 90, 40);
        destinationBox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        add(destinationBox);
    }
}
