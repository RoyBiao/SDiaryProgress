package com.ikantech.support.task.listener;


/**
 * 任务执行的控制父类.
 */
public class YiTaskListener {
    
    /**
     * Gets the.
     *
     * @return 返回的结果对象
     */
    public void get(){};
    
    /**
     * 描述：执行开始后调用.
     * */
    public void update(){};

	/**
	 * 监听进度变化.
	 *
	 * @param values the values
	 */
	public void onProgressUpdate(Integer... values){};

}
