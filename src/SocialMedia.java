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
        if (post instanceof Endorsement && !(post instanceof Comment)) {
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
        BasePost post = posts.stream()
                .filter(x -> Objects.equals(x.getId(), id))
                .findFirst()
                .orElse(null);

        if (Objects.equals(post, null)) throw new PostIDNotRecognisedException();

        // From post:
        // Find all comments and endorsements linked to post
        // Find all comments and endorsements linked to comment/endorsement
        // Repeat until clean

        int result = deleteAllRelatedPosts(post, posts);

        // Cascade up and decrement comment/endorse counts by the amount of posts that were removed by the function
        // All parents should be either Comments or Posts, so we cast to Interactable
        Interactable pointer = null;
        if (post instanceof Comment && result != 0) {
            pointer = (Interactable) findPostById(((Comment) post).getOriginalPostID());

            while (pointer != null) {
                System.out.println("Removing: " + result + " from " + ((BasePost) pointer).getId() + "\n");

                pointer.setCounts(pointer.getCommentCount() - result, pointer.getEndorseCount());
                pointer = (pointer instanceof Comment) ? (Interactable) findPostById(((Comment) pointer).getOriginalPostID()) : null;
            }
        }
    }

    /**
     * The method uses a depth-first iterative method to erase an element and all elements related to it by using a stack.
     * Not ideal, Big O of O(n + (n^2)^m), however it works for now
     * @param original Post for which all of its children should be found.
     * @param list List to be iterated over.
     * @return The number of comments removed.
     */
    int deleteAllRelatedPosts(BasePost original, List<BasePost> list) { // Add void Predicate later
        if (original instanceof Endorsement && !(original instanceof Comment)) {
            list.remove(original);
            return 0;
        }

        Stack<List<Endorsement>> levels = new Stack<>();

        int deletedComments = (original instanceof Comment) ? 1 : 0;

        List<Endorsement> start = list.stream()
                .filter(x -> x instanceof Endorsement)
                .map(x -> (Endorsement) x)
                .filter(x -> ((Endorsement) x).getOriginalPostID() == original.getId())
                .toList();

        levels.push(start);
        list.remove(original);

        while (!levels.isEmpty()) {
            var level = levels.pop();

            for (Endorsement post : level) {
                var postsRelated = list.stream()
                        .filter(x -> x instanceof Endorsement)
                        .map(x -> (Endorsement) x)
                        .filter(x -> x.getId() == post.getOriginalPostID())
                        .toList();

                levels.push(postsRelated);

                if (post instanceof Comment) deletedComments++;
                list.remove(post);
            }
        }

        return deletedComments;
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
                ("No. endorsements: " + ((Interactable) post).getEndorseCount() + " | No. comments: " + ((Interactable) post).getCommentCount())
                        + "\n" + post.getMessage() : "") ;
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
                    builder.append(showIndividualPost(reply));
                });if (post == null) throw new PostIDNotRecognisedException();
        if (post instanceof Enmment && x.getOriginalPostID() == post.getId())
                   .map(x -> (Comment) x)
                   .filter(x -> x.getOriginalPostID() == post.getId());
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
            for (String line :  reader.lines().toList()) {
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