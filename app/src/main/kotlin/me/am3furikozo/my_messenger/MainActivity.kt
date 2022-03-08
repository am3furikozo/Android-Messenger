package me.am3furikozo.my_messenger

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import me.am3furikozo.my_messenger.adapters.UserAdapter
import me.am3furikozo.my_messenger.models.User

class MainActivity : AppCompatActivity() {
  private lateinit var userRecyclerView: RecyclerView
  private lateinit var list: MutableList<User>
  private lateinit var adapter: UserAdapter
  private lateinit var auth: FirebaseAuth
  private lateinit var dbRef: DatabaseReference

  private fun logout() {
    auth.signOut()
    finish()
    startActivity(Intent(this, LoginActivity::class.java))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    auth = Firebase.auth
    dbRef = FirebaseDatabase.getInstance().reference

    list = mutableListOf()
    adapter = UserAdapter(this, list)

    userRecyclerView = findViewById(R.id.user_recycler_view)
    userRecyclerView.layoutManager = LinearLayoutManager(this)
    userRecyclerView.adapter = adapter

    dbRef.child("user").addValueEventListener(object: ValueEventListener{
      @SuppressLint("NotifyDataSetChanged")
      override fun onDataChange(snapshot: DataSnapshot) {
        list.clear()
        for (postSnapshot in snapshot.children) {
          val cur = postSnapshot.getValue(User::class.java)!!
          if (cur.uid != auth.currentUser?.uid) list.add(cur)
        }
        runOnUiThread { adapter.notifyDataSetChanged() }
      }

      override fun onCancelled(error: DatabaseError) { }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.logout) logout()
    return true
  }
}