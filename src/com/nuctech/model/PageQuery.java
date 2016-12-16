package com.nuctech.model;



/**
 * 
 * @类功能说明：
 * @作者：shangjianping
 * @创建时间：2015-5-12
 * @版本：V1.0
 */
public class PageQuery implements java.io.Serializable {


private static final long serialVersionUID = 1L;
private int page=1;
private int pageSize=20;
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}


}
