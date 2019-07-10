package com.codepath.instagram_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.instagram_app.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> mPosts;
    private Context context;

    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
        holder.username.setText(post.getUser().getUsername());
        holder.description.setText(post.getDescription());

        Glide.with(context).load(post.getImage()).into(holder.postPicture);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView postPicture;
        public TextView description;
        public TextView username;
        public Button likeBtn;
        public Button commentBtn;
        public Button replyBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            postPicture = (ImageView) itemView.findViewById(R.id.picture);
            description = (TextView) itemView.findViewById(R.id.description);
            username = (TextView) itemView.findViewById(R.id.userName);

            likeBtn = (Button) itemView.findViewById(R.id.likeBtn);
            commentBtn = (Button) itemView.findViewById(R.id.commentBtn);
            replyBtn = (Button) itemView.findViewById(R.id.replyBtn);
        }
    }
}

