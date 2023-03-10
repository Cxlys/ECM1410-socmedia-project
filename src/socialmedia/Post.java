package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Post implements Interactable {
    static int nextID = 0;

    public Post(String message, int authorID) {
        id = nextID++;
        this.message = message;
        this.authorID = authorID;
    }

    public String getMessage() {
        return message;
    }

    public int commentCount = 0;
    public int endorseCount = 0;

    protected int id;
    protected String message;
    protected int authorID;

    public int getId() {
        return id;
    }

    public int getAuthorID() {
        return authorID;
    }

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