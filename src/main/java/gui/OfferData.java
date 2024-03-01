package gui;

import data_base.JDBC;
import data_base.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class OfferData extends JPanel {
    private int selectedOffer;
    private static JTable table;
    private JScrollPane scroll;
    private static DefaultTableModel model;
    private static int[] tabelSizes = {80, 140, 140, 140, 140, 140};
    public static BaseFrame adminModifyUserFrame = new BaseFrame(600, 600);
    public static BaseFrame addReservation = new BaseFrame(600, 600);

    private void reserve(ActionEvent e, BaseFrame baseFrame, User user){
        makeReservation mk = new makeReservation(addReservation, user, selectedOffer);
        addReservation.add(mk);
        addReservation.setLocationRelativeTo(null);
        addReservation.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addReservation.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addReservation.dispose();
            }
        });
        addReservation.setVisible(true);
    }

    private void usersTableMouseClicked(MouseEvent e){
        int selectedIndex = table.getSelectedRow();
        int offerId = (int) table.getValueAt(selectedIndex, 0);
        selectedOffer = offerId;
        System.out.println(selectedOffer);
    }
    public static void refresh(User user){
        JDBC.populateTableOffer(model);
        model.fireTableDataChanged();
        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tabelSizes[i]);
        }
        table.repaint();
    }
    public OfferData(BaseFrame baseFrame, User user){
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
        JLabel travelAppLabel = new JLabel("View Offers");
        travelAppLabel.setBounds(0, 20, super.getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);

        model = new DefaultTableModel();
        JDBC.populateTableOffer(model);
        table = new JTable(model);
        table.setSelectionBackground(Color.decode("#8AC5FF"));
        table.setBackground(Color.WHITE);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tabelSizes[i]);
        }
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                usersTableMouseClicked(e);
            }
        });
        scroll = new JScrollPane(table);
        scroll.setBounds(153, 96, 700, 400);
        scroll.setBackground(Color.WHITE);
        add(scroll);

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/reserve.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/reserveH.png")));

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
            reserve(e, baseFrame, user);
        });
        add(refundButton);
    }
}
