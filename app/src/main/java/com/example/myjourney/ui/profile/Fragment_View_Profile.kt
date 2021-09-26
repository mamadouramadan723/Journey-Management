package com.example.myjourney.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.myjourney.R
import com.example.myjourney.main.Activity_Login_or_Register
import com.example.myjourney.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Fragment_View_Profile : Fragment() {

    lateinit var auth: FirebaseAuth

    private val profileref = FirebaseFirestore.getInstance().collection("profile")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserConnection()

        updateProfile_Button.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_nav_profile_to_nav_updateprofile)
        }
    }

    private fun checkUserConnection() {
        val user = auth.currentUser
        if(user == null){
            val intent = Intent(context, Activity_Login_or_Register::class.java)
            startActivity(intent)
            activity?.finish()
        }
        else{
            getUserInfos()
        }
    }

    private fun getUserInfos() = CoroutineScope(Dispatchers.IO).launch{
        val user = auth.currentUser
        try {
            user?.let {
                val myDocumentSnapshot = profileref.document(user.uid).get().await()

                val stringBuiler = StringBuilder()
                val myuser = myDocumentSnapshot.toObject<User>()
                stringBuiler.append("$myuser")

                withContext(Dispatchers.Main){
                    myuser?.let {
                        username_TextView.text = myuser.username
                        mail_TextView.text = myuser.mail
                        phonenumber_TextView.text = myuser.phonenumber
                        Picasso.get().load(myuser.image_url).into(profileImageView_info)
                    }
                }
            }
        }
        catch (e: Exception){
            //vu qu'on ne peu acceder au UI dans un coroutine on use withContext
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}