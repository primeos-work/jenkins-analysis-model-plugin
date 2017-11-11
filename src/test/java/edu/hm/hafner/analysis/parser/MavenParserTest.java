package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the class {@link JavacParser} for output log of a maven compile.
 */
public class MavenParserTest extends ParserTester {
    private static final String WARNING_TYPE = new JavacParser().getId();

    /**
     * Parses a file with two deprecation warnings.
     */
    @Test
    public void parseMaven() {
        Issues<Issue> warnings = new JavacParser().parse(openFile());

        assertEquals(5, warnings.size());

        Iterator<Issue> iterator = warnings.iterator();
        checkMavenWarning(iterator.next(), 3);
        checkMavenWarning(iterator.next(), 36);
        checkMavenWarning(iterator.next(), 47);
        checkMavenWarning(iterator.next(), 69);
        checkMavenWarning(iterator.next(), 105);
    }

    /**
     * Verifies the annotation content.
     *
     * @param annotation
     *         the annotation to check
     * @param lineNumber
     *         the line number of the warning
     */
    private void checkMavenWarning(final Issue annotation, final int lineNumber) {
        assertSoftly(softly -> {
            softly.assertThat(annotation)
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(AbstractParser.PROPRIETARY_API)
                    .hasLineStart(lineNumber)
                    .hasLineEnd(lineNumber)
                    .hasMessage(
                            "com.sun.org.apache.xerces.internal.impl.dv.util.Base64 is Sun proprietary API and may be removed in a future release")
                    .hasFileName(
                            "/home/hudson/hudson/data/jobs/Hudson main/workspace/remoting/src/test/java/hudson/remoting/BinarySafeStreamTest.java");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "maven.txt";
    }
}

