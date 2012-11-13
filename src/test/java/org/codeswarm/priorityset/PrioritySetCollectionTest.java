package org.codeswarm.priorityset;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestCollectionGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

/**
 * Uses a Guava test suite to ensure that {@link PrioritySet}
 * behaves as a proper {@link java.util.Collection Collection}.
 */
public class PrioritySetCollectionTest {

    public static junit.framework.TestSuite suite() {
        return CollectionTestSuiteBuilder
            .using(new IntegerStringPrioritySetTestCollectionGenerator())
            .named("org.codeswarm.PrioritySet as java.util.Collection")
            .withFeatures(
                CollectionFeature.KNOWN_ORDER,
                CollectionFeature.REMOVE_OPERATIONS,
                CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                CollectionSize.ANY
            )
            .createTestSuite();
    }

}

class IntegerStringPrioritySetTestCollectionGenerator implements TestCollectionGenerator<String> {

    @Override
    public PrioritySet<String, Integer> create(Object ... objects) {
        PrioritySetBuilder<String, Integer> builder = PrioritySetBuilder.prioritySetBuilder();
        PrioritySet<String, Integer> set = builder.build();
        for (Object o : objects) {
            String s = o.toString();
            set.setPriority(s, Integer.parseInt(s));
        }
        return set;
    }

    @Override
    public SampleElements<String> samples() {
        return Samples.INTEGER_STRINGS;
    }

    @Override
    public String[] createArray(int i) {
        return new String[i];
    }

    @Override
    public Iterable<String> order(java.util.List<String> objects) {
        return Ordering.natural().onResultOf(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return Integer.parseInt(s);
            }
        }).sortedCopy(objects);
    }

}
