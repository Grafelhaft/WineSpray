package de.grafelhaft.winespray.api;

/**
 * Created by @author Markus Graf (Grafelhaft) on 03.02.2017.
 */

public interface IResponse {
    String header();
    String body();
    int code();
}
