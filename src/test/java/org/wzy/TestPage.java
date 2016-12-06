package org.wzy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wzy.page.Page;
import org.wzy.page.PageSearch;
import org.wzy.test.Users;
import org.wzy.test.dao.UsersMapper;

/**
 * 
 * @author wang.zeyan -2016年12月6日
 */
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
