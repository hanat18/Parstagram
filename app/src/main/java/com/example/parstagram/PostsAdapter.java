package com.example.parstagram;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.fragments.DialougFragment2;
import com.example.parstagram.fragments.PostsFragment;
import com.example.parstagram.model.Post;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    Context context;
    public List<Post> posts;

    public PostsAdapter(List<Post> posts){
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Post post = posts.get(position);
        viewHolder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvHandle;
        private ImageView tvImage;
        private ImageView profileImage;
        private TextView tvTime;
        private TextView tvDescription;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvHandle = view.findViewById(R.id.tvHandle);
            tvImage = view.findViewById(R.id.tvImage);
            tvDescription = view.findViewById(R.id.tvDescription);
            profileImage = view.findViewById(R.id.profileImage);
            tvTime = view.findViewById(R.id.tvTime);

            view.setOnClickListener(this);
        }

        public void bind(Post post){
            tvHandle.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            ParseFile userImage = post.getUser().getParseFile("profileImage");
            if(image != null){
                Glide.with(context)
                        .load(image.getUrl())
                        .into(tvImage);
            }

            if(userImage != null){
                Glide.with(context)
                        .load(userImage.getUrl())
                        .bitmapTransform(new CropCircleTransformation(context))
                        .placeholder(R.drawable.ic_person_black_24dp)
                        .into(profileImage);
            }

            String des = "<b>" + post.getUser().getUsername() + "</b>" + "<font color=\"#808080\">" + "  " + post.getDescription();

            tvDescription.setText(Html.fromHtml(des));
            tvTime.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        }

        // Handles the row being being clicked
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Post post = posts.get(position);

                String time = getRelativeTimeAgo(post.getCreatedAt().toString());

                DialogFragment frag = new DialougFragment2();

                Bundle args = new Bundle();

                args.putString("username", post.getUser().getUsername());
                args.putString("description", post.getDescription());
                args.putString("imageUrl", post.getImage().getUrl());
                args.putString("time", time);
                String profilePic = post.getUser().getParseFile("profileImage").getUrl();
                args.putString("profile", profilePic);

                frag.setArguments(args);
                frag.show(((HomeActivity) context).getSupportFragmentManager(), "TAG");
            }
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
