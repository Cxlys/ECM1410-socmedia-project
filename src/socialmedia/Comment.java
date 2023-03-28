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

    /**
     * The method returns the endorsement count of the post.
     * @return The comment count of the post.
     */
    @Override
    public int getCommentCount() {
        return commentCount;
    }

    /**
     * The method returns the endorsement count of the post.
     * @return The endorsement count of the post.
     */
    @Override
    public int getEndorseCount() {
        return endorseCount;
    }

    /**
     * The method increments the comment count by one. Used when a post is created.
     */
    @Override
    public void incrementCommentCount() {
        commentCount++;
    }

    /**
     * The method increments the endorsement count by one. Used when a post is created.
     */
    @Override
    public void incrementEndorseCount() {
        endorseCount++;
    }

    /**
     * The method sets the comment and endorse counts to two specified numbers.
     * @param commentCount Number to set comment counter to
     * @param endorseCount Number to set endorse counter to
     */
    @Override
    public void setCounts(int commentCount, int endorseCount) {
        this.commentCount = commentCount;
        this.endorseCount = endorseCount;
    }

    /**
     * The method returns the contents of the class as a string value. Used in this format for saving and loading the
     * platform.
     * @return String containing object's contents in the format "Class,id,message,authorID,originalPostId,commentCount,endorseCount".
     */
    @Override
    public String toString() {
        return "Comment," + id + "," + message + ',' + authorID + "," + originalPostId + "," + commentCount + "," + endorseCount;
    }
}