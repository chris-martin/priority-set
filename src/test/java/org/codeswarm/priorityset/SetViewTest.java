package org.codeswarm.priorityset;

import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

/**
 * Uses a Guava test suite to ensure that {@link SetView}
 * behaves as a proper {@link java.util.Set Set}.
 */
public class SetViewTest {

    public static junit.framework.TestSuite suite() {
        return SetTestSuiteBuilder
            .using(new IntegerStringSetViewTestSetGenerator())
            .named("org.codeswarm.SetView as java.util.Set")
            .withFeatures(
                CollectionFeature.KNOWN_ORDER,
                CollectionFeature.REMOVE_OPERATIONS,
                CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                CollectionSize.ANY
            )
            .createTestSuite();
    }

}

class IntegerStringSetViewTestSetGenerator implements TestSetGenerator<String> {

    private final IntegerStringPrioritySetTestCollectionGenerator
        prioritySetGeneratorPrioritySet = new IntegerStringPrioritySetTestCollectionGenerator();

    @Override
    public SetView<String> create(Object ... objects) {
        return new SetView<String>(prioritySetGeneratorPrioritySet.create(objects));
    }

    @Override
    public SampleElements<String> samples() {
        return prioritySetGeneratorPrioritySet.samples();
    }

    @Override
    public String[] createArray(int i) {
        return prioritySetGeneratorPrioritySet.createArray(i);
    }

    @Override
    public Iterable<String> order(java.util.List<String> objects) {
        return prioritySetGeneratorPrioritySet.order(objects);
    }

}
