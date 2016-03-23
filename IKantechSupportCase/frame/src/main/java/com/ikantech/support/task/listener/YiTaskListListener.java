package com.ikantech.support.task.listener;

import java.util.List;


/**
 * 数据执行的接口.
 */
public abstract class YiTaskListListener extends YiTaskListener{

	/**
	 * Gets the list.
	 *
	 * @return 返回的结果列表
	 */
	public abstract List<?> getList();

	/**
	 * 描述：执行完成后回调.
	 * 不管成功与否都会执行
	 * @param paramList 返回的List
	 */
    public abstract void update(List<?> paramList);
	
}
