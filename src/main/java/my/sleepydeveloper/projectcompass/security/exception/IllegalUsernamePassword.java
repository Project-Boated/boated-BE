package my.sleepydeveloper.projectcompass.security.exception;

public class IllegalUsernamePassword extends AuthenticationJsonException{
    public IllegalUsernamePassword(String message) {
        super(message);
    }
}
