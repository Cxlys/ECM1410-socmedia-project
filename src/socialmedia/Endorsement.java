package socialmedia.socialmedia;

public class Endorsement extends Post {
    int originalPostID;

    public Endorsement(String message, int authorID, int originalPostID) {
        super(message, authorID);
        this.originalPostID = originalPostID;
    }

    // Horrible solution, needs to be fixed
    @Override
    public int getCommentCount() {
        return 0;
    }

    @Override
    public int getEndorseCount() {
        return 0;
    }

    @Override
    public void incrementCommentCount() {}
    @Override
    public void incrementEndorseCount() {}
}