package model.db.repo;

import org.jetbrains.annotations.NotNull;

public class ElemNotFoundException extends Exception {

    private static final long serialVersionUID = 6441482060828926996L;
    private Object where;
    private Object what;

    public ElemNotFoundException(@NotNull String errorMessage) {
        super(errorMessage);

    }

    public ElemNotFoundException(@NotNull Class<?> where, @NotNull Object what) {
        this(where.toString(), what.toString());
    }

    public ElemNotFoundException(@NotNull String where, @NotNull String what) {
        this.what = what;
        this.where = where;
    }

    public String getErrorMessage() {
        return toString();
    }

    @Override
    public String toString() {
        return "ElemNotFoundException{" +
                "where=" + where +
                ", what=" + what +
                '}';
    }
}
