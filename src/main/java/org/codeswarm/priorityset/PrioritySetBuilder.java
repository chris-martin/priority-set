package org.codeswarm.priorityset;

import java.util.Comparator;

public final class PrioritySetBuilder<T, P> {

    public static <T, P> PrioritySetBuilder<T, P> prioritySetBuilder() {
        return new PrioritySetBuilder<T, P>();
    }

    private Comparator<T> elementComparator;
    private Comparator<P> priorityComparator;

    public PrioritySetBuilder<T, P> withElementComparator(Comparator<T> elementComparator) {
        this.elementComparator = elementComparator;
        return this;
    }

    public PrioritySetBuilder<T, P> withPriorityComparator(Comparator<P> priorityComparator) {
        this.priorityComparator = priorityComparator;
        return this;
    }

    public PrioritySet<T, P> build() {
        return new PrioritySet<T, P>(elementComparator, priorityComparator);
    }

}
