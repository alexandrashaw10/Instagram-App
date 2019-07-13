package com.codepath.instagram_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

        Glide.with(context).load(post.getImage().getUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.postPicture);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView postPicture;
        public TextView description;
        public TextView username;
        public ImageButton likeBtn;
        public ImageButton commentBtn;
        public ImageButton replyBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            postPicture = itemView.findViewById(R.id.picture);
            description = itemView.findViewById(R.id.description);
            username = itemView.findViewById(R.id.userName);

            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            replyBtn = itemView.findViewById(R.id.replyBtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailViewActivity.class);
                        intent.putExtra("postId", mPosts.get(position).getObjectId());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

