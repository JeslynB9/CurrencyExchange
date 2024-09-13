package CurrencyExchange.Users;

import org.junit.*;
import processing.data.JSONObject;
import static org.junit.Assert.*;

public class AdminLoginTest {
    private static AdminLogin adminLogin;
    private static final String JSON_FILEPATH = "src/main/java/resources/test/admin.json";
    private static final int TEST_USER_ID = 1;

    @Before
    public void setup() {
        JSONObject initialJson = new JSONObject();
        adminLogin = new AdminLogin(initialJson, JSON_FILEPATH);
    }

    @Test
    public void testAddUser1() { //general valid case
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Fred", "croissants123"));
        assertTrue(adminLogin.addUser(TEST_USER_ID + 1, "Bob", "noodles456"));
        assertTrue(adminLogin.addUser(TEST_USER_ID + 2, "Henry", "Fried Chicken"));
    }

    @Test
    public void testAddUser2() { //adding existing user ID
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Fred", "croissants123"));
        assertFalse(adminLogin.addUser(TEST_USER_ID, "Bob", "noodles456"));
    }

    @Test
    public void testAddUser3() { //adding user with empty username
        assertFalse(adminLogin.addUser(TEST_USER_ID, "", "password"));
    }

    @Test
    public void testAddUser4() { //adding user null and empty spaces
        assertFalse(adminLogin.addUser(TEST_USER_ID, null, "password"));
        assertFalse(adminLogin.addUser(TEST_USER_ID, null, ""));
        assertFalse(adminLogin.addUser(TEST_USER_ID, null, null));
        assertFalse(adminLogin.addUser(TEST_USER_ID, "beb", null));
        assertFalse(adminLogin.addUser(TEST_USER_ID, "beb", ""));
        assertFalse(adminLogin.addUser(TEST_USER_ID, "", null));
        assertFalse(adminLogin.addUser(TEST_USER_ID, "", "pass"));
        assertFalse(adminLogin.addUser(TEST_USER_ID, "", ""));
    }

    @Test
    public void testAddUser5() { //adding user with special characters in username and password
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry@123", "Fried$Chicken"));
    }

    @Test
    public void testDeleteUser1() { //general valid case
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Fred", "croissants123"));
        assertTrue(adminLogin.deleteUser(TEST_USER_ID));
    }

    @Test
    public void testDeleteUser2() { //deleting non-existent user
        assertFalse(adminLogin.deleteUser(TEST_USER_ID));
    }

    @Test
    public void testDeleteUser3() { //deleting user with no ID (negative case)
        assertFalse(adminLogin.deleteUser(-1)); 
    }

    @Test
    public void testUpdatePassword1() { //general case
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertTrue(adminLogin.updatePassword(TEST_USER_ID, "Fried Chicken", "dobedobap"));
    }

    @Test
    public void testUpdatePassword2() { //null password
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "Fried Chicken", null));
    }

    @Test
    public void testUpdatePassword3() { //null username
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, null, "newPass"));
    }

    @Test
    public void testUpdatePassword4() { //space username and password
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "   ", "   "));
    }

    @Test
    public void testUpdatePassword5() { //empty username and password
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "", ""));
    }

    @Test
    public void testUpdatePassword6() { //special characters in password
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry@123", "Fried$Chicken"));
        assertTrue(adminLogin.updatePassword(TEST_USER_ID, "Fried$Chicken", "new$Password!"));
    }

    @Test
    public void testUpdatePassword7() { //invalid cases
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry@123", "Fried$Chicken"));
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "Fried$Chicken", "")); //no pass 
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "Fried$Chicken", null)); //null pass 
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "Fried$Chicken", "     ")); //empty pass 
        assertFalse(adminLogin.updatePassword(TEST_USER_ID, "Fried Chicken", "     ")); //wrong old pass 


    }

    @Test
    public void testUpdateUsername1() { //general case
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertTrue(adminLogin.updateUsername(TEST_USER_ID, "Jerry"));
    }

    @Test
    public void testUpdateUsername2() { //null new username
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertFalse(adminLogin.updateUsername(TEST_USER_ID, null));
    }

    @Test
    public void testUpdateUsername3() { //empty new username
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertFalse(adminLogin.updateUsername(TEST_USER_ID, ""));
    }

    @Test
    public void testUpdateUsername4() { //user doesn't exist
        assertFalse(adminLogin.updateUsername(TEST_USER_ID, "NewName")); 
    }

    @Test
    public void testCheckLogin1() { //general valid case
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertTrue(adminLogin.checkLogin(TEST_USER_ID, "Henry", "Fried Chicken"));
        
        assertTrue(adminLogin.addUser(TEST_USER_ID + 1, "Terry", "Fried Fish"));
        assertTrue(adminLogin.checkLogin(TEST_USER_ID + 1, "Fried Fish"));
    }

    @Test
    public void testCheckLogin2() { //username does not exist
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertFalse(adminLogin.checkLogin(TEST_USER_ID + 1, "Greg", "Fried Chicken"));

        assertFalse(adminLogin.checkLogin(TEST_USER_ID + 1, "Fried Chicken"));
    }

    @Test
    public void testCheckLogin3() { //wrong password
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertFalse(adminLogin.checkLogin(TEST_USER_ID, "Henry", "Dumplings"));

        assertTrue(adminLogin.addUser(TEST_USER_ID + 1, "Corn", "Popcorn"));
        assertFalse(adminLogin.checkLogin(TEST_USER_ID + 1, "grubs"));
    }

    @Test
    public void testCheckLogin4() { //wrong user and pass 
        assertTrue(adminLogin.addUser(TEST_USER_ID, "Henry", "Fried Chicken"));
        assertFalse(adminLogin.checkLogin(TEST_USER_ID, "Rob", "Dumplings"));

        assertTrue(adminLogin.addUser(TEST_USER_ID + 1, "Pen", "Paper"));
        assertFalse(adminLogin.checkLogin(TEST_USER_ID + 1, "Rob", "Dumplings"));
    }
}
