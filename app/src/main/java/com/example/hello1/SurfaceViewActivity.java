package com.example.hello1;


import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    Button camera_btn;
    SurfaceView camera_sf;
    //安卓硬件相机
    private Camera mCamera;
    private SurfaceHolder mHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.surface);
        camera_btn = findViewById(R.id.camera_btn);
        camera_sf = findViewById(R.id.camera_sf);
        Log.d("TAG", "打印验证");
        initViews();
        //  MyView myView = new MyView(this);
    }

    private void initViews() {

        this.camera_btn.setOnClickListener(this);
        camera_sf.setOnClickListener(this);
        //获取SurfaceView的SurfaceHolder对象
        mHolder = camera_sf.getHolder();
        //实现SurfaceHolder.Callback接口
        mHolder.addCallback(this);




    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
            //释放相机资源
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == mCamera) {
            mCamera = getCustomCamera();
            if (mHolder != null) {
                //开户相机预览
                previceCamera(mCamera, mHolder);
            }
        }
    }

    private Camera getCustomCamera() {
        if (null == mCamera) {
            //使用Camera的Open函数开机摄像头硬件
            mCamera = Camera.open();
            //Camera.open()方法说明：2.3以后支持多摄像头，所以开启前可以通过getNumberOfCameras先获取摄像头数目，
            // 再通过 getCameraInfo得到需要开启的摄像头id，然后传入Open函数开启摄像头，
            // 假如摄像头开启成功则返回一个Camera对象
        }
        return mCamera;
    }

    private void previceCamera(Camera camera, SurfaceHolder holder) {
        try {
            //获取到相机参数
            Camera.Parameters parameters = mCamera.getParameters();
            //设置图片保存格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            //设置图片大小
            parameters.setPreviewSize(480, 720);
            //设置对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

            //摄像头设置SurfaceHolder对象，把摄像头与SurfaceHolder进行绑定
            camera.setPreviewDisplay(holder);
            //调整系统相机拍照角度
            camera.setDisplayOrientation(90);
            //调用相机预览功能
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        previceCamera(mCamera, holder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        previceCamera(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
            //释放相机资源
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_sf://点击可以对焦
                if (null != mCamera)
                    mCamera.autoFocus(null);
                break;
            case R.id.camera_btn://点击进行拍照
                startTakephoto();
                break;
        }
    }

    private void startTakephoto() {

        //设置自动对焦
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (!success)   Log.d("success","success");
                if (success) {
                    mCamera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");

                            dealWithCameraData(data);
                        }
                    });
                }
            }
        });
    }

    //保存拍照数据
    private void dealWithCameraData(byte[] data) {
        FileOutputStream fos = null;
        String tempStr = "/sdcard/";
        //图片临时保存位置
        String fileName = tempStr + System.currentTimeMillis() + ".jpg";
        File tempFile = new File(fileName);
        try {
            fos = new FileOutputStream(fileName);
            //保存图片数据
            fos.write(data);
            fos.close();
            Intent intent = new Intent();
            intent.putExtra(FILE_PATH, fileName);
            setResult(500, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String FILE_PATH = "filePath";
}
