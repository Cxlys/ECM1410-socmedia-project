package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Post implements Interactable {
    static int nextID = 0;

    public Post(String message, int authorID) {
        id = nextID++;
        this.message = message;
        this.authorID = authorID;
    }

    protected int id;
    protected String message;
    protected int authorID;

    public int getId() {
        return id;
    }

    public int getAuthorID() {
        return authorID;
    }
}