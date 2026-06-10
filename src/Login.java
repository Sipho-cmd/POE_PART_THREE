import java.util.Scanner;

public class Login {
    String userName;
    String password;
    String firstName;
    String lastName;

    public Login(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean loginProcess() {
        Scanner s = new Scanner(System.in);
        int attempts = 0;

        System.out.println("\n=====================================");
        System.out.println("              LOGIN");

        while (attempts < 3) {
            System.out.print("Enter Username: ");
            String enteredUser = s.nextLine();

            System.out.print("Enter Password: ");
            String enteredPass = s.nextLine();

            if (enteredUser.equals(this.userName) && enteredPass.equals(this.password)) {
                System.out.println("\n Welcome " + firstName + " " + lastName + ". It is great to see you again!");
                return true;
            } else {
                attempts++;
                System.out.println("Username or Password incorrect. Attempts left: " + (3 - attempts));
            }
        }

        System.out.println("Login failed after 3 attempts.");
        return false;
    }
}