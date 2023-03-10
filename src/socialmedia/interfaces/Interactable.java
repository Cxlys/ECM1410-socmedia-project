package socialmedia.socialmedia.interfaces;

public interface Interactable {
    int getCommentCount();
    int getEndorseCount();
    void incrementCommentCount();
    void incrementEndorseCount();
}