package com.orlove101.android.gomassage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.orlove101.android.gomassage.R
import com.orlove101.android.gomassage.fragments.LoginFragment
import com.orlove101.android.gomassage.fragments.RegistrationFragment

class GoMassageActivity : AppCompatActivity(), RegistrationFragment.OnAlreadyHaveAccountClick,
    LoginFragment.OnBackToRegistrationClick {
    private var registrationFragment: RegistrationFragment? = null
    private var loginFragment: LoginFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_massage)
        if ( savedInstanceState == null ) {
            openRegistrationFragment()
        }
    }

    private fun openRegistrationFragment() {
        registrationFragment = RegistrationFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, registrationFragment!!)
            .commit()
    }

    private fun openLoginFragment() {
        loginFragment = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, loginFragment!!)
            .commit()
    }

    override fun onAlreadyHaveAccountClick() {
        openLoginFragment()
    }

    override fun onBackToRegistrationClick() {
        openRegistrationFragment()
    }

    override fun onBackPressed() {
        if (loginFragment?.isAdded == true) {
            openRegistrationFragment()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "GoMassageActivity"
    }
}