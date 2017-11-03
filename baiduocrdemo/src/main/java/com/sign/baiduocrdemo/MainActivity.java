package com.sign.baiduocrdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.ui.camera.CameraActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText edtName, edtLocation, edtPhone, edtNumber;

    private static final int REQUEST_CODE_CAMERA = 1000;
    private static final int REQUEST_CODE_PICK_IMAGE = 2000;

    private String path = new String();
    private TextView txtResult;//返回的数据
    private List<String> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission();
    }

    private void initView() {
        txtResult = (TextView) findViewById(R.id.txt_result);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtLocation = (EditText) findViewById(R.id.edt_locaiton);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtNumber = (EditText) findViewById(R.id.edt_number);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.baidu.ocr.ui.camera.CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int ret = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                            .READ_EXTERNAL_STORAGE);
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1000);
                        return;
                    }
                }
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int ret = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (ret == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        100);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            path = filePath;
            initData();
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            path = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
            initData();
        }
    }

    private void initData() {
        OCR.getInstance()
                .initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {
                        OCR.getInstance().initWithToken(getApplicationContext(), result.getAccessToken());
                        getData();
                    }

                    @Override
                    public void onError(OCRError error) {
                    }
                }, getApplicationContext(), "Your API Key", "Your Secret Key");
    }

    private void getData() {
        GeneralParams param = new GeneralParams();
        param.setImageFile(new File(path));
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                for (Word word : result.getWordList()) {
                    sb.append(word.getWords());
                    resultList.add(word.getWords());
                    sb.append("\n");
                }
                txtResult.setText(sb);
                setData();
            }

            @Override
            public void onError(OCRError ocrError) {
                txtResult.setText(ocrError.getErrorCode() + "  " + ocrError.getMessage());
            }
        });
    }

    //设置数据
    private void setData() {
        for (String str : resultList) {
            if (str.length() < 4) {
                edtName.setText(str);
                continue;
            }
            switch (isExpress(str)) {
                case "express":
                    edtNumber.setText(str);
                    break;
                case "phone":
                    edtPhone.setText(str);
                    break;
                default:
                    break;
            }
            if (str.contains("电话") && (str.length() >= 9)) {
                edtPhone.setText(str.substring(str.indexOf("话") + 1));
            }
            if (str.contains("省") || str.contains("市") || str.contains("区") || str.contains("县") || str.contains("镇")) {
                edtLocation.setText(str);
            }
        }
    }


    /**
     * 1.字符串只包含字母和数字即为快递单号
     * 2.字符串包含数字个数大于7，判断首个字符若是0或1且为11位数字或数字个数为7、8为电话号码或者固定电话，否则为快递单号
     */
    public String isExpress(String str) {
        int number = 0;//字符串中包含数字个数
        char[] characterArr = str.toCharArray();
        boolean flag = true;//标志是否只包含或字母
        boolean flagNumber = true;//标志只包含数字
        for (char c : characterArr) {
            if (isNumber(c)) {
                ++number;
            }
            if (!isNumber(c)) {
                flagNumber = false;
            }
            if (!isNumber(c) || isLetter(c)) {
                flag = false;
            }
        }
        if (number >= 7) {
            if (flagNumber) {
                if (number == 7 || number == 8 || str.charAt(0) == 48 || str.charAt(0) == 49 && str.length() == 11) {
                    return "phone";
                } else {
                    return "express";
                }
            } else {
                if (flag) {
                    return "express";
                }
            }
        }
        return "";
    }

    //字符是否是数字
    public boolean isNumber(char c) {
        if (c >= 48 && c <= 57) {
            return true;
        }
        return false;
    }

    //字符是否是字母
    public boolean isLetter(char c) {
        if (c >= 65 && c <= 90 || c >= 97 && c <= 122) {
            return true;
        }
        return false;
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
