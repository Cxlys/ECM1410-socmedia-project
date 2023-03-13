package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Comment extends Endorsement implements Interactable {

    public Comment(String message, int authorID, int originalPostID) {
        super(message, authorID, originalPostID);
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
}