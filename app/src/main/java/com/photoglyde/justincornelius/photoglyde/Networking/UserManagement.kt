package com.photoglyde.justincornelius.photoglyde.Networking

import android.app.Activity
import android.content.Context
import android.provider.SyncStateContract.Helpers.update
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.obf.cc
import com.photoglyde.justincornelius.photoglyde.Data.CoreUnSplash
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.USER
import com.photoglyde.justincornelius.photoglyde.Data.UserInfo

class UserManagement {

interface signUpComplete {
    fun onCallback(user:FirebaseUser?)
}

    interface PostUserProfile {
        fun onCallback(bool:Boolean)
    }



//redo sign in, call back should be on post profile
    fun createNewUser(create_user:UserInfo, password:String, context: Activity, sendResult:signUpComplete){

    var mAuth = FirebaseAuth.getInstance()

        var TAG = "login"

        println("========email and pass word ${create_user.email} and ${password}")
        mAuth.createUserWithEmailAndPassword(create_user.email!!.toString().trim(), password.toString().trim())
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    //postProfile(create_user, mAuth.currentUser!!.uid)
                    sendResult.onCallback(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                    sendResult.onCallback(null)

                }

                // ...
            }

    }


    fun postProfile(create_user: UserInfo, UID:String, postNewUserComplete:PostUserProfile){

        val ref = FirebaseDatabase.getInstance().getReference(USER).child(UID)
        ref.setValue(create_user, object : DatabaseReference.CompletionListener{
            override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {
                println("=======here are the result ${p0?.message} and $p1")
                var bool:Boolean = false

                if (p0 == null) bool = true

                postNewUserComplete.onCallback(bool)


            }
        })

    }


}
