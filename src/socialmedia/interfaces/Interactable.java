package socialmedia.socialmedia.interfaces;

/**
 * Interactable is an interface that sets the groundwork for the functionality to receive Comments and Endorsements.
 * <p>
 * Its primary functionality is allowing lists to be made consisting only of Interactable posts, and for aiding in
 * throwing NotActionablePost exceptions.
 *
 * @author Daniel Casley, Benjamin Richmond
 * @version 1.0
 */
public interface Interactable {
    /**
     * The method returns the endorsement count of the post.
     * @return The comment count of the post.
     */
    int getCommentCount();

    /**
     * The method returns the endorsement count of the post.
     * @return The endorsement count of the post.
     */
    int getEndorseCount();

    /**
     * The method sets the comment and endorse counts to two specified numbers.
     * @param commentCount Number to set comment counter to
     * @param endorseCount Number to set endorse counter to
     */
    void setCounts(int commentCount, int endorseCount);

    /**
     * The method increments the comment count by one. Used when a post is created.
     */
    void incrementCommentCount();

    /**
     * The method increments the endorsement count by one. Used when a post is created.
     */
    void incrementEndorseCount();
}