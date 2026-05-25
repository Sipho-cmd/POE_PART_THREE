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

    // MAIN MESSAGE FORM
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

        for (int i = 0; i < totalMessages; i++) {
            boolean sent = sendSingleMessage(i + 1, totalMessages);
            if (sent) {
                messagesSent++;
            }
        }

        JOptionPane.showMessageDialog(null,
                "Total Messages Sent: " + messagesSent + " out of " + totalMessages,
                "Message Summary",
                JOptionPane.INFORMATION_MESSAGE);

        printAllMessages();
    }

    // SEND SINGLE MESSAGE
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
                null, panel, "Send Message",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        String recipient = recipientField.getText().trim();
        String messageText = messageArea.getText().trim();

        // Validate recipient
        if (!(recipient.startsWith("+27") && recipient.length() == 12)) {
            JOptionPane.showMessageDialog(null,
                    "Cell phone number is incorrectly formatted.\nMust start with +27 and be exactly 12 characters.",
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

        if (messageText.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Message cannot be empty!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Generate message details
        String messageId = generateMessageId();
        int currentCount = ++messageCount;
        String hash = generateMessageHash(messageId, currentCount, messageText);

        // User actions
        String[] options = {"Send Message", "Delete Message", "Store Message"};

        int action = JOptionPane.showOptionDialog(
                null,
                "Message ID: " + messageId + "\n" +
                        "Message Hash: " + hash + "\n" +
                        "Recipient: " + recipient + "\n" +
                        "Message: " + (messageText.length() > 50 ? messageText.substring(0, 50) + "..." : messageText) + "\n\n" +
                        "What would you like to do?",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (action) {
            case 0:
                JOptionPane.showMessageDialog(null, "Message successfully sent!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayMessageDetails(messageId, hash, recipient, messageText, currentCount);
                storeMessageInJSON(messageId, hash, recipient, messageText, currentCount, "sent");
                messageHistory.add(new String[]{messageId, hash, recipient, messageText, String.valueOf(currentCount), "sent"});
                return true;

            case 1:
                JOptionPane.showMessageDialog(null, "Press 0 to delete the message.\n Message discarded.", "Discarded", JOptionPane.WARNING_MESSAGE);
                return false;

            case 2:
                JOptionPane.showMessageDialog(null, "Message successfully stored!", "Stored", JOptionPane.INFORMATION_MESSAGE);
                storeMessageInJSON(messageId, hash, recipient, messageText, currentCount, "stored");
                messageHistory.add(new String[]{messageId, hash, recipient, messageText, String.valueOf(currentCount), "stored"});
                return true;

            default:
                return false;
        }
    }

    // DISPLAY MESSAGE DETAILS
    private static void displayMessageDetails(String messageId, String hash, String recipient,
                                              String message, int count) {

        String details = String.format(

                        "Message ID:         \n" +
                        "Message Hash:       \n" +
                        "Recipient:          \n" +
                        "Message:            \n" +
                        "Total Messages:     \n" +

                messageId, hash, recipient,
                (message.length() > 40 ? message.substring(0, 40) + "..." : message),
                String.valueOf(count));

        JTextArea textArea = new JTextArea(details);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JOptionPane.showMessageDialog(null, textArea, "Message Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // PRINT ALL MESSAGES
    private static void printAllMessages() {
        if (messageHistory.isEmpty()) {
            return;
        }

        StringBuilder allMessages = new StringBuilder();

        for (int i = 0; i < messageHistory.size(); i++) {
            String[] msg = messageHistory.get(i);
            allMessages.append("Message ").append(i + 1).append(":\n");
            allMessages.append("ID:       ").append(msg[0]).append("\n");
            allMessages.append("Hash:     ").append(msg[1]).append("\n");
            allMessages.append("Recipient:").append(msg[2]).append("\n");
            allMessages.append("Message:  ").append(msg[3]).append("\n");
            allMessages.append("Status:   ").append(msg[5]).append("\n");
            allMessages.append("\n");
        }

        allMessages.append("Total Messages: ").append(messageHistory.size());

        JTextArea textArea = new JTextArea(allMessages.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 450));

        JOptionPane.showMessageDialog(null, scrollPane, "Message History",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // GENERATE MESSAGE ID
    private static String generateMessageId() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // GENERATE MESSAGE HASH
    private static String generateMessageHash(String id, int count, String message) {
        String[] words = message.trim().split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;

        return id.substring(0, 2) + ":" + count + ":" + first.toUpperCase() + last.toUpperCase();
    }

    // STORE MESSAGE IN JSON FILE
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

            System.out.println("Message stored in messages.json");

        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }
}