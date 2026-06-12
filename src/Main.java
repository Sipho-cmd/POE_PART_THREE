import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //REGISTRATION
        Registration registration = new Registration();
        registration.Register();

        //LOGIN
        Login login = new Login(registration.userName, registration.passWord,
                registration.firstName, registration.lastName);

        if (!login.loginProcess()) {
            System.out.println("Too many failed attempts. Exiting...");
            input.close();
            return;
        }

        //WELCOME MESSAGE & MENU
        System.out.println("\n=====================================");
        System.out.println("     Welcome to QuickChat, " + registration.firstName + "!");

        int menuChoice = 0;

        // WHILE LOOP - runs until user quits
        while (menuChoice != 4) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show all sent messages");
            System.out.println("3) Search messages");
            System.out.println("4) Quit");
            System.out.print("Choose an option (1-4): ");

            // Safe input handling
            try {
                menuChoice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                continue;
            }

            if (menuChoice == 1) {
                // FOR LOOP inside this method for entering multiple messages
                Message.addMultipleMessages(input, registration.userName);
                // Save to JSON file after adding messages
                Message.saveToJSON("messages.json");

            } else if (menuChoice == 2) {
                Message.displayAllMessages();

            } else if (menuChoice == 3) {
                System.out.println("\n--- SEARCH OPTIONS ---");
                System.out.println("1) Find longest message");
                System.out.println("2) Search by recipient");
                System.out.print("Choose option: ");

                try {
                    int searchChoice = Integer.parseInt(input.nextLine());
                    if (searchChoice == 1) {
                        Message.findLongestMessage();
                    } else if (searchChoice == 2) {
                        Message.searchByRecipient(input);
                    } else {
                        System.out.println("Invalid option");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }

            } else if (menuChoice == 4) {
                System.out.println("\nGoodbye! Thanks for using QuickChat!");

            } else {
                System.out.println("Invalid option. Please choose 1-4.");
            }
        }

        input.close();
    }
}