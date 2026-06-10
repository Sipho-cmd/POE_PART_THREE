import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Message {
    private String messageID;
    private String messageHash;
    private String recipient;
    private String messageText;
    private int messageNumber;
    private static ArrayList<Message> allMessages = new ArrayList<>();

    // Constructor
    public Message(String recipient, String messageText, int messageNumber, String username) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID(username, messageNumber);
        this.messageHash = generateMessageHash(recipient, messageText, messageNumber);
    }

    // Generate Message ID using substring (string manipulation)
    private String generateMessageID(String username, int messageNumber) {
        // Take first 3 characters of username (or pad with 'X' if shorter)
        String prefix = username.length() >= 3 ? username.substring(0, 3) : username + "X";
        // Message ID format: [3 chars from username][loop counter]
        return prefix.toUpperCase() + messageNumber;
    }

    // Generate Message Hash using string manipulation
    private String generateMessageHash(String recipient, String messageText, int messageNumber) {
        // Simple hash: take first 2 chars of recipient + last 2 chars of message + message number
        String recipientPart = recipient.length() >= 2 ? recipient.substring(0, 2) : recipient + "X";
        String textPart = messageText.length() >= 2 ? messageText.substring(messageText.length() - 2) : messageText + "X";
        return (recipientPart + textPart + messageNumber).toUpperCase();
    }

    // Display message details
    public void displayMessage() {
        System.out.println("----------------------------------------");
        System.out.println("Message #" + messageNumber);
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageText);
        System.out.println("----------------------------------------");
    }

    // Getters for array operations
    public String getMessageText() { return messageText; }
    public String getRecipient() { return recipient; }
    public int getMessageNumber() { return messageNumber; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }

    // Store messages as JSON file
    public static void saveToJSON(String filename) {
        JSONArray jsonArray = new JSONArray();

        for (Message msg : allMessages) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("messageNumber", msg.messageNumber);
            jsonObj.put("messageID", msg.messageID);
            jsonObj.put("messageHash", msg.messageHash);
            jsonObj.put("recipient", msg.recipient);
            jsonObj.put("messageText", msg.messageText);
            jsonArray.put(jsonObj);
        }

        try (FileWriter file = new FileWriter(filename)) {
            file.write(jsonArray.toString(4)); // Pretty print with 4 spaces
            System.out.println("\n " + allMessages.size() + " messages saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    // For loop to add multiple messages (Part 2 requirement)
    public static void addMultipleMessages(Scanner scanner, String username) {
        System.out.print("\nHow many messages do you want to send? ");
        int numMessages = Integer.parseInt(scanner.nextLine());

        // FOR LOOP as required by rubric
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
            System.out.print("Enter recipient name: ");
            String recipient = scanner.nextLine();
            System.out.print("Enter your message: ");
            String messageText = scanner.nextLine();

            // Message number created using loop counter
            Message msg = new Message(recipient, messageText, i, username);
            allMessages.add(msg);
            msg.displayMessage();
        }

        System.out.println("\n " + numMessages + " messages added successfully!");
    }

    // Array operations: Find longest message
    public static void findLongestMessage() {
        if (allMessages.isEmpty()) {
            System.out.println("No messages to display.");
            return;
        }

        Message longest = allMessages.get(0);
        for (Message msg : allMessages) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }

        System.out.println("\n LONGEST MESSAGE:");
        longest.displayMessage();
        System.out.println("Message length: " + longest.getMessageText().length() + " characters");
    }

    // Array operations: Search messages by recipient
    public static void searchByRecipient(Scanner scanner) {
        if (allMessages.isEmpty()) {
            System.out.println("No messages to search.");
            return;
        }

        System.out.print("\nEnter recipient name to search: ");
        String searchRecipient = scanner.nextLine();

        boolean found = false;
        System.out.println("\n MESSAGES SENT TO '" + searchRecipient + "':");

        for (Message msg : allMessages) {
            if (msg.getRecipient().equalsIgnoreCase(searchRecipient)) {
                msg.displayMessage();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for recipient: " + searchRecipient);
        }
    }

    // Display all messages summary
    public static void displayAllMessages() {
        if (allMessages.isEmpty()) {
            System.out.println("No messages to display.");
            return;
        }

        System.out.println("\n ALL SENT MESSAGES:");
        for (Message msg : allMessages) {
            msg.displayMessage();
        }
    }
}