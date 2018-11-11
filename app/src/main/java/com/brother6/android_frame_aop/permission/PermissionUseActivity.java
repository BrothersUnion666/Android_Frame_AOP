package com.brother6.android_frame_aop.permission;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.brother6.android_frame_aop.R;
import java.util.ArrayList;
import java.util.List;

public class PermissionUseActivity extends AppCompatActivity {

    List<PhoneBean> phoneDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_use);
        initView();
    }

    private void initView() {
        TextView tv_read_contact = findViewById(R.id.tv_read_contact);
        tv_read_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoneContact();
            }
        });
    }

    private void loadPhoneContact() {
        new Thread(){
            @Override
            public void run() {
                getPhoneNumberFromMobile();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateContact();
                    }
                });
            }
        }.start();
    }


    public List<PhoneBean> getPhoneNumberFromMobile() {
        phoneDatas.clear();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[] { "display_name", "sort_key", "contact_id",
                        "data1" }, null, null, null);
//        moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int Id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//            String Sortkey = getSortkey(cursor.getString(1));
//            PhoneInfo phoneInfo = new PhoneInfo(name, number,Sortkey,Id);
//            list.add(phoneInfo);
            PhoneBean phoneBean = new PhoneBean();
            phoneBean.setName(name);
            phoneBean.setPhoneNumber(number);
            phoneDatas.add(phoneBean);
        }
        cursor.close();
        return phoneDatas;
    }

    private void updateContact() {
        Toast.makeText(this, "dataSize = " + phoneDatas.size(), Toast.LENGTH_LONG);
    }


}
