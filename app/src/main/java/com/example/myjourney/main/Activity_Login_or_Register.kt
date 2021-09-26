package com.example.myjourney.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myjourney.R
import com.example.myjourney.models.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


class Activity_Login_or_Register : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var listener : FirebaseAuth.AuthStateListener
    private lateinit var providers: List<IdpConfig>

    private val AUTH_REQUEST_CODE = 1234
    private val profileref = FirebaseFirestore.getInstance().collection("profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_register)

        firebaseAuth = FirebaseAuth.getInstance()

        checkuserConnection()

    }
    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(listener)
    }

    override fun onStop() {
        firebaseAuth.removeAuthStateListener(listener)
        super.onStop()
    }

    private fun checkuserConnection() {
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, Activity_Main::class.java)
            startActivity(intent)
            finish()
        }
        else{
            loginRegister()
        }
    }

    private fun loginRegister() {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(
                packageName,
                true,  /* install if not available? */
                null /* minimum app version */
            )
            .setHandleCodeInApp(true)
            .setUrl("https://my-life-723.firebaseapp.com/")
            .build()

        providers = arrayListOf(
            EmailBuilder().enableEmailLinkSignIn().setActionCodeSettings(actionCodeSettings).build(),
            IdpConfig.PhoneBuilder().build(),
            IdpConfig.GoogleBuilder().build(),
            IdpConfig.FacebookBuilder().build(),
            IdpConfig.GitHubBuilder().build()
        )
        listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if(user != null){
                Toast.makeText(this@Activity_Login_or_Register, "Connected : "+user.uid, Toast.LENGTH_LONG).show()
            }
            else{
                startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), AUTH_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == AUTH_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                val response = IdpResponse.fromResultIntent(data)


                if(response != null && response.isNewUser){
                    val user = firebaseAuth.currentUser
                    user?.let {
                        val myusername = ""+user.displayName
                        val mymail = ""+user.email
                        val myuserId = ""+user.uid
                        val myphonenumber = ""+user.phoneNumber
                        val myimage_url = "https://firebasestorage.googleapis.com/v0/b/my-life-723.appspot.com/o/default%2Fworkspace.png?alt=media&token=5af43008-6cfd-40d3-a2f3-7689b3719254"
                        val myimage_name = "ProfileImageFor__"+user.uid

                        val myuser = User(mymail, myuserId, myusername, myimage_url, myimage_name, myphonenumber)
                        uploadData(myuser)
                    }
                }
                else{
                    Toast.makeText(this, "Welcome back", Toast.LENGTH_LONG).show()
                    //dès que je m'authentifie je retour au main
                    val intent = Intent(this@Activity_Login_or_Register, Activity_Main::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun uploadData(myuser: User) = CoroutineScope(Dispatchers.IO).launch{
        try {
            profileref.document(myuser.userId).set(myuser).await()
            //vu qu'on ne peu acceder au UI dans un coroutine on use withContext
            withContext(Dispatchers.Main){
                Toast.makeText(this@Activity_Login_or_Register, "User add by success", Toast.LENGTH_LONG).show()
                //dès que je m'authentifie je retour au main
                val intent = Intent(this@Activity_Login_or_Register, Activity_Main::class.java)
                startActivity(intent)
                finish()
            }
        }
        catch (e: Exception){
            //vu qu'on ne peu acceder au UI dans un coroutine on use withContext
            withContext(Dispatchers.Main){
                Toast.makeText(this@Activity_Login_or_Register, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
