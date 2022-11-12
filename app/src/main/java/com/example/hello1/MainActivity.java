package com.example.hello1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView textView = findViewById(R.id.tv);
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
        if (EasyPermissions.hasPermissions(this,PERMS)) {
            Toast.makeText(this, "应用获得相机权限", Toast.LENGTH_LONG).show();

            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 照相机拍照
            // 需要说明一下，以下操作使用照相机拍照，
            // 拍照后的图片会存放在相册中的,这里使用的这种方式有一个好处就是获取的图片是拍照后的原图，
            // 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
            ContentValues values = new ContentValues();
            Uri photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, RC_TAKE_PHOTO);


        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "给个相机权限吧~",
                    PERMISSION_REQUEST_CAMERA,
                    PERMS);
        }
    }


}