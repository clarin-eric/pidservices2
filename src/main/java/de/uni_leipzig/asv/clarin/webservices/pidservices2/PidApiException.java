package de.uni_leipzig.asv.clarin.webservices.pidservices2;

/**
 *
 * @author wilelb
 */
public class PidApiException extends Exception {
    public PidApiException(String message) {
        super(message);
    }
    
    public PidApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
