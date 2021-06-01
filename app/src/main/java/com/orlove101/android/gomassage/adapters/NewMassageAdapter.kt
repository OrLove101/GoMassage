package com.orlove101.android.gomassage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orlove101.android.gomassage.R
import com.orlove101.android.gomassage.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NewMassageAdapter(private val users: List<User>): RecyclerView.Adapter<NewMassageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMassageViewHolder {
        val newMassageListItemView = LayoutInflater
            .from(parent.context)
            .inflate(
            R.layout.list_item_new_massage,
            parent,
            false
        )

        return NewMassageViewHolder(newMassageListItemView)
    }

    override fun onBindViewHolder(holder: NewMassageViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}

class NewMassageViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private var user: User? = null
    private var profileCircleImageView: CircleImageView? = null
    private var profileNameTextView: TextView? = null

    init {
        profileCircleImageView = view.findViewById(R.id.profile_image_view)
        profileNameTextView = view.findViewById(R.id.profile_name_text_view)
    }

    fun bind(user: User): Unit {
        this.user = user
        Picasso.get()
            .load(user.profileImageUrl)
            .placeholder(R.drawable.photo_holder)
            .error(R.drawable.photo_holder)
            .into(profileCircleImageView)
        profileNameTextView?.text = user.username
    }
}

object NewMassageAdapterConstants {
    const val TAG = "NewMassageAdapter"
}