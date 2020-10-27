/*
 * Copyright (c) 2015-2030 GantSoftware.Co.Ltd. All rights reserved.
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * is not allowed to be distributed or copied without the license from
 * GantSoftware.Co.Ltd. Please contact the company for more information.
 */

package com.example.springcloudnacos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author jiangls
 * @Date 2020/10/23 11:27
 */
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Value("${server.undertow.io-threads:1}")
    private Integer ioThreads;

    @RequestMapping("/useLocalCache")
    public boolean useLocalCache() {
        return useLocalCache;
    }

    @RequestMapping("/ioThreads")
    public Integer ioThreads() {
        return ioThreads;
    }
}
