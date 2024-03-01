package gui;

import data_base.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class addTransport extends JPanel {
    private JTextField priceField;
    private String toSelected;
    private String fromSelected;
    public void add(ActionEvent e, BaseFrame baseFrame){
        double price = Double.valueOf(priceField.getText());
        int fromId = JDBC.getIdDestination(fromSelected);
        int toId = JDBC.getIdDestination(toSelected);
        if(price > 0 && fromId > 0 && toId > 0 && fromId != toId){
            JDBC.addTransport(fromId, toId, price);
        }
        else{
            JOptionPane.showMessageDialog(baseFrame, "Please fill up every field");
        }
        UserData.refresh();
    }

    public addTransport(BaseFrame baseFrame){
        setSize(600, 600);
        setLayout(null);

        JLabel title = new JLabel();
        title.setText("Add Transport");
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
            add(e, baseFrame);
            baseFrame.dispose();
        });
        add(addButton);

        JLabel priceLabel = new JLabel();
        priceLabel.setText("Price:");
        priceLabel.setBounds(30, 80, baseFrame.getWidth() - 30, 30);
        priceLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(priceLabel);

        // price textfield

        priceField = new JTextField();
        priceField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        priceField.setBounds(30, 110, baseFrame.getWidth() - 90, 40);
        add(priceField);

        String[] destinations = JDBC.populateListDestinations();

        JLabel fromLabel = new JLabel();
        fromLabel.setText("From:");
        fromLabel.setBounds(30, 155, baseFrame.getWidth() - 30, 30);
        fromLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(fromLabel);

        JComboBox<String> fromBox = new JComboBox<>(destinations);
        fromBox.setSelectedIndex(0);
        fromBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> fromBox = (JComboBox<String>) e.getSource();
                fromSelected = (String) fromBox.getSelectedItem();
            }
        });
        fromBox.setBounds(30, 185, baseFrame.getWidth() - 90, 40);
        fromBox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        add(fromBox);

        JLabel toLabel = new JLabel();
        toLabel.setText("From:");
        toLabel.setBounds(30, 230, baseFrame.getWidth() - 30, 30);
        toLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(toLabel);

        JComboBox<String> toBox = new JComboBox<>(destinations);
        toBox.setSelectedIndex(0);
        toBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> toBox = (JComboBox<String>) e.getSource();
                toSelected = (String) toBox.getSelectedItem();
            }
        });
        toBox.setBounds(30, 260, baseFrame.getWidth() - 90, 40);
        toBox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        add(toBox);

    }
}
