package com.codepath.instagram_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram_app.PostAdapter;
import com.codepath.instagram_app.R;
import com.codepath.instagram_app.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    protected SwipeRefreshLayout swipeContainer;
    protected ArrayList<Post> posts;
    protected PostAdapter adapter;
    private RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                adapter.notifyDataSetChanged();
                getPosts();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // get the posts after a refresh
    protected void getPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().getNewest();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        posts.add(i, objects.get(i));
                        adapter.notifyItemInserted(i);
                    }
                    swipeContainer.setRefreshing(false);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPosts();
    }
}
