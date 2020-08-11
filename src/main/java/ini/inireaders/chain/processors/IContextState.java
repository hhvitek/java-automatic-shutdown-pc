package ini.inireaders.chain.processors;

public interface IContextState {

    void addCommentLine(String line);

    void setHeaderComment();

    void setActualSection(String sectionName);

    void setItem(String key, String value);

    boolean isError();

    void setErrorMessage(String message);

    String getErrorMessage();

}
