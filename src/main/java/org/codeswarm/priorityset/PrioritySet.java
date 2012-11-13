package org.codeswarm.priorityset;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * A mutable collection of elements ordered by priorities.
 * Modifications to elements' priorities are reflected by the
 * collection's {@link #iterator() iteration} order.
 * In other words, the data structure represents a finite
 * mapping sorted by its values.
 *
 * <p style="font-weight:bold;">Relation to {@link Set}</p>
 * <p>Although the word "set" in this class's name implies that it is a
 * collection without duplicate elements, note that {@link PrioritySet}
 * cannot appropriately implement {@link Set Set&lt;T&gt;} because
 * {@link #equals(Object)} and {@link #hashCode()} are based on both
 * the elements ({@link T}) and their priorities ({@link P}), whereas the
 * recommended comparison mode for {@link Set}s is to compare solely by
 * the sets' elements.
 * {@link #asSet()} provides a {@link Set} implementation backed
 * by the {@link PrioritySet} that implements most of the {@link Set} methods.</p>
 *
 * <p style="font-weight:bold;">Relation to {@link Map}</p>
 * <p>This data structure closely resembles {@link Map Map&lt;T,P&gt;}.
 * It declines to implement the {@link Map} interface to escape
 * the obligation of conforming {@link #equals(Object)} and {@link #hashCode()}
 * to {@link Map} semantics, and to avoid the confusion arising from
 * {@link Map}'s method names and generic-typing deficiency
 * (for example, the method signature
 * {@link #getPriority(Object) PrioritySet#getPriority(T):P}
 * provides more clarity than {@link Map#get(Object) Map#get(Object):P}).
 * {@link #asMap()} provides a full {@link Map} implementation backed
 * by the {@link PrioritySet}.</p>
 *
 * @param <T>
 *
 *   The type of element in the set.
 *
 *   If {@link T} does not extend {@link Comparable Comparable&lt;T&gt;},
 *   then a {@link Comparator Comparator&lt;T&gt;} must be provided to the
 *   {@link #PrioritySet(Comparator, Comparator) PrioritySet constructor}
 *   (see also: {@link PrioritySetBuilder#withElementComparator(Comparator)}).
 *   This is required for a well-defined {@link #iterator() iteration} order
 *   in the case where multiple elements have the same priority.
 *   Comparison must be consistent with {@link Object#equals(Object) equals}.</p>
 *
 * @param <P>
 *
 *   The type of the priority assigned to each element.
 *
 *   The {@link #iterator() iteration} order for elements is determined
 *   foremost by the elements' priorities, in ascending order.
 *   If {@link P} does not extend {@link Comparable Comparable&lt;P&gt;},
 *   then a {@link Comparator Comparator&lt;P&gt;} must be provided to the
 *   {@link #PrioritySet(Comparator, Comparator) PrioritySet constructor}
 *   (see also: {@link PrioritySetBuilder#withPriorityComparator(Comparator)}).
 *   Comparison must be consistent with {@link Object#equals(Object) equals}.</p>
 */
public class PrioritySet<T, P> extends AbstractCollection<T> {

    final HashMap<T, Node<T, P>> hashMap;
    final TreeSet<Node<T, P>> treeSet;
    SetView<T> setView;
    MapView<T, P> mapView;

    /**
     * Constructs an empty {@link PrioritySet} that uses
     * the natural ordering of {@link T} and {@link P}.
     */
    public static <T extends Comparator<T>, P extends Comparator<P>>
    PrioritySet<T, P> newPrioritySet() {

        return new PrioritySet<T, P>(null, null);
    }

    /**
     * Constructs an empty {@link PrioritySet}.
     *
     * @param elementComparator
     *
     *   Comparison function used to sort elements having the same priority.
     *
     *   If {@link T} extends {@link Comparable Comparable&lt;T&gt;},
     *   then this parameter may be null, and the elements will be
     *   sorted by their natural ordering.
     *   Comparison must be consistent with {@link Object#equals(Object) equals}.
     *
     * @param priorityComparator
     *
     *   Comparison function used to sort priorities.
     *
     *   If {@link P} extends {@link Comparable Comparable&lt;P&gt;},
     *   then this parameter may be null, and the priorities will be
     *   sorted by their natural ordering.
     *   Comparison must be consistent with {@link Object#equals(Object) equals}.
     */
    public PrioritySet(
        @Nullable Comparator<T> elementComparator,
        @Nullable Comparator<P> priorityComparator) {

        hashMap = new HashMap<T, Node<T, P>>();

        NodeComparator<T, P> nodeComparator =
            new NodeComparator<T, P>(elementComparator, priorityComparator);

        treeSet = new TreeSet<Node<T, P>>(nodeComparator);
    }

    /**
     * Inserts {@code element} into the set and sets its priority.
     *
     * If {@code element} already belongs to the set, then the element's priority
     * is updated, and the set's {@link #size() size} remains unchanged.
     *
     * If this operation does not already belong to the set, then the set's
     * {@link #size() size} increases by 1.
     *
     * @return
     *
     *   The previous priority of {@code element}, or {@code null} if
     *   {@code element} was not previously a member of this set.
     *
     *   Whether this operation modified the {@link PrioritySet}.
     *
     *   False iff {@code element} was already a member of the set and its
     *   priority was already equal to {@code priority}.
     */
    public P setPriority(T element, P priority) {
        P previousPriority = null;
        Node<T, P> node = hashMap.get(element);
        if (node != null) {
            previousPriority = node.priority;
            if (!previousPriority.equals(priority)) {
                if (!treeSet.remove(node)) {
                    throw new AssertionError();
                }
                hashMap.remove(element);
                node = null;
            }
        }
        if (node == null) {
            node = new Node<T, P>(element, priority);
            hashMap.put(element, node);
            treeSet.add(node);
        }
        return previousPriority;
    }

    /**
     * Removes {@code element} from the set if it is present.
     *
     * @return
     *
     *   Whether this operation modified the {@link PrioritySet}.
     *   True iff {@code element} belonged to the set.
     */
    @Override
    public boolean remove(Object element) {
        Node<T, P> node = hashMap.remove(element);
        boolean modified = node != null;
        if (modified) {
            treeSet.remove(node);
        }
        return modified;
    }

    /**
     * Removes all elements from the set.
     */
    @Override
    public void clear() {
        hashMap.clear();
        treeSet.clear();
    }

    /**
     * @return
     *
     *   The number of elements in the set.
     */
    @Override
    public int size() {
        return hashMap.size();
    }

    /**
     * @return
     *
     *   Whether the {@link #size() size} of this set is equal to 0.
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @return
     *
     *   An iterator over the elements in the set.
     *
     *   Elements are ordered primarily by ascending priority,
     *   and secondarily by the ordering of the elements themselves.
     */
    @Override
    public Iterator<T> iterator() {
        NodeIterator<T, P> nodeIterator = new NodeIterator<T, P>(this);
        return new ElementIterator<T, P>(nodeIterator);
    }

    /**
     * @return
     *
     *   An iterator that iterates in the reverse order of that
     *   returned by {@link #iterator()}.
     *   Elements with higher priorities come first.
     */
    public Iterator<T> descendingIterator() {
        NodeIterator<T, P> nodeIterator = new NodeIterator<T, P>(
            hashMap, treeSet.descendingIterator());
        return new ElementIterator<T, P>(nodeIterator);
    }

    /**
     * @return
     *
     *   The priority to which {@code element} is mapped, or
     *   {@code null} if {@code element} does not belong to this set.
     */
    public @Nullable P getPriority(T element) {
        Node<T, P> node = hashMap.get(element);
        return node != null ? node.priority : null;
    }

    /**
     * @return
     *
     *   Whether {@code element} is a member of this set.
     */
    @Override
    public boolean contains(Object element) {
        return hashMap.containsKey(element);
    }

    /**
     * @return
     *
     *   A {@link Set Set&lt;T&gt;} backed by this
     *   {@link PrioritySet PrioritySet&lt;T,P&gt;}.
     *
     *   Changes to the {@link PrioritySet} affect the returned
     *   {@link Set}, and vice versa.
     *
     *   The returned {@link Set} supports element removal
     *   by {@link Set#remove(Object)} (and its related methods)
     *   and via {@link Iterator#remove()}.
     *   Addition methods such as {@link Set#add(Object)} throw
     *   {@link UnsupportedOperationException}.
     */
    public Set<T> asSet() {
        if (setView == null) {
            setView = new SetView<T>(this);
        }
        return setView;
    }

    /**
     * @return
     *
     *   A {@link Map Map&lt;T,P&gt;} backed by this
     *   {@link PrioritySet PrioritySet&lt;T,P&gt;}.
     *
     *   Changes to the {@link PrioritySet} affect the returned
     *   {@link Map}, and vice versa.
     *
     *   The returned object fully implements {@link Map}.
     */
    public Map<T, P> asMap() {
        if (mapView == null) {
            mapView = new MapView<T, P>(this);
        }
        return mapView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrioritySet that = (PrioritySet) o;
        return treeSet.equals(that.treeSet);
    }

    @Override
    public int hashCode() {
        return treeSet.hashCode();
    }

}

final class Node<T, P> {

    final T element;
    final P priority;

    Node(T element, P priority) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (priority == null) {
            throw new NullPointerException();
        }
        this.element = element;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node that = (Node) o;
        return element.equals(that.element) && priority.equals(that.priority);
    }

    @Override
    public int hashCode() {
        return 31 * element.hashCode() + priority.hashCode();
    }

}

final class MapView<T, P> extends AbstractMap<T, P> {

    private final PrioritySet<T, P> prioritySet;

    MapView(PrioritySet<T, P> prioritySet) {
        this.prioritySet = prioritySet;
    }

    @Override
    public boolean isEmpty() {
        return prioritySet.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return prioritySet.hashMap.containsKey(key);
    }

    @Override
    public P get(Object key) {
        return prioritySet.hashMap.get(key).priority;
    }

    @Override
    public void clear() {
        prioritySet.clear();
    }

    @Override
    public void putAll(Map<? extends T, ? extends P> m) {
        for (Map.Entry<? extends T, ? extends P> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public P put(T key, P value) {
        return prioritySet.setPriority(key, value);
    }

    @Override
    public int size() {
        return prioritySet.size();
    }

    @Override
    public boolean containsValue(Object value) {
        return prioritySet.hashMap.containsValue(value);
    }

    @Override
    public P remove(Object key) {
        P priority = get(key);
        prioritySet.remove(key);
        return priority;
    }

    @Override
    public Set<Entry<T, P>> entrySet() {
        return new MapViewEntrySet<T, P>(prioritySet);
    }

}

final class MapViewEntrySet<T, P> extends AbstractSet<Entry<T, P>> {

    private final PrioritySet<T, P> prioritySet;

    MapViewEntrySet(PrioritySet<T, P> prioritySet) {
        this.prioritySet = prioritySet;
    }

    @Override
    public int size() {
        return prioritySet.size();
    }

    @Override
    public boolean contains(Object o) {
        Map.Entry entry = (Entry) o;
        return prioritySet.contains(entry.getKey());
    }

    @Override
    public Iterator<Map.Entry<T, P>> iterator() {
        return new MapViewEntrySetIterator<T, P>(prioritySet);
    }

    @Override
    public boolean remove(Object o) {
        Map.Entry entry = (Entry) o;
        return prioritySet.remove(entry.getKey());
    }

    @Override
    public void clear() {
        prioritySet.clear();
    }

}

final class MapViewEntrySetIterator<T, P> implements Iterator<Entry<T, P>> {

    final PrioritySet<T, P> prioritySet;
    final NodeIterator<T, P> nodeIterator;

    MapViewEntrySetIterator(PrioritySet<T, P> prioritySet) {
        this(prioritySet, new NodeIterator<T, P>(prioritySet));
    }

    MapViewEntrySetIterator(PrioritySet<T, P> prioritySet, NodeIterator<T, P> nodeIterator) {
        this.prioritySet = prioritySet;
        this.nodeIterator = nodeIterator;
    }

    @Override
    public boolean hasNext() {
        return nodeIterator.hasNext();
    }

    @Override
    public Entry<T, P> next() {
        Node<T, P> node = nodeIterator.next();
        return new NodeEntry<T, P>(node, prioritySet);
    }

    @Override
    public void remove() {
        nodeIterator.remove();
    }

}

final class NodeEntry<T, P> implements Entry<T, P> {

    final Node<T, P> node;
    final PrioritySet<T, P> prioritySet;

    NodeEntry(Node<T, P> node, PrioritySet<T, P> prioritySet) {
        this.node = node;
        this.prioritySet = prioritySet;
    }

    @Override
    public T getKey() {
        return node.element;
    }

    @Override
    public P getValue() {
        return node.priority;
    }

    @Override
    public P setValue(P value) {
        return prioritySet.asMap().put(node.element, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry)) return false;
        Map.Entry that = (Map.Entry) o;
        return node.element.equals(that.getKey())
            && node.priority.equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return node.element.hashCode() ^ node.priority.hashCode();
    }

}

final class SetView<T> extends AbstractSet<T> {

    private final PrioritySet<T, ?> prioritySet;

    SetView(PrioritySet<T, ?> prioritySet) {
        this.prioritySet = prioritySet;
    }

    @Override
    public int size() {
        return prioritySet.size();
    }

    @Override
    public boolean contains(Object o) {
        return prioritySet.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return prioritySet.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return prioritySet.remove(o);
    }

    @Override
    public void clear() {
        prioritySet.clear();
    }

}

final class OptionalComparator<T> implements Comparator<T> {

    private final Comparator<T> comparator;

    OptionalComparator(@Nullable Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * @throws ClassCastException If there is no underlying {@link Comparator}
     *         and {@code a} and {@code b} cannot be compared by their natural ordering
     *         (probably because {@link T} does not extend {@link Comparable}).
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable) a).compareTo(b);
        }
    }

}

final class NodeComparator<T, P> implements Comparator<Node<T, P>> {

    final OptionalComparator<T> elementComparator;
    final OptionalComparator<P> priorityComparator;

    NodeComparator(
        @Nullable Comparator<T> elementComparator,
        @Nullable Comparator<P> priorityComparator) {

        this.elementComparator = new OptionalComparator<T>(elementComparator);
        this.priorityComparator = new OptionalComparator<P>(priorityComparator);
    }

    @Override
    public int compare(Node<T, P> a, Node<T, P> b) {

        if (a == b) {
            return 0;
        }

        int c;

        try {
            c = priorityComparator.compare(a.priority, b.priority);
        } catch (ClassCastException e) {
            if (!(a.priority instanceof Comparable && b.priority instanceof Comparable)) {
                throw new ClassCastException("Priority type does not extend Comparable, " +
                    "and no priority Comparator is configured.");
            } else {
                throw e;
            }
        }

        if (c != 0) {
            return c;
        }

        try {
            c = elementComparator.compare(a.element, b.element);
        } catch (ClassCastException e) {
            if (!(a.element instanceof Comparable && b.element instanceof Comparable)) {
                throw new ClassCastException("Element type does not extend Comparable, " +
                    "and no element Comparator is configured.");
            } else {
                throw e;
            }
        }

        return c;
    }

}

final class NodeIterator<T, P> implements Iterator<Node<T, P>> {

    final Map<T, Node<T, P>> map;
    final Iterator<Node<T, P>> iterator;
    Node<T, P> currentNode;

    NodeIterator(PrioritySet<T, P> prioritySet) {
        this(prioritySet.hashMap, prioritySet.treeSet.iterator());
    }

    NodeIterator(Map<T, Node<T, P>> map, Iterator<Node<T, P>> iterator) {
        this.map = map;
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Node<T, P> next() {
        currentNode = iterator.next();
        return currentNode;
    }

    @Override
    public void remove() {
        if (currentNode == null) {
            throw new IllegalStateException();
        }
        iterator.remove();
        map.remove(currentNode.element);
        currentNode = null;
    }

}

final class ElementIterator<T, P> implements Iterator<T> {

    final NodeIterator<T, P> nodeIterator;

    ElementIterator(NodeIterator<T, P> nodeIterator) {
        this.nodeIterator = nodeIterator;
    }

    @Override
    public boolean hasNext() {
        return nodeIterator.hasNext();
    }

    @Override
    public T next() {
        Node<T, P> node = nodeIterator.next();
        return node.element;
    }

    @Override
    public void remove() {
        nodeIterator.remove();
    }

}
