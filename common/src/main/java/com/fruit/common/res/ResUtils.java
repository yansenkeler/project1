package com.fruit.common.res;

import android.content.Context;

/**
 * @author liyc
 * @time 2014-5-7 上午12:59:07
 * @annotation 资源文件反射方式获取工具类
 */
public class ResUtils {
	
	static ResUtils instance;
	public static Context con;
	
	public synchronized static void initializeInstance(Context context) {
        if(instance==null)
        {
        	instance = new ResUtils();
        	con = context;
        	System.out.println(con.getPackageName());
        }
    }
	
	public static synchronized ResUtils getInstance() {
        if (instance == null) {
            throw new IllegalStateException(ResUtils.class.getSimpleName() +
                    "没有实例化，请先调用initializeInstance方法");
        }
        return instance;
    }
	
	public synchronized Context getApplicationContext() {
        return con;
    }
	
	public synchronized int getResourseIdByName(String className, String name) {
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(con.getPackageName() + ".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; i++) {
				if(classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}
			if(desireClass != null)
				id = desireClass.getField(name).getInt(desireClass);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return id;
	}
	
	public synchronized int getLayoutInt(String layoutname){
		return getInstance().getResourseIdByName(Res.layout,layoutname);
	}
	
	public synchronized int getStringInt(String stringname){
		return getInstance().getResourseIdByName(Res.string,stringname);
	}
	
	public synchronized int getIdInt(String idname){
		return getInstance().getResourseIdByName(Res.id,idname);
	}
	
	public synchronized int getDrawableInt(String drawablename){
		return getInstance().getResourseIdByName(Res.drawable,drawablename);
	}
	
	public synchronized int getAttrInt(String attrname){
		return getInstance().getResourseIdByName(Res.attr,attrname);
	}
	
	public synchronized int getAnimInt(String animname){
		return getInstance().getResourseIdByName(Res.anim,animname);
	}
	
	public synchronized int getRawInt(String rawname){
		return getInstance().getResourseIdByName(Res.raw,rawname);
	}
	
	public synchronized int getColorInt(String colorname){
		return getInstance().getResourseIdByName(Res.color,colorname);
	}
	
	public synchronized int getStyleInt(String colorname){
		return getInstance().getResourseIdByName(Res.style,colorname);
	}
}
