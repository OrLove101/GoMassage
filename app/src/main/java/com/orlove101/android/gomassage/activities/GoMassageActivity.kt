package com.orlove101.android.gomassage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.orlove101.android.gomassage.R
import com.orlove101.android.gomassage.fragments.LatestMassagesFragment
import com.orlove101.android.gomassage.fragments.LoginFragment
import com.orlove101.android.gomassage.fragments.NewMassageFragment
import com.orlove101.android.gomassage.fragments.RegistrationFragment

class GoMassageActivity : AppCompatActivity(), RegistrationFragment.RegistrationFragmentCommunicator,
    LoginFragment.LoginFragmentCommunicator, LatestMassagesFragment.LatestMassagesCommunicator,
    NewMassageFragment.NewMassageCommunicator{
    private var registrationFragment: RegistrationFragment? = null
    private var loginFragment: LoginFragment? = null
    private var latestMassagesFragment: LatestMassagesFragment? = null
    private var newMassagesFragment: NewMassageFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_massage)
        if ( savedInstanceState == null ) {
            openLatestMassagesFragment()
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

    private fun openLatestMassagesFragment() {
        latestMassagesFragment = LatestMassagesFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, latestMassagesFragment!!)
            .commit()
    }

    private fun openNewMassageFragment() {
        newMassagesFragment = NewMassageFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, newMassagesFragment!!)
            .commit()
    }

    override fun onAlreadyHaveAccountClick() {
        openLoginFragment()
    }

    override fun onUserRegistered() {
        openLatestMassagesFragment()
    }

    override fun onBackToRegistrationClick() {
        openRegistrationFragment()
    }

    override fun onUserLogin() {
        openLatestMassagesFragment()
    }

    override fun onBackPressed() {
        when {
            loginFragment?.isAdded == true -> {
                openRegistrationFragment()
            }
            newMassagesFragment?.isAdded == true -> {
                openLatestMassagesFragment()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun startRegistration() {
        openRegistrationFragment()
    }

    override fun startNewMassage() {
        openNewMassageFragment()
    }

    override fun attachToolbar() {
        supportActionBar?.hide()
    }

    override fun detachToolbar() {
        supportActionBar?.show()
    }

    companion object {
        private const val TAG = "GoMassageActivity"
    }


}