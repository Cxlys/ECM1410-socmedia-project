package socialmedia;

import socialmedia.socialmedia.*;
import socialmedia.socialmedia.excepts.*;
import socialmedia.socialmedia.interfaces.Interactable;
import socialmedia.socialmedia.interfaces.SocialMediaPlatform;

import java.io.*;
import java.util.*;

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

        // Remove all related posts after account has been removed
        posts.stream().filter(x -> x.getAuthorID() == id).forEach(x -> {
            deleteAllRelatedPosts(x, posts);
            posts.remove(x);
        });
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        if (accounts.remove(Objects.hash(handle)) == null) throw new HandleNotRecognisedException();

        // Remove all related posts after account has been removed
        posts.stream().filter(x -> x.getAuthorID() == Objects.hash(handle)).forEach(x -> {
            deleteAllRelatedPosts(x, posts);
            posts.remove(x);
        });
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

        posts.stream()
                .filter(x -> x.getAuthorID() == Objects.hash(oldHandle))
                .forEach(x -> x.setAuthorID(Objects.hash(newHandle)));
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
     * all of its Comments and Endorsements are found, at which point it removes them from said list.
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
        return null;

        /* StringBuilder builder = new StringBuilder();
        BasePost post = posts.get(id)
        postStringHierarchy(post, posts, builder);
        return builder; */
    }

    /* StringBuilder postStringHierarchy(BasePost original, List<BasePost> list, StringBuilder builder) { // Add void Predicate later
        list.stream()
                .filter(x -> x instanceof Endorsement && ((Endorsement) x).getOriginalPostID() == original.getId())
                .forEach(x -> {
                    builder.append()
                });
    }*/

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
        if (posts.size() == 0) return 0;

        Interactable currentMostEndorsed = new Post("", 0);

        for (BasePost post : posts) {
            if (post instanceof Interactable && ((Interactable) post).getEndorseCount() > currentMostEndorsed.getEndorseCount()) {
                currentMostEndorsed = (Interactable) post;
            }
        }

        return ((BasePost) currentMostEndorsed).getId();
    }

    @Override
    public int getMostEndorsedAccount() {
        if (posts.size() == 0) return 0;

        int mostEndorsedAccount = 0;
        int mostAccountEndorsements = 0;

        for (Map.Entry<Integer, User> account : accounts.entrySet()) {
            int accountEndorsements = 0;
            User user = account.getValue();
            for (BasePost post : posts) {
                if (post instanceof Interactable && post.getAuthorID() == user.getId()) {
                    accountEndorsements += ((Interactable) post).getEndorseCount();
                }

            }
            if (accountEndorsements > mostAccountEndorsements) {
                mostAccountEndorsements = accountEndorsements;
                mostEndorsedAccount = user.getId();
            }
        }

        return mostEndorsedAccount;
    }

    @Override
    public void erasePlatform() {
        // Reset the sequential nextID counter for posts
        BasePost.resetCounter();

        // Clear both post and account lists
        posts.clear();
        accounts.clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        File file = new File(filename);
        if (!file.createNewFile()) { System.out.println("File already exists."); }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (BasePost post : posts) {
                writer.write(post.toString());
                writer.newLine();
            }
            writer.newLine();
            for (Map.Entry<Integer, User> account : accounts.entrySet()) {
                writer.write(account.getValue().toString());
                writer.newLine();
            }
            writer.newLine();
            writer.write("nextId: " + BasePost.getCounter());
        }
    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub

    }
}