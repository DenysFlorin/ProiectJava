package gui;

import data_base.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterPanelTest {
    @Test
    void testValidPasswordShouldBeTrue(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertTrue(registerPanel.validatePassword("Popescu123@"));
    }
    @Test
    void testValidPasswordShouldBeTrue2(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertTrue(registerPanel.validatePassword("Testare123@"));
    }
    @Test
    void testValidPasswordShouldNotBeTrue(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertFalse(registerPanel.validatePassword("Nope"));
    }
    @Test
    void testValidateEmailShoudlBeTrue1(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertTrue(registerPanel.validateEmail("denys.cot03@e-uvt.ro"));
    }
    @Test
    void testValidateEmailShoudlBeTrue2(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertTrue(registerPanel.validateEmail("denysflo13@gmail.com"));
    }
    @Test
    void testValidateEmailShoudlNotBeTrue(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertFalse(registerPanel.validateEmail("notValidgmail@"));
    }
    @Test
    void testValidateNameShoudlBeTrue1(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertTrue(registerPanel.validateName("Denys"));
    }
    @Test
    void testValidateNameShoudlBeTrue2(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertTrue(registerPanel.validateName("Florin"));
    }
    @Test
    void testValidateNameShouldNotBeTrue1(){
        RegisterPanel registerPanel = new RegisterPanel(new BaseFrame());
        assertFalse(registerPanel.validateName("1Andrei"));
    }

}