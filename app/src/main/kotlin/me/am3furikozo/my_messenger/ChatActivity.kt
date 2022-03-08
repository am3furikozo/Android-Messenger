package me.am3furikozo.my_messenger

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import me.am3furikozo.my_messenger.adapters.MessageAdapter
import me.am3furikozo.my_messenger.models.Message

class ChatActivity : AppCompatActivity() {
  private lateinit var msgBox: EditText
  private lateinit var sendBtn: ImageView
  private lateinit var list: MutableList<Message>
  private lateinit var adapter: MessageAdapter
  private lateinit var msgRecycler: RecyclerView
  private lateinit var receiverRoom: String
  private lateinit var senderRoom: String
  private lateinit var dbRef: DatabaseReference

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)

    val name = intent.getStringExtra("name")
    val receiverUid = intent.getStringExtra("uid")!!
    val senderUid = Firebase.auth.currentUser?.uid!!

    supportActionBar?.title = name

    msgBox = findViewById(R.id.msg_box)
    sendBtn = findViewById(R.id.msg_send_btn)
    list = mutableListOf()
    adapter = MessageAdapter(this, list)
    msgRecycler = findViewById(R.id.msg_recycler)
    msgRecycler.layoutManager= LinearLayoutManager(this)
    msgRecycler.adapter = adapter
    receiverRoom = senderUid + receiverUid
    senderRoom = receiverUid + senderUid
    dbRef = FirebaseDatabase.getInstance().reference

    dbRef
      .child("chats")
      .child(senderRoom)
      .child("messages")
      .addValueEventListener(object : ValueEventListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onDataChange(snapshot: DataSnapshot) {
          list.clear()
          for (postSnapshot in snapshot.children)
            list.add(postSnapshot.getValue(Message::class.java)!!)
          runOnUiThread { adapter.notifyDataSetChanged() }
        }

        override fun onCancelled(error: DatabaseError) {}
      })

    sendBtn.setOnClickListener {
      val text = msgBox.text.toString().trim()
      if (text.isEmpty()) Toast
        .makeText(this, "Cannot send empty message", Toast.LENGTH_SHORT)
        .show()
      val msg = Message(text, senderUid)
      dbRef
        .child("chats")
        .child(senderRoom)
        .child("messages")
        .push()
        .setValue(msg)
        .addOnSuccessListener {
          dbRef
            .child("chats")
            .child(receiverRoom)
            .child("messages")
            .push()
            .setValue(msg)
        }
      msgBox.setText(String())
    }
  }
}