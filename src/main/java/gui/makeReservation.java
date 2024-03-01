package gui;

import data_base.JDBC;
import data_base.User;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Properties;

public class makeReservation extends JPanel {
    private java.util.Date firstDateSelected = null;
    private java.util.Date secondDateSelected = null;

    public void add(ActionEvent e, BaseFrame baseFrame, User user, int offerId){
        if(firstDateSelected != null && secondDateSelected != null){
            long durationMillis = secondDateSelected.getTime() - firstDateSelected.getTime();
            long durationDays = durationMillis / (1000 * 60 * 60 * 24);
            double price = JDBC.hotelPriceFromOffer(offerId) * durationDays + JDBC.transportPriceFromOffer(offerId);
            System.out.println(offerId);
            java.sql.Date check_in = new java.sql.Date(firstDateSelected.getTime());
            java.sql.Date check_out = new java.sql.Date(secondDateSelected.getTime());
            JDBC.addReservation(user.getId(), offerId, price, check_in, check_out);
            JOptionPane.showMessageDialog(baseFrame, "Reservation competed!");
        }
        else{
            JOptionPane.showMessageDialog(baseFrame, "Please complete every field");
        }
    }

    public makeReservation(BaseFrame baseFrame, User user, int offerId){
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
        addButton.setBounds(0, 430, 600, 102);
        addButton.setCursor(cursor);
        addButton.setIcon(imgButton);
        addButton.setRolloverIcon(imgHover);
        addButton.setFocusPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setHorizontalAlignment(SwingConstants.CENTER);
        addButton.addActionListener(e ->{
            add(e, baseFrame, user, offerId);
            baseFrame.dispose();
        });
        add(addButton);

        UtilDateModel model1 = new UtilDateModel();
        Properties p1 = new Properties();
        p1.put("text.today", "Today");
        p1.put("text.month", "Month");
        p1.put("text.year", "Year");
        JDatePanelImpl firstDate = new JDatePanelImpl(model1);
        firstDate.setBounds(60, 150, 200, 200);
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
        secondDate.setBounds(340, 150, 200, 200);
        secondDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondDateSelected = model2.getValue();
            }
        });
        add(secondDate);
    }
}
