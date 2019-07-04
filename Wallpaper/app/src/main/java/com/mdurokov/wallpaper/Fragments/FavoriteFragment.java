package com.mdurokov.wallpaper.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mdurokov.wallpaper.Adapters.PhotosAdapter;
import com.mdurokov.wallpaper.Models.Photo;
import com.mdurokov.wallpaper.R;
import com.mdurokov.wallpaper.Utils.RealmController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment {

    @BindView(R.id.fragment_favorite_notification)
    TextView notification;
    @BindView(R.id.fragment_favorite_recyclerview)
    RecyclerView recyclerView;


    private PhotosAdapter adapter;
    private List<Photo> photos = new ArrayList<>();
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);

        // RecycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PhotosAdapter(getActivity(), photos);
        recyclerView.setAdapter(adapter);
        getPhotos();
        return view;
    }

    public void getPhotos() {
        RealmController realmController = new RealmController();
        photos.addAll(realmController.getPhotos());

        if(photos.size() == 0)
        {
            notification.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
