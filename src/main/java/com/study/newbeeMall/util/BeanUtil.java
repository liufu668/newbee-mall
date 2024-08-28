package com.study.newbeeMall.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
public abstract class BeanUtil {

    /**
     * 将源对象的属性复制到目标对象。
     *
     * @param source 源对象，属性将从此对象复制。
     * @param target 目标对象，属性将被复制到此对象。
     * @param ignoreProperties 可选，复制过程中需要忽略的属性。
     * @return 目标对象，属性已被复制。
     */
    public static Object copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null) {
            return target;
        }
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    /**
     * 将源对象列表复制到指定类型的目标对象列表。
     *
     * @param sources 源对象列表，将从中复制属性。
     * @param clazz 目标对象的类类型。
     * @param <T> 目标对象的类型。
     * @return 包含复制了属性的目标对象的列表。
     */
    public static <T> List<T> copyList(List sources, Class<T> clazz) {
        return copyList(sources, clazz, null);
    }

    /**
     * 将源对象列表复制到指定类型的目标对象列表，并可选择性地应用回调处理。
     *
     * @param sources 源对象列表，将从中复制属性。
     * @param clazz 目标对象的类类型。
     * @param callback 可选，复制过程中用于额外处理的回调。
     * @param <T> 目标对象的类型。
     * @return 包含复制了属性的目标对象的列表。
     */
    public static <T> List<T> copyList(List sources, Class<T> clazz, Callback<T> callback) {
        List<T> targetList = new ArrayList<>();
        if (sources != null) {
            try {
                for (Object source : sources) {
                    T target = clazz.newInstance();
                    copyProperties(source, target);
                    if (callback != null) {
                        callback.set(source, target);
                    }
                    targetList.add(target);
                }
            } catch (InstantiationException e) {
                e.printStackTrace(); // 记录或处理异常
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // 记录或处理异常
            }
        }
        return targetList;
    }

    /**
     * 将 bean 对象转换为属性名和值的映射。
     *
     * @param bean 要转换的 bean 对象。
     * @param ignoreProperties 可选，在转换过程中需要忽略的属性。
     * @return 一个 map，键为属性名，值为属性值。
     */
    public static Map<String, Object> toMap(Object bean, String... ignoreProperties) {
        Map<String, Object> map = new LinkedHashMap<>();
        List<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("class"); // 忽略 "class" 属性，因为它不相关
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        for (PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()) {
            if (!ignoreList.contains(pd.getName()) && beanWrapper.isReadableProperty(pd.getName())) {
                Object propertyValue = beanWrapper.getPropertyValue(pd.getName());
                map.put(pd.getName(), propertyValue);
            }
        }
        return map;
    }

    /**
     * 将属性名和值的映射转换为指定类型的 bean 对象。
     *
     * @param map 属性名和值的映射。
     * @param beanType 要创建的 bean 对象的类类型。
     * @param <T> bean 对象的类型。
     * @return 一个 bean 对象，属性已从映射中设置。
     */
    public static <T> T toBean(Map<String, Object> map, Class<T> beanType) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(beanType);
        map.forEach((key, value) -> {
            if (beanWrapper.isWritableProperty(key)) {
                beanWrapper.setPropertyValue(key, value);
            }
        });
        return (T) beanWrapper.getWrappedInstance();
    }

    /**
     * 回调接口，用于在列表复制操作过程中进行额外处理。
     *
     * @param <T> 目标对象的类型。
     */
    public static interface Callback<T> {
        /**
         * 执行额外处理的回调方法。
         *
         * @param source 源对象。
         * @param target 目标对象。
         */
        void set(Object source, T target);
    }

    /**
     * 检查给定对象的所有字段是否都不为 null。
     *
     * @param o 要检查的对象。
     * @param clz 对象的类类型。
     * @return 如果所有字段都不为 null，则返回 true，否则返回 false。
     */
    public static boolean checkPojoNullField(Object o, Class<?> clz) {
        try {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(o) == null) {
                    return false;
                }
            }
            if (clz.getSuperclass() != Object.class) {
                return checkPojoNullField(o, clz.getSuperclass());
            }
            return true;
        } catch (IllegalAccessException e) {
            return false; // 如果发生访问错误，假定检查失败
        }
    }
}
