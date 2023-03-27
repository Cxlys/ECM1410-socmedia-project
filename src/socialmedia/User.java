package socialmedia.socialmedia;

import java.util.Objects;

public class User {
    private int id;
    private String handle;
    private String description = "";

    public User(String handle){
        this.handle = handle;
        this.id = Objects.hash(handle);
    }
    public User(String handle, String description){
        this.handle = handle;
        this.description = description;
        this.id = Objects.hash(handle);
    }

    public int getId() { return id; }
    public void updateId() { id = Objects.hash(handle); }
    public String getDescription() {
        return description;
    }
    public String getHandle() {
        return handle;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setHandle(String handle) {
        this.handle = handle;
        updateId();
    }
    public String toString(){
        return "User," + handle + ',' + description;
    }
}