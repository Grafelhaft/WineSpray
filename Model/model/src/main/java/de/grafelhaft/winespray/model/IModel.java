package de.grafelhaft.winespray.model;

/**
 * Created by Markus on 09.10.2016.
 */

public interface IModel<Type> {
    Type setId(Long id);

    Long getId();
}
