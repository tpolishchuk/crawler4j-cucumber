package unit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.fail;

public abstract class AbstractUnitTest {

    private static final Logger LOG = Logger.getLogger("[UnitTest]");
    protected String testDirPath;

    public AbstractUnitTest() {
        String testRunId = RandomStringUtils.randomNumeric(8);
        testDirPath = "tmp/" + testRunId + "/";
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            LOG.info("Starting test: " + description.getClassName() + ": "
                     + description.getMethodName() + "()");
        }
    };

    @Before
    public void setUp() {
        try {
            File tmpDirectory = new File(testDirPath);
            FileUtils.forceMkdir(tmpDirectory);
        } catch (IOException e) {
            fail("Unable to create temporary test directory " + testDirPath);
        }
    }

    @After
    public void tearDown() {
        try {
            File tmpDirectory = new File(testDirPath);
            FileUtils.forceDelete(tmpDirectory);
        } catch (IOException e) {
            fail("Unable to delete temporary test directory " + testDirPath);
        }
    }
}
