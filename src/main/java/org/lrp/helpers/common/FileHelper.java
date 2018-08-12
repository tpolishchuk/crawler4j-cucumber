package org.lrp.helpers.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Stream;

public class FileHelper {

    public static void createDirectoryIfDoesNotExist(String directoryPath) {
        File directory = new File(directoryPath);
        try {
            FileUtils.forceMkdir(directory);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Cannot create directory with path %s:\n%s",
                    directoryPath, e.getMessage()));
        }
    }

    public static void cleanDirectoryIfNotEmpty(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.listFiles() != null) {
            try {
                FileUtils.deleteDirectory(directory);
                FileUtils.forceMkdir(directory);
            } catch (IOException e) {
                throw new IllegalStateException(String.format("Cannot clean directory with path %s:\n%s",
                        directoryPath, e.getMessage()));
            }
        }
    }

    public static void createFile(File file) {
        try {
            FileUtils.touch(file);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Cannot create a file %s:\n%s",
                    file.getAbsolutePath(), e.getMessage()));
        }
    }

    public static void deleteFile(File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Cannot create a file %s:\n%s",
                    file.getAbsolutePath(), e.getMessage()));
        }
    }

    public static void appendStringToFile(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data, Charset.defaultCharset(), true);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Cannot append string to existing content of file %s:\n%s",
                    file.getAbsolutePath(), e.getMessage()));
        }
    }

    public static void appendRowToFile(File file, String data) {
        appendStringToFile(file, data + "\n");
    }

    public static String getFileContent(File file) {
        try {
            return FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Cannot get content of file %s:\n%s",
                                                          file.getAbsolutePath(), e.getMessage()));
        }
    }

    public static long getLinesAmount(File file) {
        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.count();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Cannot get content of file %s:\n%s",
                                                          file.getAbsolutePath(), e.getMessage()));
        }
    }
}
