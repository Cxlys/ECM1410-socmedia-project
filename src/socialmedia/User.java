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

    /**
     * The method returns the ID of the User object.
     * @return ID of the object.
     */
    public int getId() { return id; }

    /**
     * The method updates the ID of the object using its handle. This typically does nothing, but when the ID is updated,
     * we need to change the ID to match the hash value, and this method handles that.
     */
    void updateId() { id = Objects.hash(handle); }

    /**
     * The method returns the description of the User object.
     * @return Description of the object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The method returns the handle of the User object.
     * @return Handle of the object.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * The method returns the contents of the class as a string value. Used in this format for saving and loading the
     * platform.
     * @param description Value to set to the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The method sets the handle to a new value, then updates the ID of the class to match the Objects.hash() result.
     * @param handle The value to set to the handle.
     */
    public void setHandle(String handle) {
        this.handle = handle;
        updateId();
    }

    /**
     * The method returns the contents of the class as a string value. Used in this format for saving and loading the
     * platform.
     * @return String containing object's contents in the format "User,handle,description".
     */
    public String toString(){
        return "User," + handle + ',' + description;
    }
}