package com.mdurokov.wallpaper.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mdurokov.wallpaper.Models.Photo;
import com.mdurokov.wallpaper.R;
import com.mdurokov.wallpaper.Utils.Functions;
import com.mdurokov.wallpaper.Utils.GlideApp;
import com.mdurokov.wallpaper.Utils.RealmController;
import com.mdurokov.wallpaper.Webservices.ApiInterface;
import com.mdurokov.wallpaper.Webservices.ServiceGenerator;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullScreenPhotoActivity extends AppCompatActivity {

    private final static String TAG = FullScreenPhotoActivity.class.getSimpleName();

    @BindView(R.id.activity_full_screen_photo_photo)
    ImageView fullScreenPhoto;
    @BindView(R.id.activity_full_screen_photo_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.activity_full_screen_photo_menu)
    FloatingActionMenu menu;
    @BindView(R.id.activity_full_screen_photo_favorite)
    FloatingActionButton favorite;
    @BindView(R.id.activity_full_screen_photo_wallpaper)
    FloatingActionButton wallpaper;
    @BindView(R.id.activity_full_screen_photo_username)
    TextView username;

    @BindDrawable(R.drawable.ic_check_favorite)
    Drawable icCheckFavorite;
    @BindDrawable(R.drawable.ic_check_favorited)
    Drawable icCheckFavorited;

    private Bitmap photoBitmap;

    private Unbinder unbinder;
    private RealmController realmController;
    private Photo photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        String photoId = intent.getStringExtra("photoId");
        getPhoto(photoId);

        realmController = new RealmController();
        if(realmController.isPhotoExist(photoId)){
            favorite.setImageDrawable(icCheckFavorited);
        }else{
            favorite.setImageDrawable(icCheckFavorite);
        }
    }

    public void getPhoto(String id){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Photo> call = apiInterface.getPhoto(id);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if(response.isSuccessful()){
                    photo = response.body();
                    updateUI(photo);
                }else{
                    Log.e(TAG, "Fail: " + response.message() );
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.e(TAG, "Fail: " + t.getMessage() );
            }
        });
    }

    private void updateUI(Photo photo){
        try{
            username.setText(photo.getUser().getUsername());
            GlideApp.with(FullScreenPhotoActivity.this)
                    .load(photo.getUser().getProfileImage().getSmall())
                    .into(userAvatar);

            GlideApp.with(FullScreenPhotoActivity.this)
                    .asBitmap()
                    .load(photo.getPhotoUrl().getRegular())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            fullScreenPhoto.setImageBitmap(resource);
                            photoBitmap = resource;
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.activity_full_screen_photo_favorite)
    public void setFavorite(){
        if(realmController.isPhotoExist(photo.getId())){
            realmController.deletePhoto(photo);
            favorite.setImageDrawable(icCheckFavorite);
            Toast.makeText(this, "Removed Favorite", Toast.LENGTH_SHORT).show();
        }else{
            realmController.savePhoto(photo);
            favorite.setImageDrawable(icCheckFavorited);
            Toast.makeText(this, "Set Favorite", Toast.LENGTH_SHORT).show();
        }
        menu.close(true);
    }
    @OnClick(R.id.activity_full_screen_photo_wallpaper)
    public void setWallpaper(){
        if(photoBitmap != null){
            if(Functions.setWallpeper(FullScreenPhotoActivity.this, photoBitmap)){
                Toast.makeText(this, "Set Wallpaper Succsessfull", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Set Wallpaper Failed", Toast.LENGTH_SHORT).show();
            }

        }

        menu.close(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
