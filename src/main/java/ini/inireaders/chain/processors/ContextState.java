package ini.inireaders.chain.processors;

import ini.IIniConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ContextState implements IContextState {

    private static final Logger logger = LoggerFactory.getLogger(ContextState.class);

    private String errorMessage;

    private IIniConfig ini;

    private String actualSection = "";

    private final List<String> actualComment = new ArrayList<>();

    public ContextState(IIniConfig ini) {
        this.ini = ini;
    }

    @Override
    public void addCommentLine(String line) {
        actualComment.add(line);
    }

    @Override
    public void setHeaderComment() {
        if (!actualComment.isEmpty()) {
            ini.putHeaderComment(joinListToMultilineString(actualComment));
            actualComment.clear();
        }
    }

    @Override
    public void setActualSection(String sectionName) {
        actualSection = sectionName;

        if (!actualComment.isEmpty()) {
            ini.putSectionComment(sectionName, joinListToMultilineString(actualComment));
            actualComment.clear();
        }
    }

    @Override
    public void setItem(String key, String value) {
        if (actualSection == null || actualSection.isBlank()) {
            setErrorMessage("The item have to be inside the section.");
            return;
        }

        ini.putValue(actualSection, key, value);
        if (!actualComment.isEmpty()) {
            ini.putComment(actualSection, key, joinListToMultilineString(actualComment));
            actualComment.clear();
        }
    }

    @Override
    public boolean isError() {
        return errorMessage != null;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    public static String joinListToMultilineString(List<String> list) {
        if (list == null) {
            return null;
        }

        return String.join(System.lineSeparator(), list);
    }

}
