import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationTest {

    Registration reg = new Registration();

    //Username Test
    @Test
    void testUsernameCorrect() {
        assertTrue(reg.checkUserName("sjl_1"));
    }

    @Test
    void testUsernameIncorrect() {
        assertFalse(reg.checkUserName("Syph!!!!!!!"));
    }

    //Password Test
    @Test
    void testPasswordCorrect() {
        assertTrue(reg.checkpassWord("Sh&&ddd@ke99!"));
    }

    @Test
    void testPasswordIncorrect() {
        assertFalse(reg.checkpassWord("password"));
    }

    //Cellphone Test
    @Test
    void testCellphoneCorrect() {
        assertTrue(reg.checkPhoneNo("+27838968976"));
    }

    @Test
    void testCellphoneIncorrect() {
        assertFalse(reg.checkPhoneNo("08966553"));
    }
}