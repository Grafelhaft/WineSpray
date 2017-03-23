package de.grafelhaft.winespray.model.parser;

/**
 * Created by @author Markus Graf (Grafelhaft) on 10.10.2016.
 */

public abstract class Parser<Type> {

    protected String parsable;

    public Parser(String s) {
        this.parsable = s;
    }

    public abstract Type parse() throws ParserException;
}
