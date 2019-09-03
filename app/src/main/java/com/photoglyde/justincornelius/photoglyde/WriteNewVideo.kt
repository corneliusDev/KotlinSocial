package com.photoglyde.justincornelius.photoglyde

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

class WriteNewVideo {
    // Create new post at /user-posts/$userid/$postid and at
    // /posts/$postid simultaneously
    val key = FirebaseDatabase.getInstance().reference.child("posts").push().key

//    if (key == null) {
//        Log.w(TAG, "Couldn't get push key for posts")
//        return
//    }
//
//    val post = Post(userId, username, title, body)
//    val postValues = post.toMap()
//
//    val childUpdates = HashMap<String, Any>()
//    childUpdates["/posts/$key"] = postValues
//    childUpdates["/user-posts/$userId/$key"] = postValues
//
//    database.updateChildren(childUpdates)
}
