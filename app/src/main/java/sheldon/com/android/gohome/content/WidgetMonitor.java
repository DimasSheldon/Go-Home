package sheldon.com.android.gohome.content;

public class WidgetMonitor {
    private String name;
    private String status;
    private int photoId;

    public WidgetMonitor(String name, String status, int photoId) {
        this.name = name;
        this.status = status;
        this.photoId = photoId;
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

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
