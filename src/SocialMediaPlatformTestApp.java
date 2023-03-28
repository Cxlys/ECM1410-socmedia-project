package socialmedia;

import socialmedia.socialmedia.excepts.*;
import socialmedia.socialmedia.interfaces.SocialMediaPlatform;

import java.io.IOException;

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
	// Functions working:
	// createAccount
	// removeAccount
	// createPost
	// createComment
	// createEndorsement
	// deletePost
	// showIndividualPost
	// showAccount
	// getTotalOriginalPosts
	// getTotalCommentPosts
	// getTotalEndorsementPosts
	// getNumberOfAccounts
	// updateAccountDescription
	// changeAccountHandle
	// savePlatform
	// erasePlatform
	// loadPlatform
	// getMostEndorsedPost
	// getMostEndorsedAccount
	// showPostChildrenDetails

	/**
	 * Test method.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		System.out.println("The system compiled and started the execution...");

		SocialMediaPlatform platform = new SocialMedia();

		//TESTED THROUGHOUT:
		//showIndividualPost
		//showAccount
		//getTotalOriginalPosts
		//getTotalCommentPosts
		//getTotalEndorsementPosts
		//getNumberOfAccounts

		assert (platform.getNumberOfAccounts() == 0) : "Initial SocialMediaPlatform not empty as required.";
		if (platform.getNumberOfAccounts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");
		assert (platform.getTotalOriginalPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
		if (platform.getTotalOriginalPosts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");
		assert (platform.getTotalCommentPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
		if (platform.getTotalCommentPosts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");
		assert (platform.getTotalEndorsmentPosts() == 0) : "Initial SocialMediaPlatform not empty as required.";
		if (platform.getTotalEndorsmentPosts() == 0) System.out.println("Initial SocialMediaPlatform is empty, as required.");

		Integer id;
		try {
			//TEST = createAccount
			id = platform.createAccount("my_handle");
			System.out.println("\nNumber of accounts after addition: " + platform.getNumberOfAccounts());
			assert (platform.getNumberOfAccounts() == 1) : "number of accounts registered in the system does not match";

			//TEST = removeAccount
			platform.removeAccount(id);
			System.out.println("Number of accounts after removal: " + platform.getNumberOfAccounts());
			assert (platform.getNumberOfAccounts() == 0) : "number of accounts registered in the system does not match";

			platform.createAccount("my_handle");

			//TEST = createPost
			int postId = platform.createPost("my_handle", "Hello world!");
			System.out.println("\nNumber of posts after addition: " + platform.getTotalOriginalPosts());
			assert (platform.getTotalOriginalPosts() == 1) : "number of posts does not match intent";

			//TEST = commentPost
			int commentId = platform.commentPost("my_handle", postId, "Help");
			System.out.println("Number of comments after addition: " + platform.getTotalCommentPosts());
			assert (platform.getTotalCommentPosts() == 1) : "number of posts does not match intent";
			int secCommentId = platform.commentPost("my_handle", commentId, "Help pls");
			System.out.println("Number of comments after addition: " + platform.getTotalCommentPosts());
			assert (platform.getTotalCommentPosts() == 2) : "number of posts does not match intent";

			//TEST = endorsePost
			int endorseId = platform.endorsePost("my_handle", commentId);
			System.out.println("Number of endorsements after addition: " + platform.getTotalEndorsmentPosts());
			assert (platform.getTotalEndorsmentPosts() == 1) : "number of posts does not match intent";

			int origEndorseId = platform.endorsePost("my_handle", postId);
			System.out.println("Number of endorsements after addition: " + platform.getTotalEndorsmentPosts() + "\n");
			assert (platform.getTotalEndorsmentPosts() == 2) : "number of posts does not match intent";

			System.out.println(platform.showAccount("my_handle") + "\n");
			System.out.println(platform.showIndividualPost(postId));
			System.out.println(platform.showIndividualPost(commentId));
			System.out.println(platform.showIndividualPost(secCommentId));
			System.out.println(platform.showIndividualPost(endorseId) + "\n");

			//TEST = deletePost
			platform.deletePost(commentId);

			System.out.println(platform.showIndividualPost(postId) + "\n");

			System.out.println("\nNumber of posts total after removed: " + platform.getTotalOriginalPosts());
			System.out.println("Number of comments total after removed: " + platform.getTotalCommentPosts());
			System.out.println("Number of endorsements total after removed: " + platform.getTotalEndorsmentPosts());

			assert (platform.getTotalOriginalPosts() + platform.getTotalCommentPosts() + platform.getTotalEndorsmentPosts() == 0) : "number of posts does not match intent";

			//TEST = updateAccountDescription & changeAccount Handle
			Integer usrID = platform.createAccount("thisUser", "This is a description");
			int coolPostID = platform.createPost("thisUser", "The ACC NAME better be changing");
			System.out.println(platform.showAccount("thisUser") + "\n");
			System.out.println(platform.showIndividualPost(coolPostID) + "\n");
			platform.updateAccountDescription("thisUser", "This is an epic description");
			platform.changeAccountHandle("thisUser", "thisEpicUser");
			System.out.println(platform.showAccount("thisEpicUser") + "\n");
			System.out.println(platform.showIndividualPost(coolPostID) + "\n");

			//TEST = savePlatform & erasePlatform & loadPlatform
			platform.savePlatform("savedPlatform");
			System.out.println("Platform has been saved. There are currently "
					+ platform.getNumberOfAccounts() + " accounts, "
					+ platform.getTotalOriginalPosts() + " posts, "
					+ platform.getTotalCommentPosts() + " comments, and " +
					+ platform.getTotalEndorsmentPosts() + " endorsements.");
			platform.erasePlatform();
			System.out.println("Platform has been emptied. There are now "
					+ platform.getNumberOfAccounts() + " accounts, "
					+ platform.getTotalOriginalPosts() + " posts, "
					+ platform.getTotalCommentPosts() + " comments, and " +
					+ platform.getTotalEndorsmentPosts() + " endorsements.");
			platform.loadPlatform("savedPlatform");
			System.out.println("Platform has been loaded from file. There are now "
					+ platform.getNumberOfAccounts() + " accounts, "
					+ platform.getTotalOriginalPosts() + " posts, "
					+ platform.getTotalCommentPosts() + " comments, and " +
					+ platform.getTotalEndorsmentPosts() + " endorsements." + "\n");

			//TEST = getMostEndorsedAccount & getMostEndorsedPost
			platform.erasePlatform();
			Integer user1 = platform.createAccount("user1");
			Integer user2 = platform.createAccount("user2");
			Integer user3 = platform.createAccount("user3");
			int post1 = platform.createPost("user1", "post1");
			int post2 = platform.createPost("user2", "post2");
			int post3 = platform.createPost("user3", "post3");
			int end11 = platform.endorsePost("user1", post1);
			int end12 = platform.endorsePost("user1", post2);
			int end13 = platform.endorsePost("user1", post3);
			int end21 = platform.endorsePost("user2", post1);
			int end22 = platform.endorsePost("user2", post2);
			int end31 = platform.endorsePost("user3", post1);

			System.out.println("Most endorsed post:");
			System.out.println(platform.showIndividualPost(platform.getMostEndorsedPost()) + "\n");

			System.out.println("ID of most endorsed account: " + platform.getMostEndorsedAccount());
			System.out.println("This should match user1 which is below:");
			System.out.println(platform.showAccount("user1"));
			System.out.println("\n\n");

			int pst = platform.createPost("user1", "I like examples");
			int com1 = platform.commentPost("user1", pst, "No more than me");
			int com2 = platform.commentPost("user1", com1, "I can prove!");
			platform.commentPost("user1", com2, "Prove it");
			platform.commentPost("user1", pst, "Cant you do better");
			int com3 = platform.commentPost("user1", pst, "Where are the examples");
			platform.commentPost("user1", com3, "They are here");

			// test from root post
			StringBuilder builder = platform.showPostChildrenDetails(pst);
			System.out.println(builder.toString());

			// test from comment
			StringBuilder builder2 = platform.showPostChildrenDetails(com3);
			System.out.println(builder2.toString());

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
		} catch (IOException e) {
			assert(false) : "IOException thrown";
			System.out.println("Error 7");
		} catch (ClassNotFoundException e) {
			assert(false) : "ClassNotFoundException thrown";
			System.out.println("Error 8");
		}
	}
}