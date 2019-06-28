package com.mdurokov.snapchatclone

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)

        if(mAuth.currentUser != null){
            login()
        }
    }

    fun goClicked(view: View){
        mAuth.signInWithEmailAndPassword(editTextEmail?.text.toString(), editTextPassword?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    login()
                } else {
                    mAuth.createUserWithEmailAndPassword(editTextEmail?.text.toString(), editTextPassword?.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.result.user.uid)
                                    .child("email").setValue(editTextEmail?.text.toString())
                                login()
                            } else {
                                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

    fun login(){
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

}
