package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Clasa MainPanel.
 * Reprezintă prima scena a aplicației, una dintre scenele afisate pe BaseFrame
 */

/**
 * <h1>Clasa MainPanel</h1>
 * Clasa MainPanel extinde clasa JPanel fiind scena pe care sunt puse componentele
 * pentru prima interfață afișată de aplicație
 * <p>
 * <b>Note:</b> Reprezintă una din interfețele ”desenete” pe BaseFrame
 * Aceasta are ca scop întâmpinarea utilizatorului cu o interfață intuitivă
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class MainPanel extends JPanel {

    /**
     * Constructorul clasei MainPanel pe care sunt adăugate
     * elementele intefeței grafice
     *
     * @param mainFrame Reprezinta scheletul pe care urmeaza sa fie „desenată” scena
     */

    public MainPanel(BaseFrame mainFrame){
        setSize(1280, 720);
        setLayout(null);
        setBackground(Color.decode("#F2F2F2"));

        //Logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/test6.png")));
        Image newImage = logo.getImage().getScaledInstance(505, 528, Image.SCALE_DEFAULT);
        logo = new ImageIcon(newImage);
        JLabel imgLabel = new JLabel(logo);
        imgLabel.setBounds((int) (getWidth() * 0.55), 115, logo.getIconWidth(), logo.getIconHeight());
        add(imgLabel);

        //Title
        JLabel travelAppLabel = new JLabel("Travel Agency");
        travelAppLabel.setBounds(0, 20, super.getWidth(), 60);
        travelAppLabel.setFont(new Font("Dialog", Font.BOLD, 50));
        travelAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(travelAppLabel);

        //Motto
        JLabel label1 = new JLabel();
        label1.setText("Experience your");
        label1.setBounds(120, 115, 500, 65);
        label1.setFont(new Font("Dialog", Font.BOLD, 35));
        add(label1);

        JLabel label2 = new JLabel();
        label2.setText("Dream vacation!");
        label2.setBounds(200, 160, 500, 65);
        label2.setFont(new Font("Dialog", Font.BOLD, 35));
        add(label2);
        //Cursor
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);

        //Buttons

        //ImgButton
        ImageIcon imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/loginButton.png")));
        ImageIcon imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/hoverLoginButton.png")));

        JButton loginButton = new JButton();
        loginButton.setBounds(100, 260, 432, 102);
        loginButton.setCursor(cursor);
        loginButton.setIcon(imgButton);
        loginButton.setRolloverIcon(imgHover);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(e -> {
            mainFrame.showLoginPanel();
        });
        add(loginButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/registerButton.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/hoverRegisterButton.png")));

        JButton registerButton = new JButton();
        registerButton.setBounds(100, 390, 432, 102);
        registerButton.setCursor(cursor);
        registerButton.setIcon(imgButton);
        registerButton.setRolloverIcon(imgHover);
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.addActionListener(e -> {
            mainFrame.showRegisterPanel();
        });
        add(registerButton);

        imgButton = new ImageIcon(Objects.requireNonNull(getClass().getResource("/exitButton.png")));
        imgHover = new ImageIcon(Objects.requireNonNull(getClass().getResource("/hoverExitButton.png")));

        JButton exitButton = new JButton();
        exitButton.setBounds(100, 520, 432, 102);
        exitButton.setCursor(cursor);
        exitButton.setIcon(imgButton);
        exitButton.setRolloverIcon(imgHover);
        exitButton.setFocusPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        add(exitButton);
    }
}
