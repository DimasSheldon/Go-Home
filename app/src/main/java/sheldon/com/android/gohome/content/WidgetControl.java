package sheldon.com.android.gohome.content;

public class WidgetControl {
    private String label;
    private String value;
    private int cvColor, iconColor;
//    private int toggleState;

    public WidgetControl(String label, String value, int cvColor, int iconColor) {
        this.label = label;
        this.cvColor = cvColor;
        this.iconColor = iconColor;
        this.value = value;
//        this.toggleState = toggleState;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int cvColor() {
        return cvColor;
    }

    public int iconColor() {
        return iconColor;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setColor(int color) {
        this.cvColor = color;
    }

//    public int getToggleState() {
//        return toggleState;
//    }
//
//    public void setToggleState(int toggleState) {
//        this.toggleState = toggleState;
//    }
}
