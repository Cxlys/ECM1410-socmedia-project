package socialmedia;

import socialmedia.socialmedia.*;
import socialmedia.socialmedia.excepts.*;
import socialmedia.socialmedia.interfaces.SocialMediaPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SocialMedia implements SocialMediaPlatform {
    ArrayList<User> accounts = new ArrayList<>();
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        // Error handling
        for (User item: accounts){
            if (Objects.equals(item.getHandle(), handle)){
                throw new IllegalHandleException();
            }
        }
        if (handle.length() == 0 || handle.length() > 30 || handle.contains(" ")){
            throw new InvalidHandleException();
        }
        // Initialise
        User usr = new User(handle);
        accounts.add(usr);
        return usr.getId();
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        // Error handling
        for (User item: accounts){
            if (Objects.equals(item.getHandle(), handle)){
                throw new IllegalHandleException();
            }
        }
        if (handle.length() == 0 || handle.length() > 30 || handle.contains(" ")){
            throw new InvalidHandleException();
        }
        // Initialise
        User usr = new User(handle, description);
        accounts.add(usr);
        return usr.getId();
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        User user = null;
        for (User item: accounts)
            if (Objects.equals(item.getHandle(), handle)) user = item;

        if (Objects.equals(user, null)) throw new HandleNotRecognisedException();
        user.setDescription(description);
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        // TODO Auto-generated method stub

    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        // TODO Auto-generated method stub
        return null;
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
        return 0;
    }

    @Override
    public int getTotalOriginalPosts() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalCommentPosts() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMostEndorsedPost() {
        // TODO Auto-generated method stub
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