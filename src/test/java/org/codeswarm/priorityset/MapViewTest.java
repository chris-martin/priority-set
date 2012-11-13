package org.codeswarm.priorityset;

import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.SampleElements;
import com.google.common.collect.testing.TestMapGenerator;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

/**
 * Uses a Guava test suite to ensure that {@link MapView}
 * behaves as a proper {@link java.util.Map Map}.
 */
public class MapViewTest {

    public static junit.framework.TestSuite suite() {
        return MapTestSuiteBuilder
            .using(new IntegerStringMapViewTestSetGenerator())
            .named("org.codeswarm.MapView as java.util.Map")
            .withFeatures(
                MapFeature.GENERAL_PURPOSE,
                CollectionSize.ANY
            )
            .createTestSuite();
    }

}

class IntegerStringMapViewTestSetGenerator implements TestMapGenerator<String, Integer> {

    private final IntegerStringPrioritySetTestCollectionGenerator
        prioritySetGeneratorPrioritySet = new IntegerStringPrioritySetTestCollectionGenerator();

    @Override
    public MapView<String, Integer> create(Object ... objects) {
        PrioritySet<String, Integer> prioritySet = new PrioritySet<String, Integer>(null, null);
        for (Object o : objects) {
            Map.Entry<String, Integer> entry = (Map.Entry) o;
            prioritySet.setPriority(entry.getKey(), entry.getValue());
        }
        return new MapView<String, Integer>(prioritySet);
    }

    @Override
    public SampleElements<Map.Entry<String, Integer>> samples() {
        return SampleElements.mapEntries(Samples.INTEGER_STRINGS, Samples.INTEGERS);
    }

    @Override
    public Iterable<Map.Entry<String, Integer>> order(List<Map.Entry<String, Integer>> insertionOrder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map.Entry<String, Integer>[] createArray(int length) {
        return new Map.Entry[length];
    }

    @Override
    public String[] createKeyArray(int length) {
        return new String[length];
    }

    @Override
    public Integer[] createValueArray(int length) {
        return new Integer[length];
    }

}
