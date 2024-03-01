package gui;

import data_base.JDBC;
import data_base.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class ReservationData extends JPanel {
    private int selected;
    private static JTable table;
    private JScrollPane scroll;
    private static DefaultTableModel model = new DefaultTableModel();
    private static int[] tabelSizes = {60, 100, 100, 100, 120, 120, 100, 100, 100, 80};


    private void refund(ActionEvent e, BaseFrame baseFrame, User user){
        if(JDBC.refundable(selected)){
            JDBC.refund(selected);
            JOptionPane.showMessageDialog(baseFrame, "Refund completed");
        }
        else{
            JOptionPane.showMessageDialog(baseFrame, "Please complete every field");
        }
        ReservationData.refresh(user);

    }
    public static void refresh(User user){
        JDBC.populateTableReservation(model, user.getId());
        model.fireTableDataChanged();
        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tabelSizes[i]);
        }
        table.repaint();
    }

    private void reservationTableMouseClicked(MouseEvent e){
        int selectedIndex = table.getSelectedRow();
        int reservationId = (int) table.getValueAt(selectedIndex, 0);
        selected = reservationId;
    }

    public ReservationData(BaseFrame baseFrame, User user){
        setSize(1280, 720);
        setLayout(null);
        setBackground(Color.decode("#F2F2F2"));

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        //Profile
        ImageIcon profile = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Profile.png")));
        Image newProfile = profile.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        profile = new ImageIcon(newProfile);

        JButton profileButton = new JButton();
        profileButton.setBounds(10, 10, profile.getIconWidth(), profile.getIconHeight());
        profileButton.setCursor(cursor);
        profileButton.setIcon(profile);
        profileButton.setFocusPainted(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setBorderPainted(false);
        profileButton.addActionListener(e -> {
            baseFrame.showProfilePanel(user);
        });
        add(profileButton);

        //Welcome text
        String welcomeMessage = "<html>" +
                "<body style='text-align:center'>" +
                "<b>Hello, " + user.getFirst_name() + " " + user.getLast_name() +
                "</b>" + "</body></html>";
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(80, 20, getWidth() - 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(welcomeMessageLabel);

        //Title
        JLabel travelAppLabel = new JLabel("Manage Reservations");
        travelAppLabel.setBounds(0, 20, super.getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);

        model = new DefaultTableModel();
        JDBC.populateTableReservation(model, user.getId());
        table = new JTable(model);
        table.setSelectionBackground(Color.decode("#8AC5FF"));
        table.setBackground(Color.WHITE);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i = 0; i < 10; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tabelSizes[i]);
        }
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                reservationTableMouseClicked(e);
            }
        });
        scroll = new JScrollPane(table);
        scroll.setBounds(153, 96, 900, 400);
        scroll.setBackground(Color.WHITE);
        add(scroll);

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/refund.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/refundH.png")));

        JButton refundButton = new JButton();
        refundButton.setBounds(450, 550, 262, 102);
        refundButton.setCursor(cursor);
        refundButton.setIcon(imgButton);
        refundButton.setRolloverIcon(imgHover);
        refundButton.setFocusPainted(false);
        refundButton.setContentAreaFilled(false);
        refundButton.setBorderPainted(false);
        refundButton.setCursor(cursor);
        refundButton.addActionListener(e ->{
            refund(e, baseFrame, user);
        });
        add(refundButton);
    }
}
