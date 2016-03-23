package com.ikantech.support.task.listener;

/**
 * 数据执行的接口.
 */
public abstract class YiTaskObjectListener extends YiTaskListener{
	
	/**
	 * Gets the object.
	 *
	 * @param <T> the generic type
	 * @return 返回的结果对象
	 */
    public abstract <T> T getObject();
    
    /**
     * 描述：执行开始后调用.
     *
     * @param <T> the generic type
     * @param obj the obj
     */
    public abstract <T> void update(T obj); 
    
	
}
