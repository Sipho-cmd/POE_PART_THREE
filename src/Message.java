import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Message {

    private static int messageCount = 0;

    public static void showMessageForm() {

        JTextField recipientField = new JTextField();
        JTextArea messageArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Recipient (+27XXXXXXXXX):"));
        panel.add(recipientField);
        panel.add(new JLabel("Message (max 250 characters):"));
        panel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Send QuickChat",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String recipient = recipientField.getText();
        String messageText = messageArea.getText();

        if (!(recipient.startsWith("+27") && recipient.length() == 12)) {
            JOptionPane.showMessageDialog(null,
                    "Invalid number. Must start with +27 and be 12 digits.");
            return;
        }

        if (messageText.length() > 250) {
            JOptionPane.showMessageDialog(null,
                    "Message too long (max 250 characters)");
            return;
        }

        String messageId = generateMessageId();
        int currentCount = ++messageCount;
        String hash = generateMessageHash(messageId, currentCount, messageText);

        String[] options = {"Send", "Disregard", "Store"};

        int action = JOptionPane.showOptionDialog(
                null,
                "Choose what to do with your message",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (action) {
            case 0:
                JOptionPane.showMessageDialog(null, "Message Sent!");
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Message Discarded");
                return;
            case 2:
                JOptionPane.showMessageDialog(null, "Message Stored");
                break;
        }

        JOptionPane.showMessageDialog(null,
                "Message ID: " + messageId +
                        "\nRecipient: " + recipient +
                        "\nMessage: " + messageText +
                        "\nHash: " + hash +
                        "\nTotal Messages Sent: " + messageCount);
    }

    private static String generateMessageId() {
        Random random = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id += random.nextInt(10);
        }
        return id;
    }

    private static String generateMessageHash(String id, int count, String message) {
        String[] words = message.trim().split("\\s+");
        String first = words.length > 0 ? words[0] : "";
        String last = words.length > 1 ? words[words.length - 1] : first;
        return id.substring(0, 2) + ":" + count + ":" + first.toUpperCase() + last.toUpperCase();
    }
}