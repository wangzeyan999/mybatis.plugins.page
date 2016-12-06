package org.wzy.page;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;



/**
 * Mybatis分页插件
 * 
 * @author Fland.wang
 * @date Jul 23, 2015
 *
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class , Integer.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PagePlugin implements Interceptor {

	private static ThreadLocal<Page<Object>> pageResultThreadLocal = new ThreadLocal<Page<Object>>();
	/**
	 * Statement 处理
	 * @param statementHandler
	 * @throws Exception
	 */
	public void doStatementHandler(RoutingStatementHandler statementHandler,Object [] args) throws Exception{
		if(statementHandler == null)
			return ;
		
		BoundSql boundSql = statementHandler.getBoundSql();
		Object parameterObject = boundSql.getParameterObject();
		if(parameterObject == null)
			return ;
		
		PreparedStatementHandler delegate = (PreparedStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
		MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
		
		PageSearch page = null;
		if (parameterObject instanceof ParamMap) {
			@SuppressWarnings("unchecked")
			ParamMap<Object> paramMap = (ParamMap<Object>) parameterObject;
			page = searchPage(paramMap);
		}
		if(parameterObject instanceof PageSearch){
			page = (PageSearch) parameterObject;
		}
		if (args.length > 0 && page != null) {
			Connection connection = (Connection) args[0];
			String sql = boundSql.getSql();
			String countSql = getCountSql(sql);
			PreparedStatement countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,boundSql.getParameterMappings(), parameterObject);
			ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBS);
			parameterHandler.setParameters(countStmt);
			ResultSet rs = countStmt.executeQuery();
			
			Page<Object> pageResult = new Page<Object>();
			pageResult.setPage(page.getPage());
			pageResult.setRows(page.getRows());

			pageResultThreadLocal.set(pageResult);
			if (rs.next()) {
				Long totalCount = rs.getLong(1);
				pageResult.setTotal(totalCount);
			}
			String newSql = buildSqlPage(sql, page);
			ReflectHelper.setFieldValue(boundSql, "sql", newSql);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object doResultSetHandler(Invocation invocation) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(pageResultThreadLocal.get() != null){
			Page<Object> pr = pageResultThreadLocal.get();
			pageResultThreadLocal.remove();
			List<Page<Object>> result = new ArrayList<Page<Object>>();
			pr.setData((List<Object>) invocation.proceed());
			result.add(pr);
			return result;
		}
		return invocation.proceed();
	}
	
	
	public Object intercept(Invocation invocation) throws Throwable {
		if( invocation.getTarget() instanceof RoutingStatementHandler){
			doStatementHandler((RoutingStatementHandler)invocation.getTarget() , invocation.getArgs());
		}
		if( invocation.getTarget() instanceof DefaultResultSetHandler){
			return doResultSetHandler(invocation);
		}
		return invocation.proceed();
	}

	private static String buildSqlPage(String sql, PageSearch page) {
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" limit ");
		sb.append((page.getPage() - 1) * page.getRows());
		sb.append(",");
		sb.append(page.getRows());
		return sb.toString();
	}

	private static String getCountSql(String originalSql) {

		return "select count(0) from (" + originalSql + ") c_";
	}

	private static PageSearch searchPage(ParamMap<Object> paramMap) {
		Set<Map.Entry<String, Object>> set = paramMap.entrySet();
		for (Map.Entry<String, Object> entry : set) {
			Object value = entry.getValue();
			if (value == null)
				continue;
			if (value instanceof PageSearch) {
				return (PageSearch) value;
			}
		}
		return null;
	}

	public Object plugin(Object target) {
		if(target instanceof StatementHandler || target instanceof ResultSetHandler)
			return Plugin.wrap(target, this);
		return target;
	}

	public void setProperties(Properties arg0) {

	}
}
