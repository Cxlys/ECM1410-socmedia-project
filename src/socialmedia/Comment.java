package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

/**
 * Comment is a class that inherits the Endorsement class (the capabilities to store an <strong>original ID</strong>),
 * as well as implementing the Interactable interface, allowing it to be commented and endorsed.
 *
 * @author Daniel Casley, Benjamin Richmond
 * @version 1.0
 */
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