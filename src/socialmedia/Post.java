package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

/**
 * Post is a class that inherits BasePost and implements the interface of Interactable, allowing it to store comment
 * and endorsmeent information.
 * <p></p>
 * Post is inherited by itself to store the functionalities for an original post, as well as the methods of Interactable,
 * without allowing Endorsement to inherit the same functionality, therefore saving space and complexity.
 *
 * @author Daniel Casley, Benjamin Richmond
 * @version 1.0
 */
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

    /**
     * The method returns the contents of the class as a string value. Used in this format for saving and loading the
     * platform.
     * @return String containing object's contents in the format "Class,id,message,authorID,commentCount,endorseCount".
     */
    @Override
    public String toString() {
        return "Post," + id + "," + message + ',' + authorID + "," + commentCount + "," + endorseCount;
    }
}