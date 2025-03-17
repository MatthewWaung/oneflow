package com.oneflow.prm.core.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.MybatisDefaultParameterHandler;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.oneflow.prm.config.EcoFlowConfig;
import com.oneflow.prm.core.constant.Constants;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.core.utils.ContextUtil;
import com.oneflow.prm.core.utils.StringUtils;
import com.oneflow.prm.entity.dao.sys.SysRole;
import com.oneflow.prm.entity.dao.sys.SysUser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: jin
 * @create: 2023-09-03 14:57
 **/
@Slf4j
@NoArgsConstructor
@Intercepts(
        {@Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class})
        })
@Component
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

    private static final List<String> tenantTable = new ArrayList<>();
    final String PRM_CUSTOMER = "prm_customer";
    final String PRM_ORDER = "prm_order";
    final String PRM_SAP_SHIP_STATUS = "prm_sap_ship_status";
    @Autowired
    HttpServletRequest request;
    @Autowired
    private EcoFlowConfig ecoFlowConfig;
    private DataSource dataSource;

    public DataScopeInterceptor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        SysUser currentUser = ContextUtil.getCurrentUser();
        // 可以通过获取invocation的目标获取StatementHandler，因为@Intercepts拦截的目标类就是StatementHandler
//        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        sqlParser(metaObject);
        // 先判断是不是SELECT操作 不是直接过滤
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        if (CollectionUtils.isEmpty(currentUser.getRoles())) {
            return invocation.proceed();
            //throw new MyBatisSystemException(new Exception("权限获取失败，请联系管理员或者重新登录！"));
        }
//        BoundSql boundSql1 = statementHandler.getBoundSql();
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        int forSize = boundSql.getParameterMappings().size();
        Object paramObj = boundSql.getParameterObject();

        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        try {
            Statement statement = (Statement) CCJSqlParserUtil.parse(originalSql);
            Select select = (Select) statement;
            processSelectBody(select.getSelectBody());
            SelectBody selectBody = select.getSelectBody();
            PlainSelect plainSelect = (PlainSelect) selectBody;
            Table table = (Table) plainSelect.getFromItem();
            Alias alias = table.getAlias();
            String tableName = table.getName();

            String tableAlias = "";
            if (Objects.nonNull(alias)) {
                tableAlias = alias.getName().trim();
            }
            boolean hasCount = false;
            if (originalSql.toLowerCase().contains("count(")) {
                hasCount = true;
            }
            if (hasCount) {
                if (!originalSql.toLowerCase().contains("where")) {
                    originalSql += " where 1=1 ";
                }
            }

            StringBuffer sqlString = new StringBuffer();
            List<SysRole> roles = JSONObject.parseArray(JSONObject.toJSONString(currentUser.getRoles()), SysRole.class);

            if (PRM_SAP_SHIP_STATUS.equals(tableName)) {
                // 添加查看客户对应交货单权限
                addShipStatusScope(tableName, tableAlias, sqlString);
            } else {
                for (SysRole role : roles) {
                    if ("1".equals(role.getStatus())) {
                        continue;
                    }
                    String dataScope = role.getDataScope();

                    if (Constants.DATA_SCOPE_ALL.equals(dataScope)) {
                        //全部
                        sqlString = new StringBuffer();
                        break;
                    } else if (Constants.DATA_SCOPE_CUSTOM.equals(dataScope)) {
                        if (StringUtils.isEmpty(tableAlias)) {
                            sqlString.append(StringUtils.format(
                                    " OR dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ",
                                    role.getRoleId()));
                        } else {
                            //自定义
                            sqlString.append(StringUtils.format(
                                    " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", tableAlias,
                                    role.getRoleId()));
                        }
                    } else if (Constants.DATA_SCOPE_DEPT.equals(dataScope)) {
                        if (StringUtils.isEmpty(tableAlias)) {
                            sqlString.append(StringUtils.format(" OR dept_id = {} ", currentUser.getDeptId()));
                        } else {
                            //部门
                            sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", tableAlias, currentUser.getDeptId()));
                        }
                    } else if (Constants.DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                        if (StringUtils.isEmpty(tableAlias)) {
                            //部门以及下级部门
                            sqlString.append(StringUtils.format(
                                    " OR dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                                    currentUser.getDeptId(), currentUser.getDeptId()));
                        } else {
                            //部门以及下级部门
                            sqlString.append(StringUtils.format(
                                    " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                                    tableAlias, currentUser.getDeptId(), currentUser.getDeptId()));
                        }
                    } else if (Constants.DATA_SCOPE_SELF.equals(dataScope)) {
                        //本人
                        if (StringUtils.isNotEmpty(currentUser.getJobNum()) && StringUtils.isNotEmpty(currentUser.getUserName())) {
                            if (StringUtils.isEmpty(tableAlias)) {
                                sqlString.append(StringUtils.format(" OR find_in_set( '{}' , create_by ) ", currentUser.getJobNum() + "-" + currentUser.getUserName()));
                            } else {
                                sqlString.append(StringUtils.format(" OR find_in_set( '{}' , {}.create_by )", currentUser.getJobNum() + "-" + currentUser.getUserName(), tableAlias));
                            }

                            // 添加对应客户对应订单权限
                            addCustomerScope(tableName, tableAlias, sqlString);

                        } else {
                            // 数据权限为仅本人且没有userAlias别名不查询任何数据
                            sqlString.append(" OR 1=0 ");
                        }
                    }
                }
            }

            if (StringUtils.isNotEmpty(sqlString)) {
                if (hasCount) {
                    if (PRM_CUSTOMER.equalsIgnoreCase(tableName)) {
                        String origCondition = originalSql.substring(originalSql.toLowerCase().lastIndexOf("where") + 5);
                        origCondition = origCondition.replaceAll("\\?", " '{}' ");
                        if (boundSql.getParameterObject() instanceof Map) {
                            List<Object> objects = new ArrayList<>();
                            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(boundSql.getParameterObject()));
                            for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                                String property = parameterMapping.getProperty();
                                if ("page".equalsIgnoreCase(property) || "pageSize".equalsIgnoreCase(property)) {
                                    continue;
                                }
                                if (jsonObject.containsKey(property)) {
                                    objects.add(jsonObject.get(property));
                                } else if (boundSql.hasAdditionalParameter(property)) {
                                    objects.add(boundSql.getAdditionalParameter(property));
                                } else if (property.contains(".")) {
                                    Object read = JSONPath.read(jsonObject.toJSONString(), "$." + property + "");
                                    objects.add(read);
                                }
                            }
                            origCondition = StringUtils.format(origCondition, objects.toArray());
                        } else {
                            origCondition = StringUtils.format(origCondition, paramObj);
                        }

                        originalSql = originalSql + " and ( " + sqlString.substring(4) + ")";
                        if (StringUtils.isEmpty(tableAlias)) {
                            originalSql = origCondition.contains("1=1") ? originalSql + " OR common_type = 0 " : originalSql + " OR ( common_type = 0 and " + origCondition + " )";
                        } else {
                            //originalSql += StringUtils.format(" OR {}.common_type = 0 ", tableAlias);
                            originalSql = origCondition.contains("1=1") ? originalSql + StringUtils.format(" OR {}.common_type = 0 ", tableAlias)
                                    : originalSql + StringUtils.format(" OR ( {}.common_type = 0 and ", tableAlias) + origCondition + " )";
                        }
                    } else {
                        originalSql = originalSql + " and ( " + sqlString.substring(4) + ")";
                    }
                } else {
                    originalSql = buildSQLStatement(originalSql, sqlString, boundSql);
                }
            }
            metaObject.setValue("delegate.boundSql.sql", originalSql);
        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e.getCause(), originalSql);
        } catch (CustomException be) {
            log.warn(be.getDefaultMessage());
            return invocation.proceed();
        }

        //使用mybatis-plus的分页插件的sql拼接处理
        IPage<?> page = null;
        if (paramObj instanceof IPage) {
            page = (IPage<?>) paramObj;
        } else if (paramObj instanceof Map) {
            for (Object arg : ((Map<?, ?>) paramObj).values()) {
                if (arg instanceof IPage) {
                    page = (IPage<?>) arg;
                    break;
                }
            }
        }
        if (Objects.nonNull(page)) {
            Connection connection = (Connection) invocation.getArgs()[0];
            if (page.isSearchCount()) {
                int limit = originalSql.lastIndexOf("LIMIT");
                String countSql = "select count(1) from ( " + originalSql.replace(originalSql.substring(limit), " ") + " ) temp_date";
                try (PreparedStatement statement = connection.prepareStatement(countSql)) {
                    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
                    parameterMappings = parameterMappings.stream().filter(re -> (!re.getProperty().contains("mybatis_plus_"))).collect(Collectors.toList());
                    BoundSql sql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, boundSql.getParameterObject());
                    DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), sql);
                    parameterHandler.setParameters(statement);
                    long total = 0;
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            total = resultSet.getLong(1);
                        }
                    }
                    page.setTotal(total);
                } catch (Exception e) {
                    throw ExceptionUtils.mpe("Error: Method queryTotal execution error of sql : \n %s \n", e, countSql);
                }
            }
        }
        return invocation.proceed();
    }

//    public static void main(String[] args) {
//        String a = "s sd sd  Where sdsddds    Where a> a ? ? ? ".toLowerCase();
//        a = a.replace("?", "{}");
//        System.out.println(StringUtils.format(a, 1, 2, 3));
//    }

    //将原始sql和数据权限sql拼装到一起
    private String buildSQLStatement(String originalSql, StringBuffer whereSql, BoundSql boundSql) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(originalSql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        Expression where = plain.getWhere();
        Table table = (Table) plain.getFromItem();
        Alias alias = table.getAlias();
        String tableAlias = "";
        if (Objects.nonNull(alias)) {
            tableAlias = alias.getName().trim();
        }
        String tableName = table.getName();
        if (Objects.isNull(where)) {
            if (StringUtils.isNotEmpty(whereSql)) {
                String substringSql = whereSql.toString().substring(4);
                if (PRM_CUSTOMER.equalsIgnoreCase(tableName)) {
                    if (StringUtils.isEmpty(tableAlias)) {
                        substringSql += " OR common_type = 0 ";
                    } else {
                        substringSql += StringUtils.format(" OR {}.common_type = 0 ", tableAlias);
                    }
                }
                Expression expression = CCJSqlParserUtil
                        .parseCondExpression(substringSql);
                Expression whereExpression = (Expression) expression;
                plain.setWhere(whereExpression);
            }
        } else {
            String newWhere = where.toString();
            if (StringUtils.isNotEmpty(whereSql)) {
                if (PRM_CUSTOMER.equalsIgnoreCase(tableName)) {
                    String tempWhere = newWhere + " and ( " + whereSql.toString().substring(4) + " )";
                    if (StringUtils.isEmpty(tableAlias)) {
                        newWhere = " OR  ( common_type = 0  and " + newWhere + " ) ";
                    } else {
                        newWhere = StringUtils.format(" OR ( {}.common_type = 0  and ", tableAlias) + newWhere + " ) ";
                    }
                    newWhere = newWhere.replaceAll("\\?", " '{}' ");
                    int forSize = boundSql.getParameterMappings().size();
                    if (boundSql.getParameterObject() instanceof Map) {
                        List<Object> objects = new ArrayList<>();
                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(boundSql.getParameterObject()));
                        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                            String property = parameterMapping.getProperty();
                            if ("page".equalsIgnoreCase(property) || "pageSize".equalsIgnoreCase(property)) {
                                continue;
                            }

                            if (jsonObject.containsKey(property)) {
                                objects.add(jsonObject.get(property));
                            } else if (boundSql.hasAdditionalParameter(property)) {
                                objects.add(boundSql.getAdditionalParameter(property));
                            } else if (property.contains(".")) {
                                Object read = JSONPath.read(jsonObject.toJSONString(), "$." + property + "");
                                objects.add(read);
                            }
                        }
                        newWhere = StringUtils.format(newWhere, objects.toArray());
                    } else {
                        newWhere = StringUtils.format(newWhere, boundSql.getParameterObject());
                    }

                    newWhere = tempWhere + newWhere;
                } else {
                    newWhere += " and ( " + whereSql.toString().substring(4) + " )";
                }
            }
            Expression expression = CCJSqlParserUtil
                    .parseCondExpression(newWhere);
            plain.setWhere(expression);
        }
        return select.toString();
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    //JSqlParser解析sql的方法
    private void processPlainSelect(PlainSelect plainSelect) throws CustomException {
        FromItem fromItem = plainSelect.getFromItem();
        if (fromItem instanceof Table) {
            Table fromTable = (Table) fromItem;
            if (CollectionUtils.isEmpty(ecoFlowConfig.getRoleTenantTables()) || !ecoFlowConfig.getRoleTenantTables().contains(fromTable.getName())) {
                throw new CustomException(fromTable.getName() + "表已被忽略 不进行数据权限过滤");
            }
        } else {
            processFromItem(fromItem);
        }
    }

    private void processSelectBody(SelectBody selectBody) throws CustomException {
        if (selectBody == null) {
            return;
        }
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            processSelectBody(withItem.getSubSelect().getSelectBody());
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodys = operationList.getSelects();
            if (CollectionUtils.isNotEmpty(selectBodys)) {
                for (SelectBody body : selectBodys) {
                    processSelectBody(body);
                }
            }
        }
    }

    private void processFromItem(FromItem fromItem) throws CustomException {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoinList() != null) {
                for (Join join : subJoin.getJoinList()) {
                    processJoin(join);
                }
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {
            log.debug("Perform a subquery, if you do not give us feedback");
        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
    }

    private void processJoin(Join join) throws CustomException {
        if (join.getRightItem() instanceof Table) {
            Table fromTable = (Table) join.getRightItem();
            if (CollectionUtils.isEmpty(ecoFlowConfig.getRoleTenantTables()) || !ecoFlowConfig.getRoleTenantTables().contains(fromTable.getName())) {
                throw new CustomException(fromTable.getName() + "表已被忽略 不进行数据权限过滤");
            }
        }
    }

    /**
     * 添加权限，有权限看到客户的人员，也有权限能看到订单为此客户的单据
     *
     * @param tableName
     */
    private void addCustomerScope(String tableName, String tableAlias, StringBuffer sqlString) {
        if (!PRM_ORDER.equals(tableName)) {
            return;
        }

        List<Long> customerIdList = (List<Long>) request.getAttribute("customerIdList");
        if (CollectionUtils.isEmpty(customerIdList)) {
            return;
        }
        List<String> ids = customerIdList.stream().map(x -> String.valueOf(x)).collect(Collectors.toList());

        // 添加权限
        if (StringUtils.isEmpty(tableAlias)) {
            sqlString.append(StringUtils.format(" OR customer_id IN ({})", String.join(",", ids)));
        } else {
            sqlString.append(StringUtils.format(" OR {}.customer_id IN ({})", tableAlias, String.join(",", ids)));
        }
    }

    private void addShipStatusScope(String tableName, String tableAlias, StringBuffer sqlString) {
        List<Long> orderIdList = (List<Long>) request.getAttribute("orderIdList");
        if (CollectionUtils.isEmpty(orderIdList)) {
            return;
        }
        List<String> ids = orderIdList.stream().map(String::valueOf).collect(Collectors.toList());
        // 添加权限
        if (StringUtils.isEmpty(tableAlias)) {
            sqlString.append(StringUtils.format(" AND order_id IN ({})", String.join(",", ids)));
        } else {
            sqlString.append(StringUtils.format(" AND {}.order_id IN ({})", tableAlias, String.join(",", ids)));
        }
    }

}
