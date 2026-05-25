import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    void testMessageLengthSuccess() {
        String shortMessage = "Hi Mike, can you join us for dinner tonight?";
        assertTrue(shortMessage.length() <= 250);
    }

    @Test
    void testMessageLengthFailure() {
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            longMessage.append("a");
        }
        assertTrue(longMessage.toString().length() > 250);
    }

    @Test
    void testRecipientFormatSuccess() {
        String recipient = "+27718693002";
        assertTrue(recipient.startsWith("+27") && recipient.length() == 12);
    }

    @Test
    void testRecipientFormatFailure() {
        String recipient = "08575975889";
        assertFalse(recipient.startsWith("+27") && recipient.length() == 12);
    }
}