package behavior.steps;

import cucumber.api.java8.En;
import io.cucumber.datatable.DataTable;
import org.lrp.helpers.common.FileHelper;

import java.io.File;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class FilesSteps implements En {

    public FilesSteps() {

        Then("the following files should be created", (DataTable filePathsTable) -> {
            List<String> filePaths = filePathsTable.asList();

            for (String filePath : filePaths) {
                File file = new File(filePath);

                assertTrue("File " + filePath + " does not exist", file.exists());
            }
        });

        Then("file {string} should contain at least {long} URLs", (String filePath, Long expectedLinesAmount) -> {
            File file = new File(filePath);
            long actualLinesAmount = FileHelper.getLinesAmount(file);

            assertTrue("File contains unexpected lines amount", actualLinesAmount >= expectedLinesAmount);
        });
    }
}
