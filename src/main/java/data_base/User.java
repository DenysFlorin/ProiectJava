package data_base;

/**
 * Clasa User, înglobează datele aferente unui utilizator al aplicației
 *
 *
 */
/**
 * <h1>Clasa de obiecte aferente tabelului de utilizatori din baza de date </h1>
 * Conține câmpurile din tabela utilizatorilor, acestea fiind finale deoarece
 * nu este nevoie să fie modificate pe parcursul rulării.
 *
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-30 */

public class User {
    private final int id;
    private final String email;
    private final String password;
    private final String first_name;
    private final String last_name;
    private final boolean valid;

    /**
     * Constructor pentru clasa User
     *
     * @param id Id-ul utilizatorului în baza de date
     * @param email Adresa de email a utilizatorului
     * @param password Parola utilizatorului
     * @param first_name Prenumele utilizatorului
     * @param last_name Numele utilizatorului
     * @param valid
     */

    public User(int id, String email, String password, String first_name, String last_name, boolean valid){
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
        this.password = password;
        this.email = email;
        this.valid = valid;
    }

    public User(){
        this.id = -1;
        this.email = "";
        this.password = "";
        this.first_name = "";
        this.last_name = "";
        this.valid = false;
    }

    /**
     * Getter pentru id
     *
     * @return id-ul utilizatorului
     */
    public int getId() {
        return id;
    }

    /**
     * Getter pentru first name
     *
     * @return Prenumele utilizatorului
     */

    public String getFirst_name() {
        return first_name;
    }

    /**
     * Getter pentru last name
     *
     * @return Numele utilizatorului
     */

    public String getLast_name() {
        return last_name;
    }

    /**
     * Getter pentru parola
     *
     * @return Parola utilizatorului
     */

    public String getPassword() {
        return password;
    }

    /**
     * Getter pentru adresa de email
     *
     * @return Adresa de email a utilizatorului
     */

    public String getEmail() {
        return email;
    }

    public boolean getValid(){ return valid;}

}
