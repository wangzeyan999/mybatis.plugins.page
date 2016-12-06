package org.wzy;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.wzy.page.PagePlugin;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;

@Configuration

// mybatis mapper的接口
@MapperScan(basePackages = "org.wzy.test.dao")
public class ApplicationContextConfiguration {


	@Bean(initMethod="init",destroyMethod="close")
	public DataSource dataSource(){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://192.168.3.243:3306/shna?useUnicode=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true");
		dataSource.setUsername("wang.zeyan");
		dataSource.setPassword("123456");
		return dataSource;
	}
	
	@Bean
	public SqlSessionFactoryBean getSessionFactory(ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		
		//这里,把我们的插件加入进去
		sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PagePlugin()} );
		
		//mapper.xml扫描设置
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+"org/wzy/test/mapper/*.xml"));
		return sqlSessionFactoryBean;
	}
}
