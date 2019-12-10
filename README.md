# CatchUp

## Description

CatchUp is an Android Reddit reader app that allows the user to indicate a set of subreddits that they would like to follow and store. 
The app presents the user with a post from one of their selected subreddits. 
They can interact with the post or they can swipe to see another one. 

## Key features

* Fetches and stores a set of subreddits
* Shows a post from one subreddit at a time
* The user can scroll/swipe horizontally to see other posts
* Provides the possibility to interact with a post (i.e. view image posts, play video posts)

## User interface

CatchUp has three views: 
* The Subreddits View can add or remove subreddits
* The Pager View, where the user can swipe right or left to get to the next or previous post
* The Details View shows the selected post in full screen, it also shows the comments

### Subreddits View

The Subreddits View shows the list of subreddits that are already added. 
This view also lets the user remove subreddits or to add new ones.

### Pager View

The Pager View is the main view of the app. It shows one post at a time from a random unseen subreddit that the user follows.
The user can swipe right to see another new post, 
or they can swipe left to read again a post that they've already seen. 
When the user selects a post by tapping on it, the app takes them to the Details View.


### Details View

The Details View shows the selected post in full screen. 
The whole view is vertically scrollable so the user can comfortably read even longer posts.
This view also shows the comments related to this post.
