package gui;

import data_base.JDBC;
import data_base.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Clasa AdminPanel.
 * Reprezintă meniul de baza pentru utilizatorii cu rol de admin
 */

/**
 * <h1>Clasa AdminPanel</h1>
 * Clasa AdminPanel extinde clasa JPanel fiind scena pe care sunt puse componentele
 * pentru interfața de baza a meniului de Admin
 * <p>
 * <b>Note:</b> Reprezintă una din interfețele ”desenete” pe BaseFrame
 * Aceasta oferă accesul la diversele opțiuni pe care rolul de admin le are
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class AdminPanel extends JPanel {

    public static BaseFrame addOffer = new BaseFrame(600, 600);

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
    /**
     * Constructorul clasei AdminPanel în care sunt așezate
     * obiectele aferente interfeței
     *
     * @param mainFrame Reprezinta scheletul pe care urmeaza sa fie „desenată” scena
     * @param user Obiect de tipul User ce incapsulează datele utilizatorului.
     */
    public AdminPanel(BaseFrame mainFrame, User user){
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
        JLabel adminTitle = new JLabel("Admin Panel");
        adminTitle.setBounds(0, 20, super.getWidth(), 60);
        adminTitle.setFont(new Font("Dialog", Font.BOLD, 50));
        adminTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(adminTitle);

        //Buttons
        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/UserData.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/UserDataHover.png")));

        JButton userDataButton = new JButton();
        userDataButton.setBounds(100, 170, 432, 102);
        userDataButton.setCursor(cursor);
        userDataButton.setIcon(imgButton);
        userDataButton.setRolloverIcon(imgHover);
        userDataButton.setFocusPainted(false);
        userDataButton.setContentAreaFilled(false);
        userDataButton.setBorderPainted(false);
        userDataButton.addActionListener(e -> {
            mainFrame.showUserData(user);
        });;
        add(userDataButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/makeNewOffer.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/makeNewOfferHover.png")));

        JButton makeNewOffer = new JButton();
        makeNewOffer.setBounds(100, 300, 432, 102);
        makeNewOffer.setCursor(cursor);
        makeNewOffer.setIcon(imgButton);
        makeNewOffer.setRolloverIcon(imgHover);
        makeNewOffer.setFocusPainted(false);
        makeNewOffer.setContentAreaFilled(false);
        makeNewOffer.setBorderPainted(false);
        makeNewOffer.addActionListener(e->{
            offer(e);
        });
        add(makeNewOffer);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logout.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logoutH.png")));

        JButton logout = new JButton();
        logout.setBounds(100, 430, 432, 102);
        logout.setCursor(cursor);
        logout.setIcon(imgButton);
        logout.setRolloverIcon(imgHover);
        logout.setFocusPainted(false);
        logout.setContentAreaFilled(false);
        logout.setBorderPainted(false);
        logout.addActionListener(e->{
            mainFrame.showMainPanel();
        });
        add(logout);

        //Logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/test6.png")));
        Image newImage = logo.getImage().getScaledInstance(505, 528, Image.SCALE_SMOOTH);
        logo = new ImageIcon(newImage);
        JLabel imgLabel = new JLabel(logo);
        imgLabel.setBounds((int) (getWidth() * 0.55), (int) (getHeight() * 0.12), logo.getIconWidth(), logo.getIconHeight());
        add(imgLabel);
    }

}
