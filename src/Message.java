import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;

public class Message {

    private static ArrayList<String[]> messageHistory = new ArrayList<>();
    private static int messageCount = 0;

    // =========================
    // MAIN MESSAGE FORM
    // =========================
    public static void showMessageForm() {

        // Ask how many messages
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("How many messages do you want to send?"), gbc);

        gbc.gridy = 1;
        JSpinner messageSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panel.add(messageSpinner, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "QuickChat Setup", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        int totalMessages = (int) messageSpinner.getValue();
        int messagesSent = 0;

        // Loop for multiple messages
        for (int i = 0; i < totalMessages; i++) {
            boolean sent = sendSingleMessage(i + 1, totalMessages);
            if (sent) {
                messagesSent++;
            }
        }

        // Display total messages sent
        JOptionPane.showMessageDialog(null,
                "Total Messages Sent: " + messagesSent + " out of " + totalMessages,
                "QuickChat Summary",
                JOptionPane.INFORMATION_MESSAGE);

        // Print all messages
        printAllMessages();
    }

    // =========================
    // SEND SINGLE MESSAGE
    // =========================
    private static boolean sendSingleMessage(int current, int total) {

        JTextField recipientField = new JTextField();
        JTextArea messageArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Message " + current + " of " + total));
        panel.add(new JLabel("Recipient (+27XXXXXXXXX):"));
        panel.add(recipientField);
        panel.add(new JLabel("Message (max 250 characters):"));
        panel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "Send QuickChat",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        String recipient = recipientField.getText();
        String messageText = messageArea.getText();

        // Validate recipient
        if (!(recipient.startsWith("+27") && recipient.length() == 12)) {
            JOptionPane.showMessageDialog(null,
                    "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate message length
        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            JOptionPane.showMessageDialog(null,
                    "Message exceeds 250 characters by " + excess + "; please reduce the size.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Generate message details
        String messageId = generateMessageId();
        int currentCount = ++messageCount;
        String hash = generateMessageHash(messageId, currentCount, messageText);

        // User actions (Send/Disregard/Store)
        String[] options = {"Send Message", "Disregard Message", "Store Message"};

        int action = JOptionPane.showOptionDialog(
                null,
                "Message ID: " + messageId + "\n" +
                        "Message Hash: " + hash + "\n" +
                        "Recipient: " + recipient + "\n" +
                        "Message: " + messageText + "\n\n" +
                        "Choose what to do with your message:",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (action) {
            case 0:
                JOptionPane.showMessageDialog(null, "Message successfully sent");
                displayMessageDetails(messageId, hash, recipient, messageText, currentCount);
                storeMessageInJSON(messageId, hash, recipient, messageText, currentCount, "sent");
                messageHistory.add(new String[]{messageId, hash, recipient, messageText, String.valueOf(currentCount), "sent"});
                return true;

            case 1:
                JOptionPane.showMessageDialog(null, "Press 0 to delete the message.\nMessage discarded");
                return false;

            case 2:
                JOptionPane.showMessageDialog(null, "Message successfully stored");
                storeMessageInJSON(messageId, hash, recipient, messageText, currentCount, "stored");
                messageHistory.add(new String[]{messageId, hash, recipient, messageText, String.valueOf(currentCount), "stored"});
                return true;

            default:
                return false;
        }
    }

    // =========================
    // DISPLAY MESSAGE DETAILS
    // =========================
    private static void displayMessageDetails(String messageId, String hash, String recipient, String message, int count) {
        JOptionPane.showMessageDialog(null,
                "Message ID: " + messageId + "\n" +
                        "Message Hash: " + hash + "\n" +
                        "Recipient: " + recipient + "\n" +
                        "Message: " + message + "\n" +
                        "Total Messages: " + count,
                "Message Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================
    // PRINT ALL MESSAGES
    // =========================
    private static void printAllMessages() {
        if (messageHistory.isEmpty()) {
            return;
        }

        StringBuilder allMessages = new StringBuilder();
        allMessages.append("=== ALL MESSAGES ===\n\n");

        for (int i = 0; i < messageHistory.size(); i++) {
            String[] msg = messageHistory.get(i);
            allMessages.append("Message ").append(i + 1).append(":\n");
            allMessages.append("  ID: ").append(msg[0]).append("\n");
            allMessages.append("  Hash: ").append(msg[1]).append("\n");
            allMessages.append("  Recipient: ").append(msg[2]).append("\n");
            allMessages.append("  Message: ").append(msg[3]).append("\n");
            allMessages.append("  Status: ").append(msg[5]).append("\n");
            allMessages.append("  -------------------\n");
        }

        JTextArea textArea = new JTextArea(allMessages.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "Message History", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================
    // GENERATE MESSAGE ID
    // =========================
    private static String generateMessageId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // =========================
    // GENERATE HASH
    // =========================
    private static String generateMessageHash(String id, int count, String message) {
        String[] words = message.trim().split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;

        return id.substring(0, 2) + ":" + count + ":" + first.toUpperCase() + last.toUpperCase();
    }

    // =========================
    // STORE MESSAGE IN JSON (CORRECTED)
    // =========================
    private static void storeMessageInJSON(String messageId, String hash, String recipient,
                                           String message, int count, String status) {
        try {
            JSONObject jsonMsg = new JSONObject();
            jsonMsg.put("messageId", messageId);
            jsonMsg.put("messageHash", hash);
            jsonMsg.put("recipient", recipient);
            jsonMsg.put("message", message);
            jsonMsg.put("totalMessages", count);
            jsonMsg.put("status", status);
            jsonMsg.put("timestamp", java.time.LocalDateTime.now().toString());

            try (FileWriter file = new FileWriter("messages.json", true)) {
                file.write(jsonMsg.toString(4) + ",\n");
            }

        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}