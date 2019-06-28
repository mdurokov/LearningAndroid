package com.mdurokov.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    var textViewReceivedMessage: TextView? = null
    var imageViewReceivedImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        textViewReceivedMessage = findViewById(R.id.textViewReceivedMessage)
        imageViewReceivedImage = findViewById(R.id.imageViewReceivedImage)

        textViewReceivedMessage?.text = intent.getStringExtra("message")

        val imageTask = ImageDownloader()
        val image = imageTask.execute(intent.getStringExtra("imageUrl")).get()

        imageViewReceivedImage?.setImageBitmap(image)

    }


    inner class ImageDownloader : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            try {

                val url = URL(urls[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connect()
                val inputStream = urlConnection.inputStream

                return BitmapFactory.decodeStream(inputStream)


            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid).child("snaps")
            .child(intent.getStringExtra("snapKey")).removeValue()

        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName"))
            .delete()

    }
}
