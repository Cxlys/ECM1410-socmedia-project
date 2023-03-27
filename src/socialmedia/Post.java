package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Post extends BasePost implements Interactable {
    public Post(String message, int authorID) {
        id = nextID++;
        this.message = message;
        this.authorID = authorID;
    }
    public Post(String message, int authorID, int ID) {
        this.id = ID;
        this.message = message;
        this.authorID = authorID;
    }
    int commentCount = 0;
    int endorseCount = 0;

    @Override
    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public int getEndorseCount() {
        return endorseCount;
    }

    @Override
    public void incrementCommentCount() {
        commentCount++;
    }

    @Override
    public void incrementEndorseCount() {
        endorseCount++;
    }
    @Override
    public void setCounts(int commentCount, int endorseCount) {
        this.commentCount = commentCount;
        this.endorseCount = endorseCount;
    }

    
    @Override
    public String toString() {
        return "Post," + id + "," + message + ',' + authorID + "," + commentCount + "," + endorseCount;
    }
}