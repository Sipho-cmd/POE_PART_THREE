import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        // Registration
        Registration registration = new Registration();
        registration.Register();

        // Login attempt
        Scanner s = new Scanner(System.in);

        System.out.println("Please login in: ");
        System.out.println("Enter Username: ");
        String loginUser = s.nextLine();

        System.out.println("Enter Password: ");
        String loginPass = s.nextLine();

        // Creates a login object
        Login login = new Login(
                registration.userName,
                registration.passWord,
                registration.firstName,
                registration.lastName);

        String loginResult = login.loginUser(loginUser, loginPass);
        System.out.println(loginResult);

        if (loginResult.toLowerCase().contains("welcome")) {

            JOptionPane.showMessageDialog(null, "Welcome to QuickChat");

            while (true) {
                String[] options = {"Send Messages", "Show recently sent messages", "Quit"};

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Choose an option:",
                        "QuickChat Menu",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                switch (choice) {
                    case 0:
                        Message.showMessageForm();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "Coming Soon");
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "Goodbye!");
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