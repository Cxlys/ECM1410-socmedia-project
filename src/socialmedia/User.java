package socialmedia.socialmedia;

public class User {
    private int id;
    private String handle;
    private String description;
    private static int nextId = 0;
    public User(String handle){
        this.handle = handle;
        this.id = nextId++;
    }
    public User(String handle, String description){
        this.handle = handle;
        this.description = description;
        this.id = nextId++;
    }
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}