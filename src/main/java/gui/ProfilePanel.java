package gui;

import data_base.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Clasa ProfilePanel.
 * Reprezintă meniul de baza pentru utilizatorul client
 */

/**
 * <h1>Clasa ProfilePanel</h1>
 * Clasa ProfilePanel extinde clasa JPanel fiind scena pe care sunt puse componentele
 * pentru interfața de baza a meniului de Client
 * <p>
 * <b>Note:</b> Reprezintă una din interfețele ”desenete” pe BaseFrame
 * Aceasta oferă accesul la diversele opțiuni pe care rolul de client le are
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class ProfilePanel extends JPanel {

    private static BaseFrame AddReservation = new BaseFrame(600, 800);;
    private void reservation(ActionEvent e, User user){
        AddReservation addR = new AddReservation(AddReservation, user);
        AddReservation.add(addR);
        AddReservation.setLocationRelativeTo(null);
        AddReservation.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        AddReservation.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AddReservation.dispose();
            }
        });
        AddReservation.setVisible(true);
    }
    /**
     * Constructorul clasei ProfilePanel în care sunt așezate
     * obiectele aferente interfeței
     *
     * @param mainFrame Reprezinta scheletul pe care urmeaza sa fie „desenată” scena
     * @param user Obiect de tipul User ce incapsulează datele utilizatorului.
     */

    public ProfilePanel(BaseFrame mainFrame, User user){
        setSize(1280, 720);
        setLayout(null);
        setBackground(Color.decode("#F2F2F2"));

        //Logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/test6.png")));
        Image newImage = logo.getImage().getScaledInstance(505, 528, Image.SCALE_SMOOTH);
        logo = new ImageIcon(newImage);
        JLabel imgLabel = new JLabel(logo);
        imgLabel.setBounds((int) (getWidth() * 0.55), (int) (getHeight() * 0.12), logo.getIconWidth(), logo.getIconHeight());
        add(imgLabel);

        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        //Profile
        ImageIcon profile = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Profile.png")));
        Image newProfile = profile.getImage().getScaledInstance(60 , 60, Image.SCALE_SMOOTH);
        profile = new ImageIcon(newProfile);

        JButton profileButton = new JButton();
        profileButton.setBounds(10, 10, profile.getIconWidth(), profile.getIconHeight());
        profileButton.setCursor(cursor);
        profileButton.setIcon(profile);
        profileButton.setFocusPainted(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setBorderPainted(false);
        profileButton.addActionListener(e->{
            mainFrame.showProfilePanel(user);
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
        JLabel travelAppLabel = new JLabel("Travel Agency");
        travelAppLabel.setBounds(0, 20, super.getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);


        //Buttons

        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ViewButton.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ViewButtonhover.png")));

        JButton viewButton = new JButton();
        viewButton.setBounds(100, 170, 432, 102);
        viewButton.setCursor(cursor);
        viewButton.setIcon(imgButton);
        viewButton.setRolloverIcon(imgHover);
        viewButton.setFocusPainted(false);
        viewButton.setContentAreaFilled(false);
        viewButton.setBorderPainted(false);
        viewButton.addActionListener(e->{
            mainFrame.showReservationData(user);
        });
        add(viewButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/MakeButton.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/MakeButtonhover.png")));

        JButton makeButton = new JButton();
        makeButton.setBounds(100, 300, 432, 102);
        makeButton.setCursor(cursor);
        makeButton.setIcon(imgButton);
        makeButton.setRolloverIcon(imgHover);
        makeButton.setFocusPainted(false);
        makeButton.setContentAreaFilled(false);
        makeButton.setBorderPainted(false);
        makeButton.addActionListener(e->{
            reservation(e, user);
        });
        add(makeButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/offer.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/offerH.png")));

        JButton offerButton = new JButton();
        offerButton.setBounds(100, 430, 432, 102);
        offerButton.setCursor(cursor);
        offerButton.setIcon(imgButton);
        offerButton.setRolloverIcon(imgHover);
        offerButton.setFocusPainted(false);
        offerButton.setContentAreaFilled(false);
        offerButton.setBorderPainted(false);
        offerButton.addActionListener(e->{
            mainFrame.showOfferData(user);
        });
        add(offerButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logout.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logoutH.png")));

        JButton logout = new JButton();
        logout.setBounds(100, 560, 432, 102);
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


    }
}
