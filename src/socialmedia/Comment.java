package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Comment extends Endorsement implements Interactable {

    public Comment(String message, int authorID, int originalPostID) {
        super(message, authorID, originalPostID);
    }
    public Comment(String message, int authorID, int originalPostID, int id) {
        super(message, authorID, originalPostID);
        this.id = id;
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
        return "Comment," + id + "," + message + ',' + authorID + "," + originalPostId + "," + commentCount + "," + endorseCount;
    }
}