package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Comment extends Post {
    int originalPostId;

    public Comment(String message, int authorID, int originalPostID) {
        super(message, authorID);
        this.originalPostId = originalPostID;
    }
}