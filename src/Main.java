import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        Registration registration = new Registration();
        registration.Register();

        Login login = new Login(registration.userName, registration.passWord,
                registration.firstName, registration.lastName);
        login.loginProcess();

        System.out.println("Welcome to QuickChat.");

        int menuChoice = 0;

        while (menuChoice != 3) {

            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            menuChoice = Integer.parseInt(input.nextLine());

            if (menuChoice == 1) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Message.showMessagePopup();
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if (menuChoice == 2) {
                System.out.println("Coming Soon.");

            } else if (menuChoice == 3) {
                System.out.println("Goodbye!");
            }
        }

        input.close();
    }
}