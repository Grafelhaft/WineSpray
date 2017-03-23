package de.grafelhaft.winespray.model.csv;

/**
 * Created by @author Markus Graf (Grafelhaft) on 27.10.2016.
 */

public interface ICsvParsable<Type> {

    Type fromCSV(String csv);

    String toCSV();

}
