package ini.myini;

import ini.IIniSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CustomIIniSection implements IIniSection, Iterable<CustomItem> {

    private static final Logger logger         = LoggerFactory.getLogger(CustomIIniSection.class);
    private              String sectionComment = "";
    private              String name;

    // items value=key
    private final List<CustomItem> items = new ArrayList<>();

    public CustomIIniSection(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @Nullable String getValue(@NotNull String key) {
        if (key.isBlank()) {
            logger.warn("Asking for an empty key.");
            return null;
        }

        Optional<CustomItem> optItem = items.stream()
                                            .filter(x -> key.equals(x.getKey()))
                                            .findAny();

        return optItem.map(CustomItem::getValue)
                      .orElse(null);
    }

    @Override
    public boolean hasItem(@NotNull String key) {
        if (key.isBlank()) {
            logger.warn("Asking for an empty key.");
            return false;
        }

        return items.stream()
                    .anyMatch(x -> key.equals(x.getKey()));
    }

    @Override
    public void putValue(@NotNull String key, @NotNull String value) {
        if (key.isBlank()) {
            logger.warn("Trying to input an empty key/value item.");
            return;
        }

        items.stream()
             .filter(x -> key.equals(x.getKey()))
             .findAny()
             .ifPresentOrElse(x -> x.setValue(value), () -> {
                 CustomItem item = new CustomItem(key, value, "");
                 items.add(item);
             });

    }

    @Override
    public void putComment(@NotNull String key, @NotNull String comment) {

        if (key.isBlank()) {
            logger.warn("Trying to use an invalid blank key.");
            return;
        }

        items.stream()
             .filter(x -> key.equals(x.getKey()))
             .findAny()
             .ifPresentOrElse(x -> x.setComment(comment), () -> {
                 CustomItem item = new CustomItem(key, "", comment);
                 items.add(item);
             });
    }

    @Override
    public void putSectionComment(@NotNull String comment) {
        sectionComment = comment;
    }

    @Override
    public @NotNull String getComment(@NotNull String key) {
        if (key.isBlank()) {
            logger.warn("Trying to use an invalid blank key.");
            return "";
        }

        Optional<CustomItem> optItem = items.stream()
                                            .filter(x -> key.equals(x.getKey()))
                                            .findAny();

        return optItem.map(CustomItem::getComment)
                      .orElse("");
    }

    @Override
    public @NotNull String getSectionComment() {
        return sectionComment;
    }

    @Override
    public String toString() {
        String toStr = "";
        if (sectionComment != null && !sectionComment.isBlank()) {
            toStr +=
                    CustomIIniConfig.createToStringComment(sectionComment) + System.lineSeparator();
        }
        toStr += "[" + name + "]" + System.lineSeparator();

        /*
            [section] there is no empty line between a section name and the first comment
            # the first comment
            key1 = value1
            <--empty line-->
            # the second comment
            key2 = value2
            <-empty line-->
            [section2]
         */
        String tmpToStr = "";
        for (CustomItem item : items) {
            if (item.hasComment()) { // insert empty space before any comment line
                tmpToStr += System.lineSeparator();
            }

            tmpToStr += item.toString() + System.lineSeparator();
        }
        toStr += tmpToStr.stripLeading(); // remove an empty space between a section name and the
        // first comment if any exists

        return toStr;
    }

    @Override
    public @NotNull Iterator<CustomItem> iterator() {
        return items.iterator();
    }

}
