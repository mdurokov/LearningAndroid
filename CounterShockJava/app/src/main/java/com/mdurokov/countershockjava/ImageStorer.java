package com.mdurokov.countershockjava;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ImageStorer {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public ImageStorer(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(ShockUtils.SHOCK_SCARED_PREFS, context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void storeImages(List<ImageModel> images){
        String key = context.getString(R.string.key_storer_offline_images);
        Gson gson = new Gson();
        editor.putString(key, gson.toJson(images));
        editor.commit();
    }

    private List<ImageModel> getStoredImages(){
        String imagesAsJson = preferences.getString(context.getString(R.string.key_storer_offline_images), null);
        if(imagesAsJson == null || imagesAsJson.length() == 0){
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ImageModel>>(){}.getType();
        List<ImageModel> storedImages = gson.fromJson(imagesAsJson, type);
        return storedImages;
    }

    public void addImage(ImageModel image){
        List<ImageModel> images = getStoredImages();
        images.add(image);
        storeImages(images);
    }

    public List<ImageModel> getAllImages(){
        ArrayList<ImageModel> assetImages = new ArrayList<>();
        assetImages.add(new ImageModel(0, "clown", true));
        assetImages.add(new ImageModel(1, "doll", true));
        assetImages.add(new ImageModel(2, "lama", true));

        assetImages.addAll(getStoredImages());
        return assetImages;
    }

    public ImageModel getSelectedImage(){
        List<ImageModel> images = getAllImages();
        ImageModel defaultImage = images.get(0);
        int imageId = preferences.getInt(context.getString(R.string.key_photo_id), 0);
        for(ImageModel image: images){
            if(image.getId() == imageId){
                return image;
            }
        }

        editor.putInt(context.getString(R.string.key_photo_id), 0);
        editor.commit();

        return defaultImage;
    }

}
