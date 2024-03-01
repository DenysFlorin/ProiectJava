package gui;

import data_base.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class AddOffer extends JPanel {
    private String destinationSelected = null;
    private String hotelSelected = null;
    private String transportSelected = null;
    private JComboBox<String> hotelBox;
    private JComboBox<String> transportBox;
    public void add(ActionEvent e, BaseFrame baseFrame){
        int hotelId = Integer.parseInt(hotelSelected.substring(0, hotelSelected.indexOf('.')));
        int transportId = Integer.parseInt(transportSelected.substring(0, transportSelected.indexOf('.')));
        if(hotelId > 0 && transportId > 0){
            JDBC.addOffer(hotelId, transportId);
        }
        else{
            JOptionPane.showMessageDialog(baseFrame, "Please complete every field");
        }
        UserData.refresh();
    }

    private void updateOptions() {
        if (destinationSelected != null && !destinationSelected.isEmpty()) {
            String[] hotels = JDBC.populateListHotels(JDBC.getIdDestination(destinationSelected));
            String[] transport = JDBC.populateListTransport(JDBC.getIdDestination(destinationSelected));
            hotelBox.setModel(new DefaultComboBoxModel<>(hotels));
            transportBox.setModel(new DefaultComboBoxModel<>(transport));
        } else {
            hotelBox.setModel(new DefaultComboBoxModel<>());
            transportBox.setModel(new DefaultComboBoxModel<>());
        }
    }

    public AddOffer(BaseFrame baseFrame){
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
            add(e, baseFrame);
            baseFrame.dispose();
        });
        add(addButton);

        String[] destinations = JDBC.populateListDestinations();

        JLabel destinationsLabel = new JLabel();
        destinationsLabel.setText("Destination:");
        destinationsLabel.setBounds(30, 80, baseFrame.getWidth() - 30, 30);
        destinationsLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(destinationsLabel);

        JComboBox<String> destinationBox = new JComboBox<>(destinations);
        destinationBox.setSelectedIndex(0);
        destinationBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> destinationBox = (JComboBox<String>) e.getSource();
                destinationSelected = (String) destinationBox.getSelectedItem();
                updateOptions();
                repaint();
            }
        });
        destinationBox.setBounds(30, 110, baseFrame.getWidth() - 90, 40);
        destinationBox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        add(destinationBox);


        JLabel hotelLabel = new JLabel();
        hotelLabel.setBounds(30, 155, baseFrame.getWidth() - 30, 30);
        hotelLabel.setText("Hotel Service:");
        hotelLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(hotelLabel);

        hotelBox = new JComboBox<>();
        hotelBox.setBounds(30, 185, baseFrame.getWidth() - 90, 40);
        hotelBox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        hotelBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> hotelBox = (JComboBox<String>) e.getSource();
                hotelSelected = (String) hotelBox.getSelectedItem();
            }
        });
        add(hotelBox);

        JLabel transportLabel = new JLabel();
        transportLabel.setBounds(30, 230, baseFrame.getWidth() - 30, 30);
        transportLabel.setText("Transport Service:");
        transportLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 25));
        add(transportLabel);

        transportBox = new JComboBox<>();
        transportBox.setBounds(30, 260, baseFrame.getWidth() - 90, 40);
        transportBox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        transportBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> hotelBox = (JComboBox<String>) e.getSource();
                transportSelected = (String) transportBox.getSelectedItem();
            }
        });
        add(transportBox);
        updateOptions();
    }
}
