package com.oneflow.prm.service.component.activiti;

import com.oneflow.prm.core.enums.ActivitiTypeEnums;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApprovalFactory {

    private final ConcurrentHashMap<ActivitiTypeEnums.CatalogEnum, ApprovalHandler> approvalHandlerMap = new ConcurrentHashMap<>();

    @Resource
    public void setApprovalService(List<ApprovalHandler> approvalHandlerList) {
        /**
         * approvalHandlerList 参数是从 Spring 的 Bean 工厂中获取的。具体来说，ApprovalFactory 类通过 @Resource 注解将 approvalHandlerList 注入进来，
         * 而 approvalHandlerList 是 Spring 容器中所有实现了 ApprovalHandler 接口的 Bean 的集合。
         * 所以这里的@Resource注解很重要，不能缺失
         *
         * Spring 的依赖注入：
         * Spring 容器会自动扫描并管理所有带有 @Component、@Service、@Repository 等注解的类，并将它们注册为 Bean。
         * 如果有多个类实现了同一个接口（例如 ApprovalHandler），Spring 会将这些类的实例收集到一个 List 中。
         * @Resource 注解：
         * @Resource 是 Java 的标准注解，用于依赖注入。它可以注入单个 Bean 或者一个 Bean 的集合。
         */

        for (ApprovalHandler approvalHandler : approvalHandlerList) {
            approvalHandlerMap.put(approvalHandler.getCatalogEnum(), approvalHandler);
        }
    }

    /**
     * 根据流程类型获取对应的审批处理器
     *
     * @param catalogEnum
     * @return
     */
    public ApprovalHandler getApprovalHandler(ActivitiTypeEnums.CatalogEnum catalogEnum) {
        if (approvalHandlerMap.get(catalogEnum) == null) {
            return null;
        }
        return approvalHandlerMap.get(catalogEnum);
    }

}
