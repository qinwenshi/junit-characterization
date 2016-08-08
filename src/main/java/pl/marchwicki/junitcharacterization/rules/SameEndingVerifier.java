package pl.marchwicki.junitcharacterization.rules;

import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.myers.Equalizer;
import org.junit.rules.Verifier;
import pl.marchwicki.junitcharacterization.ReadLines;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static pl.marchwicki.junitcharacterization.IsEmptyPatchMatcher.empty;

/**
 * Created by qinwenshi on 8/8/16.
 */
public class SameEndingVerifier extends Verifier {
    private final File pinchFile;
    private final ByteArrayOutputStream capturedStream;
    private final String seperator;

    public SameEndingVerifier(File pinchFile, ByteArrayOutputStream capturedStream, String seperator) {
        this.pinchFile = pinchFile;
        this.capturedStream = capturedStream;
        this.seperator = seperator;
    }

    @Override
    protected void verify() throws Throwable {
        List<String> actual = ReadLines.fromStream(capturedStream);
        List<String> original = ReadLines.fromFile(pinchFile);

        Patch<String> patch = DiffUtils.diff(original, actual, new Equalizer<String>() {
            @Override
            public boolean equals(String s, String t1) {
                DiffRowGenerator generator = new DiffRowGenerator.Builder()
                        .ignoreWhiteSpaces(true)
                        .columnWidth(Integer.MAX_VALUE) // do not wrap
                        .build();
                List<DiffRow> rows = generator.generateDiffRows(split2(s), split2(t1));
                return rows.get(rows.size()-1).getTag() == DiffRow.Tag.EQUAL;
            }
            private List<String> split2(String content) {
                return Arrays.asList(content.split(seperator));
            }

        });

        assertThat(patch, is(empty()));
    }


}
