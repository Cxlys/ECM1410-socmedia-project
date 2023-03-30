package socialmedia.socialmedia;

/**
 * BasePost is an <i>abstract class</i> used to construct the classes of <strong>Post</strong>, <strong>Comment</strong> and
 * <strong>Endorsement</strong>.
 * <p>
 * Its existence serves to allow for all components to be stored in one homogenous list, while also allowing Endorsement
 * to inherit Post's methods without inheriting the Interactable interface.
 *
 * @author Daniel Casley
 * @version 1.0
 */
public abstract class BasePost {

    static int nextID = 0;

    /**
     * The method resets the nextID counter stored in the BasePost abstract class. This means that the next BasePost of
     * any type will have the ID of 0.
     */
    public static void resetCounter() {
        nextID = 0;
    }

    /**
     * The method returns the nextID counter stored in the BasePost abstract class.
     * @return The nextID counter
     */
    public static int getCounter() {
        return nextID;
    }

    /**
     * The method sets the nextID counter to a specified number.
     * @param counter Number to set counter to
     */
    public static void setCounter(int counter){
        nextID = counter;
    }
    
    protected int id;
    protected String message;
    protected int authorID;

    /**
     * The method returns the message stored in the post.
     * @return Message stored.
     */
    public String getMessage() {
        return message;
    }

    /**
     * The method returns the ID of the post.
     * @return ID stored.
     */
    public int getId() {
        return id;
    }

    /**
     * The method returns the ID of the user that created the post.
     * @return Author ID stored.
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * The method sets the authorID to a new number.
     * @param newID Number to set authorID to.
     */
    public void setAuthorID(int newID) { authorID = newID; }

    @Override
    public String toString() {
        return "";
    }
}
