package org.codeswarm.priorityset;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.codeswarm.priorityset.PrioritySetBuilder.prioritySetBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * A potpourri of tests for {@link PrioritySet}.
 */
public class PrioritySetTest {

    /**
     * The {@link PrioritySet#size() size} of an empty set is 0.
     */
    @Test
    public void testEmpty() {
        PrioritySet set = prioritySetBuilder().build();
        assertThat(set.size(), equalTo(0));
    }

    /**
     * For a newly-constructed set, {@link PrioritySet#isEmpty() isEmpty} is true.
     */
    @Test
    public void testIsEmptyTrue() {
        PrioritySet set = prioritySetBuilder().build();
        assertTrue(set.isEmpty());
    }

    /**
     * For a set with something in it, {@link PrioritySet#isEmpty() isEmpty} is false.
     */
    @Test
    public void testIsEmptyFalse() {
        PrioritySetBuilder<Integer, Integer> builder = prioritySetBuilder();
        PrioritySet<Integer, Integer> set = builder.build();
        set.setPriority(8, 6);
        assertFalse(set.isEmpty());
    }

    /**
     * After a set is cleared, {@link PrioritySet#isEmpty() isEmpty} is true.
     */
    @Test
    public void testIsEmptyAfterClear() {
        PrioritySetBuilder<Integer, Integer> builder = prioritySetBuilder();
        PrioritySet<Integer, Integer> set = builder.build();
        set.setPriority(8, 6);
        set.clear();
        assertTrue(set.isEmpty());
    }

    /**
     * Two empty sets are {@link PrioritySet#equals(Object) equal}.
     */
    @Test
    public void testEmptyEquals() {
        assertEquals(
            prioritySetBuilder().build(),
            prioritySetBuilder().build());
    }

    /**
     * Two empty sets have the same {@link PrioritySet#hashCode() hash code}.
     */
    @Test
    public void testEmptyHashCode() {
        assertEquals(
            prioritySetBuilder().build().hashCode(),
            prioritySetBuilder().build().hashCode());
    }

    /**
     * For a key that has been inserted,
     * {@link PrioritySet#contains(Object) contains} returns true.
     */
    @Test
    public void testContainsTrue() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        assertTrue(set.contains("abc"));
    }

    /**
     * For a key that has not been inserted,
     * {@link PrioritySet#contains(Object) contains} returns false.
     */
    @Test
    public void testContainsFalse() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("ab", 5);
        assertFalse(set.contains("abc"));
    }

    /**
     * For a key that has been removed,
     * {@link PrioritySet#contains(Object) contains} returns false.
     */
    @Test
    public void testContainsFalseAfterRemove() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        set.remove("abc");
        assertFalse(set.contains("abc"));
    }

    /**
     * A {@link PrioritySet#setPriority(Object, Object) setPriority}
     * operation on an empty set returns {@code null}.
     */
    @Test
    public void testPutReturnNull() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        assertNull(set.setPriority("abc", 5));
    }

    /**
     * After {@link PrioritySet#clear() clearing} the set,
     * the {@link PrioritySet#size() size} is 0.
     */
    @Test
    public void testClearSize() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        set.clear();
        assertThat(set.size(), equalTo(0));
    }

    /**
     * After {@link PrioritySet#clear() clearing} the set,
     * {@link PrioritySet#setPriority(Object, Object) setting}
     * the same priority for an element again returns {@code null},
     */
    @Test
    public void testPutReturnNullAfterClear() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        set.clear();
        assertNull(set.setPriority("abc", 5));
    }

    /**
     * Re-{@link PrioritySet#setPriority(Object, Object) setting}
     * a different priority for the same element returns the previous priority.
     */
    @Test
    public void testPutReturnWithPriorityChange() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        assertThat(set.setPriority("abc", 6), equalTo(5));
    }

    /**
     * Re-{@link PrioritySet#setPriority(Object, Object) setting} the same
     * element+priority a second time returns the priority.
     */
    @Test
    public void testPutReturnFalse() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        assertThat(set.setPriority("abc", 5), equalTo(5));
    }

    /**
     * An empty set is not equal to a set with one element.
     */
    @Test
    public void testEmptyOneNotEquals() {
        PrioritySet<String, Integer> set, emptySet;
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            set = builder.build();
            set.setPriority("abc", 5);
        }
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            emptySet = builder.build();
        }
        assertThat(set, not(equalTo(emptySet)));
    }

    /**
     * After {@link PrioritySet#setPriority(Object, Object) adding} a single element,
     * the {@link PrioritySet#size() size} is 1.
     */
    @Test
    public void testOneSize() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        assertThat(set.size(), equalTo(1));
    }

    /**
     * The {@link PrioritySet#getPriority(Object) priority} of an element
     * should be the same as the priority we gave when it was
     * {@link PrioritySet#setPriority(Object, Object) put}.
     */
    @Test
    public void testOneGetPriority() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        assertThat(set.getPriority("abc"), equalTo(5));
    }

    /**
     * If we {@link PrioritySet#setPriority(Object, Object) add} in a single element, the
     * {@link PrioritySet#iterator() iterator} includes exactly that element.
     */
    @Test
    public void testOneIterator() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        Iterator<String> it = set.iterator();
        assertThat(it.next(), equalTo("abc"));
        assertFalse(it.hasNext());
    }

    /**
     * Two sets with the same contents are {@link PrioritySet#equals(Object) equal}.
     */
    @Test
    public void testOneEquals() {
        PrioritySet<String, Integer> a, b;
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            a = builder.build();
            a.setPriority("abc", 5);
        }
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            b = builder.build();
            b.setPriority("abc", 5);
        }
        assertThat(a, equalTo(b));
    }

    /**
     * Two sets with the same elements but having different priorities
     * are not {@link PrioritySet#equals(Object) equal}.
     */
    @Test
    public void testOneNotEquals1() {
        PrioritySet<String, Integer> a, b;
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            a = builder.build();
            a.setPriority("abc", 5);
        }
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            b = builder.build();
            b.setPriority("abc", 6);
        }
        assertThat(a, not(equalTo(b)));
    }

    /**
     * Two sets with different elements are not
     * {@link PrioritySet#equals(Object) equal}.
     */
    @Test
    public void testOneNotEquals2() {
        PrioritySet<String, Integer> a, b;
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            a = builder.build();
            a.setPriority("abc", 5);
        }
        {
            PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
            b = builder.build();
            b.setPriority("abcd", 6);
        }
        assertThat(a, not(equalTo(b)));
    }

    /**
     * The {@link PrioritySet#iterator() iteration} order is by ascending priority.
     */
    @Test
    public void testIteration() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("two", 2);
        set.setPriority("four", 4);
        set.setPriority("three", 3);
        set.setPriority("one", 1);
        set.setPriority("six", 6);
        set.setPriority("five", 5);
        Iterator<String> it = set.iterator();
        assertThat(it.next(), equalTo("one"));
        assertThat(it.next(), equalTo("two"));
        assertThat(it.next(), equalTo("three"));
        assertThat(it.next(), equalTo("four"));
        assertThat(it.next(), equalTo("five"));
        assertThat(it.next(), equalTo("six"));
        assertFalse(it.hasNext());
    }

    /**
     * The {@link PrioritySet#descendingIterator() descending iteration}
     * order is by descending priority.
     */
    @Test
    public void testDescendingIteration() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("two", 2);
        set.setPriority("four", 4);
        set.setPriority("three", 3);
        set.setPriority("one", 1);
        set.setPriority("six", 6);
        set.setPriority("five", 5);
        Iterator<String> it = set.descendingIterator();
        assertThat(it.next(), equalTo("six"));
        assertThat(it.next(), equalTo("five"));
        assertThat(it.next(), equalTo("four"));
        assertThat(it.next(), equalTo("three"));
        assertThat(it.next(), equalTo("two"));
        assertThat(it.next(), equalTo("one"));
        assertFalse(it.hasNext());
    }

    /**
     * The {@link PrioritySet#iterator() iteration} order is by ascending priority,
     * using the most recently-specified priority.
     */
    @Test
    public void testIterationAfterModification() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("two", 86);
        set.setPriority("four", -345);
        set.setPriority("three", 3);
        set.setPriority("one", 4);
        set.setPriority("six", 6);
        set.setPriority("five", 5);
        set.remove("three");
        set.setPriority("four", 4);
        set.setPriority("one", 1);
        set.setPriority("two", 2);
        Iterator<String> it = set.iterator();
        assertThat(it.next(), equalTo("one"));
        assertThat(it.next(), equalTo("two"));
        assertThat(it.next(), equalTo("four"));
        assertThat(it.next(), equalTo("five"));
        assertThat(it.next(), equalTo("six"));
        assertFalse(it.hasNext());
    }

    /**
     * The {@link PrioritySetBuilder#withPriorityComparator(Comparator) priority comparator}
     * determines the {@link PrioritySet#iterator() iteration} order.
     */
    @Test
    public void testReversePriority() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        builder.withPriorityComparator(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("two", 2);
        set.setPriority("four", 4);
        set.setPriority("three", 3);
        set.setPriority("one", 1);
        set.setPriority("six", 6);
        set.setPriority("five", 5);
        Iterator<String> it = set.iterator();
        assertThat(it.next(), equalTo("six"));
        assertThat(it.next(), equalTo("five"));
        assertThat(it.next(), equalTo("four"));
        assertThat(it.next(), equalTo("three"));
        assertThat(it.next(), equalTo("two"));
        assertThat(it.next(), equalTo("one"));
        assertFalse(it.hasNext());
    }

    /**
     * The set does not accept null elements.
     */
    @Test(expected = NullPointerException.class)
    public void testNullElement() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority(null, 4);
    }

    /**
     * The set does not accept null priorities.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPriority() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("four", null);
    }

    /**
     * The elements must implement {@link Comparable}
     * if no {@link Comparator} is used.
     */
    @Test(expected = ClassCastException.class)
    public void testNotComparableElement() {
        PrioritySetBuilder<Object, Integer> builder = prioritySetBuilder();
        PrioritySet<Object, Integer> set = builder.build();
        set.setPriority(new Object(), 5);
        set.setPriority(new Object(), 5);
    }

    /**
     * {@link Iterator#remove() Removing} from the
     * {@link PrioritySet#asMap() map}'s {@link Map#entrySet() entry set}
     * iterator removes from the priority set.
     */
    @Test
    public void testMapIteratorRemove() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();

        set.setPriority("two", 2);
        set.setPriority("three", 3);
        set.setPriority("one", 1);

        {
            Iterator<Map.Entry<String, Integer>> it = set.asMap().entrySet().iterator();
            it.next();
            it.next();
            it.remove();
        }
        Iterator<String> it = set.iterator();
        assertThat(it.next(), equalTo("one"));
        assertThat(it.next(), equalTo("three"));
        assertFalse(it.hasNext());
    }

    /**
     * {@link Iterator#remove() Removing} from the
     * {@link PrioritySet#asSet() set}'s iterator
     * removes from the priority set.
     */
    @Test
    public void testSetIteratorRemove() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();

        set.setPriority("two", 2);
        set.setPriority("three", 3);
        set.setPriority("one", 1);

        {
            Iterator<String> it = set.asSet().iterator();
            it.next();
            it.next();
            it.remove();
        }
        Iterator<String> it = set.iterator();
        assertThat(it.next(), equalTo("one"));
        assertThat(it.next(), equalTo("three"));
        assertFalse(it.hasNext());
    }

    @Test
    public void testToStringEmpty() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        assertThat(set.toString(), equalTo("[]"));
    }

    @Test
    public void testToStringOne() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 4);
        assertThat(set.toString(), equalTo("[abc=4]"));
    }

    @Test
    public void testToStringTwo() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 4);
        set.setPriority("def", 6);
        assertThat(set.toString(), equalTo("[abc=4, def=6]"));
    }

    @Test
    public void testDefaultPriority1() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        builder.withDefaultPriority(7);
        PrioritySet<String, Integer> set = builder.build();
        set.add("abc");
        assertThat(set.getPriority("abc"), equalTo(7));
    }

    @Test
    public void testDefaultPriority2() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        builder.withDefaultPriority(7);
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        assertThat(set.getPriority("abc"), equalTo(5));
    }

    @Test
    public void testDefaultPriority3() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        builder.withDefaultPriority(7);
        PrioritySet<String, Integer> set = builder.build();
        set.add("abs");
        set.setPriority("abc", 5);
        assertThat(set.getPriority("abc"), equalTo(5));
    }

    @Test
    public void testDefaultPriority4() {
        PrioritySetBuilder<String, Integer> builder = prioritySetBuilder();
        builder.withDefaultPriority(7);
        PrioritySet<String, Integer> set = builder.build();
        set.setPriority("abc", 5);
        set.add("abs");
        assertThat(set.getPriority("abc"), equalTo(5));
    }

}
