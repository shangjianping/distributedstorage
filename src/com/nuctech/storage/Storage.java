package com.nuctech.storage;

import java.util.List;

import com.nuctech.model.PageQuery;


/**
 * 
 * @类功能说明：存储抽象类
 * @作者：shangjianping
 * @创建时间：2015-5-11
 * @版本：V1.0
 */
public abstract class Storage<T> {

	
	
	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 增加单个存储对象
	 * @param fileInfo
	 * @return
	 */
	public abstract int insert(T obj);

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量增加存储对象
	 * @param list
	 */
	public abstract int insert(List<T> list);

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 读取单个存储对象
	 * @param id
	 * @return
	 */
	public abstract T read(String id);

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量读取存储对象
	 * @param fileInfo
	 * @return
	 */
	public abstract List<T> read(Object obj);
	public abstract List<T> read(Object obj,PageQuery pageQuery);
	public abstract List<T> read(Object obj,String filterquery,PageQuery pageQuery);

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 更新存储对象
	 * @param fileInfo
	 * @return
	 */
	public abstract int update(T obj);

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 删除单个存储对象
	 * @param id
	 */
	public abstract int delete(String id);

	/**
	 * @创建人：shangjianping
	 * @创建时间：2015-5-11
	 * @功能描述： 批量删除存储对象
	 * @param fileInfo
	 */
	public abstract int delete(Object obj);
	
	
	public abstract int isExists(String id);
	public void connect() {}
	
	public void close(){}
}