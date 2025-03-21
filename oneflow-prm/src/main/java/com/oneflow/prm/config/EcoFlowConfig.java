package com.oneflow.prm.config;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: jin
 * @create: 2023-09-03 15:36
 **/
@Configuration
public class oneflowConfig {

    // 可以从配置文件yml文件获取
    public List<String> getRoleTenantTables() {
        List<String> roleDataScopeList = new ArrayList<>();
        roleDataScopeList.add("prm_customer");
        roleDataScopeList.add("prm_order");
        return roleDataScopeList;
    }

}
