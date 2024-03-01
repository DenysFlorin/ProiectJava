package gui;

import data_base.JDBC;
import data_base.User;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Objects;
import java.util.Properties;


public class AddReservation extends JPanel {
    private String destinationSelected = null;
    private String hotelSelected = null;
    private String transportSelected = null;
    private java.util.Date firstDateSelected = null;
    private java.util.Date secondDateSelected = null;
    private JComboBox<String> hotelBox;
    private JComboBox<String> transportBox;

    public void add(ActionEvent e, BaseFrame baseFrame, User user){
        int hotelId = Integer.parseInt(hotelSelected.substring(0, hotelSelected.indexOf('.')));
        int transportId = Integer.parseInt(transportSelected.substring(0, transportSelected.indexOf('.')));
        if(hotelId > 0 && transportId > 0 && firstDateSelected != null && secondDateSelected != null){
            JDBC.addOffer(hotelId, transportId);
            int offerId = JDBC.lastOfferMade();
            long durationMillis = secondDateSelected.getTime() - firstDateSelected.getTime();
            long durationDays = durationMillis / (1000 * 60 * 60 * 24);
            double price = JDBC.transportPrice(transportId) + JDBC.hotelPrice(hotelId) * durationDays;

            java.sql.Date check_in = new java.sql.Date(firstDateSelected.getTime());
            java.sql.Date check_out = new java.sql.Date(secondDateSelected.getTime());
            JDBC.addReservation(user.getId(), offerId, price, check_in, check_out);
            JOptionPane.showMessageDialog(baseFrame, "Reservation competed!");
        }
        else{
            JOptionPane.showMessageDialog(baseFrame, "Please complete every field");
        }
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
    public AddReservation(BaseFrame baseFrame, User user){
        setSize(600, 800);

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
        addButton.setBounds(0, 630, 600, 102);
        addButton.setCursor(cursor);
        addButton.setIcon(imgButton);
        addButton.setRolloverIcon(imgHover);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setHorizontalAlignment(SwingConstants.CENTER);
        addButton.addActionListener(e ->{
            add(e, baseFrame, user);
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

        UtilDateModel model1 = new UtilDateModel();
        Properties p1 = new Properties();
        p1.put("text.today", "Today");
        p1.put("text.month", "Month");
        p1.put("text.year", "Year");
        JDatePanelImpl firstDate = new JDatePanelImpl(model1);
        firstDate.setBounds(60, 320, 200, 200);
        firstDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                firstDateSelected = model1.getValue();
            }
        });
        add(firstDate);

        UtilDateModel model2 = new UtilDateModel();
        Properties p2 = new Properties();
        p2.put("text.today", "Today");
        p2.put("text.month", "Month");
        p2.put("text.year", "Year");
        JDatePanelImpl secondDate = new JDatePanelImpl(model2);
        secondDate.setBounds(340, 320, 200, 200);
        secondDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondDateSelected = model2.getValue();
            }
        });
        add(secondDate);
    }
}
