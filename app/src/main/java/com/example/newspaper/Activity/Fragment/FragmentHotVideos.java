package com.example.newspaper.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.newspaper.R;

public class FragmentHotVideos extends Fragment {
    ImageView imageMeoCon;
    public static FragmentHotVideos newInstance() {

        Bundle args = new Bundle();

        FragmentHotVideos fragment = new FragmentHotVideos();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.hot_videos, container, false);
        imageMeoCon= view.findViewById(R.id.imageMeoCon);
        final ScaleGestureDetector scaleGestureDetector;
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new MyGesture());
        imageMeoCon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        return view;
    }

    private class MyGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        Float scale=1.0F, onScaleStart=0F , onScaleEnd=0F;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale*= detector.getScaleFactor();
            imageMeoCon.setScaleX(scale);
            imageMeoCon.setScaleY(scale);
            return super.onScale(detector);
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            onScaleStart= scale;
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            onScaleEnd= scale;
            super.onScaleEnd(detector);
        }
    }
}
