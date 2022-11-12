package com.example.hello1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
        NewClass newClass=new NewClass();
        newClass.setI(50);
      int i=  newClass.getI();
    }
    public void test(){

    }
    public void hello(){

    }
}