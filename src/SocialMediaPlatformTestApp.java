package socialmedia;

import socialmedia.socialmedia.excepts.*;
import socialmedia.socialmedia.interfaces.SocialMediaPlatform;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the SocialMediaPlatform interface -- note you will
 * want to increase these checks, and run it on your SocialMedia class (not the
 * BadSocialMedia class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class SocialMediaPlatformTestApp {
	// Check List:
	// createAccount
	// removeAccount
	// createPost
	// createComment
	// createEndorsement
	// getTotalOriginalPosts
	// getTotalCommentPosts
	// getTotalEndorsementPosts
	// getNumberOfAccounts

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		SocialMediaPlatform platform = new SocialMedia();

		assert (platform.getNumberOfAccounts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		if (platform.getNumberOfAccounts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");
		assert (platform.getTotalOriginalPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		if (platform.getTotalOriginalPosts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");
		assert (platform.getTotalCommentPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		if (platform.getTotalCommentPosts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");
		assert (platform.getTotalEndorsmentPosts() == 0) : "Innitial SocialMediaPlatform not empty as required.";
		if (platform.getTotalEndorsmentPosts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");

		Integer id;
		try {
			id = platform.createAccount("my_handle");
			System.out.println("\nNumber of accounts after addition: " + platform.getNumberOfAccounts());
			assert (platform.getNumberOfAccounts() == 1) : "number of accounts registered in the system does not match";

			platform.removeAccount(id);
			System.out.println("Number of accounts after removal: " + platform.getNumberOfAccounts());
			assert (platform.getNumberOfAccounts() == 0) : "number of accounts registered in the system does not match";

			platform.createAccount("my_handle");

			int postId = platform.createPost("my_handle", "Hello world!");
			System.out.println("\nNumber of posts after addition: " + platform.getTotalOriginalPosts());
			assert (platform.getTotalOriginalPosts() == 1) : "number of posts does not match intent";

			int commentId = platform.commentPost("my_handle", postId, "Help");
			int secCommentId = platform.commentPost("my_handle", commentId, "Help pls");
			System.out.println("Number of comments after addition: " + platform.getTotalCommentPosts());
			assert (platform.getTotalCommentPosts() == 2) : "number of posts does not match intent";

			int endorseId = platform.endorsePost("my_handle", commentId);
			System.out.println("Number of endorsements after addition: " + platform.getTotalEndorsmentPosts());
			assert (platform.getTotalEndorsmentPosts() == 1) : "number of posts does not match intent";

			System.out.println("\n");
			System.out.println(platform.showIndividualPost(postId));
			platform.deletePost(commentId);

			System.out.println("\nNumber of posts total after removed: " + platform.getTotalOriginalPosts());
			System.out.println(platform.showIndividualPost(postId));
			assert (platform.getTotalOriginalPosts() + platform.getTotalCommentPosts() + platform.getTotalEndorsmentPosts() == 0) : "number of posts does not match intent";

		} catch (IllegalHandleException e) {
			assert (false) : "IllegalHandleException thrown incorrectly";
			System.out.println("Error 0");
		} catch (InvalidHandleException e) {
			assert (false) : "InvalidHandleException thrown incorrectly";
			System.out.println("Error 1");
		} catch (AccountIDNotRecognisedException e) {
			assert (false) : "AccountIDNotRecognizedException thrown incorrectly";
			System.out.println("Error 2");
		} catch (InvalidPostException e) {
			assert(false) : "InvalidPostException thrown";
			System.out.println("Error 3");
		} catch (HandleNotRecognisedException e) {
			assert(false) : "HandleNotRecognisedException thrown";
			System.out.println("Error 4");
		} catch (PostIDNotRecognisedException e) {
			assert (false) : "PostIDNotRecognisedException thrown";
			System.out.println("Error 5");
		} catch (NotActionablePostException e) {
			assert(false) : "NotActionablePostException thrown";
			System.out.println("Error 6");
		}
	}
}
