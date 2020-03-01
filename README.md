# CatchUp

CatchUp is an Android Reddit reader app that allows the user to indicate a set of subreddits 
that they would like to follow and store. 
The app presents the user with one post at a time from one of their selected subreddits. 
They can interact with the post or they can swipe to see another one. 

## Get it from the Play Store
Play Store link: https://play.google.com/store/apps/details?id=com.udacity.catchup

## Generate APK

Run 
```./gradlew installRelease``` 
from the root directory. Gradle will take care off everything ;) 

## Key features

* Fetches and stores a set of subreddits
* Shows a post from one subreddit at a time
* The user can swipe horizontally to see other Reddit posts
* Provides the possibility to interact with a post (i.e. view image posts, play video posts)
* The user can share the posts
* Dark mode

## User interface

CatchUp has three views: 
* The Subreddits View can add and remove subreddits
* The Pager View, where the user can swipe right or left to get to the next or previous Reddit post
* The Details View shows the selected post in full screen, it also shows the comments

### Subreddits View

The Subreddits View shows the list of subreddits that are already added. 
This view also lets the user remove subreddits or add new ones.

### Pager View

The Pager View is the main view of the app. 
It shows one Reddit post at a time from a random subreddit that the user follows. 
The post appears on a card and it can have texts, links, images and videos.
The user can swipe right to see another new post, 
or they can swipe left to read again a post that they've already seen. 
When the user selects a post by tapping on it, the app takes them to the Details View.

### Details View

The Details View shows the selected Reddit post in full screen. 
This view also supports media content like images and videos.
The whole view is vertically scrollable so the user can comfortably read even longer posts.
This view also shows the comments related to this post.
