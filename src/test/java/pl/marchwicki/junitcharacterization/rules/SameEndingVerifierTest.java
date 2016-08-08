package pl.marchwicki.junitcharacterization.rules;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.fail;
import static pl.marchwicki.junitcharacterization.builders.TestByteArrayOutputStreamBuilder.aStream;
import static pl.marchwicki.junitcharacterization.builders.TestFileBuilder.aFile;

/**
 * Created by qinwenshi on 8/8/16.
 */
public class SameEndingVerifierTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void should_pass_on_same_ending_content() throws IOException {
        final String STRING = "foo foo > bar";
        final String STRING2 = "foo bar > bar";
        final File file = aFile(folder)
                .withContent(STRING).build();

        final ByteArrayOutputStream baos = aStream()
                .withContent(STRING2).build();

        SameEndingVerifier verifier = new SameEndingVerifier(file, baos, ">");

        try {
            verifier.verify();
        } catch (Throwable throwable) {
            fail("No differences on comparison", throwable);
        }
    }

    @Test(expected = AssertionError.class)
    public void should_fail_on_different_ending_content() throws Throwable {
        final String STRING = "foo foo bar";
        final String STRING2 = "foo bar bar";
        final File file = aFile(folder)
                .withContent(STRING).build();

        final ByteArrayOutputStream baos = aStream()
                .withContent(STRING2).build();

        SameEndingVerifier verifier = new SameEndingVerifier(file, baos, ">");

        verifier.verify();

    }

}
