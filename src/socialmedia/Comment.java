package socialmedia.socialmedia;

import socialmedia.socialmedia.interfaces.Intractable;

public class Comment extends Post implements Intractable {
    int originalPostID;
    int commentCount;
    int endorseCount;
}