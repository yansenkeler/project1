package com.fruit.common.application;

import android.app.Application;

/**
 * @author liangchen
 * @time 2014-5-30 下午2:32:46
 * @annotation 
 */
public class FrameApplication {

	private static Application instance;

	public static synchronized void initializeInstance(Application application) {
		if(instance==null)
		{
			instance = application;
		}
	}

	public static synchronized Application getInstance() {
		if (instance == null) {
			throw new IllegalStateException(FrameApplication.class.getSimpleName() +
					"没有实例化，请先调用initializeInstance方法");
		}
		return instance;
	}
}
