package gui;

import javax.swing.*;
import data_base.User;

import java.util.Objects;

/**
 * Clasa BaseFrame.
 * Reprezintă fereastra de baza pe care sunt afișate diferite interfețe
 */

/**
 * <h1>Clasa fereastră de bază</h1>
 * Clasa BaseFrame reprezintă fereastra lansată la rularea aplicației
 * aceasta afișează diverse interfețe
 * <p>
 * <b>Note:</b> Reprezintă scheletul pe care urmează să fie ”desenate”
 * diverse scene ale aplicației. *
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class BaseFrame extends JFrame {
    private JPanel currentPanel;

    public BaseFrame(){
        initialize();
    }
    public BaseFrame(int w, int h){ initiere(w, h); }

    /**
     * Metodă de inițiere a ferestrei de bază
     *
     * Această metodă inițiază JFrame-ul de bază și setează aspecte generale precum
     * Titlu, Mărime, locație de deschidere etc.
     */
    private void initiere(int w, int h){
        setTitle("Travel Agency");

        setSize(w, h);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setResizable(false);

        setLocationRelativeTo(null);
    }
    private void initialize(){
        setTitle("Travel Agency");

        setSize(1280, 720);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setResizable(false);

        setLocationRelativeTo(null);

        showMainPanel();
    }

    /**
     * Metoda showLoginPanel
     *
     * Această metodă afișează pe fereastra de bază scena pentru logare
     */
    public void showLoginPanel(){
        if(currentPanel != null){
            getContentPane().remove(currentPanel);
        }
        currentPanel = new LoginPanel(this);
        add(currentPanel);

        revalidate();
        repaint();
    }

    /**
     * Metoda showRegisterPanel
     *
     * Această metodă afișează pe fereastra de bază scena pentru înregistrare
     */
    public void showRegisterPanel() {
        if (currentPanel != null) {
            getContentPane().remove(currentPanel);
        }

        currentPanel = new RegisterPanel(this);
        add(currentPanel);

        revalidate();
        repaint();
    }

    /**
     * Metoda showProfilePanel
     *
     * Această metodă afișează pe fereastra de bază scena aferentă profilului utilizatorului
     *
     * @param user Obiect ce înglobează datele aferente utilizatorului.
     */
    public void showProfilePanel(User user) {
        if (currentPanel != null) {
            getContentPane().remove(currentPanel);
        }

        currentPanel = new ProfilePanel(this, user);
        add(currentPanel);

        revalidate();
        repaint();
    }

    /**
     * Metoda showAdminPanel
     *
     * Această metodă, similar metodei showProfilePanel afișează scena aferentă unui admin
     *
     * @param user Obiect ce înglobează datele unui administrator
     */

    public void showAdminPanel(User user){
        if(currentPanel != null){
            getContentPane().remove(currentPanel);
        }
        currentPanel = new AdminPanel(this, user);
        add(currentPanel);

        revalidate();
        repaint();
    }

    /**
     * Metodă showUserData
     *
     * Această metodă, afișează scena unde sunt prezentate datele utilizatorilor.
     *
     * @param user
     */

    public void showUserData(User user){
        if(currentPanel != null){
            getContentPane().remove(currentPanel);
        }
        currentPanel = new UserData(this, user);
        add(currentPanel);

        revalidate();
        repaint();
    }

    /**
     * Metodă showMainPanel
     *
     * Această metodă este utilizată pentru a afișa prima scena a aplicației
     */

    public void showMainPanel(){
        if(currentPanel != null){
            getContentPane().remove(currentPanel);
        }
        currentPanel = new MainPanel(this);
        add(currentPanel);

        revalidate();
        repaint();
    }



    public void showReservationData(User user){
        if(currentPanel != null){
            getContentPane().remove(currentPanel);
        }
        currentPanel = new ReservationData(this, user);
        add(currentPanel);

        revalidate();
        repaint();
    }
    public void showOfferData(User user){
        if(currentPanel != null){
            getContentPane().remove(currentPanel);
        }
        currentPanel = new OfferData(this, user);
        add(currentPanel);

        revalidate();
        repaint();
    }
}

