package com.codepath.instagram_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram_app.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class ProfileFragment extends TimelineFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void getPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser().getNewest().forCurrentUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        posts.add(i, objects.get(i));
                        adapter.notifyItemInserted(i);
                    }
                } else {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }
}



