import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        // Registration
        Registration registration = new Registration();
        registration.Register();

        // Login attempt
        Scanner s = new Scanner(System.in);

        System.out.println("\n=====================================");
        System.out.println("Please login in:");
        System.out.println("=====================================");

        System.out.print("Enter Username: ");
        String loginUser = s.nextLine();

        System.out.print("Enter Password: ");
        String loginPass = s.nextLine();

        // Create login object
        Login login = new Login(
                registration.userName,
                registration.passWord,
                registration.firstName,
                registration.lastName
        );

        // Login result
        String loginResult = login.loginUser(loginUser, loginPass);
        System.out.println(loginResult);

        // Check login success (improved)
        if (loginResult.toLowerCase().contains("welcome")) {

            // Welcome popup
            JOptionPane.showMessageDialog(
                    null,
                    "Hello " + registration.firstName + " " + registration.lastName + "\nWelcome to QuickChat!",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // QuickChat Menu
            while (true) {

                String[] options = {"Select Quickchat", "Send Quickchat", "Quit"};

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Welcome to QuickChat\nChoose an option:",
                        "QuickChat Menu",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                switch (choice) {
                    case 0:
                        JOptionPane.showMessageDialog(
                                null,
                                "This feature is coming soon",
                                "QuickChat",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        break;

                    case 1:
                        Message.showMessageForm();
                        break;

                    case 2:
                        JOptionPane.showMessageDialog(
                                null,
                                "Goodbye!",
                                "QuickChat",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        s.close();
                        return;
                }
            }

        } else {
            JOptionPane.showMessageDialog(
                    null,
                    loginResult,
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            System.out.println("Exiting program due to login failure.");
            s.close();
        }
    }
}