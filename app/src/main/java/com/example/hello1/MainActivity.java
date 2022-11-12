package com.example.hello1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int RC_TAKE_PHOTO = 200;
    String[] PERMS = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button textView = findViewById(R.id.tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomethingWithCamera();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果传递给EasyPermission
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CAMERA)
    public void doSomethingWithCamera() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            Toast.makeText(this, "应用获得相机权限", Toast.LENGTH_LONG).show();


            // photo();
            Intent intent = new Intent(this, SurfaceViewActivity.class);
            startActivityForResult(intent,RC_TAKE_PHOTO);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "给个相机权限吧~", PERMISSION_REQUEST_CAMERA, PERMS);
        }
    }

    public void photo() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 照相机拍照
        // 需要说明一下，以下操作使用照相机拍照，
        // 拍照后的图片会存放在相册中的,这里使用的这种方式有一个好处就是获取的图片是拍照后的原图，
        // 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
        startActivityForResult(intent, RC_TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("hlj", "test");
        if (requestCode == RC_TAKE_PHOTO && resultCode ==500) {
            //  Log.d("hlj",requestCode+"---返回码："+resultCode+"----"+data.getExtras().getString((MediaStore.EXTRA_OUTPUT)));
            String filePath = getIntent().getStringExtra(SurfaceViewActivity.FILE_PATH);


            if (filePath!="") {
                try {
                    FileInputStream fis = new FileInputStream(filePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    Matrix matrix = new Matrix();
                    //通过Matrix把图片旋转90度
                    matrix.setRotate(90);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    ImageView iv=findViewById(R.id.iv);
                    iv.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }



        }
    }
}