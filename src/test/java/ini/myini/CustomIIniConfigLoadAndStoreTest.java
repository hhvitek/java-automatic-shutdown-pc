package ini.myini;


import ini.IIniConfig;
import ini.InvalidConfigFileFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

class CustomIIniConfigLoadAndStoreTest {

    @Test
    void testLoadFile_testToStringMethod()
            throws InvalidConfigFileFormatException, IOException {

        String resourceName = "ini/myini/default_config_ini.ini";
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());

        IIniConfig ini = new CustomIIniConfig();
        ini.load(file);

        String actual = ini.toString();
        String expected = Files.readString(file.toPath());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testLoadReader()
            throws InvalidConfigFileFormatException, IOException {
        String expected = String.join(System.lineSeparator(), "# my header comment 1st line.",
                                      "# my header comment 2nd line.", "",
                                      "# my first section comment.", "[first_section]",
                                      "first_key = first_value", "second_key = second_value", "",
                                      "[second_section]",
                                      "# the second section, first item comment.",
                                      "first_ket = first_value_second_section"
        );
        StringReader reader = new StringReader(expected);

        IIniConfig ini = new CustomIIniConfig();
        ini.load(new BufferedReader(reader));

        String actual = ini.toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testLoadReaderErrorUnknownCommentSymbol() {
        String input = String.join(System.lineSeparator(), "; my section comment", "[section]",
                                   "key = value"
        );

        StringReader reader = new StringReader(input);
        CustomIIniConfig ini = new CustomIIniConfig();

        Assertions.assertThrows(InvalidConfigFileFormatException.class,
                                () -> ini.load(new BufferedReader(reader))
        );

    }

    @Test
    void testLoadReaderParsingErrorItemBeforeFirstSection() {
        String input = String.join(System.lineSeparator(), "# header comment", "",
                                   "# the item comment that should have been the section one",
                                   "key = value", "[section]", "key = value"
        );

        StringReader reader = new StringReader(input);
        CustomIIniConfig ini = new CustomIIniConfig();

        Assertions.assertThrows(InvalidConfigFileFormatException.class,
                                () -> ini.load(new BufferedReader(reader))
        );

    }

    @Test
    void testStore(@TempDir Path tempDir)
            throws IOException, InvalidConfigFileFormatException {

        Path tmpConfigFile = tempDir.resolve("created_config_ini.ini");
        String expectedConfigFileResourceName = "ini/myini/default_config_ini.ini";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File actualFile = tmpConfigFile.toFile();
        File expectedFile = new File(classLoader.getResource(expectedConfigFileResourceName).getFile());

        IIniConfig ini = new CustomIIniConfig();
        ini.load(expectedFile);

        ini.store(actualFile);

        String actual = Files.readString(actualFile.toPath());
        String expected = Files.readString(expectedFile.toPath());

        Assertions.assertEquals(expected, actual, "Files do not match.");
    }

}
