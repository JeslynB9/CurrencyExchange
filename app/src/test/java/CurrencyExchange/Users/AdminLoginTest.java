package CurrencyExchange.Users;

import org.junit.*;
import processing.data.JSONObject;
import static org.junit.Assert.*;

public class AdminLoginTest {
    private static AdminLogin AdminLogin;

    @Before
    public void setup() {
        String jsonFilepath = "src/main/java/resources/test/admin.json";

        JSONObject initialJson = new JSONObject();

        JSONObject obj = new JSONObject();
        obj.setString("john", "john1234");
        obj.setString("kate", "1234kate");
        obj.setString("anony", "anony9876");

        AdminLogin = new AdminLogin(initialJson, jsonFilepath);
    }

    @Test 
    public void testAddUser1() { //general valid case 
        assertTrue(AdminLogin.addUser("Fred", "croissants123"));
        assertTrue(AdminLogin.addUser("Bob", "noodles456"));
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));

    }   
    
    @Test
    public void testAddUser2() { //adding existing user 
        assertTrue(AdminLogin.addUser("Bob", "RaMeN"));
    }

    @Test
    public void testAddUser3() { //adding user with empty username
        assertFalse(AdminLogin.addUser("", "password"));
    }

    @Test
    public void testAddUser4() { //adding null username 
        assertFalse(AdminLogin.addUser(null, "password"));
    }

    @Test 
    public void testAddUser5() {
        assertTrue(AdminLogin.addUser("Henry@123", "Fried$Chicken"));
    }
    
    @Test
    public void testDeleteUser1() { //general valid case 
        assertTrue(AdminLogin.addUser("Fred", "croissants123"));
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertTrue(AdminLogin.deleteUser("Fred"));
    }

    @Test
    public void testDeleteUser2() { //deleting non-exisent user
        assertFalse(AdminLogin.deleteUser("Spongebob"));
    }

    @Test 
    public void testDeleteUser3() { //deleting user with no characters 
        assertFalse(AdminLogin.deleteUser(""));
    }

    @Test
    public void testUpdatePassword1() { //general case 
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertTrue(AdminLogin.updatePassword("Henry","Fried Chicken", "dobedobap"));
    }

    @Test
    public void testUpdatePassword2() { //null password
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertFalse(AdminLogin.updatePassword("Henry", "Fried Chicken", null));
    }

    @Test
    public void testUpdatePassword3() { //null username
        assertFalse(AdminLogin.updatePassword(null, "Fried Chicken", "nullName"));
    }

    @Test
    public void testUpdatePassword4() { //space username and password
        assertFalse(AdminLogin.updatePassword("   ", "oldPass", "   "));
    }

    @Test
    public void testUpdatePassword5() { //empty username and password
        assertFalse(AdminLogin.updatePassword("","", ""));
    }

    @Test
    public void testUpdatePassword6() { //special characters
        assertTrue(AdminLogin.addUser("Henry@123", "Fried$Chicken"));
        assertTrue(AdminLogin.updatePassword("Henry@123", "Fried$Chicken", "new$Password!"));
    }

    @Test
    public void testUpdateUser1() { //general case 
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertTrue(AdminLogin.updateUser("Henry", "Jerry"));
    }

    @Test
    public void testUpdateUser2() { //null new username
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertFalse(AdminLogin.updateUser("Henry", null));
    }

    @Test
    public void testUpdateUser3() { //no new username
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertFalse(AdminLogin.updateUser("Henry", ""));
    }

    @Test 
    public void testUpdateUser4() { //user doesnt exist 
        assertFalse(AdminLogin.updateUser("Tiger", ""));
    }

    @Test
    public void testCheckLogin1() { //general valid case 
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertTrue(AdminLogin.checkLogin("Henry", "Fried Chicken"));
    }

    @Test
    public void testCheckLogin2() { //username doesnt exist 
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertFalse(AdminLogin.checkLogin("Greg", "Fried Chicken"));
    }

    @Test
    public void testCheckLogin3() { //wrong password 
        assertTrue(AdminLogin.addUser("Henry", "Fried Chicken"));
        assertFalse(AdminLogin.checkLogin("Henry", "Dumplings"));
    }
}
