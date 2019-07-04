package com.mdurokov.wallpaper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mdurokov.wallpaper.Adapters.PhotosAdapter;
import com.mdurokov.wallpaper.Models.Collection;
import com.mdurokov.wallpaper.Models.Photo;
import com.mdurokov.wallpaper.R;
import com.mdurokov.wallpaper.Utils.GlideApp;
import com.mdurokov.wallpaper.Webservices.ApiInterface;
import com.mdurokov.wallpaper.Webservices.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {

    private final String TAG = CollectionFragment.class.getSimpleName();

    @BindView(R.id.fragment_collection_username)
    TextView username;
    @BindView(R.id.fragment_collection_description)
    TextView description;
    @BindView(R.id.fragment_collection_user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.fragment_collection_title)
    TextView title;

    @BindView(R.id.fragment_collection_progresbar)
    ProgressBar progressBar;
    @BindView(R.id.fragment_collection_recyclerview)
    RecyclerView recyclerView;

    private List<Photo> photos = new ArrayList<>();
    private PhotosAdapter adapter;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        unbinder = ButterKnife.bind(this, view);
        //RecycleView

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PhotosAdapter(getActivity(), photos);
        recyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        Log.i(TAG, "BUNDLE: " + bundle);
        int collectionId = bundle.getInt("collectionId");
        showProgressBar(true);

        getInformationOfCollection(collectionId);
        getPhotosOfCollection(collectionId);

        return view;
    }

    private void getInformationOfCollection(int collectionId){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Collection> call = apiInterface.getInformationOfCollection(collectionId);
        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {
                if(response.isSuccessful()){
                    Collection collection = response.body();
                    title.setText(collection.getTitle());
                    description.setText(collection.getDescription());
                    username.setText(collection.getUser().getUsername());
                    GlideApp.with(getActivity())
                            .load(collection.getUser().getProfileImage().getSmall())
                            .into(userAvatar);
                }else{
                    Log.d(TAG, "Fail: " + response.message());
                }


            }

            @Override
            public void onFailure(Call<Collection> call, Throwable t) {
                Log.d(TAG, "Fail: " + t.getMessage());
            }
        });
    }

    private void getPhotosOfCollection(int collectionId){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getCollectionPhotos(collectionId);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.isSuccessful()){
                    photos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else{
                    Log.d(TAG, "Fail: " + response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d(TAG, "Fail: " + t.getMessage());
                showProgressBar(false);
            }
        });
    }

    private void showProgressBar(boolean isShow){
        if(isShow){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
