package com.mdurokov.countershockjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    AudioStorer audioStorer;
    ImageStorer imageStorer;

    ImageView imageViewScary;
    ImageView playIcon;
    TextView textViewAudio;

    TextToSpeech tts;
    MediaPlayer mediaPlayer;

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.prankSurface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
                finish();
            }
        });

        preferences = getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS, Context.MODE_PRIVATE);
        editor = preferences.edit();

        audioStorer = new AudioStorer(this);
        imageStorer = new ImageStorer(this);

        imageViewScary = findViewById(R.id.imageViewScary);
        textViewAudio = findViewById(R.id.textViewAudio);
        playIcon = findViewById(R.id.imageViewPlayIcon);
        updateUI();

        findViewById(R.id.audioSurface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if(prev != null){
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                AudioPickerDialogFragment dialogFragment = new AudioPickerDialogFragment();
                dialogFragment.setCancelable(true);
                dialogFragment.show(ft, "dialog");
            }
        });

        imageViewScary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if(prev != null){
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                ImagePickerDialogFragment dialogFragment = new ImagePickerDialogFragment();
                dialogFragment.setCancelable(true);
                dialogFragment.show(ft, "dialog");
            }
        });

        findViewById(R.id.playSurface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioModel audio = audioStorer.getSelectedAudio();
                if(audio.isTTS()){
                    final String toSpeak = audio.getDescriptionMessage();
                    tts = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status == TextToSpeech.SUCCESS){
                                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                }else{
                    //Audio Clips
                    Uri uri;
                    if(audio.isAsset()){
                        uri = ShockUtils.getRawUri(getBaseContext(), audio.getAudioFilename());
                    }else{
                        uri = Uri.fromFile(new File(audio.getAudioFilename()));
                    }

                    if(mediaPlayer != null){
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            updateAudioIcon(false);
                            return;
                        }
                    }

                    mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            updateAudioIcon(false);
                        }
                    });
                    mediaPlayer.start();
                    updateAudioIcon(true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addButton){
            final PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.addButton));
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.addImage:
                            addImageDialog();
                            break;
                        case R.id.addAudio:
                            addAudioDialog();
                            break;
                    }
                    return false;
                }
            });

            popupMenu.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addAudioDialog(){
        final EditText soundEditText = new EditText(this);
        soundEditText.setHint("Message to speak");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add audio")
                .setMessage("Enter message for text to speach")
                .setView(soundEditText)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = soundEditText.getText().toString();
                        if(message.equals("") || message == null || message.trim().isEmpty()){
                            Toast.makeText(MainActivity.this, "Message can't be empty",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            addTTSAudio(message);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.show();
    }

    private void addTTSAudio(String message) {
        int mediaId = preferences.getInt(getString(R.string.key_next_media_id), ShockUtils.STARTING_ID);
        editor.putInt(getString(R.string.key_next_media_id), mediaId + 1);
        editor.commit();

        AudioModel audioModel = new AudioModel(mediaId, message);
        audioStorer.addAudio(audioModel);

    }

    private void addImageDialog() {
        final EditText urlBox = new EditText(this);
        urlBox.setHint("Image URL");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Image Url")
                .setMessage("Import an image from web")
                .setView(urlBox)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = urlBox.getText().toString();
                        if(url.trim().isEmpty() || url == null  || url.equals("")){
                            Toast.makeText(MainActivity.this, "Url can't be empty!",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            downloadImageToFile(url);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.show();
    }

    private void downloadImageToFile(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveImage(resource);
                    }
                });
    }

    private void saveImage(Bitmap bitmap){
        FileOutputStream outputStream = null;
        File file = createInternalFile(UUID.randomUUID().toString());
        int mediaId = preferences.getInt(getString(R.string.key_next_media_id), ShockUtils.STARTING_ID);
        editor.putInt(getString(R.string.key_next_media_id), mediaId + 1);
        editor.commit();

        ImageModel imageModel = new ImageModel(mediaId, file.getAbsolutePath(), false);
        try{
            outputStream = new FileOutputStream(new File(imageModel.getImageFilename()));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

            imageStorer.addImage(imageModel);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private File createInternalFile(String filename){
        File outputDir = getExternalCacheDir();
        File outputFile = new File(outputDir, filename);

        return outputFile;
    }

    private void createNotification(){
        String notificationMessage = "Tap to shock friends";
        int requestId = (int) System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, SupriseActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, requestId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMessage))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "AnyThing_Channel";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(4586, builder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver,
                new IntentFilter(ShockUtils.MEDIA_UPDATED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
    }

    private void updateUI(){
        ImageModel imageModel = imageStorer.getSelectedImage();
        Uri imageUri;
        if(imageModel.isAsset()){
            imageUri = ShockUtils.getDrawableUri(this, imageModel.imageFilename);
        }else{
            imageUri = Uri.fromFile(new File(imageModel.getImageFilename()));
        }

        // Updating to current selected image
        Glide.with(this)
                .load(imageUri)
                .into(imageViewScary);

        // handle audio text
        AudioModel audioModel = audioStorer.getSelectedAudio();
        textViewAudio.setText(audioModel.getDescriptionMessage());
    }

    private void updateAudioIcon(boolean isPlaying){
        if(isPlaying){
            playIcon.setImageResource(R.drawable.ic_pause);
        }else{
            playIcon.setImageResource(R.drawable.ic_play);
        }
    }
}
