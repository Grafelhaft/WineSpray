package de.grafelhaft.winespray.model;

/**
 * Created by Markus on 09.10.2016.
 */

public abstract class AModel<Type> implements IModel<Type> {

    private Long id;

    public Long getId() {
        return id;
    }

    public Type setId(Long id) {
        this.id = this.id == null ? id : this.id;
        return (Type) this;
    }
}
