package ini.myini;

public class CustomItem {

    private String key     = "";
    private String comment = "";
    private String value   = "";

    public CustomItem() {
    }

    public CustomItem(String key, String value, String comment) {
        this.key = key;
        this.value = value;
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean hasComment() {
        return comment != null && !comment.isBlank();
    }

    @Override
    public String toString() {
        String toStr = "";
        if (comment != null && !comment.isBlank()) {
            toStr += CustomIIniConfig.createToStringComment(comment) + System.lineSeparator();
        }

        return toStr + key + " = " + value;
    }

}
