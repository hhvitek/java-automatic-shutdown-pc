package ini.inireaders;

import ini.IIniConfig;
import ini.InvalidConfigFileFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Parses specific ini configuration file such as:
 *
 * <pre>
 * {@code
 * # my header comment 1st line.
 * # my header comment 2nd line.
 *
 * # my first section comment.
 * [first_section]
 * first_key = first_value
 * second_key = second_value
 *
 * [second_section]
 * # the second section, first item comment.
 * first_ket = first_value_second_section
 * }
 * </pre>
 *
 * <p>
 * Reads the input line by line. The any line of the valid configuration file have to comfort to
 * the one of the following rules:
 *
 * <ol>
 *     <li>Comment
 *         <ul>
 *             <li>Begins with the "#" character
 *             <li>#[space] any characters to the end of line
 *         </ul>
 *     <li>Empty line
 *     <li>Section
 *         <ul>
 *             <li>Begins with the "[" character
 *             <li>[[a-zA-Z_0-9]+] ... word character \w+
 *         </ul>
 *     <li>Item line defined by key and value
 *          <ul>
 *             <li>Begins with any word character \w
 *             <li>key = value
 *             <li>\w+[space]=[space]\w+
 *          </ul>
 * </ol>
 */
public interface IIniReader {

    void load(@NotNull IIniConfig ini, @NotNull File file)
            throws IOException, InvalidConfigFileFormatException;

    void load(@NotNull IIniConfig ini, @NotNull BufferedReader reader)
            throws IOException, InvalidConfigFileFormatException;

}
