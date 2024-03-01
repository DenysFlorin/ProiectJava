package gui;

import data_base.*;

import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Clasa UserData.
 * Reprezintă interfața în care adminul poate vizualiza datele tuturor utilizatorilor.
 */

/**
 * <h1>Clasa UserData</h1>
 * Clasa UserData extinde clasa JPanel fiind scena pe care sunt oferite
 * informațiile cu privire la toți utilizatorii aplicației
 *
 * <p>
 * <b>Note:</b> Reprezintă una din interfețele ”desenete” pe BaseFrame
 *
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class UserData extends JPanel {


    private User selectedUser = null;
    private static JTable table;
    private JScrollPane scroll;
    private static DefaultTableModel model;
    private static int[] tabelSizes = {60, 120, 120, 220, 200, 120};
    public static BaseFrame adminModifyUserFrame = new BaseFrame(600, 600);
    public static BaseFrame addDest = new BaseFrame(600, 600);
    public static BaseFrame addHotel = new BaseFrame(600, 600);
    public static BaseFrame addTrans = new BaseFrame(600, 600);
    public static BaseFrame addOffer = new BaseFrame(600, 600);

    /**
     * Metoda usersTableMouseClicked creaza un obiect de tip User
     * cu datele dintr-un anumit rand selectat din tabel, urmand ca
     * acesta sa fie folosit pentru alte operatii
     *
     * @param e
     */
    private void usersTableMouseClicked(MouseEvent e){
        int selectedIndex = table.getSelectedRow();
        int userId = (int) table.getValueAt(selectedIndex, 0);
        selectedUser = JDBC.searchUserById(userId);
    }


    /**
     * Metoda delete sterge utilizatorul determinat de datele obiectului user selectat
     * din tabel.
     *
     * @param e
     */
    private void delete(ActionEvent e){
        JDBC.deleteUserById(selectedUser.getId());
        JDBC.populateTable(model);
        model.fireTableDataChanged();
        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tabelSizes[i]);
        }
        table.repaint();
    }

    public static void refresh(){
        JDBC.populateTable(model);
        model.fireTableDataChanged();
        for (int i = 0; i < 6; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(tabelSizes[i]);
        }
        table.repaint();
    }
    /**
     * Metoda modify ofera o interfata in care se pot modifica datele unui utilizator.
     * @param e
     */
    private void modify(ActionEvent e){
        ModifyUser modifyUser = new ModifyUser(adminModifyUserFrame, selectedUser);
        adminModifyUserFrame.add(modifyUser);
        adminModifyUserFrame.setLocationRelativeTo(null);
        adminModifyUserFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        adminModifyUserFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                adminModifyUserFrame.dispose();
            }
        });
        adminModifyUserFrame.setVisible(true);
    }

    private void destination(ActionEvent e){
        AddDestination addDestination = new AddDestination(addDest);
        addDest.add(addDestination);
        addDest.setLocationRelativeTo(null);
        addDest.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addDest.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addDest.dispose();
            }
        });
        addDest.setVisible(true);
    }
    private void hotel(ActionEvent e){
        AddHotel addH = new AddHotel(addHotel);
        addHotel.add(addH);
        addHotel.setLocationRelativeTo(null);
        addHotel.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addHotel.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addHotel.dispose();
            }
        });
        addHotel.setVisible(true);
    }
    private void transport(ActionEvent e){
        addTransport addT = new addTransport(addTrans);
        addTrans.add(addT);
        addTrans.setLocationRelativeTo(null);
        addTrans.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addTrans.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addTrans.dispose();
            }
        });
        addTrans.setVisible(true);
    }
    private void offer(ActionEvent e){
        AddOffer addO = new AddOffer(addOffer);
        addOffer.add(addO);
        addOffer.setLocationRelativeTo(null);
        addOffer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addOffer.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addOffer.dispose();
            }
        });
        addOffer.setVisible(true);
    }

    public UserData(BaseFrame mainFrame, User user){
        setSize(1280, 720);
        setLayout(null);
        setBackground(Color.decode("#F2F2F2"));

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        //Profile
        ImageIcon profile = new ImageIcon(Objects.requireNonNull(getClass().getResource("/adminProfile.png")));
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
            mainFrame.showAdminPanel(user);
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
        JLabel travelAppLabel = new JLabel("Manage Users");
        travelAppLabel.setBounds(0, 20, super.getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);

        model = new DefaultTableModel();
        JDBC.populateTable(model);
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
        scroll.setBounds(353, 96, 843, 400);
        scroll.setBackground(Color.WHITE);
        add(scroll);

        //Buttons

        //1. Delete Button

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/deleteButton.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/deleteButtonHover.png")));

        JButton deleteButton = new JButton();
        deleteButton.setBounds(450, 550, 262, 102);
        deleteButton.setCursor(cursor);
        deleteButton.setIcon(imgButton);
        deleteButton.setRolloverIcon(imgHover);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setCursor(cursor);
        deleteButton.addActionListener(e ->{
            delete(e);
        });
        add(deleteButton);

        //2. Modify Button


        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/modifyButton.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/modifyButtonHover.png")));


        JButton modifyButton = new JButton();
        modifyButton.setBounds(850, 550, 262, 102);
        modifyButton.setCursor(cursor);
        modifyButton.setIcon(imgButton);
        modifyButton.setRolloverIcon(imgHover);
        modifyButton.setFocusPainted(false);
        modifyButton.setContentAreaFilled(false);
        modifyButton.setBorderPainted(false);
        modifyButton.addActionListener(e ->{
            modify(e);
        });
        add(modifyButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addD.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addDH.png")));


        JButton addDestionation = new JButton();
        addDestionation.setBounds(10, 100, 212, 82);
        addDestionation.setCursor(cursor);
        addDestionation.setIcon(imgButton);
        addDestionation.setRolloverIcon(imgHover);
        addDestionation.setFocusPainted(false);
        addDestionation.setContentAreaFilled(false);
        addDestionation.setBorderPainted(false);
        addDestionation.addActionListener(e ->{
            destination(e);
        });
        add(addDestionation);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addH.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addHH.png")));

        JButton addHotel = new JButton();
        addHotel.setBounds(10, 182, 212, 82);
        addHotel.setIcon(imgButton);
        addHotel.setCursor(cursor);
        addHotel.setRolloverIcon(imgHover);
        addHotel.setFocusPainted(false);
        addHotel.setContentAreaFilled(false);
        addHotel.setBorderPainted(false);
        addHotel.addActionListener(e ->{
            hotel(e);
        });
        add(addHotel);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addT.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addTH.png")));

        JButton addTransport = new JButton();
        addTransport.setBounds(10, 346, 212, 82);
        addTransport.setIcon(imgButton);
        addTransport.setCursor(cursor);
        addTransport.setRolloverIcon(imgHover);
        addTransport.setFocusPainted(false);
        addTransport.setContentAreaFilled(false);
        addTransport.setBorderPainted(false);
        addTransport.addActionListener(e ->{
            transport(e);
        });
        add(addTransport);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addO.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/addOH.png")));

        JButton addOffer = new JButton();
        addOffer.setBounds(10, 428, 212, 82);
        addOffer.setIcon(imgButton);
        addOffer.setCursor(cursor);
        addOffer.setRolloverIcon(imgHover);
        addOffer.setFocusPainted(false);
        addOffer.setContentAreaFilled(false);
        addOffer.setBorderPainted(false);
        addOffer.addActionListener(e->{
            offer(e);
        });
        add(addOffer);
    }
}
