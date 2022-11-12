package com.example.hello1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity implements View.OnTouchListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  SurfaceGesturePath surfaceGesturePath = new SurfaceGesturePath(this);
        // setContentView(surfaceGesturePath);

        //  setContentView(R.layout.myview);
        View inflater = LayoutInflater.from(this).inflate(R.layout.myview, null, false);
        Button button = inflater.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("AAAAA", "DDDDDDDDDDDDDDDDDDDDDDDDD");
            }
        });
        surfaceGesturePath = inflater.findViewById(R.id.su);
      //  surfaceGesturePath.setOnTouchListener(this);
        setContentView(inflater);
    }

    SurfaceGesturePath surfaceGesturePath;

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        surfaceGesturePath.onTouchEvent(event);
        return false;
    }
}
