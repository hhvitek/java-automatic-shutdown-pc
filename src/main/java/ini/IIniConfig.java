package ini;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * Specifies all methods required by INI library.
 */
public interface IIniConfig {

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param sectionName the key (section name) whose associated value is to be returned
     *
     * @return the value of IIniSection ({@link IIniSection}) to which the specified key is mapped,
     * or
     * {@code null} if this map contains no mapping for the key
     */
    @Nullable
    IIniSection getSection(@NotNull String sectionName);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param sectionName the first part of the key (section name) whose associated value is to be
     *                    returned
     * @param key         the second part of the key (key) whose associated value is to be returned
     *
     * @return the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key
     */
    @Nullable
    String getValue(@NotNull String sectionName, @NotNull String key);

    /**
     * Returns {@code true} if the specified key (sectionName and key) is mapped
     * or returns {@code false}.
     *
     * @param sectionName the first part of the key (section name) whose associated value is to be
     *                    searched
     * @param key         the second part of the key (key) whose associated value is to be looked
     *                    for.
     *
     * @return Returns {@code true} if the specified key (sectionName and key) is mapped
     * or returns {@code false}.
     */
    boolean hasItem(@NotNull String sectionName, @NotNull String key);

    /**
     * Returns {@code true} if the specified section (sectionName) is mapped
     * or returns {@code false}.
     *
     * @param sectionName the section name to look for.
     *
     * @return Returns {@code true} if the specified section (sectionName) is mapped
     * or returns {@code false}.
     */
    boolean hasSection(@NotNull String sectionName);

    /**
     * Loads the configuration file
     *
     * @param file the configuration file
     *
     * @throws IOException                      well working with the file system...
     * @throws InvalidConfigFileFormatException if the configuration file is invalid.
     */
    void load(@NotNull File file)
            throws IOException, InvalidConfigFileFormatException;

    void load(@NotNull BufferedReader reader)
            throws IOException, InvalidConfigFileFormatException;

    /**
     * Adds the comment to the specified section.
     *
     * @param sectionName section name
     * @param comment     comment to add
     */
    void putSectionComment(@NotNull String sectionName, @NotNull String comment);

    /**
     * Adds the comment to the specified item in the section.
     *
     * @param sectionName section name
     * @param key         item's identifier key
     * @param comment     the comment
     */
    void putComment(@NotNull String sectionName, @NotNull String key, @NotNull String comment);

    /**
     * Simply replace/add header comment.
     *
     * @param comment the header comment
     */
    void putHeaderComment(@NotNull String comment);

    /**
     * Returns the comment for the specific item, identified by a sectionName and a key of the item.
     * If the item doesn't have any comment, an empty String is returned.
     *
     * @param sectionName section name
     * @param key         item's identifier key
     *
     * @return the item's comment or an empty string if the comment doesn't exit
     */
    String getComment(@NotNull String sectionName, @NotNull String key);

    /**
     * Returns the section comment for the specific section.
     * If the section doesnt have any section comment, an empty String is returned.
     *
     * @param sectionName section name
     *
     * @return the section's comment or an empty string if the comment doesn't exit
     */
    String getSectionComment(@NotNull String sectionName);

    /**
     * Returns the header comment for the whole ini.
     * On an empty header comment, returns an empty String.
     *
     * @return the ini's header comment or an empty string if the comment doesn't exit
     */
    String getHeaderComment();

    /**
     * Adds the new value with specified mapping (sectionName and key).
     *
     * @param sectionName section name
     * @param key         item's identifier key
     * @param value       the new value to add
     */
    void putValue(@NotNull String sectionName, @NotNull String key, @NotNull String value);

    /**
     * Stores the current object into the specified file.
     *
     * @param file the targeted file.
     *
     * @throws IOException working with a file system
     */

    void store(@NotNull File file)
            throws IOException;

    String toString();

}
