package com.orlove101.android.gomassage.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orlove101.android.gomassage.R

class LatestMassagesFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_latest_massages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyUserIsLoggedIn()
    }

    private fun verifyUserIsLoggedIn() {
        val uid = Firebase.auth.uid

        if ( uid == null ) {
            (activity as LatestMassagesCommunicator).startRegistration()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.navigate_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {
                (activity as? LatestMassagesCommunicator)?.startNewMassage()
            }
            R.id.menu_sign_out -> {
                Firebase.auth.signOut()
                (activity as? LatestMassagesCommunicator)?.startRegistration()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    interface LatestMassagesCommunicator {
        fun startRegistration()
        fun startNewMassage()
    }

    companion object {
        fun newInstance(): LatestMassagesFragment {
            val fragment = LatestMassagesFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
        private const val TAG = "LatestMassagesFragment"
    }
}