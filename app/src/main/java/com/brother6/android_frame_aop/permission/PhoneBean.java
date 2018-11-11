package com.brother6.android_frame_aop.permission;

import android.text.TextUtils;

/**
 *
 * created by zfc on 2018/9/17 14:02
 */
public class PhoneBean {

    private String name;

    /**
     * 获取手机联系人的数据属性
     */
    private String phoneNumber;


    public PhoneBean setName(String name) {
        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name + "--" + phoneNumber;
    }



    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
