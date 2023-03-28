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

        // Replacing user in the database to accommodate for the new handle (since our hash has been changed).
        accounts.remove(Objects.hash(oldHandle), user);
        user.setHandle(newHandle);
        accounts.put(Objects.hash(newHandle), user);

        // Replace all authorIDs with the new handle's ID.
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
        int endorsementCount = 0;

        for (BasePost post : userPosts)
            if (post instanceof Interactable) endorsementCount += ((Interactable) post).getEndorseCount();

        return "ID: " + user.getId() + "\n" +
                "Handle: " + user.getHandle() + "\n" +
                "Description: " + user.getDescription() + "\n" +
                "Post Count: " + userPosts.size() + "\n" +
                "Endorse Count: " + endorsementCount;
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

    /**
     * The method searches through our database and finds the first post with the given postID.
     * @param id ID to search for
     * @return The post related to that ID, or null if a post was not found.
     */
    BasePost findPostById(int id) {
        return posts.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {

        BasePost post = findPostById(id);

        // Error handling
        if (!accounts.containsKey(Objects.hash(handle)))
            throw new HandleNotRecognisedException();
        if (Objects.equals(post, null)) {
            throw new PostIDNotRecognisedException();
        }
        if (!(post instanceof Interactable)) {
            throw new NotActionablePostException();
        }

        Endorsement endorsement = new Endorsement(post.getMessage(), Objects.hash(handle), post.getId());
        posts.add(endorsement);

        // Not necessary to increment all endorse counts, so simply increment the original post by one.
        ((Interactable) post).incrementEndorseCount();

        return endorsement.getId();
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {

        BasePost post = findPostById(id);

        // Error handling
        if (!accounts.containsKey(Objects.hash(handle)))
            throw new HandleNotRecognisedException();
        if (Objects.equals(post, null)) {
            throw new PostIDNotRecognisedException();
        }
        if (post instanceof Endorsement && !(post instanceof Comment)) {
            throw new NotActionablePostException();
        }
        if (message.length() > 100 || message.length() == 0)
            throw new InvalidPostException();

        Comment comment = new Comment(message, Objects.hash(handle), id);
        posts.add(comment);

        Interactable pointer = (Interactable) findPostById(comment.getOriginalPostID());

        // Cascade up and increment all parents' comment counts
        while (pointer != null) {
            pointer.incrementCommentCount();
            pointer = (pointer instanceof Comment) ? (Interactable) findPostById(((Comment) pointer).getOriginalPostID()) : null;
        }

        return comment.getId();
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        BasePost post = findPostById(id);

        if (Objects.equals(post, null)) throw new PostIDNotRecognisedException();

        int removedCommentCount = deleteAllRelatedPosts(post, posts);

        // Cascade up and decrement comment/endorse counts by the amount of posts that were removed by the function
        // All parents should be either Comments or Posts, so we cast to Interactable
        Interactable pointer;
        if (post instanceof Comment && removedCommentCount != 0) {
            pointer = (Interactable) findPostById(((Comment) post).getOriginalPostID());

            while (pointer != null) {
                System.out.println("Removing: " + removedCommentCount + " from " + ((BasePost) pointer).getId() + "\n");

                pointer.setCounts(pointer.getCommentCount() - removedCommentCount, pointer.getEndorseCount());
                pointer = (pointer instanceof Comment) ? (Interactable) findPostById(((Comment) pointer).getOriginalPostID()) : null;
            }
        }
    }

    /**
     * The method uses a depth-first iterative method to erase an element and all elements related to it by using a stack.
     * Not ideal, Big O of O(n + (n^2)^m), however it works for now
     * @param original Post for which all of its children should be found.
     * @param list List to be iterated over.
     * @return The number of comments removed, for use in correcting commentCount values.
     */
    int deleteAllRelatedPosts(BasePost original, List<BasePost> list) { // Add void Predicate later
        if (!(original instanceof Interactable)) { // If original is an Endorsement
            list.remove(original);
            return 0;
        }

        // Create stack to store "tree" levels
        Stack<List<Endorsement>> levels = new Stack<>();

        // Prepare container to store number of deleted comments
        int deletedComments = (original instanceof Comment) ? 1 : 0;

        // Get all children of the original Post
        List<Endorsement> start = list.stream()
                .filter(x -> x instanceof Endorsement)
                .map(x -> (Endorsement) x)
                .filter(x -> ((Endorsement) x).getOriginalPostID() == original.getId())
                .toList();

        // Push to stack, and remove it from posts.
        levels.push(start);
        list.remove(original);

        // Run until the stack is empty (or, until we can no longer find children of any nodes in level).
        while (!levels.isEmpty()) {
            var level = levels.pop();

            // For every child in level...
            for (Endorsement post : level) {
                // Get all children of the post
                var postsRelated = list.stream()
                        .filter(x -> x instanceof Endorsement)
                        .map(x -> (Endorsement) x)
                        .filter(x -> x.getId() == post.getOriginalPostID())
                        .toList();

                // Push all found children to the stack
                levels.push(postsRelated);

                // Remove all posts discovered from the stack and continue to the next level
                if (post instanceof Comment) deletedComments++;
                list.remove(post);
            }
        }

        return deletedComments;
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        // Error handling
        BasePost post = findPostById(id);

        if (Objects.equals(post, null)) throw new PostIDNotRecognisedException();

        return "ID: " + post.getId() + "\n" +
                "Account: " + accounts.get(post.getAuthorID()).getHandle() + "\n" + ((post instanceof Interactable) ?
                ("No. endorsements: " + ((Interactable) post).getEndorseCount() + " | No. comments: " + ((Interactable) post).getCommentCount())
                        + "\n" + post.getMessage() : "") ;
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {

        BasePost post = findPostById(id);
        StringBuilder builder = new StringBuilder();

        // Error handling
        if (post == null) throw new PostIDNotRecognisedException();
        if (!(post instanceof Interactable)) throw new NotActionablePostException();

        builder.append(showIndividualPost(id)).append("\n");
        buildChildrenString(post, builder, 0);

        return builder;
    }

    /**
     * The method builds a string to fit the required format of {@link #showPostChildrenDetails(int) showPostChildrenDetails(id)}
     * by recursively iterating over the children of the target post in a <strong>breadth-first manner.</strong>
     * @param target Post in posts list to be targeted by the recursive function.
     * @param builder StringBuilder reference object to append strings to.
     * @param indent Starting indent for the printed list.
     */
    void buildChildrenString(BasePost target, StringBuilder builder, int indent) throws PostIDNotRecognisedException {
        try {
            // For every post in our list...
            for (BasePost post : posts) {
                // If the post is a comment and its originalID matches our target's ID...
                if (post instanceof Comment && ((Comment) post).getOriginalPostID() == target.getId()) {
                    String[] lines = showIndividualPost(post.getId()).split("\n");

                    // Append a string in the required format to the builder
                    String appString = (((indent == 0) ? "" : "|\n") + "| > " + lines[0] + "\n").indent(indent) +
                            ((lines[1] + "\n") +
                            (lines[2] + "\n") +
                            (lines[3])).indent(4 + indent);

                    builder.append(appString);

                    // Recursively call this function, incrementing the indent by 4 (the tab space taken by "| > ")
                    buildChildrenString(post, builder, indent + 4);
                }
            }
        } catch (PostIDNotRecognisedException e) {
            throw new PostIDNotRecognisedException();
        }
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

        // For each user in the database
        for (Map.Entry<Integer, User> account : accounts.entrySet()) {
            int accountEndorsements = 0;
            User user = account.getValue();

            // Loop through all the posts and find all posts related to this user, then increment their accountEndorsements
            // for each post found.
            for (BasePost post : posts) {
                if (post instanceof Interactable && post.getAuthorID() == user.getId()) {
                    accountEndorsements += ((Interactable) post).getEndorseCount();
                }

            }
            // If we have found a more endorsed account, replace the ID in mostAccountEndorsements with the new one.
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (BasePost post : posts) {
                writer.write(post.toString());
                writer.newLine();
            }
            for (Map.Entry<Integer, User> account : accounts.entrySet()) {
                writer.write(account.getValue().toString());
                writer.newLine();
            }
            writer.write("nextId," + BasePost.getCounter());
        } catch (IOException e) {
            throw new IOException();
        }
    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub
        // Get file
        File file = new File(filename);

        // Instantiate reader
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // For each line in the file
            for (String line : reader.lines().toList()) {
                // Split file by commas
                String[] data = line.split(",");
                switch (data[0]) {
                    case "Post" -> {
                        Post post = new Post(data[2], Integer.parseInt(data[3]), Integer.parseInt(data[1]));
                        post.setCounts(Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                        posts.add(post);
                    }

                    case "Comment" -> {
                        Comment com = new Comment(data[2], Integer.parseInt(data[3]), Integer.parseInt(data[1]));
                        com.setCounts(Integer.parseInt(data[4]), Integer.parseInt(data[5]));
                        posts.add(com);
                    }

                    case "Endorsement" -> {
                        Endorsement end = new Endorsement(data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]),  Integer.parseInt(data[1]));
                        posts.add(end);
                    }

                    case "User" -> {
                        User usr = null;
                        if (data.length >2){
                            usr = new User(data[1], data[2]);}
                        else{
                            usr = new User(data[1]);
                        }
                        accounts.put(Objects.hash(data[1]), usr);
                    }

                    case "nextId" -> BasePost.setCounter(Integer.parseInt(data[1]));
                    case "" -> System.out.println("Empty line");

                    case "Default" -> throw new ClassNotFoundException();
                }
            }
        } catch (IOException e) {
            throw new IOException();
        }
    }


}