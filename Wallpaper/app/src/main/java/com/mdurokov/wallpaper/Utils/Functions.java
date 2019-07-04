package com.mdurokov.wallpaper.Utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mdurokov.wallpaper.R;

import java.io.IOException;

import retrofit2.http.Path;

public class Functions {

    public static void changeMainFragment(FragmentActivity fragmentActivity, Fragment fragment){
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    public static void changeMainFragmentWithBack(FragmentActivity fragmentActivity, Fragment fragment){
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static boolean setWallpeper(Activity activity, Bitmap bitmap){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        try{
            wallpaperManager.setBitmap(bitmap);
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

}
