package org.wzy.page;


import java.io.Serializable;
import java.util.Map;

/**
 * 
 * Mapper条件对象
 * 
 * @author Fland.wang
 * @date Jul 23, 2015
 *
 */
public class PageSearch implements Serializable {

	private static final long serialVersionUID = -2212110473576867343L;

	private Integer page = 1;
	private Integer rows = 10;
	
	/*	扩展属性	*/
	private transient Map<String,Object> ext;
	
	public PageSearch() {
		super();
	}
	
	public PageSearch(Integer page, Integer rows) {
		super();
		this.page = page;
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Map<String, Object> getExt() {
		return ext;
	}

	public void setExt(Map<String, Object> ext) {
		this.ext = ext;
	}
}
