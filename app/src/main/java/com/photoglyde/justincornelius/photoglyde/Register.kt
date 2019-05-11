package com.photoglyde.justincornelius.photoglyde

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Networking.ConnectionUtils
import com.photoglyde.justincornelius.photoglyde.Networking.NoConnection


import kotlinx.android.synthetic.main.onboard_landing.*
import java.util.logging.Filter

class Register : AppCompatActivity() {

    val REQUEST_CODE_CHOOSE = 101
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth

   // var mSelected =  List<Uri>()

    override fun onDestroy() {
        super.onDestroy()
       // FirebaseAuth.getInstance().signOut()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            println("=====user not null $user")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboard_landing)


        firebaseAuth = FirebaseAuth.getInstance()
        configureGoogleSignIn()




       continue_email.setOnClickListener {



           val isConnected = ConnectionUtils().isConnectingToInternet(this)

           println("========= connection health ${isConnected}")

           if (isConnected) {
               val intent = Intent(this, OnBoardingActivity::class.java)
               startActivity(intent)
               finish()
           }else{
               startActivityForResult(Intent(this, NoConnection::class.java), 101)
           }

        }


        google_sign_in.setOnClickListener {
            signIn()
        }


    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

               println("=====profile info ${acct.givenName} and ${acct.familyName} and ${acct.email}")
                val intent = Intent(this, OnBoardingActivity::class.java)


                intent.putExtra("SKIP", "SKIP")
                startActivity(intent)

            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }


}
