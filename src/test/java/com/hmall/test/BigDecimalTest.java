package com.hmall.test;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by @Author tachai on 2017/9/2.
 *
 * @Email 1206966083@qq.com
 */
public class BigDecimalTest {
    @Test
    public void test1(){
        System.out.println(0.05+0.01);
    }
    @Test
    public void test2(){
        BigDecimal b1=new BigDecimal("0.05");
        BigDecimal b2=new BigDecimal("0.01");
        System.out.println(b1.add(b2));
    }
}
