package py.com.buybox.trackingSystem.exceptions;

public class InvalidReCaptchaException extends Throwable {
    public InvalidReCaptchaException(String response_contains_invalid_characters) {
    }
}
