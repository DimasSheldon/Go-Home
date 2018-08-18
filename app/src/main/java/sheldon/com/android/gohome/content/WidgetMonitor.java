package sheldon.com.android.gohome.content;

public class WidgetMonitor {
    private String label;
    private String value;
    private int icon, cvColor, iconColor;

    public WidgetMonitor(String label, String value, int icon, int cvColor, int iconColor) {
        this.icon = icon;
        this.label = label;
        this.cvColor = cvColor;
        this.iconColor = iconColor;
        this.value = value;
        this.iconColor = iconColor;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int getIcon() {
        return icon;
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

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setColor(int color) {
        this.cvColor = color;
    }
}
