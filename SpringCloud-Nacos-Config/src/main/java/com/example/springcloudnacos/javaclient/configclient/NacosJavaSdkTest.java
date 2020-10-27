package com.example.springcloudnacos.javaclient.configclient;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.Executor;

public class NacosJavaSdkTest {

    private static String NACOS_SERVER_ADDR;
    private static String NACOS_CONFIG_DATAID;
    private static String NACOS_CONFIG_GROUP;

    //从bootstrap.properties中获取nacos配置
    static {
        ResourceLoader rl = new PathMatchingResourcePatternResolver();
        Resource res = rl.getResource("classpath:/bootstrap.properties");
        Properties props = new Properties();
        try {
            props.load(res.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        NACOS_SERVER_ADDR = props.getProperty("spring.cloud.nacos.config.server-addr", "27.0.0.1:8848");
        NACOS_CONFIG_DATAID = props.getProperty("spring.application.name", "example");
        NACOS_CONFIG_GROUP = props.getProperty("spring.cloud.nacos.config.group", "DEFAULT_GROUP");
    }

    /**
     * 用于服务启动的时候从 Nacos 获取配置。
     */
    public static void getConfig() {
        try {
            String serverAddr = NACOS_SERVER_ADDR;
            String dataId = NACOS_CONFIG_DATAID;
            String group = NACOS_CONFIG_GROUP;
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println("Nacos config content: " + content);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果希望 Nacos 推送配置变更，可以使用 Nacos 动态监听配置接口来实现。
     */
    public static void addListener(Listener listener) {
        try {
            String serverAddr = NACOS_SERVER_ADDR;
            String dataId = NACOS_CONFIG_DATAID;
            String group = NACOS_CONFIG_GROUP;
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 3000);
            System.out.println("addListener method Nacos config content: " + content);
            configService.addListener(dataId, group, listener);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消监听配置，取消监听后配置不会再推送。
     * 测试好像没有效果
     */
    public static void removeListener(Listener listener) {
        try {
            String serverAddr = NACOS_SERVER_ADDR;
            String dataId = NACOS_CONFIG_DATAID;
            String group = NACOS_CONFIG_GROUP;
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 3000);
            configService.removeListener(dataId, group, listener);
        } catch (NacosException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
//        getConfig();


//        addListener(new myListener());
//        while (true) {// 测试让主线程不退出，因为订阅配置是守护线程，主线程退出守护线程就会退出。 正式代码中无需下面代码
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        Listener listener = new myListener();
        addListener(listener);
        int count = 0;
        while (true) {
            if (count == 10) {
                System.out.println("remove nacos config listener");
                removeListener(listener);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
    }
}

class myListener implements Listener {

    @Override
    public Executor getExecutor() {
        return null;
    }
    @Override
    public void receiveConfigInfo(String configInfo) {
        System.out.println("recieve by listener: \n" + configInfo);
    }

}
