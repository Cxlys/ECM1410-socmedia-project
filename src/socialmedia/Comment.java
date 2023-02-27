package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Interactable;

public class Comment extends Post implements Interactable {
    int originalPostID;
    int commentCount;
    int endorseCount;
}