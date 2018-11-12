package com.brother6.aop.parse.permission;

import android.util.Log;

import com.brother6.aop.PermissionInterface;
import com.brother6.aop.anotation.permission.PermissionFailed;
import com.brother6.aop.anotation.permission.PermissionSuccess;
import com.brother6.aop.anotation.permission.RequestPermissons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PermissionParse {

    private DefaultPermissionInterface permissionInterface = new DefaultPermissionInterface();
    //TODO 注意 避免内存泄露
    private Object obj;

    public  void bind(Object obj) {
        this.obj = obj;
        Class clazz = (obj).getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field field = checkFields(fields, RequestPermissons.class);
        parseAnno(obj,field,RequestPermissons.class);

        Method methodRequestSuccess = checkMethods(clazz.getMethods(),PermissionSuccess.class);
        permissionInterface.setSuccessMethod(methodRequestSuccess);

        Method methodRequestFailed = checkMethods2(clazz.getMethods(),PermissionFailed.class);
        permissionInterface.setFailedMethod(methodRequestFailed);
    }

    /**
     * 解析注解中的值
     */
    public void parseAnno(Object obj, Field field, Class<RequestPermissons> requestPermissonsClass) {
        field.setAccessible(true);
        RequestPermissons permissonAnno = field.getAnnotation(requestPermissonsClass);
        permissionInterface.setRequestCode(permissonAnno.requestCode());
        try {
            Object o = field.get(obj);
            permissionInterface.setPermisstions((String[]) o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Field checkFields(Field[] fields, Class<RequestPermissons> requestPermissonsClass) {

        if (fields == null || fields.length == 0) {
            //TODO 抛出异常
            throw new RuntimeException("please check the annotation class ， it don't " +
                    "has any fields");
        }

        Field result = null;
        for (Field field:
             fields) {
            if (field.getAnnotation(requestPermissonsClass) != null) {
                if (result != null) {
                    throw new RuntimeException("please don't use the anntation" +
                            "int the sample class");
                }
                result = field;
            }
        }
        return result;
    }

    /**
     * 检擦使用注解的合法性
     * @param methods
     * @param requestPermissonsClass
     * @return
     */
    private Method checkMethods(Method[] methods, Class<PermissionSuccess> requestPermissonsClass) {

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

    private Method checkMethods2(Method[] methods, Class<PermissionFailed> requestPermissonsClass) {

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



    private class DefaultPermissionInterface implements PermissionInterface {

        private int requestCode;

        private String[] permisstions;

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

