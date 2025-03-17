package com.oneflow.prm.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.oneflow.prm.core.utils.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 字段自动填充处理器
 * @author Dunvida
 * @date 2022年02月24日 16:16
 */
@Component
@Slf4j
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("start insert fill ....");
		strictInsertFill(metaObject, "createBy", String.class, ContextUtil.getCurrentUserId());
		strictInsertFill(metaObject, "tenantId", String.class, ContextUtil.getCurrentTenantId());
		//this.strictInsertFill(metaObject, "createOrgId", String.class, baseContext.getCurrentOrgId());
		strictInsertFill(metaObject, "createTime", LocalDateTime.class, getSetterTypeOfLocal(metaObject, "createTime"));

		//另一种不严格的方式
		//插入时，自动填充创建时间和修改时间
//		this.setFieldValByName("createTime", new Date(), metaObject);
//		this.setFieldValByName("updateTime", new Date(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("start update fill ....");
		strictUpdateFill(metaObject, "updateBy", String.class, ContextUtil.getCurrentUserId());
		setFieldValByName("updateTime", getSetterTypeOfLocal(metaObject, "updateTime"), metaObject);
	}

	/**
	 *  通过属性元信息获取指定属性的当前值
	 *  <p>主要解决不同的日期类型的字段当前值</p>
	 * @param metaObject
	 * @param propertyName
	 * @return
	 */
	private Object getSetterTypeOfLocal(MetaObject metaObject, String propertyName){
		Class<?> targetType = metaObject.getSetterType(propertyName);
		Object obj = null;
		if(LocalDateTime.class.equals(targetType)) {
			obj = LocalDateTime.now();
		}
		else if(LocalDate.class.equals(targetType)) {
			obj = LocalDate.now();
		}
		else if(Date.class.equals(targetType)) {
			obj = new Date();
		}
		return obj;
	}
}
