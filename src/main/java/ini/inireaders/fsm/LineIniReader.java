package ini.inireaders.fsm;

import ini.IIniConfig;
import ini.InvalidConfigFileFormatException;
import ini.inireaders.IIniReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * For the detailed description of configuration file see:
 * {@link IIniReader}
 *
 * <p>
 * This is the concrete implementation of the above {@code ILoader} interface. This is an one-file
 * implementation.
 * Using as straight forward approach as possible.
 * <p>
 * If-else statement is used to determine how the input line should be processed.
 * <p>
 * The state is spread across all methods.
 */
public class LineIniReader implements IIniReader {

    private static final Logger logger = LoggerFactory.getLogger(LineIniReader.class);

    // this configuration is gonna be modified.
    private IIniConfig ini;

    // ##### STATE VARIABLES

    // FSM last accumulated comments
    // List... because it could be a multiline comment
    private final List<String> activeComment = new ArrayList<>();

    // FSM last section encountered
    // Used for an item mapping -> last_section+key_encountered=value
    private String activeSection = "";

    // FSM configuration file header comment...
    // true until the first blank line or the first section encountered.
    private boolean isHeaderCommentPossible = true;

    // ##### END STATE VARIABLES

    // FSM patterns to parse encountered lines
    private static final Pattern commentPattern = Pattern.compile("#\\s*(.*)");
    private static final Pattern sectionPattern = Pattern.compile("\\[([a-zA-Z_0-9]+)]");
    private static final Pattern itemPattern    = Pattern.compile("([a-zA-Z_0-9]+)\\s*=\\s*(.*)");

    // clear FSM state
    // called at the beginning of an input parsing.
    private void init() {
        activeComment.clear();
        activeSection = "";
        isHeaderCommentPossible = true;
    }

    @Override
    public void load(@NotNull IIniConfig ini, @NotNull File file)
            throws IOException, InvalidConfigFileFormatException {

        init();
        this.ini = ini;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            load(ini, reader);
        }
    }

    @Override
    public void load(@NotNull IIniConfig ini, @NotNull BufferedReader reader)
            throws IOException, InvalidConfigFileFormatException {

        init();
        this.ini = ini;

        String line;
        while ((line = reader.readLine()) != null) {
            if (!processLine(line)) {
                throw new InvalidConfigFileFormatException(
                        "Parsing error encountered around the line: " + line);
            }
        }
    }

    /**
     * Process the current line in the input. Expects either a comment, a blank line, a section or
     * an
     * item line. See {@link LineIniReader}
     *
     * @param line The input configuration. Well just one line of it.
     *
     * @return false if parsing had failed. The input configuration is considered invalid.
     */
    private boolean processLine(String line) {
        if (line.isBlank()) {
            return processEmptyLine();
        } else if (line.startsWith("#")) {
            return processCommentLine(line);
        } else if (line.startsWith("[")) {
            return processSectionLine(line);
        } else {
            return processItemLine(line);
        }
    }

    private boolean processCommentLine(String line) {
        Matcher m = commentPattern.matcher(line);
        if (m.matches()) {
            activeComment.add(m.group(1));
            return true;
        }
        return false;
    }

    private boolean processEmptyLine() {
        if (isHeaderCommentPossible && !activeComment.isEmpty()) {
            ini.putHeaderComment(String.join(System.lineSeparator(), activeComment));
            activeComment.clear();
        }

        isHeaderCommentPossible = false;
        return true;
    }

    private boolean processSectionLine(String line) {
        isHeaderCommentPossible = false;

        Matcher m = sectionPattern.matcher(line);
        if (m.matches()) {
            String sectionName = m.group(1);
            activeSection = sectionName;
            ini.putSectionComment(sectionName, String.join(System.lineSeparator(), activeComment));
            activeComment.clear();
            return true;
        }

        return false;
    }

    private boolean processItemLine(String line) {
        if (activeSection != null && !activeSection.isBlank()) { // the item line have to be
            // "inside" the section [section1]item=value
            Matcher m = itemPattern.matcher(line);
            if (m.matches()) {
                String key = m.group(1);
                String value = m.group(2);
                ini.putValue(activeSection, key, value);
                ini.putComment(activeSection, key,
                               String.join(System.lineSeparator(), activeComment)
                );
                activeComment.clear();
                return true;
            }
        }
        return false;
    }

}
