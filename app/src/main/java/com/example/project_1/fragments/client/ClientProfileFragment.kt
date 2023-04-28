package com.example.project_1.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.project_1.R
import com.example.project_1.activities.MainActivity
import com.example.project_1.activities.SelectRolesActivity
import com.example.project_1.activities.client.update.ClientUpdateActivity
import com.example.project_1.models.User
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView

class ClientProfileFragment : Fragment() {

    private val TAG = "CLIENT_PROFILE_FRAGMENT"

    private var myView : View? = null
    private var buttonSelectRol : Button ? = null
    private var circleImageView : CircleImageView ? = null
    private lateinit var sharedPref: SharedPref
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = inflater.inflate(R.layout.fragment_client_profile, container, false)
        sharedPref = SharedPref(requireActivity())
        this.getUserSession()
        buttonSelectRol = myView?.findViewById(R.id.button_select_rol)
        circleImageView = myView?.findViewById(R.id.circleImageView)
        val textName  = myView?.findViewById<TextView>(R.id.textViewName)
        val textEmail  = myView?.findViewById<TextView>(R.id.textViewEmail)
        val textPhone  = myView?.findViewById<TextView>(R.id.textViewPhone)
        myView?.findViewById<ImageView>(R.id.imageLogout)?.setOnClickListener {logout()}
        myView?.findViewById<Button>(R.id.button_update_profile)?.setOnClickListener {goToUpdate()}
        if (!user.image.isNullOrBlank()) {
            Glide.with(requireContext()).load(user.image).into(circleImageView!!)
        }
        val completeName ="${user.name} ${user.lastname}"
        textName?.text = completeName
        textEmail?.text = user.email
        textPhone?.text = user.phone
        buttonSelectRol?.setOnClickListener {goToSelectRol()}
        return myView
    }

    private fun getUserSession() {
        Log.i(TAG, "getUserSession: ")
        val gson = Gson()
        if (!this.sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }

    private fun logout() {
        this.sharedPref.remove("user")
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToUpdate() {
        val intent = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(intent)
    }

    private fun goToSelectRol() {
        Log.i(TAG, "goToSelectRol: ")
        val intent = Intent(requireContext(), SelectRolesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}