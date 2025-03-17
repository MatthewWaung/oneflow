package com.oneflow.prm.core.utils;

import com.oneflow.prm.entity.dao.sys.SysUser;

/**
 * 类 {@code ContextUtil} 获取上下文数据对象的工具类
 * @author Dunvida
 * @date 2022年02月23日 17:19
 */
public class ContextUtil {

	private static ContextUtil contextUtil;


	/**
	 * 获取当前执行人
	 * @return 用户详情
	 */
	public static SysUser getCurrentUser(){
		try {
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			Assert.notNull(authentication, "当前登录用户不能为空");
//			Object principal = authentication.getPrincipal();
			//// TODO: 2022/3/3
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

    /**
     * 获取当前用户ID
     */
	public static String getCurrentUserId(){
		//// TODO: 2022/3/3
//		IUser user = getCurrentUser();
//		return BeanUtils.isEmpty(user)?null:user.getUserId();
		return null;
	}

	/**
	 * 设置当前租户ID
	 */
	public static String getCurrentTenantId(){
		//// TODO: 2022/3/3
//		IUser user = getCurrentUser();
//		return BeanUtils.isEmpty(user)?null:user.getUserId();
		return null;
	}

	/**
	 * 获取当前用户工号id
	 */
	public static String getCurrentUserJobNumber(){
		//// TODO: 2022/3/3
//		IUser user = getCurrentUser();
//		return BeanUtils.isEmpty(user)?null:user.getUserId();
		return null;
	}

	/**
	 * 根据用户账户获取用户信息
	 * @param account 用户账号
	 * @return 用户详情
	 */
	public static String getUserByAccount(String account)
	{
//		Assert.isTrue(StringUtil.isNotEmpty(account), "必须传入用户账号");
//		IUserService userServiceImpl=AppUtil.getBean(IUserService.class);
//		IUser user = userServiceImpl.getUserByAccount(account);
//		Assert.isTrue(BeanUtils.isNotEmpty(user), String.format("账号为：%s的用户不存在", account));
//		return user;
		//// TODO: 2022/3/3
		return null;
	}

    /**
     * 设置当前用户账号
     * @param account 用户账号
     */
	public static void setCurrentUserByAccount(String account){
		//setCurrentUser(getUserByAccount(account));
		//// TODO: 2022/3/3
	}

}
