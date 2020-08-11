package ini.myini;


import ini.IIniConfig;
import ini.IIniSection;
import ini.InvalidConfigFileFormatException;
import ini.inireaders.FactoryReader;
import ini.inireaders.IIniReader;
import ini.inireaders.fsm.LineIniReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for representation of INI configurations.
 */
public class CustomIIniConfig implements IIniConfig, Iterable<IIniSection> {

    private static final Logger logger = LoggerFactory.getLogger(CustomIIniConfig.class);

    // ready for extension, might be used by child class. Such as Setting.
    private final Map<String, IIniSection> sections = new LinkedHashMap<>();

    private String     headerComment;
    private IIniReader loader = new LineIniReader();

    public CustomIIniConfig() {
        loader = FactoryReader.getDefaultIniReader();
    }

    public CustomIIniConfig(FactoryReader.IniReaders type) {
        loader = FactoryReader.getIniReader(type);
    }

    public CustomIIniConfig(IIniReader loader) {
        this.loader = loader;
    }

    @Override
    public @Nullable IIniSection getSection(@NotNull String sectionName) {
        return sections.get(sectionName);

    }

    @Override
    public @Nullable String getValue(@NotNull String sectionName, @NotNull String key) {
        if (hasSection(sectionName)) {
            return getSection(sectionName).getValue(key);
        }
        return null;
    }

    @Override
    public boolean hasItem(@NotNull String sectionName, @NotNull String key) {
        if (hasSection(sectionName)) {
            return getSection(sectionName).hasItem(key);
        }
        return false;
    }

    @Override
    public boolean hasSection(@NotNull String sectionName) {
        return getSection(sectionName) != null;
    }

    @Override
    public void load(@NotNull File file)
            throws IOException, InvalidConfigFileFormatException {
        loader.load(this, file);
    }

    @Override
    public void load(@NotNull BufferedReader reader)
            throws IOException, InvalidConfigFileFormatException {
        loader.load(this, reader);
    }

    @Override
    public void putSectionComment(@NotNull String sectionName, @NotNull String comment) {
        if (!hasSection(sectionName)) {
            createSection(sectionName);
        }

        getSection(sectionName).putSectionComment(comment);
    }

    @Override
    public void putComment(@NotNull String sectionName, @NotNull String key,
                           @NotNull String comment) {
        if (!hasSection(sectionName)) {
            createSection(sectionName);
        }

        getSection(sectionName).putComment(key, comment);

    }

    @Override
    public void putValue(@NotNull String sectionName, @NotNull String key, @NotNull String value) {
        if (hasSection(sectionName)) {
            getSection(sectionName).putValue(key, value);
        } else {
            IIniSection newSection = createSection(sectionName);
            newSection.putValue(key, value);
        }

    }

    @Override
    public void store(@NotNull File file)
            throws IOException {
        // Files.writeString
        // try PrintWriter FileWriter print
        Files.write(file.toPath(), this.toString()
                                       .getBytes());

    }

    @Override
    public String toString() {
        String toStr = "";
        if (headerComment != null && !headerComment.isBlank()) {
            toStr += createToStringComment(headerComment) + System.lineSeparator();
            toStr += System.lineSeparator(); // empty line
        }

        toStr += sections.values()
                         .stream()
                         .map(IIniSection::toString)
                         .reduce((x, y) -> x + System.lineSeparator() + y)
                         .orElse("");

        return toStr.strip();

    }

    @Override
    public @NotNull String getComment(@NotNull String sectionName, @NotNull String key) {

        if (hasSection(sectionName)) {
            return getSection(sectionName).getComment(key);
        }

        return "";
    }

    @Override
    public @NotNull String getSectionComment(@NotNull String sectionName) {
        if (hasSection(sectionName)) {
            return getSection(sectionName).getSectionComment();
        }
        return "";
    }

    @Override
    public @NotNull String getHeaderComment() {
        return headerComment;
    }

    @Override
    public void putHeaderComment(@NotNull String comment) {
        headerComment = comment;

    }

    static String createToStringComment(String multiline) {
        if (multiline == null || multiline.isBlank()) {
            return multiline;
        }

        return multiline.lines()
                        .map("# "::concat)
                        .reduce((x, y) -> x + System.lineSeparator() + y)
                        .orElse("");
    }

    @Override
    public @NotNull Iterator<IIniSection> iterator() {
        return sections.values()
                       .iterator();
    }

    private @NotNull IIniSection createSection(@NotNull String sectionName) {
        if (!hasSection(sectionName)) {
            IIniSection section = new CustomIIniSection(sectionName);
            sections.put(sectionName, section);
            return section;
        }
        return getSection(sectionName);
    }

}
