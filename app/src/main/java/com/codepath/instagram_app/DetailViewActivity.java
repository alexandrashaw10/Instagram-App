package com.codepath.instagram_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.instagram_app.model.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailViewActivity extends AppCompatActivity {

    @BindView(R.id.picture)
    ImageView picture;
    @BindView(R.id.userName)
    TextView username;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.likeBtn)
    ImageButton likeBtn;
    @BindView(R.id.commentBtn)
    ImageButton commentBtn;
    @BindView(R.id.replyBtn)
    ImageButton replyBtn;
    @BindView(R.id.timeStamp)
    TextView timeStamp;

    String postId;

    String bodyTxt;
    String usernameTxt;
    ParseFile imageFile;
    Date createdAt;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        context = this;

        ButterKnife.bind(this);

        postId = getIntent().getStringExtra("postId");

        final Post.Query postQuery = new Post.Query();
        postQuery.withUser();

        postQuery.getInBackground(postId, new GetCallback<Post>() {
            @Override
            public void done(Post object, ParseException e) {
                if (e == null) {
                    bodyTxt = object.getDescription();
                    usernameTxt = object.getUser().getUsername();
                    imageFile = object.getImage();
                    createdAt = object.getCreatedAt();

                    description.setText(bodyTxt);
                    username.setText(usernameTxt);
                    timeStamp.setText(createdAt.toString());

                    if (context != null) {
                        Glide.with(context).load(imageFile.getUrl())
                                .apply(RequestOptions.centerCropTransform())
                                .into(picture);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
