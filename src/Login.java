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

    public void loginProcess() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter Username: ");
        String enteredUser = s.nextLine();

        System.out.print("Enter Password: ");
        String enteredPass = s.nextLine();

        if (enteredUser.equals(this.userName) && enteredPass.equals(this.password)) {
            System.out.println("Welcome " + firstName + " " + lastName + ". It is great to see you again");
        } else {
            System.out.println("Username or Password incorrect. Please try again.");
        }
    }
}