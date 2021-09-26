package com.example.myjourney.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myjourney.R
import com.example.myjourney.models.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.EmailBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class Activity_Splash : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 2000// x*1000 = x*1 sec

    private val AUTH_REQUEST_CODE = 1234
    private lateinit var firebaseAuth : FirebaseAuth
    private val profileref = FirebaseFirestore.getInstance().collection("profile")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()
        //set full screen
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN )

        //au cas où on entre dans l'appli avec le lien d'identification,
        if (AuthUI.canHandleIntent(intent)) {
            val link = intent.data.toString()
            val providers = Arrays.asList(
                EmailBuilder().build()
            )

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(providers)
                    .build(),
                AUTH_REQUEST_CODE
            )
        }

        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this@Activity_Splash, Activity_Main::class.java))
            finish()
        }, SPLASH_TIME_OUT)
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
                        //Log.d("+++linked", ""+myuser)
                    }
                }
                else{
                    Toast.makeText(this, "Welcome back", Toast.LENGTH_LONG).show()
                    //dès que je m'authentifie je retour au main
                    val intent = Intent(this, Activity_Main::class.java)
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
                Toast.makeText(this@Activity_Splash, "User add by success", Toast.LENGTH_LONG).show()
                //dès que je m'authentifie je retour au main
                val intent = Intent(this@Activity_Splash, Activity_Main::class.java)
                startActivity(intent)
                finish()
            }
        }
        catch (e: Exception){
            //vu qu'on ne peu acceder au UI dans un coroutine on use withContext
            withContext(Dispatchers.Main){
                Toast.makeText(this@Activity_Splash, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}