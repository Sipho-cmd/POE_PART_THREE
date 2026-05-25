import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {

        //Registration
        Registration registration = new Registration();
        registration.Register();

        //login attempt
        Scanner s = new Scanner(System.in);

        System.out.println("Please login in: ");
        System.out.println("Enter Username: ");
        String loginUser = s.nextLine();

        System.out.println("Enter Password: ");
        String loginPass = s.nextLine();

        //Creates a login object
        Login login = new Login(
                registration.userName,
                registration.passWord,
                registration.firstName,
                registration.lastName);

        //Login result
        String loginResult = login.loginUser(loginUser, loginPass);
        System.out.println(loginResult);

        //check login success
        if (loginResult.toLowerCase().contains("Welcome")) {

            //Hello popup
            JOptionPane.showMessageDialog(null, "Hello" + registration.firstName + "" +
                    registration.lastName + "\nWelcome to Quickchat!", "Welcome", JOptionPane.INFORMATION_MESSAGE);

            //Quickchat Menu
            while (true) {
                String[] options = {"Select Quickchat", "Send Quickchat", "Quit"};

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Welcome to Quickchat\nChoose an option:",
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
                                "Quickchat",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        break;

                    case 1:
                        Message.showMessageForm();
                        break;

                    case 2:
                        JOptionPane.showMessageDialog(
                                null,
                                "Goodbye",
                                "QuickChat",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        s.close();
                        return;

                }
            }

        } else {
            System.out.println("Exiting program due to login failure.");
            s.close();

        }
    }
}