package org.codeswarm.priorityset;

import java.util.Comparator;

import javax.annotation.Nullable;

/**
 * A slightly easier way to invoke
 * {@link PrioritySet#PrioritySet(Object, Comparator, Comparator)
 * the PrioritySet constructor}.
 *
 * <p>This builder may be reused to construct multiple {@link PrioritySet}s,
 * but note that the builder is mutable.
 * Its parameter-setting methods all return {@code this} to support
 * invocation chaining for your convenience.</p>
 */
public final class PrioritySetBuilder<T, P> {

    /**
     * @return
     *
     *   A new {@link PrioritySetBuilder} with default values:
     *   <ul>
     *     <li>No default priority ({@link PrioritySet#add(Object)}
     *     will throw {@link UnsupportedOperationException})</li>
     *     <li>No element comparator ({@link T} must implement {@link Comparable})</li>
     *     <li>No priority comparator ({@link P} must implement {@link Comparable})</li>
     *   </ul>
     */
    public static <T, P> PrioritySetBuilder<T, P> prioritySetBuilder() {
        return new PrioritySetBuilder<T, P>();
    }

    private P defaultPriority;
    private Comparator<T> elementComparator;
    private Comparator<P> priorityComparator;

    /**
     * @return
     *
     *   A new {@link PrioritySet} constructed with the builder's current settings.
     */
    public PrioritySet<T, P> build() {
        return new PrioritySet<T, P>(defaultPriority, elementComparator, priorityComparator);
    }

    /**
     * @param defaultPriority
     *
     *   The default priority for new elements added via {@link PrioritySet#add(Object)}.
     *   If {@code null}, then {@link PrioritySet#add(Object)} throws
     *   {@link UnsupportedOperationException}.
     *
     * @return
     *
     *   {@code this}
     */
    public PrioritySetBuilder<T, P> withDefaultPriority(
            @Nullable P defaultPriority) {

        this.defaultPriority = defaultPriority;
        return this;
    }

    /**
     * @param elementComparator
     *
     *   Comparison function used to sort elements having the same priority.
     *
     *   If {@link T} extends {@link Comparable Comparable&lt;T&gt;},
     *   then this parameter may be null, and the elements will be
     *   sorted by their natural ordering.
     *   Comparison must be consistent with {@link Object#equals(Object) equals}.
     *
     *  @return
     *
     *   {@code this}
     */
    public PrioritySetBuilder<T, P> withElementComparator(
            @Nullable Comparator<T> elementComparator) {

        this.elementComparator = elementComparator;
        return this;
    }

    /**
     * @param priorityComparator
     *
     *   Comparison function used to sort priorities.
     *
     *   If {@link P} extends {@link Comparable Comparable&lt;P&gt;},
     *   then this parameter may be null, and the priorities will be
     *   sorted by their natural ordering.
     *   Comparison must be consistent with {@link Object#equals(Object) equals}.
     *
     * @return
     *
     *   {@code this}
     */
    public PrioritySetBuilder<T, P> withPriorityComparator(
            @Nullable Comparator<P> priorityComparator) {

        this.priorityComparator = priorityComparator;
        return this;
    }

}
