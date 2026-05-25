import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    Login login = new Login("sjl_1", "Sh&&ddd@ke99!", "Syph", "Smith");

    //Login True and False
    @Test
    void testLoginSuccess() {
        assertEquals("Welcome Syph Smith. It is great to see you again", login.loginUser("Syph",
                "Sh&&ddd@ke99!")
        );
    }

    @Test
    void testLoginFail() {
        assertEquals("Username or Password incorrect. Please try again.", login.loginUser("wrong",
                "wrong")
        );
    }
}