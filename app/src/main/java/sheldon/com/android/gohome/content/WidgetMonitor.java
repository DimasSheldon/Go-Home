package sheldon.com.android.gohome.content;


public class WidgetMonitor {
    private String label;
    private String status;
    private int icon, cvColor, iconColor;

    public WidgetMonitor(String label, String status, int icon, int cvColor, int iconColor) {
        this.label = label;
        this.status = status;
        this.icon = icon;
        this.cvColor = cvColor;
        this.iconColor = iconColor;
    }

    public String getLabel() {
        return label;
    }

    public String getStatus() {
        return status;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setColor(int color) {
        this.cvColor = color;
    }
}
