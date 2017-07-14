package com.easydb.mobile.easydb;

import com.easydb.mobile.easydb.bean.House;
import com.easydb.mobile.easydb.bean.User;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSB()throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append("admin");
        System.out.println(sb.length());
        System.out.println(sb.substring(0,sb.length()-3));
        sb = new StringBuffer(sb.substring(0,sb.length()-3));
        System.out.println(sb.toString());
    }



    @Test
    public void testFinally(){
        System.out.println("成功获取：" + getString());
    }

    public static String getString(){
        String a = "123456";
        try {
            System.out.println(a.toString());
            return a;
        } catch (Exception e) {
            System.out.println("抓到了异常");
            e.printStackTrace();
        }finally {
            System.out.println("裁决异常");
        }
        System.out.println("执行结束");
        return a;
    }


    @Test
    public void testType(){
        Field[] fields = House.class.getDeclaredFields();
        for (Field field : fields){
            System.out.println("getGenericType:" + field.getGenericType().toString() + ",class:" + field.getType());
            Type type = field.getGenericType();
            System.out.println(ParameterizedType.class.toString());
            if ((type instanceof ParameterizedType))
            {
                ParameterizedType pType = (ParameterizedType)field.getGenericType();
                System.out.println(pType.toString());
                if (pType.getActualTypeArguments().length == 1)
                {
                    Class<?> pClazz = (Class)pType.getActualTypeArguments()[0];
                    if (pClazz != null) {
//                        otm.setOneClass(pClazz);
                        System.out.println("[0] class不为空：" + pClazz.getName());
                    }
                }
                else
                {
                    Class<?> pClazz = (Class)pType.getActualTypeArguments()[1];
                    if (pClazz != null) {
                        System.out.println("[1] class不为空：" + pClazz.getName());
                    }
                }
            }
        }
    }


    @Test
    public void testObject(){
        User user = new User(1,"lisan",6);
        System.out.println("设置前打印=" + user.toString());
        setUserHouseId(user,8);
        System.out.println("设置后打印=" + user.toString());
    }

    public void setUserHouseId(User user,int houseId){
        user.setHouseId(houseId);
        System.out.println("设置中打印=" + user.toString());
    }
}