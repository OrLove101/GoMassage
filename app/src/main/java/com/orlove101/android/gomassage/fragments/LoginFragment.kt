package com.orlove101.android.gomassage.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orlove101.android.gomassage.R

class LoginFragment: Fragment() {
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var backToRegistrationEditText: TextView? = null
    private var loginButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEditText = view.findViewById(R.id.login_edit_text_email)
        passwordEditText = view.findViewById(R.id.login_edit_text_password)
        backToRegistrationEditText = view.findViewById(R.id.back_to_registration_text_view)
        loginButton = view.findViewById(R.id.login_button)

        loginButton?.setOnClickListener {
            performLogin()
        }

        backToRegistrationEditText?.setOnClickListener {
            (activity as? OnBackToRegistrationClick)?.onBackToRegistrationClick()
        }
    }

    interface OnBackToRegistrationClick {
        fun onBackToRegistrationClick()
    }

    private fun performLogin() {
        val email = emailEditText?.text.toString()
        val password = passwordEditText?.text.toString()

        hideKeyBoard()
        if ( !isValidLoginData(email, password) ) {
            Snackbar.make(view as View, R.string.invalid_input_values, Snackbar.LENGTH_LONG)
                .setAction("x") {}
                .show()
            return
        }
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if ( !it.isSuccessful ) return@addOnCompleteListener
                Log.d(TAG, "User login with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to login: ${it.message}")
                Snackbar.make(view as View, R.string.failed_login, Snackbar.LENGTH_LONG)
                    .setAction("x") {}
                    .show()
            }
    }

    private fun isValidLoginData(email: String, password: String): Boolean {
        return email.isNotEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && password.count() >= 6
    }

    private fun hideKeyBoard(): Unit {
        val inputMethodManager = (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE)) as?
                InputMethodManager

        inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    companion object {
        private const val TAG = "LoginFragment"

        fun newInstance(): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}