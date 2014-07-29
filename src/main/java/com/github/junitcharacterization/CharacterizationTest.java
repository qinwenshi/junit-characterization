package com.github.junitcharacterization;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CharacterizationTest implements TestRule {

    private final ByteArrayOutputStream capturedStream = new ByteArrayOutputStream();
    private List<TestRule> rules;

    public CharacterizationTest() {
        rules.add(new OutputCapture(capturedStream));
        rules.add(new CaptureVerifier(capturedStream));
    }

    @Override
    public Statement apply(Statement base, Description description) {
        for (TestRule each : rules) {
            base = each.apply(base, description);
        }
        return base;
    }
}