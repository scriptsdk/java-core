package de.scriptsdk.core.model.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Crome696
 * @version 1.0
 */
public class BaseList<T> extends ArrayList<T> {
    public BaseList() {
        super();
    }

    @SafeVarargs
    public BaseList(T... args) {
        super();
        addRange(args);
    }

    public BaseList(List<T> list) {
        super();
        this.addAll(list);
    }

    @SafeVarargs
    public final void addRange(T... args) {
        Collections.addAll(this, args);
    }
}
