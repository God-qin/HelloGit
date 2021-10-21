package report;

/**
 * Created by terry.qian on 2017/11/8.
 */
public enum ColumnCategory {
    DIMENSION(1),
    QUOTA(2),
    TIME(3),
    MAP_DIMENSION(4);

    private int value;

    private ColumnCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public ColumnCategory valueOf(int value) {
        switch (value) {

            case 1:
                return DIMENSION;
            case 2:
                return QUOTA;
            case 3:
                return TIME;
            case 4:
                return MAP_DIMENSION;
            default:
                return null;
        }
    }
}
