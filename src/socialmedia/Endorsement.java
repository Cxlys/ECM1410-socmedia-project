package socialmedia.socialmedia;

public class Endorsement extends BasePost {
    int originalPostId;

    public Endorsement(String message, int authorID, int originalPostId) {
        this.id = nextID++;
        this.message = message;
        this.authorID = authorID;
        this.originalPostId = originalPostId;
    }
    public Endorsement(String message, int authorID, int originalPostId, int id) {
        this.id = id;
        this.message = message;
        this.authorID = authorID;
        this.originalPostId = originalPostId;
    }

    /**
     * The method returns the ID of the original post that was endorsed.
     * @return ID of the endorsed post.
     */
    public int getOriginalPostID() {
        return originalPostId;
    }

    /**
     * The method returns the contents of the class as a string value. Used in this format for saving and loading the
     * platform.
     * @return String containing object's contents in the format "Class,id,message,authorID,originalPostId".
     */
    @Override
    public String toString() {
        return "Endorsement," + id + "," + message + ',' + authorID + "," + originalPostId;
    }
}