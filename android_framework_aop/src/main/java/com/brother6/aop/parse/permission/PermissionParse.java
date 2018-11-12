package com.brother6.aop.parse.permission;

import android.util.Log;

import com.brother6.aop.business.permission.PermissionInterface;
import com.brother6.aop.anotation.permission.PermissionFailed;
import com.brother6.aop.anotation.permission.PermissionSuccess;
import com.brother6.aop.anotation.permission.RequestPermissons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PermissionParse extends BaseAnnoParse{

    private DefaultPermissionInterface permissionInterface = new DefaultPermissionInterface();
    //TODO 注意 避免内存泄露
    private Object obj;

    public  void bind(Object obj) {
        this.obj = obj;
        Class clazz = (obj).getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field field = getFieldByAnnotation(fields, RequestPermissons.class);

        //初始化需要申请的权限的值
        initRequestPermissionValue(obj,field,RequestPermissons.class);

        //获取 权限请求成功时的回调方法
        Method requestSuccessCallbackMethod = getSuccessCallbackMethod(clazz,PermissionSuccess.class);
        //设置 权限请求成功的回调方法
        permissionInterface.setSuccessMethod(requestSuccessCallbackMethod);

        //获取 权限请求失败的回调方法
        Method requestFailedCallbackMethod = getFailedCallbackMethod(clazz,PermissionFailed.class);

        //设置 权限请求失败的回调方法
        permissionInterface.setFailedMethod(requestFailedCallbackMethod);
    }

    /**
     *设置请求权限的值
     */
    public void initRequestPermissionValue(Object obj, Field field, Class<RequestPermissons> requestPermissonsClass) {
        field.setAccessible(true);
        RequestPermissons permissonAnno = field.getAnnotation(requestPermissonsClass);
        permissionInterface.setRequestCode(permissonAnno.requestCode());
        try {
            Object o = field.get(obj);
            if (o instanceof String[]) {
                permissionInterface.setPermisstions((String[]) o);
            } else {
                throw new RuntimeException("please make sure the annotation \"RequestPermissons\" has been" +
                        "used on the field whose type is String[]");
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    /**
     * 检擦使用注解的合法性
     * @param clazz
     * @param requestPermissonsClass
     * @return
     */
    private Method getSuccessCallbackMethod(Class clazz, Class<PermissionSuccess> requestPermissonsClass) {
        //TODO 注意这里只是 查找当前字节码， 实际需要遍历所有字节码
        Method[] methods = clazz.getMethods();

        if (methods == null || methods.length == 0) {
            //TODO 抛出异常
            throw new RuntimeException("please check the annotation class ， it don't " +
                    "has any method");
        }

        Method result = null;
        for (Method method:
                methods) {
            if (method.getAnnotation(requestPermissonsClass) != null) {
                PermissionSuccess permissionSuccess = method.getAnnotation(requestPermissonsClass);
                if (permissionSuccess.requestCode() == permissionInterface.getRequestCode()) {
                    result = method;
                }
            }
        }
        return result;
    }

    private Method getFailedCallbackMethod(Class clazz, Class<PermissionFailed> requestPermissonsClass) {
        //TODO 注意这里只是 查找当前字节码， 实际需要遍历所有字节码
        Method[] methods = clazz.getMethods();
        if (methods == null || methods.length == 0) {
            //TODO 抛出异常
            throw new RuntimeException("please check the annotation class ， it don't " +
                    "has any method");
        }

        Method result = null;
        for (Method method:
                methods) {
            if (method.getAnnotation(requestPermissonsClass) != null) {
                PermissionFailed permissionFailed = method.getAnnotation(requestPermissonsClass);
                if (permissionFailed.requestCode() == permissionInterface.getRequestCode()) {
                    result = method;
                }
            }
        }
        return result;
    }


    /**
     * 请求权限后的回调接口， 通过内部应用的方式实现
     */
    private class DefaultPermissionInterface implements PermissionInterface {

        /**
         * 请求码，用来确保 请求的权限和回调是一一对应的
         */
        private int requestCode;

        /**
         * 要申请的权限
         */
        private String[] permisstions;

        /**
         * 申请权限后，返回的接口 （通过反射执行回去）
         */
        private Method methodRequestSuccess;
        private Method methodRequestFailed;

        @Override
        public int getPermissionsRequestCode() {
            return requestCode;
        }

        @Override
        public String[] getPermissions() {
            return permisstions;
        }

        @Override
        public void requestPermissionsSuccess() {
            if (methodRequestSuccess != null) {
                try {
                    methodRequestSuccess.setAccessible(true);
                    methodRequestSuccess.invoke(obj);
                } catch (Exception e) {
                    Log.e("canjun", "success excute failed");
                }
            }
        }

        @Override
        public void requestPermissionsFail() {
            if (methodRequestFailed != null) {
                try {
                    methodRequestSuccess.setAccessible(true);
                    methodRequestFailed.invoke(obj);
                } catch (Exception e) {
                    Log.e("canjun", "success excute failed");
                }
            }
        }


        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public void setPermisstions(String[] permisstions) {
            this.permisstions = permisstions;
        }

        public void setSuccessMethod(Method methodRequestSuccess) {
            this.methodRequestSuccess = methodRequestSuccess;
        }
        public void setFailedMethod(Method methodRequestFailed) {
            this.methodRequestFailed = methodRequestFailed;
        }

        public int getRequestCode() {
            return requestCode;
        }
    }

    public DefaultPermissionInterface getPermissionInterface() {
        return permissionInterface;
    }
}

