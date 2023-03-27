package socialmedia.socialmedia.interfaces;

public interface Interactable {
    int getCommentCount();
    int getEndorseCount();
    void setCounts(int comCount, int endCount);
    void incrementCommentCount();
    void incrementEndorseCount();
}