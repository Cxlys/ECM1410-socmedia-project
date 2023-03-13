package socialmedia.socialmedia;

public class Endorsement extends BasePost {
    int originalPostID;

    public Endorsement(String message, int authorID, int originalPostID) {
        this.message = message;
        this.authorID = authorID;
        this.originalPostID = originalPostID;
    }
}