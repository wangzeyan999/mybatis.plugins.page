package org.wzy.page;

import java.util.List;

/**
 * 
 * Mapper 返回对象
 * @author wang.zeyan
 * @date 2016年9月5日
 */
public class Page<T> {

	/*	当前页	*/
	private Integer page;
	
	/*	条数	*/
	private Integer rows;
	
	/*	总条数	*/
	private Long total;
	
	/*	总页数	*/
	private Long totalPage;
	
	/*	数据	*/
	private List<T> data;

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

	public Long getTotal() {
		return total;
	}
	
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public void setTotal(Long total) {
		this.total = total;
		if (total <= 0)
			return;
		Long totalPage = this.total / this.rows;
		if (this.total % this.rows > 0)
			totalPage++;
		this.totalPage = totalPage;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageResult [");
		if (page != null) {
			builder.append("page=");
			builder.append(page);
			builder.append(", ");
		}
		if (rows != null) {
			builder.append("rows=");
			builder.append(rows);
			builder.append(", ");
		}
		if (total != null) {
			builder.append("total=");
			builder.append(total);
			builder.append(", ");
		}
		if (totalPage != null) {
			builder.append("totalPage=");
			builder.append(totalPage);
			builder.append(", ");
		}
		if (data != null) {
			builder.append("data=");
			builder.append(data);
		}
		builder.append("]");
		return builder.toString();
	}

	
}
