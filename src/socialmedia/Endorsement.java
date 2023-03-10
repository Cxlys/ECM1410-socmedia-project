package socialmedia.socialmedia;

public class Endorsement extends Post {
    int originalPostID;

    public Endorsement(String message, int authorID) {
        super(message, authorID);
    }
}