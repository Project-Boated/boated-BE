package my.sleepydeveloper.projectcompass.security.exception;

public class IllegalRequestJsonLogin extends AuthenticationJsonException{
    public IllegalRequestJsonLogin(String message) {
        super(message);
    }
}
