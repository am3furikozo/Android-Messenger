package me.am3furikozo.my_messenger.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import me.am3furikozo.my_messenger.ChatActivity
import me.am3furikozo.my_messenger.R
import me.am3furikozo.my_messenger.models.User

class UserAdapter(private val ctx: Context, private val list: List<User>) :
  RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

  class UserViewHolder(
    itemView: View,
    val textName: TextView = itemView.findViewById(R.id.txt_name)
  ) : RecyclerView.ViewHolder(itemView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
    UserViewHolder(
      LayoutInflater.from(ctx).inflate(R.layout.user_layout, parent, false)
    )

  override fun onBindViewHolder(holder: UserViewHolder, pos: Int) {
    val user = list[pos]
    holder.textName.text = user.name
    holder.itemView.setOnClickListener {
      val intent = Intent(ctx, ChatActivity::class.java)
      intent.putExtras(
        bundleOf(
          "name" to user.name,
          "uid" to user.uid
        )
      )
      ctx.startActivity(intent)
    }
  }

  override fun getItemCount() = list.size
}