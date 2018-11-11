package com.brother6.android_frame_aop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.brother6.android_frame_aop.permission.PermissionUseActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 跳转到权限使用界面
     * @param view
     */
    public void toPermissionPage(View view) {
        Intent intent = new Intent(this, PermissionUseActivity.class);
        startActivity(intent);
    }
}
