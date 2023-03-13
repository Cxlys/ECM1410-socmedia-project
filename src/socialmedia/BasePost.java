package socialmedia.socialmedia;

public abstract class BasePost {

    static int nextID = 0;
    
    protected int id;
    protected String message;
    protected int authorID;

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public int getAuthorID() {
        return authorID;
    }
}
