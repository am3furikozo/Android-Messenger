package me.am3furikozo.my_messenger.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.am3furikozo.my_messenger.R
import me.am3furikozo.my_messenger.models.Message

class MessageAdapter(private val ctx: Context, private val list: List<Message>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private companion object {
    const val ITEM_RECEIVED = 1
    const val ITEM_SENT = 2
  }

  abstract class MessageViewHolder(itemView: View, val message: TextView) :
    RecyclerView.ViewHolder(itemView)

  private class ReceivedViewHolder(itemView: View) :
    MessageViewHolder(itemView, itemView.findViewById(R.id.txt_received_message))

  private class SentViewHolder(itemView: View) :
    MessageViewHolder(itemView, itemView.findViewById(R.id.txt_sent_message))

  override fun getItemViewType(pos: Int) =
    if (Firebase.auth.currentUser?.uid.equals(list[pos].sender)) ITEM_SENT else ITEM_RECEIVED

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    if (viewType == ITEM_RECEIVED) ReceivedViewHolder(
      LayoutInflater.from(ctx).inflate(R.layout.received_message_layout, parent, false)
    )
    else SentViewHolder(
      LayoutInflater.from(ctx).inflate(R.layout.sent_message_layout, parent, false)
    )

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) =
    if (MessageViewHolder::class.java.isAssignableFrom(holder.javaClass))
      (holder as MessageViewHolder).message.text = list[pos].text
    else Unit

  override fun getItemCount() = list.size
}