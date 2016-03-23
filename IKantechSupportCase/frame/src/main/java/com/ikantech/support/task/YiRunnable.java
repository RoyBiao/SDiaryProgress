package com.ikantech.support.task;

import com.ikantech.support.task.listener.YiTaskListener;


/**
 * 数据执行单位.
 */
public class YiRunnable {
	
	/** 记录的当前索引. */
	private int position;
	 
 	/** 执行完成的回调接口. */
    private YiTaskListener listener; 
    
	/**
	 * Instantiates a new ab task item.
	 */
	public YiRunnable() {
		super();
	}

	/**
	 * Instantiates a new ab task item.
	 *
	 * @param listener the listener
	 */
	public YiRunnable(YiTaskListener listener) {
		super();
		this.listener = listener;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Gets the listener.
	 *
	 * @return the listener
	 */
	public YiTaskListener getListener() {
		return listener;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(YiTaskListener listener) {
		this.listener = listener;
	}

} 

