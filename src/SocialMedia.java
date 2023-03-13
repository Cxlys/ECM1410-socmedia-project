package socialmedia;

import socialmedia.socialmedia.*;
import socialmedia.socialmedia.excepts.*;
import socialmedia.socialmedia.interfaces.Interactable;
import socialmedia.socialmedia.interfaces.SocialMediaPlatform;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class SocialMedia implements SocialMediaPlatform {

    HashMap<Integer, User> accounts = new HashMap<>();
    ArrayList<BasePost> posts = new ArrayList<>();

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        // Error handling
        if (accounts.containsKey(Objects.hash(handle)))
            throw new IllegalHandleException();
        if (handle.length() == 0 || handle.length() > 30 || handle.contains(" "))
            throw new InvalidHandleException();

        // Initialise
        User usr = new User(handle);
        accounts.put(Objects.hash(handle), usr);
        return Objects.hash(handle);
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        // Error handling
        if (accounts.containsKey(Objects.hash(handle)))
            throw new IllegalHandleException();
        if (handle.length() == 0 || handle.length() > 30 || handle.contains(" "))
            throw new InvalidHandleException();

        // Initialise
        User usr = new User(handle, description);
        accounts.put(Objects.hash(handle), usr);
        return Objects.hash(handle);
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        if (accounts.remove(id) == null) throw new AccountIDNotRecognisedException();
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (accounts.remove(Objects.hash(handle)) == null) throw new HandleNotRecognisedException();
    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {

        User user = accounts.get(Objects.hash(oldHandle));

        // Error handling
        if (Objects.equals(user, null))
            throw new HandleNotRecognisedException();
        if (accounts.containsKey(Objects.hash(newHandle)))
            throw new IllegalHandleException();
        if (newHandle.length() == 0 || newHandle.length() > 30 || newHandle.contains(" "))
            throw new InvalidHandleException();

        accounts.remove(Objects.hash(oldHandle), user);
        user.setHandle(newHandle);
        accounts.put(Objects.hash(newHandle), user);
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        User user = accounts.get(Objects.hash(handle));

        if (Objects.equals(user, null)) throw new HandleNotRecognisedException();
        user.setDescription(description);
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {   // Not complete
        User user = accounts.get(Objects.hash(handle));

        // Error handling
        if (Objects.equals(user, null))
            throw new HandleNotRecognisedException();

        List<BasePost> userPosts = posts.stream().filter(x -> x.getAuthorID() == user.getId()).toList();

        return "ID: " + user.getId() + "\n" +
                "Handle: " + user.getHandle() + "\n" +
                "Description: " + user.getDescription() + "\n" +
                "Post Count: " + userPosts.size() + "\n" +
                "Endorse Count: *placeholder* \n";
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        // Error handling
        if (!accounts.containsKey(Objects.hash(handle)))
            throw new HandleNotRecognisedException();
        if (message.length() > 100 || message.length() == 0)
            throw new InvalidPostException();

        Post post = new Post(message, Objects.hash(handle));
        posts.add(post);
        return post.getId();
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        BasePost post = posts.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElse(null);

        // Error handling
        if (!accounts.containsKey(Objects.hash(handle)))
            throw new HandleNotRecognisedException();
        if (Objects.equals(post, null)) {
            throw new PostIDNotRecognisedException();
        }
        if (post instanceof Endorsement) {
            throw new NotActionablePostException();
        }

        Endorsement endorsement = new Endorsement(post.getMessage(), Objects.hash(handle), post.getId());
        posts.add(endorsement);
        return endorsement.getId();
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {

        BasePost post = posts.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElse(null);

        // Error handling
        if (!accounts.containsKey(Objects.hash(handle)))
            throw new HandleNotRecognisedException();
        if (Objects.equals(post, null)) {
            throw new PostIDNotRecognisedException();
        }
        if (post instanceof Endorsement) {
            throw new NotActionablePostException();
        }
        if (message.length() > 100 || message.length() == 0)
            throw new InvalidPostException();

        Comment comment = new Comment(message, Objects.hash(handle), id);
        posts.add(comment);
        return comment.getId();
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        BasePost post = posts.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElse(null);

        if (Objects.equals(post, null)) throw new PostIDNotRecognisedException();

        // From post:
        // Find all comments and endorsements linked to post
        // Find all comments and endorsemnets linked to comment/endorsement
        // Repeat until clean

        deleteAllRelatedPosts(post, posts);
    }

    /**
     * The method recursively takes a post from a given list, and iterates over the list in a depth-first manner until
     * all of its Comments and Endorsements are found, at which point it removes them.
     * @param original Post for which all of its children should be found.
     * @param list List to be iterated over.
     */
    void deleteAllRelatedPosts(BasePost original, List<BasePost> list) { // Add void Predicate later
        list.stream()
                .filter(x -> x instanceof Endorsement && ((Endorsement) x).getOriginalPostID() == original.getId())
                .forEach(x -> {
                    if (x instanceof Comment) deleteAllRelatedPosts(x, list);
                    posts.remove(x);
                });
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        // Error handling
        BasePost post = posts.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElse(null);

        if (Objects.equals(post, null)) throw new PostIDNotRecognisedException();

        return "ID: " + post.getId() + "\n" +
                "Account: " + accounts.get(post.getAuthorID()).getHandle() + "\n" + ((post instanceof Interactable) ?
                ("No. endorsements: " + ((Interactable) post).getEndorseCount() + " | No. comments: " + ((Interactable) post).getCommentCount()) : "");
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumberOfAccounts() {
        // TODO Auto-generated method stub
        return accounts.size();
    }

    @Override
    public int getTotalOriginalPosts() {
        // TODO Auto-generated method stub
        return (int) posts.stream()
                .filter(x -> x instanceof Post)
                .count();
    }

    @Override
    public int getTotalEndorsmentPosts() {
        // TODO Auto-generated method stub
        return (int) posts.stream()
                .filter(x -> !(x instanceof Interactable))
                .count();
    }

    @Override
    public int getTotalCommentPosts() {
        // TODO Auto-generated method stub
        return (int) posts.stream()
                .filter(x -> x instanceof Comment)
                .count();
    }

    @Override
    public int getMostEndorsedPost() {
        return 0;
    }

    @Override
    public int getMostEndorsedAccount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void erasePlatform() {
        // TODO Auto-generated method stub

    }

    @Override
    public void savePlatform(String filename) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub

    }
}