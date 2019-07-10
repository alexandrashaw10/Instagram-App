package com.codepath.instagram_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.instagram_app.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private ParseUser currentUser;
    Context context = this;

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
                        // TODO: make fragment
                        return true;
                    case R.id.action_create_post:
                        Intent createPostIntent = new Intent(context, CreatePostActivity.class);
                        startActivity(createPostIntent);
                        return true;
                    default: return true;
                }
            }
        });

        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
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

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getNewest(new Date()).withUser().getTop();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    posts.addAll(0, objects);
                    adapter.notifyItemInserted(0);
                    rvPosts.scrollToPosition(0);
                    Log.i("HomeActivity", String.format("item at position 0: %s", objects.get(0)));
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
