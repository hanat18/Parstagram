package com.example.parstagram.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class DialougFragment2 extends DialogFragment {
    public TextView tvHandle;
    public ImageView tvImage;
    public TextView tvDescription;
    public TextView tvTime;
    public Button back;
    public ImageView profileImage;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        tvHandle = view.findViewById(R.id.tvHandle);
        tvImage = view.findViewById(R.id.tvImage);
        tvDescription = view.findViewById(R.id.tvDescription);
        back = view.findViewById(R.id.backButton);
        tvTime = view.findViewById(R.id.tvTime);
        profileImage = view.findViewById(R.id.profileImage);

        Bundle mArgs = getArguments();
        tvHandle.setText(mArgs.getString("username"));
        tvDescription.setText(mArgs.getString("description"));
        String image = mArgs.getString("imageUrl");
        tvTime.setText(mArgs.getString("time"));
        String profileImage3 = mArgs.getString("profile");

        String des = "<b>" + mArgs.getString("username") + "</b>" + "<font color=\"#808080\">" + "  " + mArgs.getString("description");

        tvDescription.setText(Html.fromHtml(des));

        if(image != null){
            Glide.with(context)
                    .load(image)
                    .into(tvImage);
        }

        if(profileImage3 != null){
            Glide.with(context)
                    .load(profileImage3)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .placeholder(R.drawable.ic_person_black_24dp)
                    .into(profileImage);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
