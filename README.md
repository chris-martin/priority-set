Priority Set
============

A mutable collection of elements ordered by priorities.
Modifications to elements' priorities are reflected by the collection's iteration order.
In other words, the data structure represents a finite mapping sorted by its values.

Example usage
-------------
```java
import org.codeswarm.priorityset.PrioritySet;
import org.codeswarm.priorityset.PrioritySetBuilder;

public class Demo {
    public static void main(String[] args) {

        // Construct a new PrioritySet with default options, using
        // String as the element type and Double as the priority type.
        PrioritySet<String, Double> ps = PrioritySet.<String, Double>newPrioritySet();

        // Insert a few onomatopoeias.
        ps.setPriority("crackle", 2.0);
        ps.setPriority("pop", 3.0);
        ps.setPriority("snap", 1.0);

        // Prints "[snap, crackle, pop]".
        System.out.println(ps.asSet());

        // Alter the priority of "snap".
        ps.setPriority("snap", 2.5);

        // Prints "[crackle, snap, pop]"
        System.out.println(ps.asSet());

        // Define a comparator that gives the opposite of natural ordering.
        class ReverseComparator<T extends Comparable<T>> implements java.util.Comparator<T> {
            public int compare(T a, T b) {
                return b.compareTo(a);
            }
        }

        // Construct a new PrioritySet where items have a default priority of 5.0
        // and priorities are sorted in the reverse direction.
        ps = PrioritySetBuilder.<String, Double>prioritySetBuilder()
            .withDefaultPriority(5.0)
            .withPriorityComparator(new ReverseComparator<Double>())
            .build();

        // Duplicate insertions have no effect; the result will have only one duck.
        // Duck is given the default priority of 5.0.
        ps.add("duck");
        ps.add("duck");

        // Set another bird's priority.
        ps.setPriority("goose", 1.0);

        // Prints "[duck, goose]".
        System.out.println(ps.asSet());

    }
}
```

Download
--------

Priority Set is available from Maven Central.
The latest version is 1.0.

```xml
<dependency>
  <groupId>org.codeswarm</groupId>
  <artifactId>priority-set</artifactId>
  <version>1.0</version>
</dependency>
```

