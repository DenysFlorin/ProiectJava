import data_base.JDBC;
import gui.BaseFrame;
import javax.swing.*;

/**
 * Clasa de lansare a aplicației.
 * Acest program creează și afișează o fereastră de bază Swing.
 */

/**
 * <h1>Clasa de lansare a aplicației</h1>
 * Clasa AppLauncher lansează aplicația prin inițierea și afișarea
 * unui ferestre de bază Swing
 * <p>
 * <b>Note:</b> Pe această fereastră urmează să fie ”desenate”
 * diverse scene ale aplicației. *
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-29 */

public class AppLauncher {

    /**
     * Metoda principală care lansează aplicația.
     *
     *
     * @param args Argumentele liniei de comandă (nu sunt utilizate în acest exemplu).
     * @return Nu returnează nimic.
     */

    public static void main(String[] args) {
        // Inițializează fereastra de bază

        if(!JDBC.verifyExistence()){
            JDBC.createSchema();
            JDBC.createTables();
        }
        SwingUtilities.invokeLater(() -> {BaseFrame baseFrame = new BaseFrame();

            // Face fereastra vizibilă
        baseFrame.setVisible(true);
        });
    }
}