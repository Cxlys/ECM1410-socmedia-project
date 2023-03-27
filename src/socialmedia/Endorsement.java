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
    
    public int getOriginalPostID() {
        return originalPostId;
    }

    @Override
    public String toString() {
        return "Endorsement," + id + "," + message + ',' + authorID + "," + originalPostId;
    }
}