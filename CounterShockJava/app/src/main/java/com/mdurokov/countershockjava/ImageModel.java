package com.mdurokov.countershockjava;

import java.io.Serializable;
import java.util.Objects;

public class ImageModel implements Serializable {
    int id;
    String imageFilename;
    boolean isAsset;

    public ImageModel(int id, String imageFilename, boolean isAsset) {
        this.id = id;
        this.imageFilename = imageFilename;
        this.isAsset = isAsset;
    }

    public int getId() {
        return id;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public boolean isAsset() {
        return isAsset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageModel that = (ImageModel) o;
        return id == that.id &&
                isAsset == that.isAsset &&
                Objects.equals(imageFilename, that.imageFilename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageFilename, isAsset);
    }
}
