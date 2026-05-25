public class Login {

    String userName;
    String password;
    String firstName;
    String lastName;

    public Login(String userName,String password,String firstName,String lastName){
        this.userName= userName;
        this.password= password;
        this.firstName= firstName;
        this.lastName= lastName;

    }

    //login method
    public String loginUser(String Username,String Password){

        if(Username.equals(this.userName) && Password.equals(this.password)) {
            return "Welcome " + firstName + " " + lastName + " " + "It is great to see you again";

            //If the username and password doesn't match the original values
        }else{
            return "Username or Password incorrect. Please try again.";
        }
    }
}