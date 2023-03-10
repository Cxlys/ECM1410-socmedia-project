# ECM1410-socmedia-project

Flash notes from reading specification…
	• Simple social media platform

Users can post:
	• Original messages
	• Comments
	• Endorsement posts
		○ Quote tweets

Users have:
	• Unique ID
	• String handle
	• Description

When an account is removed, all of its posts, comments and endorsements should also be removed.

Posts have:
	• Unique ID
		○ Higher the ID, the more recently it was posted
	• Message up to 100-chars.
	• Author link
	• Keep track of comments and endorsements

Endorsements and comments are children of posts.

Comments have:
	• A way to point to another post

Endorsements have…
	• A way to replicate the endorsed message 
	• A way to refer to original or comment posts
	• They cannot be endorsed or commented

When a Post is removed, all of its endorsements should also be removed.

Front-end not important, back-end correctness will be tested through the back-end interface on submission. Interface is SocialMediaTestApp?

MiniSocialMedia for non-paired individuals![image](https://user-images.githubusercontent.com/33805873/223468520-9a184feb-dceb-4c4a-9366-7d5bde695563.png)

