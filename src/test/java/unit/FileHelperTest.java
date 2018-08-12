package unit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.lrp.helpers.common.FileHelper;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileHelperTest extends AbstractUnitTest {

    @Test
    public void createDirectoryIfDoesNotExist() {
        String directoryPath = testDirPath + RandomStringUtils.randomNumeric(5);
        File childDirectory = new File(directoryPath);

        FileHelper.createDirectoryIfDoesNotExist(directoryPath);

        assertTrue(directoryPath + " directory is not found", childDirectory.exists());
    }

    @Test
    public void cleanDirectoryIfNotEmpty() {
        String filename = RandomStringUtils.randomNumeric(10) + ".log";
        File parentDirectory = new File(testDirPath);
        File file = new File(testDirPath + filename);

        FileHelper.createFile(file);
        FileHelper.cleanDirectoryIfNotEmpty(testDirPath);

        Collection<File> files = FileUtils.listFiles(parentDirectory, null, true);
        assertTrue("Directory is not clean", files.isEmpty());
    }

    @Test
    public void createFile() {
        String filename = RandomStringUtils.randomNumeric(10) + ".log";
        File file = new File(testDirPath + filename);

        FileHelper.createFile(file);

        assertTrue(filename + " file is not found", file.exists());
    }

    @Test
    public void deleteFile() {
        String filename = RandomStringUtils.randomNumeric(10) + ".log";
        File parentDirectory = new File(testDirPath);
        File file = new File(testDirPath + filename);

        FileHelper.createFile(file);
        FileHelper.deleteFile(file);

        Collection<File> files = FileUtils.listFiles(parentDirectory, new String[]{".log"}, false);
        assertTrue("File is present in directory", files.isEmpty());
    }

    @Test
    public void appendStringToFile() {
        String filename = RandomStringUtils.randomNumeric(10) + ".log";
        String string = RandomStringUtils.randomAlphabetic(10);
        File file = new File(testDirPath + filename);

        FileHelper.createFile(file);
        FileHelper.appendStringToFile(file, string);

        String fileContent = FileHelper.getFileContent(file);
        assertTrue("File content ends with an unexpected data", fileContent.endsWith(string));
    }

    @Test
    public void appendRowToFile() {
        String filename = RandomStringUtils.randomNumeric(10) + ".log";
        String string = RandomStringUtils.randomAlphabetic(10);
        File file = new File(testDirPath + filename);

        FileHelper.createFile(file);
        FileHelper.appendRowToFile(file, string);

        String fileContent = FileHelper.getFileContent(file);
        assertTrue("File content ends with an unexpected data", fileContent.endsWith(string + "\n"));
    }

    @Test
    public void getLinesAmount() {
        String filename = RandomStringUtils.randomNumeric(10) + ".log";
        String string = RandomStringUtils.randomAlphabetic(10);
        File file = new File(testDirPath + filename);

        FileHelper.createFile(file);
        FileHelper.appendRowToFile(file, string);
        FileHelper.appendRowToFile(file, string);
        FileHelper.appendRowToFile(file, string);

        long linesCount = FileHelper.getLinesAmount(file);
        assertEquals("File contains unexpected lines amount", 3, linesCount);
    }
}
