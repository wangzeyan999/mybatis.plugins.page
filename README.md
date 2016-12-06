# Mybatis分页插件-mybatis.plugins.page

	个人使用基于Spring,Mybatis,作者99%时间在使用Mysql,所以暂时只支持Mysql.
	
### 该插件基于Mapper,无需再Service上做额外工作,Jar在lib目录下

### 第一步 Spring Configuration
    @Configuration
	//mybatis mapper的接口
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

### 第二步 Mapper接口, 例:
    public interface UsersMapper {
		//参数中包含 PageSearch 就会分页,当然,如果分页需要Page对象去接收,这是分页的
		Page<Users> selectAll(PageSearch pageSearch);
		
		//这是不分页的
		List<Users> selectAll()
	}
###

### 第三步 Mapper.xml 例:
    <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	<mapper namespace="org.wzy.test.dao.UsersMapper">
	  <resultMap id="BaseResultMap" type="org.wzy.test.Users">
	    <id column="userId" jdbcType="BIGINT" property="userId" />
	    <result column="userName" jdbcType="VARCHAR" property="userName" />
	    <result column="password" jdbcType="VARCHAR" property="password" />
	  </resultMap>
	  <sql id="Base_Column_List">
	    userId, userName, password
	  </sql>
	  <select id="selectAll" resultMap="BaseResultMap">
	  	select * from users
	  </select>
	</mapper>
###

### 第四步 测试 例:
    @RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(classes = ApplicationContextConfiguration.class)
	public class TestPage {
	
		@Autowired
		UsersMapper mapper;
	
		@Test
		public void testPage(){
			//这一步 ,通常在controller层 就已经组装好了.
			PageSearch pageSearch = new PageSearch(1,10);
			
			
			Page<Users> result = mapper.selectAll(pageSearch);
			System.out.println(result);
		}
	}
###
### 这是结果
	PageResult [page=1, rows=10, total=16, totalPage=2, data=[Users [userId=1, userName=admin, password=admin], Users [userId=2, userName=wangzeyan, password=wangzeyan], Users [userId=11, userName=dapeng, password=dapeng], Users [userId=12, userName=122121, password=], Users [userId=13, userName=1111, password=1111], Users [userId=14, userName=2222, password=2222], Users [userId=15, userName=3333, password=33333], Users [userId=16, userName=4444, password=4444], Users [userId=17, userName=55555, password=555555], Users [userId=19, userName=77777, password=333333]]]
###

	