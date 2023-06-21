package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.uko.R;
import com.mobile.uko.adapter.OurLocationAdapter;
import com.mobile.uko.adapter.OurSpacesAdapter;
import com.mobile.uko.model.ExplorerResponse;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreActivity extends AppCompatActivity implements OurLocationAdapter.onItemClick{

    private ImageView img_intro, img_community, arrow_community_left, arrow_community_right, img_slice, slice_arrow_left, slice_arrow_right, img_podcast;
    private RecyclerView rv_locations, rv_spaces, rv_podcast;
    private TextView txt_intro, txt_community_title, txt_community, txt_podcast_title, txt_podcast_description;
    private APIClient apiClient;
    private RelativeLayout rl_community, rl_podcast;
    ExplorerResponse explorerResponse = new ExplorerResponse();
    OurLocationAdapter locationAdapter;
    OurSpacesAdapter spacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        apiClient = new APIClient();

        initUI();
        getExplorers();
    }

    private void initUI() {
        img_intro = (ImageView) findViewById(R.id.img_top);
        txt_intro = (TextView) findViewById(R.id.tv_intro_text);
        img_community = (ImageView) findViewById(R.id.img_community);
        arrow_community_left = (ImageView) findViewById(R.id.img_community_left);
        arrow_community_right = (ImageView) findViewById(R.id.img_community_right);
        img_slice = (ImageView) findViewById(R.id.img_slice);
        slice_arrow_left = (ImageView) findViewById(R.id.img_slice_left);
        slice_arrow_right = (ImageView) findViewById(R.id.img_slice_right);
        rv_locations = (RecyclerView) findViewById(R.id.rv_our_location);
        rv_spaces = (RecyclerView) findViewById(R.id.rv_our_spaces);
        rv_podcast = (RecyclerView) findViewById(R.id.rv_uko_podcast);
        txt_community_title = (TextView) findViewById(R.id.txt_community_title);
        txt_community = (TextView) findViewById(R.id.txt_community);
        rl_community = (RelativeLayout) findViewById(R.id.rel_community);
        rl_podcast = (RelativeLayout) findViewById(R.id.rel_podcast);
        img_podcast = (ImageView) findViewById(R.id.img_podcast);
        txt_podcast_title = (TextView) findViewById(R.id.txt_podcast_title);
        txt_podcast_description = (TextView) findViewById(R.id.txt_podcast_description);

        rl_community.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("webview_url", getString(R.string.community_url));
            intent.putExtras(bundle);
            startActivity(intent);
        });

        rl_podcast.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("webview_url", getString(R.string.pod_cast_url));
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void getExplorers() {
        Common.showLoading(ExploreActivity.this);

        Call<ExplorerResponse> response = apiClient.getServices().getExplorer();
        response.enqueue(new Callback<ExplorerResponse>() {
            @Override
            public void onResponse(Call<ExplorerResponse> call, Response<ExplorerResponse> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    explorerResponse = response.body();

                    updateUI(explorerResponse);
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Toast.makeText(ExploreActivity.this, object.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExplorerResponse> call, Throwable t) {
                Common.hideLoading();
            }
        });
    }

    private void updateUI(ExplorerResponse response) {
        if (response.getIntro() != null) {
            txt_intro.setText(response.getIntro().getDescription());
            Glide.with(this)
                    .load(response.getIntro().getImages().get(0).getUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.explore_top).dontAnimate().error(R.drawable.explore_top))
                    .into(img_intro);
        }

        if (response.getCommunity() != null) {
            txt_community_title.setText(response.getCommunity().getTitle());
            txt_community.setText(response.getCommunity().getDescription());
            Glide.with(this)
                    .load(response.getCommunity().getImages().get(0).getUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.our_community).dontAnimate().error(R.drawable.our_community))
                    .into(img_community);
        }

        if (response.getPodcast() != null) {
            txt_podcast_title.setText(response.getPodcast().get(0).getTitle());
            txt_podcast_description.setText(response.getPodcast().get(0).getDescription());
            Glide.with(this)
                    .load(response.getPodcast().get(0).getImages().get(0).getUrl())
                    .apply(RequestOptions.placeholderOf(R.drawable.img_podcast).dontAnimate().error(R.drawable.img_podcast))
                    .into(img_podcast);
        }

        locationAdapter = new OurLocationAdapter(ExploreActivity.this, response.getPropertiesLocations(), this);
        rv_locations.setLayoutManager(new LinearLayoutManager(ExploreActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rv_locations.setItemAnimator(new DefaultItemAnimator());
        rv_locations.setAdapter(locationAdapter);

        spacesAdapter = new OurSpacesAdapter(ExploreActivity.this, response.getSpaces());
        rv_spaces.setLayoutManager(new LinearLayoutManager(ExploreActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rv_spaces.setItemAnimator(new DefaultItemAnimator());
        rv_spaces.setAdapter(spacesAdapter);
    }

    @Override
    public void onClick(int id) {
        Intent intent = new Intent(this, EnquireActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}