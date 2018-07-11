package sheldon.com.android.gohome.content;

public class WidgetControl {
    private String name;
    private String status;
    private int toggleState;

    public WidgetControl(String name, String status, int toggleState) {
        this.name = name;
        this.status = status;
        this.toggleState = toggleState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getToggleState() {
        return toggleState;
    }

    public void setToggleState(int toggleState) {
        this.toggleState = toggleState;
    }
}
