package socialmedia.socialmedia;

public abstract class BasePost {

    static int nextID = 0;

    public String getMessage() {
        return message;
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
