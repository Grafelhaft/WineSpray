package de.grafelhaft.winespray.model.parser;

/**
 * Created by @author Markus Graf (Grafelhaft) on 10.10.2016.
 */

public class ParserFactory {

    public static Parser createParser(int parserVersion, String s) {
        switch (parserVersion) {
            case ParserVersion.WIP_LWK_RLP:
                return new WipLwkRlpParser(s);
            default:
                return null;
        }
    }

}
