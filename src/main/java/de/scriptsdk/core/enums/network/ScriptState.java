package de.scriptsdk.core.enums.network;

import de.scriptsdk.core.interfaces.Enumerable;

public enum ScriptState implements Enumerable {
    UNKNOWN(0),
    STARTED(1),
    PAUSED(2);

    private final Integer id;

    ScriptState(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
