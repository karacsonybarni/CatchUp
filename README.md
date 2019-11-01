# CatchUp
## Description
### Problem:
The user subscribes to a large number of subreddits, finds it hard to keep up with them, and has
long stretches where some are neglected. He/she wants a way to get news/posts/threads from
all of them equally.
### Proposed Solution:
The app allows the user to indicate a set of subreddits that they would like to follow and store.
(`The app pulls content only from SFW, or safe for work, subreddits.) The app presents the user
with a post from one of their selected subreddits. They can interact with the post (i.e. view
image posts, play video posts, etc) or they can dismiss it to receive another. The app displays
comments (or a subset), thumbnails, and comment text and links. It uses intents to
communicate with the Android apps, components, and services.
## Intended User
Occasional reddit users.
## Features
### Main features:
● Stores a set of subreddits
● Shows a post from one subreddit at a time
● Provides possibility to interact with a post (i.e. view image posts, play video posts)
● Displays comments (or a subset), thumbnails, and comment text and links
● The user can scroll/swipe left or right to see other posts
## Technical details
App will be written solely in the Java Programming Language using Android Studio 3.5.1 with
Gradle 5.4.1.
The value resources (colors, strings, themes etc.) will be stored in the src/main/res/values folder
in their corresponding xml files.
### Gradle dependencies:
● android.arch.persistence.room:runtime:1.1.1
● android.arch.lifecycle:extensions:2.1.0
● androidx.lifecycle:lifecycle-common-java8:2.1.0
● androidx.appcompat:appcompat:1.1.0
● androidx.cardview:cardview:1.0.0
● androidx.recyclerview:recyclerview:1.0.0
● com.android.support.constraint:constraint-layout:1.1.3
● com.google.android.gms:play-services-ads:18.2.0
● com.google.firebase:firebase-analytics:17.2.1
● com.squareup.retrofit2:retrofit:2.6.0
● com.squareup.picasso:picasso:2.71828
● android.arch.persistence.room:compiler:1.1.1
