package gui;

import data_base.User;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class NewServicePanel extends JPanel {
    public NewServicePanel(BaseFrame mainFrame, User user){
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

    }
}
