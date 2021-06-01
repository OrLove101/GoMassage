package com.orlove101.android.gomassage.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.orlove101.android.gomassage.R
import com.orlove101.android.gomassage.adapters.NewMassageAdapter
import com.orlove101.android.gomassage.model.User

class NewMassageFragment: Fragment() {
    private var toolbarBackImageView: View? = null
    private var newMassageRecyclerView: RecyclerView? = null
    private var newMassageAdapter: NewMassageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_massage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarBackImageView = view.findViewById(R.id.toolbar_back_image_view)
        toolbarBackImageView?.setOnClickListener {
            activity?.onBackPressed()
        }

        newMassageRecyclerView = view.findViewById(R.id.new_massage_recycler_view)
        newMassageRecyclerView.apply {
            this?.layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            newMassageAdapter = NewMassageAdapter(users)
            this?.adapter = newMassageAdapter
            this?.addItemDecoration(DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            ))
        }

        fetchUsers()
    }

    private fun fetchUsers(): Unit {
        val ref = Firebase.database.reference.child("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)

                    if ( user != null && !isContainThatUser(user) ) {
                        users.add(user)
                        newMassageAdapter?.notifyItemInserted(newMassageAdapter?.itemCount ?: 0)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "DatabaseError")
            }
        })
    }

    private fun isContainThatUser(user: User): Boolean {
        users.forEach {
            if ( it.uid == user.uid) {
                return true
            }
        }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? NewMassageCommunicator)?.attachToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? NewMassageCommunicator)?.detachToolbar()
    }

    interface NewMassageCommunicator {
        fun attachToolbar()
        fun detachToolbar()
    }

    companion object {
        fun newInstance(): NewMassageFragment {
            val fragment = NewMassageFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
        private const val TAG = "NewMassageFragment"
        private val users = ArrayList<User>()
    }
}