package org.wzy.test.dao;

import org.wzy.page.Page;
import org.wzy.page.PageSearch;
import org.wzy.test.Users;

public interface UsersMapper {
	
	Page<Users> selectAll(PageSearch pageSearch);
}
