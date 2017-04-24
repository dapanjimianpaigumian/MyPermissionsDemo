package com.yulu.zhaoxinpeng.mypermissionsdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {

    //定义一个requestcode
    private static final int CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCall();
            }
        });
    }

    // 1. 检测清单的权限有没有授权成功
    private void testCall() {
        /**
         * 检测权限：
         * 返回值：
         * PERMISSION_GRANTED 授权已经成功
         * PERMISSION_DENIED 授权被拒绝
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // 清单文件虽然添加了权限，但是授权没有成功，需要向用户申请权限
            // 2. 向用户申请权限:可以申请多个，都可以在String[] 里面加上
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);

        } else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            //如果清单授权成功
            callPhone();
        }
    }

    // 3. 处理权限的申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, "拒绝授权", Toast.LENGTH_SHORT).show();
                // 额外的处理：能不能在用户完全被拒绝的时候，友好的提示一下
                // 4. 彻底被拒绝，不会再展示权限申请的对话框信息，我们自己写个提示一下
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
                    showDialog();
                }
            }
        }
    }

    // 显示的提示信息：提醒用户去更改权限设置
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setMessage("权限被彻底拒绝，可以到设置页面打开，才可以使用此功能")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到本应用的设置页面，可以打开权限信息
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .show();
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri parse = Uri.parse("tel:" + "1008611");
        intent.setData(parse);
        startActivity(intent);
    }
}
