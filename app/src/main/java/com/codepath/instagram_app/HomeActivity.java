package com.codepath.instagram_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.instagram_app.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private ParseUser currentUser;
    Context context = this;
    private SwipeRefreshLayout swipeContainer;

    BottomNavigationView bottomNavigationView;

    @BindView(R.id.rvPosts) RecyclerView rvPosts;
    ArrayList<Post> posts;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // TODO: put recycler view in fragment 1
                        return true;
                    case R.id.action_create_post:
                        // TODO: put create post data into fragment 2
                        Intent createPostIntent = new Intent(context, CreatePostActivity.class);
                        startActivity(createPostIntent);
                        return true;
                        // TODO add 3rd case that is for profile and fragment 3
                    // TODO this will remove error going between creating posts and fragment
                    default: return true;
                }
            }
        });

        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // TODO fix bug that refresh adds the top post again instead of refreshing the current posts
        // TODO Question: refreshing posts doesn't actually do anything cause you're the only one with the app
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                ParseUser.logOut();
                currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public ParseUser getCurrentUser() {
        return currentUser;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPosts();
    }

    // get the posts after a refresh
    public void getPosts() {
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
                }
            }
        });
    }
}
