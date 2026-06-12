import java.util.Scanner;

public class Registration {
    String userName;
    String passWord;
    String firstName;
    String lastName;
    String phoneNumber;

    public boolean checkUserName(String userName) {
        return userName.length() <= 5 && userName.contains("_");
    }

    public boolean checkpassWord(String passWord) {
        if (passWord.length() < 8) return false;

        boolean hasUppercase = false;
        boolean hasSpecial = false;
        boolean hasDigit = false;

        for (char ch : passWord.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUppercase = true;
            if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
            if (Character.isDigit(ch)) hasDigit = true;
        }

        return hasUppercase && hasSpecial && hasDigit;
    }

    public boolean checkPhoneNo(String phoneNumber) {
        return phoneNumber.matches("(\\+27|0)[0-9]{9}");
    }

    public void Register() {
        Scanner s = new Scanner(System.in);

        System.out.println("\n=====================================");
        System.out.println("           REGISTRATION");

        System.out.print("Enter your first name: ");
        firstName = s.nextLine();

        System.out.print("Enter your last name: ");
        lastName = s.nextLine();

        while (true) {
            System.out.print("Enter your username (must contain '_' and be ≤5 chars): ");
            userName = s.nextLine();

            if (checkUserName(userName)) {
                System.out.println("Username successfully captured");
                break;
            } else {
                System.out.println("Username must contain '_' and be 5 characters or less");
            }
        }

        while (true) {
            System.out.print("Enter your password (≥8 chars, uppercase, digit, special): ");
            passWord = s.nextLine();

            if (checkpassWord(passWord)) {
                System.out.println("Password successfully captured");
                break;
            } else {
                System.out.println("Password must be at least 8 characters long, contain an uppercase, digit, and " +
                        "special character");
            }
        }

        while (true) {
            System.out.print("Enter your Phone Number (+27 or 0 followed by 9 digits): ");
            phoneNumber = s.nextLine();

            if (checkPhoneNo(phoneNumber)) {
                System.out.println("Phone number successfully captured");
                break;
            } else {
                System.out.println("Phone number must contain '+27' or start with 0");
            }
        }

        System.out.println("\nRegistration Complete!\n");
    }
}