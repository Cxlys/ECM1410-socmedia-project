package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Comment extends Post implements Interactable {
    int originalPostID;

    public Comment(String message, int authorID, int originalPostID) {
        super(message, authorID);
        this.originalPostID = originalPostID;
    }
}