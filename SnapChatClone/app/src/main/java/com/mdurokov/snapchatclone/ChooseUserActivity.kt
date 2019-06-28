package com.mdurokov.snapchatclone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChooseUserActivity : AppCompatActivity() {

    var listViewChooseUser: ListView? = null
    var emails: ArrayList<String> = ArrayList()
    var keys: ArrayList<String> = ArrayList()
    val mAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        listViewChooseUser = findViewById(R.id.listViewChooseUser)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        listViewChooseUser?.adapter = adapter

        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {}

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                emails.add(p0?.child("email")?.value as String)
                keys.add(p0.key)
                adapter.notifyDataSetChanged()
            }
        })

        listViewChooseUser?.onItemClickListener = AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val snapMap: Map<String, String> = mapOf("from" to mAuth.currentUser?.email.toString(),
                "imageName" to intent.getStringExtra("imageName"),
                "imageUrl" to intent.getStringExtra("imageUrl"),
                "message" to intent.getStringExtra("message"))

            FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(position)).child("snaps")
                .push().setValue(snapMap)

            val intent = Intent(this, SnapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}
