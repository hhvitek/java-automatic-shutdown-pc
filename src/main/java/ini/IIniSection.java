package ini;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Specifies possible methods of INI section
 */
public interface IIniSection {

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     *
     * @return the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key
     */
    @Nullable String getValue(@NotNull String key);

    /**
     * Returns {@code true} if the specified key (key) is mapped
     * or returns {@code false}.
     *
     * @param key the key whose associated value is to be looked for.
     *
     * @return Returns {@code true} if the specified key is mapped
     * or returns {@code false}.
     */
    boolean hasItem(@NotNull String key);

    /**
     * Inserts the new value into the section
     *
     * @param key   the name of the specific item
     * @param value the associated value to be inserted into the structure.
     */
    void putValue(@NotNull String key, @NotNull String value);

    /**
     * Inserts the new comment for the specific item (key).
     *
     * @param key     the name of the specific item
     * @param comment the associated comment to be inserted into the structure
     */
    void putComment(@NotNull String key, @NotNull String comment);

    /**
     * Inserts the new section comment.
     *
     * @param comment the associated comment to be inserted into the structure
     */
    void putSectionComment(@NotNull String comment);

    /**
     * Returns the comment for the specific item (key).
     *
     * @param key the item's key
     *
     * @return If the key doesn't exist returns an empty String.
     */
    String getComment(@NotNull String key);

    /**
     * Returns the section comment. If the section comments doesn't exist returns an empty String.
     *
     * @return comment the associated comment to be inserted into the structure
     */
    String getSectionComment();

    String toString();

}
