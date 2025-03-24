package com.oneflow.oms.core.aspectj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oneflow.oms.core.annotation.ModifyRecord;
import com.oneflow.oms.core.common.PageResult;
import com.oneflow.oms.core.constant.BizConstant;
import com.oneflow.oms.core.utils.SpringUtil;
import com.oneflow.oms.entity.record.ModifyRecordDO;
import com.oneflow.oms.enums.DataType;
import com.oneflow.oms.enums.ModuleType;
import com.oneflow.oms.enums.BusinessType;
import com.oneflow.oms.service.IModifyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class ModifyRecordAspect {

    @Pointcut("@annotation(com.oneflow.oms.core.annotation.ModifyRecord)")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        ModifyRecordDO modifyRecordDO = new ModifyRecordDO();
        ModifyRecord modifyRecord = getAnnotationModify(joinPoint);
        if (null == modifyRecord) {
            return joinPoint.proceed();
        }

        ModuleType moduleType = modifyRecord.moduleType();
        BusinessType businessType = modifyRecord.businessType();
        DataType dataType = modifyRecord.dataType();
//        User currentUser = SpringUtil.getBean(TokenUtils.class).getUser();
        Object[] originalArgs = joinPoint.getArgs();
        if (ModuleType.ORDER.ordinal() == moduleType.ordinal()) {
            JSONObject params = JSONObject.parseObject(JSONObject.toJSONString(originalArgs[0]));
            if (BizConstant.CALCULATE.equals(params.getString("operationType"))) {
                return joinPoint.proceed();
            }
        }

        // 修改记录前的操作
        recordBefore(modifyRecordDO, moduleType, businessType, dataType, modifyRecord, joinPoint);

        // 执行业务代码
        Object result = joinPoint.proceed();

        // 修改记录后的操作
        recordAfter(modifyRecordDO, moduleType, businessType, dataType, modifyRecord, joinPoint);

        return result;
    }


    /**
     * 查询数据
     *
     * @param iService   服务名称
     * @param methodName 方法名称
     * @param params     参数
     * @return
     */
    public Object springInvokeMethod(Class iService, String methodName, Object[] params, ModifyRecordDO modifyRecordDO) {
        try {
            Object service = SpringUtil.getBean(iService);
            // 找到方法 getTaxRateCfgByCondition
            Method method = ReflectionUtils.findMethod(service.getClass(), methodName, null);
            if (ObjectUtils.isNotEmpty(params)) {
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(params[0]));
                String id = jsonObject.getString("id");

                if (StringUtils.isNotEmpty(id)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> parameterType : parameterTypes) {
                        if (parameterType.getTypeName().equalsIgnoreCase("java.lang.long")) {
                            modifyRecordDO.setModuleId(Long.parseLong(id));
                            return method.invoke(SpringUtil.getBean(iService), Long.parseLong(id));
                        } else if (parameterType.getTypeName().equalsIgnoreCase("java.lang.string")) {
                            modifyRecordDO.setModuleId(Long.parseLong(id));
                            return method.invoke(SpringUtil.getBean(iService), id);
                        }

                        Object newInstance = parameterType.newInstance();
                        Field startingPointCode = parameterType.getDeclaredField("id");
                        startingPointCode.setAccessible(true);
                        modifyRecordDO.setModuleId(Long.parseLong(id));
                        return method.invoke(SpringUtil.getBean(iService), newInstance);
                    }
                }
            }
        } catch (Exception e) {
            log.info("springInvokeMethod iService: {}, methodName: {}, params: {}, modifyRecordDO: {}", iService, methodName, params, modifyRecordDO);
            log.error("springInvokeMethod error.:", e);
        }
        return null;
    }

    public void recordBefore(ModifyRecordDO modifyRecordDO, ModuleType moduleType, BusinessType businessType, DataType dataType, ModifyRecord modifyRecord, ProceedingJoinPoint joinPoint) {
        try {
            modifyRecordDO.setModuleType(moduleType.ordinal());
            modifyRecordDO.setBusinessType(businessType.ordinal());
            modifyRecordDO.setDataType(dataType.ordinal());
            modifyRecordDO.setStatus(0);
            modifyRecordDO.setIp(null);
//            modifyRecordDO.setTraceId(ContextHolder.getTraceId());

            Object[] originalArgs = joinPoint.getArgs();

            Object oldData = null;
            if (BusinessType.UPDATE.ordinal() == businessType.ordinal()) {
                Class iService = modifyRecord.iService();
                String methodName = modifyRecord.methodName();
                Object findResult = springInvokeMethod(iService, methodName, originalArgs, modifyRecordDO);
                if (null == findResult) {
                    return;
                }


                if (findResult instanceof PageResult) {
                    PageResult pageResult = (PageResult) findResult;
                    List list = pageResult.getResult();
                    if (CollectionUtils.isNotEmpty(list)) {
                        oldData = list.get(0);
                    }
                } else {
                    oldData = findResult;
                }

                modifyRecordDO.setOldData(JSONObject.toJSONString(oldData));
                modifyRecordDO.setPostData(JSONObject.toJSONString(originalArgs[0]));
            }
        } catch (Exception e) {
            log.error("ModifyRecordAspect recordBefore error: ", e);
        }
    }

    public void recordAfter(ModifyRecordDO modifyRecordDO, ModuleType moduleType, BusinessType businessType, DataType dataType, ModifyRecord modifyRecord, ProceedingJoinPoint joinPoint) {
        CompletableFuture.runAsync(() -> {

            // 此处仅处理了修改数据的情况，其它BusinessType需要单独处理
            if (BusinessType.UPDATE.ordinal() == businessType.ordinal()) {
                Class iService = modifyRecord.iService();
                String methodName = modifyRecord.methodName();
                Object[] originalArgs = joinPoint.getArgs();
                Object newResult = springInvokeMethod(iService, methodName, originalArgs, modifyRecordDO);
                if (null == newResult) {
                    return;
                }

                Object newData = "";
                if (newResult instanceof PageResult) {
                    PageResult pageResult = (PageResult) newResult;
                    List list = pageResult.getResult();
                    if (CollectionUtils.isNotEmpty(list)) {
                        newData = list.get(0);
                    }
                } else {
                    newData = newResult;
                }

                modifyRecordDO.setNewData(JSONObject.toJSONString(newData));
                JSONObject changeData = new JSONObject();
                JSONObject descriptionData = new JSONObject();
                compareData(JSONObject.parseObject(modifyRecordDO.getOldData()), newData, changeData, descriptionData, null);

                modifyRecordDO.setChangeData(JSONObject.toJSONString(changeData));
                modifyRecordDO.setDescription(JSONObject.toJSONString(descriptionData));

                SpringUtil.getBean(IModifyRecordService.class).insertModifyRecord(modifyRecordDO);
            }
        }
//                , ThreadUtils.instance()  // 这里需要指定自定义的线程池
        );
    }

    private void compareData(Object oldData, Object newData, JSONObject changeData, JSONObject descriptionData, String parentKey) {
        // 处理JSON数组
        if (oldData instanceof JSONArray) {
            JSONArray oldJsonArray = (JSONArray) oldData;
            JSONArray newJsonArray = (JSONArray) newData;
            List<JSONObject> oldJsonList = oldJsonArray.toJavaList(JSONObject.class);
            List<JSONObject> newJsonList = newJsonArray.toJavaList(JSONObject.class);

//            int oldSize = oldJsonList.size();
//            int newSize = newJsonList.size();
//            if (newSize >= oldSize) {
//                for (int i = 0; i < newJsonList.size(); i++) {
//                    if (i < oldSize) {
//                        if (changeData.containsKey(parentKey) && changeData.get(parentKey) instanceof JSONArray) {
//                            JSONArray tempArray = changeData.getJSONArray(parentKey);
//                            JSONObject tempJson = new JSONObject();
//                            tempJson.put("id", oldJsonList.get(i).getLong("id"));
//                            tempArray.add(tempJson);
//                        }
//                        compareData(oldJsonList.get(i), newJsonList.get(i), changeData, descriptionData, parentKey);
//                    } else {
//                        // 新增数据
//                    }
//                }
//            } else {
//                for (int i = 0; i < oldJsonList.size(); i++) {
//                    if (i <= newSize) {
//                        compareData(oldJsonList.get(i), newJsonList.get(i), changeData, descriptionData, parentKey);
//                    } else {
//                        // 删除数据
//                    }
//                }
//            }

            Map<String, JSONObject> oldMap = oldJsonList.stream().filter(re -> Objects.nonNull(re.getString("id"))).collect(Collectors.toMap(jsonObject -> jsonObject.getString("id"), Function.identity(), (old, newObject) -> newObject));
            Map<String, JSONObject> newMap = newJsonList.stream().filter(re -> Objects.nonNull(re.getString("id"))).collect(Collectors.toMap(jsonObject -> jsonObject.getString("id"), Function.identity(), (old, newObject) -> newObject));

            // 根据id判断，oldMap和newMap两者都有说明是修改，如果oldMap有而newMap没有则说明被删除，如果oldMap没有而newMap有则说明是新增
            for (Map.Entry<String, JSONObject> oldJson : oldMap.entrySet()) {
                String id = oldJson.getKey();
                JSONObject newJson = newMap.get(id);
                if (Objects.nonNull(newJson)) {
                    compareData(newJson, oldJson.getValue(), changeData, descriptionData, parentKey);
                } else {
                    // 删除的数据
                    JSONObject deleteJson = oldJson.getValue();
//                    jsonData2objectData(deleteJson, descriptionData);
//                    deleteJson.put("updateFlag", "D");
//                    ((JSONArray) changeData.get(parentKey)).add(deleteJson);

                    JSONObject changeValue = new JSONObject();
                    changeValue.put("oldValue", null);
                    changeValue.put("newValue", null);
                    changeValue.put("delValue", deleteJson);
                    changeValue.put("updateFlag", "D");
                    changeData.put(parentKey, changeValue);
                }
            }

            // 新增的数据，newMap中存在oldMap不存在，即二者的差集为新增
            Sets.SetView<String> difference = Sets.difference(newMap.keySet(), oldMap.keySet());
            for (String key : difference) {
                JSONObject newAddJson = newMap.getOrDefault(key, new JSONObject());
//                jsonData2objectData(newAddJson, descriptionData);
//                newAddJson.put("updateFlag", "I");
//                ((JSONArray) changeData.get(parentKey)).add(newAddJson);

                JSONObject changeValue = new JSONObject();
                changeValue.put("oldValue", null);
                changeValue.put("newValue", null);
                changeValue.put("addValue", newAddJson);
                changeValue.put("updateFlag", "I");
                changeData.put(parentKey, changeValue);
            }

        } else {
            // 处理JSON对象

            JSONObject oldJson = JSONObject.parseObject(JSONObject.toJSONString(oldData));
            JSONObject newJson = JSONObject.parseObject(JSONObject.toJSONString(newData));

            for (String key : oldJson.keySet()) {
                // 因为FastJson序列化时会将值为null的字段过滤掉，oldJson存在的字段而newJson中不存在的字段是为删除（此处没有处理删除情况）
                if (oldJson.get(key) instanceof JSONArray) {
                    if (newJson.containsKey(key)) {
                        if (!changeData.containsKey(key)) {
                            changeData.put(key, new JSONArray());
                        }
                        compareData(oldJson.get(key), newJson.get(key), changeData, descriptionData, key);
                    }
                } else if (oldJson.get(key) instanceof JSONObject) {
                    if (newJson.containsKey(key)) {
                        if (!changeData.containsKey(key)) {
                            JSONObject tempJson = new JSONObject();
                            tempJson.put("id", ((JSONObject) oldJson.get(key)).getLong("id"));
                            changeData.put(key, tempJson);
                        }
                        compareData(oldJson.get(key), newJson.get(key), changeData, descriptionData, key);
                    }
                } else {
                    if (newJson.containsKey(key)) {
                        if (!oldJson.getString(key).equalsIgnoreCase(newJson.getString(key))) {
                            if (StringUtils.isNotEmpty(parentKey)) {
                                if (changeData.get(parentKey) instanceof JSONObject) {
                                    JSONObject parentChangeJson = changeData.getJSONObject(parentKey);
                                    JSONObject changeValue = new JSONObject();
                                    changeValue.put("oldValue", oldJson.getString(key));
                                    changeValue.put("newValue", newJson.getString(key));
                                    if (null == parentChangeJson) {
                                        parentChangeJson = new JSONObject();
                                        parentChangeJson.put(key, changeValue);
                                    } else {
                                        parentChangeJson.put(key, changeValue);
                                    }
                                    changeData.put(parentKey, parentChangeJson);
                                } else if (changeData.get(parentKey) instanceof JSONArray) {
                                    JSONArray arrayJson = changeData.getJSONArray(parentKey);
                                    JSONObject changeValue = new JSONObject();
                                    changeValue.put("oldValue", oldJson.getString(key));
                                    changeValue.put("newValue", newJson.getString(key));

                                    if (CollectionUtils.isEmpty(arrayJson)) {
                                        arrayJson = new JSONArray();
                                        JSONObject tempJson = new JSONObject();
                                        tempJson.put(key, changeValue);
                                        arrayJson.add(tempJson);
                                    } else {
                                        JSONObject tempJson = arrayJson.getJSONObject(arrayJson.size() - 1);
                                        tempJson.put(key, changeValue);
                                    }
                                    changeData.put(parentKey, arrayJson);
                                }
                            } else {
                                JSONObject changeValue = new JSONObject();
                                changeValue.put("oldValue", oldJson.getString(key));
                                changeValue.put("newValue", newJson.getString(key));
                                changeData.put(key, changeValue);
                            }

                            descriptionData.put(key, "[" + key + "] 从 " + oldJson.get(key) + " 修改为 " + newJson.get(key));
                        }
                    }
                }
            }

            // 因为FastJson序列化时会将值为null的字段过滤掉，所以需要遍历newJson以获取新增的字段
            for (String key : newJson.keySet()) {
                if (!oldJson.containsKey(key)) {
                    if (ObjectUtils.isNotEmpty(newJson.get(key))) {
                        if (newJson.get(key) instanceof JSONArray || newJson.get(key) instanceof JSONObject) {
                            String jsonString = JSONObject.toJSONString(newJson.get(key));  // 这里将对象转为String可能对后续处理有点影响
                            JSONObject changeValue = new JSONObject();
                            changeValue.put("oldValue", null);
                            changeValue.put("newValue", jsonString);
                            changeData.put(key, changeValue);
                            descriptionData.put(key, "新增视图 [" + key + "] 值为： " + jsonString);
                        } else {
                            JSONObject changeValue = new JSONObject();
                            changeValue.put("oldValue", null);
                            changeValue.put("newValue", newJson.get(key));
                            changeData.put(key, changeValue);
                            descriptionData.put(key, "新增字段 [" + key + "] 值为： " + newJson.get(key));
                        }
                    }
                }
            }
        }

    }

    private void jsonData2objectData(Object jsonObject, JSONObject stringBuffer) {
        try {
            if (jsonObject instanceof JSONArray) {
                JSONArray oldJsonArray = (JSONArray) jsonObject;
                for (Object obj : oldJsonArray) {
                    JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(obj));
                    for (String key : parseObject.keySet()) {
                        jsonData2objectData(parseObject.get(key), stringBuffer);
                    }
                }
            } else if (jsonObject instanceof JSONObject) {
                JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(jsonObject));
                for (String key : parseObject.keySet()) {
                    if (parseObject.get(key) instanceof JSONArray) {
                        jsonData2objectData(parseObject.get(key), stringBuffer);
                    } else {
                        stringBuffer.put(key, "新增字段 [" + key + "] 值为：" + parseObject.get(key));
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private ModifyRecord getAnnotationModify(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(ModifyRecord.class);
        }
        return null;
    }

}
