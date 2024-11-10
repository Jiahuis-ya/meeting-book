package com.itheima;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    @Test
    public void testThreadLocalSetAndGet(){
        //提供一个ThreadLocal对象
        ThreadLocal tl = new ThreadLocal();

        //开启两个线程  new Thread(线程任务，线程名字)
        new Thread(()->{
            tl.set("萧炎");
            System.out.println(Thread.currentThread().getName()+": "+tl.get());// 萧炎
            System.out.println(Thread.currentThread().getName()+": "+tl.get());// 萧炎
            System.out.println(Thread.currentThread().getName()+": "+tl.get());// 萧炎
        },"蓝色").start();

        new Thread(()->{
            tl.set("药尘");
            System.out.println(Thread.currentThread().getName()+": "+tl.get());// 药尘
            System.out.println(Thread.currentThread().getName()+": "+tl.get());// 药尘
            System.out.println(Thread.currentThread().getName()+": "+tl.get());// 药尘
        },"绿色").start();
    }
}
