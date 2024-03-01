package data_base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JDBCTest {

    @Test
    void testValidateLoginShouldNotBeNull1(){
        JDBC jdbc = new JDBC();
        assertNotNull(jdbc.validateLogin("denys.cot03@e-uvt.ro", "admin"));
    }

    @Test
    void testValidateLoginShouldNotBeNull2(){
        JDBC jdbc = new JDBC();
        assertNotNull(jdbc.validateLogin("testare@gmail.com", "Testare123@"));
    }
    @Test
    void testValidateLoginShouldBeNull(){
        JDBC jdbc = new JDBC();
        assertNull(jdbc.validateLogin("notInDateBase@gmail.com", "ShoudlNotBeHere"));
    }

    @Test
    void testRegister(){
        JDBC jdbc = new JDBC();
        assertTrue(jdbc.register("Ion", "Ionesco", "testare2@gmail.com", "Testare123@2"));
    }
    @Test
    void testCheckEmailShouldBeTrue1(){
        JDBC jdbc = new JDBC();
        assertTrue(jdbc.checkEmail("denys.cot03@e-uvt.ro"));
    }
    @Test
    void testCheckEmailShouldbeTrue2(){
        JDBC jdbc = new JDBC();
        assertTrue(jdbc.checkEmail("denysflo13@gmail.com"));
    }
    @Test
    void testCheckEmailShouldNotBeTrue(){
        JDBC jdbc = new JDBC();
        assertFalse(jdbc.checkEmail("ShouldNotBe@InDatabase.com"));
    }
}