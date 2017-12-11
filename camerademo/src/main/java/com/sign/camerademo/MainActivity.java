package com.sign.camerademo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView imageView;
    private RxPermissions rxPermissions;
    private File mFile;
    private File newFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        imageView = (ImageView) findViewById(R.id.imageView);
        rxPermissions = new RxPermissions(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.GALLERY://系统相册数据回调
                if (data != null) {
                    Uri imageCaiUri = data.getData();
                    String path = Utils.getPathFromUri(this, imageCaiUri);
                    mFile = new File(path);
                    uploadImage(path);
                }
                break;
            case Constant.CAMERA://拍照回调
                if (resultCode == RESULT_OK) {
                    String path = mFile.getPath();
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }
                    uploadImage(path);
                }
                break;
            default:
                break;
        }
    }

    private void uploadImage(String path) {
        //图片压缩
        Bitmap smallBitmap = Utils.getSmallBitmap(path);
        //删除拍照的文件
        if (mFile != null && mFile.exists()) {
            Toast.makeText(this, "原图片大小" + mFile.length(), Toast.LENGTH_LONG).show();
//            mFile.delete();
        }
        //保存压缩后的图片
        newFile = Utils.saveBitmap(smallBitmap);
        Toast.makeText(this, "压缩后图片大小" + newFile.length(), Toast.LENGTH_LONG).show();
        Glide.with(MainActivity.this).load(newFile).into(imageView);
        //模拟上传图片
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //上传成功
                dialog.dismiss();
                newFile.delete();
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //拍照
            case R.id.menu_camera:
                requestCamera();
                break;
            //相册
            case R.id.menu_gallery:
                requestGallery();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //请求相册相关权限
    private void requestGallery() {
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            //调用相册
                            Utils.takeGallery(mContext);
                        } else {
                            // 未获取权限
                            Toast.makeText(MainActivity.this, "请前往设置中心允许权限！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //请求相机相关权限
    private void requestCamera() {
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            //http://blog.csdn.net/d231123/article/details/49178987
                            //因为miui系统没有自动创建那个文件夹，所以拍照没有找到那个文件夹，就卡起了，
                            // 解决办法先判断文件夹是否存在，如果不存在，就先创建文件夹。再执行后面的方法就OK了！
                            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "CameraDemo");
                            if (!file.exists()) {
                                file.mkdir();
                            }

                            mFile = Utils.newTempImageFile();
                            Uri imageUri;
                            if (Build.VERSION.SDK_INT < 24) {
                                imageUri = Uri.fromFile(mFile);
                            } else {
                                //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                                //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
                                imageUri = FileProvider.getUriForFile(mContext, "com.sign.camerademo.fileprovider", mFile);
                            }
                            //调用相机
                            Utils.takeCamera(mContext, imageUri);
                        } else {
                            // 未获取权限
                            Toast.makeText(MainActivity.this, "请前往设置中心允许权限！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
}