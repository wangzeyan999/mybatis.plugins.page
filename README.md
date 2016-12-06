# Mybatis分页插件-mybatis.plugins.page

	个人使用基于Spring,Mybatis,作者99%时间在使用Mysql,所以暂时只支持Mysql.<br/>
	
# 该插件基于Mapper,无需再Service上做额外工作

### 第一步 Spring Configuration
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
###

	