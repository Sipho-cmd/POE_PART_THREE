import org.json.JSONObject;
import org.json.JSONArray;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Message {

    public String messageID;
    public String recipient;
    public String messageText;
    public String messageHash;
    public int messageNumber;

    public static ArrayList<String> sentMessages = new ArrayList<String>();
    public static int totalMessagesSent = 0;

    public Message(String recipient, String messageText, int messageNumber) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random rand = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + rand.nextInt(10);
        }
        return id;
    }

    public boolean checkMessageID() {
        if (messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    public String checkRecipientCell() {
        if (recipient.startsWith("+27") && recipient.length() == 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
        String[] words = messageText.trim().split(" ");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    public String SentMessage(int choice) {
        if (choice == 1) {
            totalMessagesSent++;
            String record = "Message ID: " + messageID + " | Hash: " + messageHash + " | Recipient: " + recipient + " | Message: " + messageText;
            sentMessages.add(record);
            return "Message successfully sent.";
        }
        if (choice == 0) {
            return "Press 0 to delete the message.";
        }
        if (choice == 2) {
            storeMessage();
            return "Message successfully stored.";
        }
        return "Invalid choice.";
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        String allMessages = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            allMessages = allMessages + sentMessages.get(i) + "\n";
        }
        return allMessages;
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        try {
            JSONObject msgObj = new JSONObject();
            msgObj.put("MessageID", messageID);
            msgObj.put("MessageHash", messageHash);
            msgObj.put("Recipient", recipient);
            msgObj.put("Message", messageText);

            JSONArray jsonArray = new JSONArray();

            File file = new File("storedMessages.json");
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get("storedMessages.json")));
                jsonArray = new JSONArray(content);
            }

            jsonArray.put(msgObj);
            Files.write(Paths.get("storedMessages.json"), jsonArray.toString(2).getBytes());

        } catch (Exception e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // =========================
    // PURE JFRAME POP-UP WINDOW (NO JOPTIONPANE)
    // =========================
    public static void showMessagePopup() {
        JFrame frame = new JFrame("QuickChat Messenger");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("SEND MESSAGE");
        titleLabel.setBounds(150, 10, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel);

        // Recipient Label
        JLabel recipientLabel = new JLabel("Recipient (+27XXXXXXXXX):");
        recipientLabel.setBounds(30, 60, 180, 25);
        mainPanel.add(recipientLabel);

        // Recipient Field
        JTextField recipientField = new JTextField();
        recipientField.setBounds(220, 60, 180, 25);
        mainPanel.add(recipientField);

        // Message Label
        JLabel messageLabel = new JLabel("Message (max 250 chars):");
        messageLabel.setBounds(30, 100, 180, 25);
        mainPanel.add(messageLabel);

        // Message Area
        JTextArea messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBounds(30, 130, 370, 120);
        mainPanel.add(scrollPane);

        // Send Button
        JButton sendBtn = new JButton("Send Message");
        sendBtn.setBounds(50, 280, 120, 35);
        sendBtn.setBackground(new Color(50, 205, 50));
        sendBtn.setForeground(Color.WHITE);
        mainPanel.add(sendBtn);

        // Disregard Button
        JButton disregardBtn = new JButton("Disregard");
        disregardBtn.setBounds(180, 280, 100, 35);
        disregardBtn.setBackground(new Color(255, 69, 0));
        disregardBtn.setForeground(Color.WHITE);
        mainPanel.add(disregardBtn);

        // Store Button
        JButton storeBtn = new JButton("Store Message");
        storeBtn.setBounds(290, 280, 120, 35);
        storeBtn.setBackground(new Color(70, 130, 200));
        storeBtn.setForeground(Color.WHITE);
        mainPanel.add(storeBtn);

        // Result Label
        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(50, 330, 350, 30);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(resultLabel);

        frame.add(mainPanel);

        // Send Button Action
        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String recipient = recipientField.getText().trim();
                String messageText = messageArea.getText().trim();

                if (!(recipient.startsWith("+27") && recipient.length() == 12)) {
                    resultLabel.setText("Error: Invalid number. Must start with +27 and be 12 digits.");
                    resultLabel.setForeground(Color.RED);
                    return;
                }

                if (messageText.length() > 250) {
                    resultLabel.setText("Error: Message exceeds 250 characters.");
                    resultLabel.setForeground(Color.RED);
                    return;
                }

                if (messageText.isEmpty()) {
                    resultLabel.setText("Error: Message cannot be empty.");
                    resultLabel.setForeground(Color.RED);
                    return;
                }

                Message msg = new Message(recipient, messageText, totalMessagesSent + 1);
                String result = msg.SentMessage(1);
                resultLabel.setText("Success: " + result);
                resultLabel.setForeground(new Color(0, 100, 0));

                // Show message details
                JFrame detailsFrame = new JFrame("Message Details");
                detailsFrame.setSize(400, 250);
                detailsFrame.setLocationRelativeTo(frame);
                detailsFrame.setLayout(new BorderLayout());

                JTextArea detailsArea = new JTextArea();
                detailsArea.setEditable(false);
                detailsArea.setText("Message ID: " + msg.messageID + "\n" +
                        "Message Hash: " + msg.messageHash + "\n" +
                        "Recipient: " + msg.recipient + "\n" +
                        "Message: " + msg.messageText + "\n" +
                        "Total Messages: " + totalMessagesSent);
                detailsArea.setMargin(new Insets(10, 10, 10, 10));
                detailsFrame.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

                JButton closeBtn = new JButton("Close");
                closeBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        detailsFrame.dispose();
                        frame.dispose();
                    }
                });
                detailsFrame.add(closeBtn, BorderLayout.SOUTH);

                detailsFrame.setVisible(true);
            }
        });

        // Disregard Button Action
        disregardBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultLabel.setText("Message discarded.");
                resultLabel.setForeground(Color.GRAY);
                frame.dispose();
            }
        });

        // Store Button Action
        storeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String recipient = recipientField.getText().trim();
                String messageText = messageArea.getText().trim();

                if (!(recipient.startsWith("+27") && recipient.length() == 12)) {
                    resultLabel.setText("Error: Invalid number.");
                    resultLabel.setForeground(Color.RED);
                    return;
                }

                if (messageText.isEmpty()) {
                    resultLabel.setText("Error: Message cannot be empty.");
                    resultLabel.setForeground(Color.RED);
                    return;
                }

                Message msg = new Message(recipient, messageText, totalMessagesSent + 1);
                String result = msg.SentMessage(2);
                resultLabel.setText("Success: " + result);
                resultLabel.setForeground(new Color(0, 100, 0));

                JOptionPane pane = new JOptionPane();
                // No JOptionPane used - just closing
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}