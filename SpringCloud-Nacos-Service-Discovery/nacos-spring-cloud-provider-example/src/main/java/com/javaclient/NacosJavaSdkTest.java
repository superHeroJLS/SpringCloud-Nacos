package com.javaclient;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;

public class NacosJavaSdkTest {

    public static String nacosServerAddr = "127.0.0.1:8848";
    public static String nacosServiceName= "service-consumer";

    /**
     * 获取服务下的所有实例
     */
    public static void getAllInstances() {
        try {
            NamingService naming = NamingFactory.createNamingService(nacosServerAddr);
            System.out.println(naming.getAllInstances(nacosServiceName));
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听服务下的实例列表变化。
     */
    public static void subscribe() {
        NamingService naming = null;
        try {
            naming = NamingFactory.createNamingService(nacosServerAddr);
            naming.subscribe(nacosServiceName, event -> {
                if (event instanceof NamingEvent) {
                    System.out.println("service name: " + ((NamingEvent) event).getServiceName());
                    System.out.println("instance name: " + ((NamingEvent) event).getInstances());
                    System.out.println("group name: " + ((NamingEvent) event).getGroupName());
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消监听服务下的实例列表变化
     * 测试之后好像也没有效果
     */
    public static void unsubscribe() {
        NamingService naming = null;
        try {
            naming = NamingFactory.createNamingService(nacosServerAddr);
            naming.subscribe(nacosServiceName, event -> {});
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        getAllInstances();

//        subscribe();
//        while (true) {// 测试让主线程不退出，因为订阅配置是守护线程，主线程退出守护线程就会退出。 正式代码中无需下面代码
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


        subscribe();
        int i = 0;
        while (true) {
            if (i == 10)
                System.out.println("unsubscribe");
                unsubscribe();//取消订阅
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }



    }
}
