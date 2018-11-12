package com.brother6.aop.parse.permission;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


/**
 * 注解基本解析的基类类
 */
public class BaseAnnoParse {


    /**
     * 根据注解获取获被注解的成员属性
     * @param fields
     * @param requestPermissonsClass
     * @param <T>
     * @return
     */
    protected <T extends Annotation> Field getFieldByAnnotation(Field[] fields, Class<T> requestPermissonsClass) {

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
}
