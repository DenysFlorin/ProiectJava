package data_base;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import gui.ReservationData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clasa care conține metodele pentru interacțiunea cu baza de date folosind JDBC.
 */
/**
 * <h1>Clasa de gestionare a interacțiunii cu baza de date</h1>
 * Clasa JDBC are rolul de legătură între aplicație și baza de date.
 * validarea proceselor de login si register,
 * popularea tabelelor cu date disponibile utilizatorilor cu rol de administrator
 * <p>
 * <b>Note:</b> Am utilizat serverul MySQL, această clasa gestionează activități precum:
 * validarea proceselor de login și register,
 * popularea tabelelor cu date oferite utilizatorilor cu rol de admin.
 * @author Denys Cot
 * @version 1.0
 * @since 2023-12-30 */

public class JDBC {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/agentiep3";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "begoforohe52";
    private static final String DB_SCHEMA = "agentiep3";

    /**
     * Metoda pentru a valida procesul de Login.
     *
     * @param email Adresa de email a utilizatorului.
     * @param password Parola utilizatorului.
     * @return un Obiect de tip User caracterizat de datele utilizatorului.
     */

    public static User validateLogin(String email, String password){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE email = ? AND password = ?"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                boolean valid = resultSet.getBoolean("validate");
                return new User(userId, email, password, first_name, last_name, valid);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda pentru realizarea procesului de Register
     *
     * @param first_name Prenumele utilizatorului
     * @param last_name Numele utilizatorului
     * @param email Adresa de email a utilizatorului
     * @param password Parola aleasă a utilizatorului
     * @return
     */

    public static boolean register(String first_name, String last_name, String email, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            if (!checkEmail(email)) {
                boolean valid = false;

                // Insert user data
                try (PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO user(first_name, last_name, email, password, validate) VALUES(?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, first_name);
                    preparedStatement.setString(2, last_name);
                    preparedStatement.setString(3, email);
                    preparedStatement.setString(4, password);
                    preparedStatement.setBoolean(5, valid);

                    int affectedRows = preparedStatement.executeUpdate();

                    if (affectedRows == 0) {
                        return false;
                    }

                    // Retrieve generated user_id
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);

                            // Insert user role
                            try (PreparedStatement roleStatement = connection.prepareStatement(
                                    "INSERT INTO user_roles(user_id, role_id) VALUES(?, ?)")) {
                                roleStatement.setInt(1, id);
                                roleStatement.setInt(2, 2);

                                int roleAffectedRows = roleStatement.executeUpdate();

                                return roleAffectedRows > 0;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metoda care verifica dacă adresa este în baza de date
     *
     * Metoda verifică în tabela de utilizatori dacă email-ul exista
     *
     * @param email Adresa de email a utilizatorului
     * @return adevărat dacă adresa există în baza de date, false altfel
     */
    public static boolean checkEmail(String email){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE email = ?"
            );
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Metodă care populează campurile tabelului cu datele utilizatorilor
     *
     * Această Metodă populează tabelul afișat în meniul adminului
     * Sunt oferite coloanele: user, first name, last name, password, email și role.
     *
     * @param model Obiect de tip DefaultTableModel după care se structurează tabelul
     */
    public static void populateTable(DefaultTableModel model) {
        model.setColumnIdentifiers(new String[]{"User id", "First Name", "Last Name", "Password", "Email", "Role"});
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, u.validate, ur.role_id," +
                            "CASE WHEN ur.role_id = 1 THEN 'admin' WHEN ur.role_id = 2 THEN 'user' ELSE 'unknown' END AS role " +
                            "FROM user u " +
                            "JOIN user_roles ur ON u.user_id = ur.user_id")) {

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("user_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("password"),
                            resultSet.getString("email"),
                            resultSet.getString("role")
                    };

                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodă care verifică existența schemei bazei de date
     *
     * Această metodă verifică existența schemei bazei de date
     * urmand ca tabelele aferente acesteia sa fie create in cazul in care aceasta nu exista
     *
     * @return True daca schema exista, fals daca nu.
     */
    public static boolean verifyExistence(){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ?"
            );
            preparedStatement.setString(1, DB_SCHEMA);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count > 0;
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metoda createSchema creaza schema bazei de date în cazul în care aceasta nu există deja
     *
     * @return
     */
    public static void createSchema(){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "CREATE SCHEMA ?"
            );
            preparedStatement.setString(1, DB_SCHEMA);

            preparedStatement.executeQuery();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda createTables produce tabelele bazei de date, aceasta este folosită
     * în cazul în care acestea nu există.
     *
     */
    public static void createTables(){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement m_destinationTable = connection.prepareStatement(
                    "CREATE TABLE `destination` (\n" +
                            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `name` varchar(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`)\n" +
                            ")"
            );
            PreparedStatement m_hotelServicesTable = connection.prepareStatement(
                    "CREATE TABLE `hotel_services` (\n" +
                            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `price` decimal(10,2) NOT NULL,\n" +
                            "  `room_type` varchar(45) NOT NULL,\n" +
                            "  `destination_id` int NOT NULL,\n" +
                            "  PRIMARY KEY (`id`)\n" +
                            ")"
            );
            PreparedStatement m_offerTable = connection.prepareStatement(
                    "CREATE TABLE `offer` (\n" +
                            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `hotel_id` int NOT NULL,\n" +
                            "  `transport_id` int NOT NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  KEY `hotel_id_idx` (`hotel_id`,`transport_id`),\n" +
                            "  KEY `transport_id_idx` (`transport_id`),\n" +
                            "  CONSTRAINT `hotelService_id` FOREIGN KEY (`hotel_id`) REFERENCES `hotel_services` (`id`),\n" +
                            "  CONSTRAINT `transportService_id` FOREIGN KEY (`transport_id`) REFERENCES `transport_services` (`id`)\n" +
                            ")"
            );
            PreparedStatement m_reservationTable = connection.prepareStatement(
                    "CREATE TABLE `reservation` (\n" +
                            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `user_id` int NOT NULL,\n" +
                            "  `offer_id` int NOT NULL,\n" +
                            "  `total_price` decimal(10,2) NOT NULL,\n" +
                            "  `check_in` datetime NOT NULL,\n" +
                            "  `check_out` datetime NOT NULL,\n" +
                            "  `refunded` tinyint DEFAULT NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  KEY `user_id_idx` (`user_id`),\n" +
                            "  KEY `offer_id_idx` (`offer_id`),\n" +
                            "  CONSTRAINT `custo_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),\n" +
                            "  CONSTRAINT `off_id` FOREIGN KEY (`offer_id`) REFERENCES `offer` (`id`)\n" +
                            ")"
            );
            PreparedStatement m_rolesTable = connection.prepareStatement(
                    "CREATE TABLE `roles` (\n" +
                            "  `roles_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `roles_name` varchar(45) NOT NULL,\n" +
                            "  PRIMARY KEY (`roles_id`)\n" +
                            ")"
            );
            PreparedStatement m_transporServicesTable = connection.prepareStatement(
                    "CREATE TABLE `transport_services` (\n" +
                            "  `id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `from_id` int NOT NULL,\n" +
                            "  `to_id` int NOT NULL,\n" +
                            "  `price` decimal(10,2) NOT NULL,\n" +
                            "  PRIMARY KEY (`id`),\n" +
                            "  KEY `from_id_idx` (`from_id`),\n" +
                            "  KEY `to_id_idx` (`to_id`),\n" +
                            "  CONSTRAINT `from_id` FOREIGN KEY (`from_id`) REFERENCES `destination` (`id`),\n" +
                            "  CONSTRAINT `to_id` FOREIGN KEY (`to_id`) REFERENCES `destination` (`id`)\n" +
                            ")"
            );
            PreparedStatement m_userTable = connection.prepareStatement(
                    "CREATE TABLE `user` (\n" +
                            "  `user_id` int NOT NULL AUTO_INCREMENT,\n" +
                            "  `first_name` varchar(45) NOT NULL,\n" +
                            "  `email` varchar(45) NOT NULL,\n" +
                            "  `password` varchar(45) NOT NULL,\n" +
                            "  `last_name` varchar(45) NOT NULL,\n" +
                            "  `validate` tinyint NOT NULL,\n" +
                            "  PRIMARY KEY (`user_id`)\n" +
                            ")"
            );
            PreparedStatement m_userRolesTable = connection.prepareStatement(
                    "CREATE TABLE `user_roles` (\n" +
                            "  `user_id` int NOT NULL,\n" +
                            "  `role_id` int NOT NULL,\n" +
                            "  PRIMARY KEY (`user_id`,`role_id`),\n" +
                            "  KEY `role_id_idx` (`role_id`),\n" +
                            "  CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`roles_id`),\n" +
                            "  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)\n" +
                            ")"
            );
            m_userTable.executeQuery();
            m_rolesTable.executeQuery();
            m_userRolesTable.executeQuery();
            m_destinationTable.executeQuery();
            m_hotelServicesTable.executeQuery();
            m_transporServicesTable.executeQuery();
            m_offerTable.executeQuery();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * Metoda deleteUserById sterge din baza de date un utilizator
     * folosindu-se id-ul acestuia
     *
     * @param userId Id-ul utilizatorului trimis spre stergere
     * @return True daca stergerea s-a produs, false altfel
     */
    public static boolean deleteUserById(int userId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement deleteFromRoleTable = connection.prepareStatement(
                    "DELETE FROM user_roles WHERE user_id = ?"
            );
            deleteFromRoleTable.setInt(1, userId);
            int deletedRolesRow = deleteFromRoleTable.executeUpdate();

            PreparedStatement deleteFromUserTable = connection.prepareStatement(
                    "DELETE FROM user WHERE user_id = ?"
            );
            deleteFromUserTable.setInt(1, userId);
            int deletedUserRow = deleteFromUserTable.executeUpdate();

            return deletedUserRow > 0 && deletedRolesRow > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metoda searchUserById cauta in baza de date un utilizator
     * returnandu-l in cazul in care il gaseste.
     * @param userId Id-ul utilizatorului de cautat
     * @return Returneaza un Obiect de tip user ce inglobeaza datele utilizatorului daca acesta este in baza de date
     * In caz contrar returneaza null
     */
    public static User searchUserById(int userId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE user_id = ?"
            );
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                boolean valid = resultSet.getBoolean("validate");
                return new User(userId, email, password, first_name, last_name, valid);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda getRole returneaza rolul utilizatorului in contextul aplicatiei
     * (client, admin)
     *
     * @param user Utilizatorul al carui rol se cauta
     * @return 1 daca utilizatorul este admin, 2 daca este client
     */
    public static int getRole(User user){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT role_id FROM user_roles WHERE user_id = ?"
            );
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getInt("role_id");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * Metoda updateFirstName actualizeaza prenumele unui utilizator
     *
     * @param user Utilizatorul al carui prenume vrem sa actualizam
     * @param first_name Noul prenume
     */
    public static void updateFirstName(User user, String first_name){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user SET first_name = ? WHERE user_id = ?"
            );
            preparedStatement.setString(1, first_name);
            preparedStatement.setInt(2, user.getId());

            int rowsUpdate = preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda updateLastName, lucreaza in mod similar cu metoda updateFirstName
     * Aceasta gestioneaza actualizarea numelui de familie.
     * @param user Utilizatorul al carui nume trebuie actualizat
     * @param last_name Noul nume al utilizatorului.
     */
    public static void updateLastName(User user, String last_name){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user SET last_name = ? WHERE user_id = ?"
            );
            preparedStatement.setString(1, last_name);
            preparedStatement.setInt(2, user.getId());

            int rowsUpdate = preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda updateEmail, actualizeaza emailul unui utilizator.
     * @param user Utilizatorul al carui email trebuie acctualizat
     * @param email Noul sau email
     */
    public static void updateEmail(User user, String email){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user SET email = ? WHERE user_id = ?"
            );
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, user.getId());

            int rowsUpdate = preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda updatePassword, actualizeaza parola unui utilizator
     *
     * @param user Utilizatorul a carui parola dorim sa actualizam
     * @param password Noua sa parola.
     */
    public static void updatePassword(User user, String password){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user SET password = ? WHERE user_id = ?"
            );
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, user.getId());

            int rowsUpdate = preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda updateRole actualizeaza rolul unui utilizator in contextul aplicatiei.
     * @param user Utilizatorul al carui rol se actualizeaza
     * @param role Noul rol al utilizatorului.
     */
    public static void updateRole(User user, int role){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user_roles SET role_id = ? WHERE user_id = ?"
            );
            preparedStatement.setInt(1, role);
            preparedStatement.setInt(2, user.getId());

            int rowsUpdate = preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda care adauga o noua intrare in tabela Destinatiilor
     *
     * @param name Numele Destinatiei
     */
    public static void addDestination(String name){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO destination (name) VALUES (?)"
            );
            preparedStatement.setString(1, name);

            int rowsUpdate = preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda care adauga o noua intrare in tabela serviciilor hoteliere
     *
     * @param type Tipul de Camera
     * @param price Pretul pe noapte.
     */
    public static void addHotel(String type, double price, int destinationId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO hotel_services (price, room_type, destination_id) VALUES (?, ?, ?)"
            );
            preparedStatement.setBigDecimal(1, BigDecimal.valueOf(price));
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, destinationId);

            int rowsUpdate = preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda populateListDestinasions incarca destinatiile existente
     * In tabela destinatii intr-un array de stringuri
     *
     * @return Returneaza un array ce contine numele destinatiilor
     */
    public static String[] populateListDestinations(){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT name FROM destination"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> destination = new ArrayList<>();
            while(resultSet.next()){
                String name = resultSet.getString("name");
                destination.add(name);
            }
            String[] namesArray = destination.toArray(new String[0]);

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return namesArray;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda getIdDestionation cauta id-ul unei destinatii in tabela acestora dupa numele destiantiei
     * @param name Numele Destinatiei de cautat
     * @return id-ul destinatiei
     *
     */
    public static int getIdDestination(String name){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM destination WHERE name = ?"
            );
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("id");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda addTransport adauga o noua intrare in tabela serviciilor de transport
     * @param fromId Id-ul destinatiei de plecare
     * @param toId Id-ul destinatiei finale
     * @param price Pretul transportului
     */
    public static void addTransport(int fromId, int toId, double price){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO transport_services (from_id, to_id, price) VALUE (?, ?, ?)"
            );
            preparedStatement.setInt(1, fromId);
            preparedStatement.setInt(2, toId);
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(price));

            int rowsUpdate = preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda populateListHotels adauga intr-o lista toate serviciile hoteliere
     * dintr-o anumita destinatie
     * @param destinationId Id-ul destinatiei
     * @return Returneaza lista serviciilor hoteliere din destinatia respectiva
     */
    public static String[] populateListHotels(int destinationId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id, price, room_type FROM hotel_services WHERE destination_id = ?"
            );
            preparedStatement.setInt(1, destinationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> hotels = new ArrayList<>();
            while(resultSet.next()){
                String name = resultSet.getInt("id") + ". RoomType: " + resultSet.getString("room_type") + ", Price: " + resultSet.getString("price");
                hotels.add(name);
            }
            String[] namesArray = hotels.toArray(new String[0]);

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return namesArray;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda populateListTransport populeaza un array cu serviciile de transport asociate unei destinatii
     * @param destinationId Id-ul destinatiei dorite
     * @return Un array ce contine serviciile de transport
     */
    public static String[] populateListTransport(int destinationId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id, price FROM transport_services WHERE to_id = ?"
            );
            preparedStatement.setInt(1, destinationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> transports = new ArrayList<>();
            while(resultSet.next()){
                String name = resultSet.getInt("id") + "." + " Price: " + resultSet.getString("price");
                transports.add(name);
            }
            String[] namesArray = transports.toArray(new String[0]);

            resultSet.close();
            preparedStatement.close();
            connection.close();

            return namesArray;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda addOffer adauga o intrare noua in tabelul Offer
     * @param hotelId Id-ul serviciului hotelier ales
     * @param transportId Id-ul serviciul de transport ales
     */
    public static void addOffer(int hotelId, int transportId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO offer (hotel_id, transport_id) VALUE (?, ?)"
            );
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, transportId);

            int rowsUpdate = preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda addReservation adauga o intrare noua in tabelul Reservations
     * @param userId id-ul utilizatorului care a facut rezervarea
     * @param offerId oferta pe care a rezervat-o
     * @param price Pretul intreg al serviciilor alese
     * @param checkIn Data de checkIn
     * @param checkOut Data de checkOut
     */
    public static void addReservation(int userId, int offerId, double price, Date checkIn, Date checkOut){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO reservation (user_id, offer_id, total_price, check_in, check_out, refunded) VALUE (?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, offerId);
            preparedStatement.setDouble(3, price);
            preparedStatement.setDate(4, checkIn);
            preparedStatement.setDate(5, checkOut);
            preparedStatement.setInt(6, 0);

            int rowsUpdate = preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda lasOfferMade returneaza id-ul ultimei oferte facute pentru a realiza o rezervare
     * @return returneaza id-ul ultimei oferte facute
     */
    public static int lastOfferMade(){
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM offer WHERE id = (SELECT MAX(id) FROM offer);"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("id");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda transportPrice returneaza pretul unui anumit serviciu de transport
     * @param transportId Id-ul serviciului de transport
     * @return Returneaza pretul serviciului
     */
    public static double transportPrice(int transportId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT price FROM transport_services WHERE id = ?"
            );
            preparedStatement.setInt(1, transportId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getDouble("price");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda hotelPrice returneaza pretul unui anumit serviciu hotelier
     * @param hotelId Id-ul serviciului hotelier
     * @return Returneaza pretul serviciului
     */
    public static double hotelPrice(int hotelId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT price FROM hotel_services WHERE id = ?"
            );
            preparedStatement.setInt(1, hotelId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getDouble("price");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda populateTableReservation populeaza campurile tabelului in care un
     * utilizator isi poate vedea rezervarile facute si datele acestora
     *
     * @param model Structura tabelului
     * @param userId Id-ul utilizatorului
     */
    public static void populateTableReservation(DefaultTableModel model, int userId) {
        model.setColumnIdentifiers(new String[]{"Reservation_id", "Room_Type", "Hotel_Price","Transport_Price", "From_Destination", "To_Destination", "Total_Price", "CheckIn", "CheckOut", "Refunded"});
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT\n" +
                            "    res.id AS reservation_id,\n" +
                            "    hotel.room_type,\n" +
                            "    hotel.price AS hotel_price,\n" +
                            "    transport.price AS transport_price,\n" +
                            "    from_destination.name AS from_destination_name,\n" +
                            "    to_destination.name AS to_destination_name,\n" +
                            "    res.total_price,\n" +
                            "    res.check_in,\n" +
                            "    res.check_out,\n" +
                            "    CASE\n" +
                            "        WHEN res.refunded = 0 THEN 'No'\n" +
                            "        ELSE 'Yes'\n" +
                            "    END AS refunded\n" +
                            "FROM\n" +
                            "    reservation res\n" +
                            "JOIN\n" +
                            "    offer off ON res.offer_id = off.id\n" +
                            "JOIN\n" +
                            "    hotel_services hotel ON off.hotel_id = hotel.id\n" +
                            "JOIN\n" +
                            "    transport_services transport ON off.transport_id = transport.id\n" +
                            "JOIN\n" +
                            "    destination from_destination ON transport.from_id = from_destination.id\n" +
                            "JOIN\n" +
                            "    destination to_destination ON transport.to_id = to_destination.id\n" +
                            "WHERE\n" +
                            "    res.user_id = ?;")) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("reservation_id"),
                            resultSet.getString("room_type"),
                            resultSet.getDouble("hotel_price"),
                            resultSet.getDouble("transport_price"),
                            resultSet.getString("from_destination_name"),
                            resultSet.getString("to_destination_name"),
                            resultSet.getDouble("total_price"),
                            resultSet.getDate("check_in"),
                            resultSet.getDate("check_out"),
                            resultSet.getString("refunded")
                    };

                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda refund anuleaza rezervarea in cazul in care clientul isi doreste asta
     * @param reservationId
     */
    public static void refund(int reservationId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE reservation SET refunded = ? WHERE id = ?"
            );
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, reservationId);

            int rowsUpdate = preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda refundable verifica daca rezervarea data spre anulare nu este deja anulata
     * @param reservationId
     * @return
     */
    public static boolean refundable(int reservationId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT refunded FROM reservation WHERE id = ?"
            );
            preparedStatement.setInt(1, reservationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt("refunded") == 0){
                    return true;
                }
                else return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metoda populateTableOffer populeaza cu ofertele existente un tabel pentru a oferi
     * utilizatorilor o interfata din care sa poata sa isi rezerve o oferta
     * @param model Structura tabelului
     */
    public static void populateTableOffer(DefaultTableModel model){
        model.setColumnIdentifiers(new String[]{"Offer id", "Room_type", "Hotel_Price", "Transport_Price", "From_Destination", "To_Destination"});
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT\n" +
                            "    o.id AS offer_id,\n" +
                            "    h.room_type,\n" +
                            "    h.price AS hotel_price,\n" +
                            "    t.price AS transport_price,\n" +
                            "    d_from.name AS from_destination,\n" +
                            "    d_to.name AS to_destination\n" +
                            "FROM\n" +
                            "    offer o\n" +
                            "JOIN\n" +
                            "    hotel_services h ON o.hotel_id = h.id\n" +
                            "JOIN\n" +
                            "    transport_services t ON o.transport_id = t.id\n" +
                            "JOIN\n" +
                            "    destination d_from ON t.from_id = d_from.id\n" +
                            "JOIN\n" +
                            "    destination d_to ON t.to_id = d_to.id;")) {

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Object[] rowData = {
                            resultSet.getInt("offer_id"),
                            resultSet.getString("room_type"),
                            resultSet.getString("hotel_price"),
                            resultSet.getString("transport_price"),
                            resultSet.getString("from_destination"),
                            resultSet.getString("to_destination")
                    };

                    model.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda hotelPriceFromOffer ofera pretul serviciului hotelier pentru o oferta anume
     * @param offerId Id-ul ofertei
     * @return Returneaza pretul
     */
    public static double hotelPriceFromOffer(int offerId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT\n" +
                            "    o.id AS offer_id,\n" +
                            "    h.price AS hotel_price\n" +
                            "FROM\n" +
                            "    offer o\n" +
                            "JOIN\n" +
                            "    hotel_services h ON o.hotel_id = h.id\n" +
                            "WHERE\n" +
                            "    o.id = ?;\n"
            );
            preparedStatement.setInt(1, offerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getDouble("hotel_price");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda transportPriceFromOffer ofera pretul serviciului de transport pentru o oferta anume
     * @param offerId Id-ul ofertei
     * @return Returneaza pretul
     */
    public static double transportPriceFromOffer(int offerId){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT\n" +
                            "    o.id AS offer_id,\n" +
                            "    t.price AS transport_price\n" +
                            "FROM\n" +
                            "    offer o\n" +
                            "JOIN\n" +
                            "    transport_services t ON o.transport_id = t.id\n" +
                            "WHERE\n" +
                            "    o.id = ?;\n"
            );
            preparedStatement.setInt(1, offerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getDouble("transport_price");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
}
