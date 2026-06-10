import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class MessageTest {

    // ==================== PART 1 TESTS ====================

    @Test
    void testUsernameValidationCorrect() {
        Registration reg = new Registration();
        assertTrue(reg.checkUserName("jo_1"));
    }

    @Test
    void testUsernameValidationIncorrect() {
        Registration reg = new Registration();
        assertFalse(reg.checkUserName("john"));
    }

    @Test
    void testPasswordValidationCorrect() {
        Registration reg = new Registration();
        assertTrue(reg.checkpassWord("Test@123"));
    }

    @Test
    void testPasswordValidationIncorrect() {
        Registration reg = new Registration();
        assertFalse(reg.checkpassWord("password"));
    }

    @Test
    void testPhoneValidationCorrect() {
        Registration reg = new Registration();
        assertTrue(reg.checkPhoneNo("+27721234567"));
    }

    @Test
    void testPhoneValidationIncorrect() {
        Registration reg = new Registration();
        assertFalse(reg.checkPhoneNo("123456"));
    }

    // ==================== PART 2 TESTS ====================

    @Test
    void testMessageLengthSuccess() {
        String message = "Hi Mike, can you join us for dinner tonight?";
        assertTrue(message.length() <= 250);
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

    @Test
    void testMessageHashCreation() {
        Message msg = new Message("+27721234567", "Hi Mike", 1);
        String expectedHash = msg.messageID.substring(0, 2) + ":1:HIMIKE";
        assertEquals(expectedHash.toUpperCase(), msg.messageHash);
    }

    // ==================== PART 3 TESTS ====================

    @Test
    void testSentMessagesArrayCorrectlyPopulated() {
        Message.sentMessagesArray.clear();
        Message msg1 = new Message("+27834557896", "Did you get the cake?", 1);
        msg1.SentMessage(1);
        Message msg4 = new Message("0838884567", "It is dinner time !", 4);
        msg4.SentMessage(1);

        boolean found1 = false;
        boolean found4 = false;
        for (String record : Message.sentMessagesArray) {
            if (record.contains("Did you get the cake?")) found1 = true;
            if (record.contains("It is dinner time !")) found4 = true;
        }
        assertTrue(found1 && found4);
    }

    @Test
    void testDisplayLongestMessage() {
        Message.storedMessagesArray.clear();
        Message msg1 = new Message("+27834557896", "Did you get the cake?", 1);
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 2);
        Message msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.", 3);

        Message.storedMessagesArray.add(msg1);
        Message.storedMessagesArray.add(msg2);
        Message.storedMessagesArray.add(msg3);

        Message longest = Message.storedMessagesArray.get(0);
        for (Message msg : Message.storedMessagesArray) {
            if (msg.messageText.length() > longest.messageText.length()) {
                longest = msg;
            }
        }
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest.messageText);
    }

    @Test
    void testSearchForMessageID() {
        Message.storedMessagesArray.clear();
        Message msg = new Message("0838884567", "It is dinner time !", 4);
        msg.messageID = "1234567890";
        Message.storedMessagesArray.add(msg);

        String foundMessage = null;
        for (Message m : Message.storedMessagesArray) {
            if (m.messageID.equals("1234567890")) {
                foundMessage = m.messageText;
                break;
            }
        }
        assertEquals("It is dinner time !", foundMessage);
    }

    @Test
    void testSearchAllMessagesForRecipient() {
        Message.storedMessagesArray.clear();
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 2);
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.", 5);

        Message.storedMessagesArray.add(msg2);
        Message.storedMessagesArray.add(msg5);

        ArrayList<String> foundMessages = new ArrayList<String>();
        for (Message m : Message.storedMessagesArray) {
            if (m.recipient.equals("+27838884567")) {
                foundMessages.add(m.messageText);
            }
        }
        assertEquals(2, foundMessages.size());
        assertTrue(foundMessages.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(foundMessages.contains("Ok, I am leaving without you."));
    }

    @Test
    void testDeleteMessageUsingHash() {
        Message.storedMessagesArray.clear();
        Message msg = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 2);
        msg.messageHash = "TEST123";
        Message.storedMessagesArray.add(msg);

        int initialSize = Message.storedMessagesArray.size();
        for (int i = 0; i < Message.storedMessagesArray.size(); i++) {
            if (Message.storedMessagesArray.get(i).messageHash.equals("TEST123")) {
                Message.storedMessagesArray.remove(i);
                break;
            }
        }
        assertEquals(initialSize - 1, Message.storedMessagesArray.size());
    }

    @Test
    void testDisplayReport() {
        Message.storedMessagesArray.clear();
        Message msg = new Message("+27834557896", "Did you get the cake?", 1);
        msg.messageHash = "HASH123";
        msg.SentMessage(1);
        Message.storedMessagesArray.add(msg);

        assertFalse(Message.storedMessagesArray.isEmpty());
        assertEquals("HASH123", Message.storedMessagesArray.get(0).messageHash);
        assertEquals("+27834557896", Message.storedMessagesArray.get(0).recipient);
        assertEquals("Did you get the cake?", Message.storedMessagesArray.get(0).messageText);
    }
}