package nl.suriani.calculator.dsl.model.shared;

import java.util.List;

public interface Guards {
    static void isAssigned(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value required but missing");
        }
    }

    static <T> void isNotEmpty(List<T> list) {
        isAssigned(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Value required but missing");
        }
    }
}
