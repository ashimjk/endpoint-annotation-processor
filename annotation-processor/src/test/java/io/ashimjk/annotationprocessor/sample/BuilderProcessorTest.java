package io.ashimjk.annotationprocessor.sample;

import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

class BuilderProcessorTest {

    private @MonotonicNonNull BuilderProcessor builderProcessor;

    @BeforeEach
    void setup() {
        this.builderProcessor = new BuilderProcessor();
    }

    @Test
    @SuppressWarnings("nullness")
    @DisplayName("Test Build Processor")
    void testBuildProcessor() {
        Truth.assert_()
                .about(javaSource())
                .that(JavaFileObjects.forResource("sources/Sample.java"))
                .processedWith(builderProcessor)
                .compilesWithoutError();
    }

}