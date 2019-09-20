package com.example.newspaper.Activity.DialogCustom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.newspaper.R;

public class DialogZoomImage extends DialogFragment {
    ImageView imageView;
    ScaleGestureDetector scaleGestureDetector;
    public static DialogZoomImage newInstance(String linkImage) {
        DialogZoomImage dialog= new DialogZoomImage();
        Bundle args = new Bundle();
        args.putString("linkImage", linkImage);
        DialogZoomImage fragment = new DialogZoomImage();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_zoom_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String linkImage= getArguments().getString("linkImage");
        imageView= view.findViewById(R.id.imageZoom);
        Glide.with(getContext())
                .load(linkImage)
                .into(imageView);
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });*/
        scaleGestureDetector= new ScaleGestureDetector(getContext(), new MyGesture());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }
    class MyGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        Float scale= 1.0F;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale*= detector.getScaleFactor();
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            return super.onScale(detector);
        }
    }
}
