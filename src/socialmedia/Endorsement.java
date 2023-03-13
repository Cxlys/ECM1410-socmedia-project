package socialmedia.socialmedia;

public class Endorsement extends BasePost {
    int originalPostId;

    public Endorsement(String message, int authorID, int originalPostId) {
        this.message = message;
        this.authorID = authorID;
        this.originalPostId = originalPostId;
    }
    
    public int getOriginalPostID() {
        return originalPostId;
    }
}