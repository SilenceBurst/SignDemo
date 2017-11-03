package com.sign.camerademo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * 项目名:    CameraDemo
 * 包名       com.sign.camerademo
 * 文件名:    Utils
 * 创建者:    CYS
 * 创建时间:  2017/10/9 0009 on 15:28
 * 描述:     TODO
 */
public class Utils {
    /**
     * 创建一个临时图片文件 防止拍照后得到的图片自动压缩
     */
    public static File newTempImageFile() {
        return new File(Environment.getExternalStorageDirectory() + File.separator + "CameraDemo" + File.separator, System.currentTimeMillis() + ".png");
    }

    /**
     * 调用拍照
     */
    public static void takeCamera(Context activity, Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (activity instanceof Activity) {
            ((Activity) activity).startActivityForResult(intent, Constant.CAMERA);
        } else {
            ((FragmentActivity) activity).startActivityForResult(intent, Constant.CAMERA);
        }
    }

    /**
     * 调用相册程序
     *
     * @param context
     */
    public static void takeGallery(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            ((Activity) context).startActivityForResult(intent, Constant.GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "没有找到相册", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据Uri得到绝对地址
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getPathFromUri(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } else {
            return contentUri.getPath();
        }
        return res;
    }

    /**
     * 将压缩后的图片保存在本地
     *
     * @param smallBitmap
     * @return
     */
    public static File saveBitmap(Bitmap smallBitmap) {
        File newFile = newTempImageFile();
        try {
            FileOutputStream out = new FileOutputStream(newFile);
            smallBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e("TAG", e.toString() + "---保存文件");
        }
        return newFile;
    }

    /**
     * 图片压缩
     *
     * @param filePath 图片路径
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // 计算高度
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 解码与inSampleSize设置位图
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            //30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    /**
     * 压缩图片
     *
     * @param options   参数配置
     * @param reqWidth  宽度
     * @param reqHeight 高度
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

}
