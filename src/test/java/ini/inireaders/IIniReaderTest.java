package ini.inireaders;

import ini.IIniConfig;
import ini.InvalidConfigFileFormatException;
import ini.myini.CustomIIniConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public abstract class IIniReaderTest {

    protected IIniReader iniReader;

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
        IIniConfig ini;
        try (StringReader reader = new StringReader(expected)) {
            ini = new CustomIIniConfig(iniReader);
            ini.load(new BufferedReader(reader));
        }

        String actual = ini.toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testLoadReaderErrorUnknownCommentSymbol() {
        String input = String.join(System.lineSeparator(), "; my section comment", "[section]",
                "key = value"
        );

        try (StringReader reader = new StringReader(input)) {
            IIniConfig ini = new CustomIIniConfig(iniReader);

            Assertions.assertThrows(InvalidConfigFileFormatException.class,
                    () -> ini.load(new BufferedReader(reader))
            );
        }

    }

    @Test
    void testLoadReaderParsingErrorItemBeforeFirstSection() {
        String input = String.join(System.lineSeparator(), "# header comment", "",
                "# the item comment that should have been the section one",
                "key = value", "[section]", "key = value"
        );

        try (StringReader reader = new StringReader(input)) {
            IIniConfig ini = new CustomIIniConfig(iniReader);

            Assertions.assertThrows(InvalidConfigFileFormatException.class,
                    () -> ini.load(new BufferedReader(reader))
            );
        }

    }

}
