package com.orlove101.android.gomassage.fragments

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.orlove101.android.gomassage.R
import com.orlove101.android.gomassage.model.User
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*

class RegistrationFragment: Fragment() {
    private var usernameEditText: EditText? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var registerButton: Button? = null
    private var alreadyHaveAccountButton: TextView? = null
    private var selectPhotoButton: Button? = null
    private var selectPhotoImageView: CircleImageView? = null

    private var selectedPhotoUri: Uri? = null

    private val getContent: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
            if ( imageUri != null ) {
                selectedPhotoUri = imageUri
                Picasso.get().load(imageUri).into(selectPhotoImageView)
                selectPhotoButton?.alpha = 0f
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameEditText = view.findViewById(R.id.registration_edit_text_username)
        emailEditText = view.findViewById(R.id.edit_text_text_email)
        passwordEditText = view.findViewById(R.id.registration_edit_text_password)
        registerButton = view.findViewById(R.id.register_button)
        alreadyHaveAccountButton = view.findViewById(R.id.already_have_account_text_view)
        selectPhotoButton = view.findViewById(R.id.select_photo_registration_button)
        selectPhotoImageView = view.findViewById(R.id.select_photo_image_view_register)

        registerButton?.setOnClickListener {
            performRegister()
        }

        alreadyHaveAccountButton?.setOnClickListener {
            (activity as? OnAlreadyHaveAccountClick)?.onAlreadyHaveAccountClick()
        }

        selectPhotoButton?.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun performRegister(): Unit {
        val username = usernameEditText?.text.toString()
        val email = emailEditText?.text.toString()
        val password = passwordEditText?.text.toString()

        hideKeyBoard()
        if ( !isValidRegistrationData(email, password, username) ) {
            Snackbar.make(view as View, R.string.invalid_input_values, Snackbar.LENGTH_LONG)
                .setAction("x") {}
                .show()
            return
        }
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG, "User registered with uid: ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if ( selectedPhotoUri == null ) return

        val filename = UUID.randomUUID().toString()
//        val ref = Firebase.storage.getReference("/images/$filename") the same as below
        val ref = Firebase.storage.reference.child("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
                    .addOnFailureListener {
                        Log.d(TAG, "Fail to download profile image url")
                    }
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = Firebase.auth.uid ?: ""
        val username = usernameEditText?.text.toString()
        val ref = Firebase.database.reference.child("/users/$uid")

        val user = User(uid, username, profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "User uploaded to database successfully")
            }
            .addOnFailureListener {
                Log.d(TAG, "User uploading to database failed")
            }
    }

    private fun isValidRegistrationData(email: String, password: String, username: String):
            Boolean {
        return email.isNotEmpty()
                && username.isNotEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.count() >= 6
    }

    private fun hideKeyBoard(): Unit {
        val inputMethodManager = (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE)) as?
                InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    interface OnAlreadyHaveAccountClick {
        fun onAlreadyHaveAccountClick()
    }

    companion object {
        private const val TAG = "RegistrationFragment"

        fun newInstance(): RegistrationFragment {
            val fragment = RegistrationFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}