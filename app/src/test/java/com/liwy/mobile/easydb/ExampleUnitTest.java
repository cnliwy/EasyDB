package com.liwy.mobile.easydb;

import com.liwy.mobile.easydb.annotation.Table;
import com.liwy.mobile.easydb.bean.Student;

import org.junit.Test;

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
    public void testNull(){
        Student student = new Student();
        System.out.println("float:" + student.ageFloat);
        System.out.println("double:" + student.ageDouble);
        System.out.println("long:" + student.ageLong);
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
}