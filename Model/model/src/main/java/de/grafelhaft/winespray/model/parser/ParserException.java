package de.grafelhaft.winespray.model.parser;

/**
 * Created by @author Markus Graf (Grafelhaft) on 03.02.2017.
 */

public class ParserException extends Exception {

    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
