package socialmedia.socialmedia;

public abstract class BasePost {

    static int nextID = 0;
    
    public static void resetCounter() {
        nextID = 0;
    }

    public static int getCounter() {
        return nextID;
    }
    
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
    public void setAuthorID(int newID) { authorID = newID; }

    @Override
    public String toString() {
        return "";
    }
}
