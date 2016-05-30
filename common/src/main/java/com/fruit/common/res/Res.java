package com.fruit.common.res;


/**
 * @author liyc
 * @time 2012-2-29 下午4:51:41
 * @annotation 资源获取方法
 */
public class Res {
	public static final String style = "style";
	public static final String string = "string";
	public static final String id = "id";
	public static final String layout = "layout";
	public static final String drawable = "drawable";
	public static final String attr = "attr";
	public static final String anim = "anim";
	public static final String raw = "raw";
	public static final String color = "color";
	private static final String packageName = "keler.epoint.mobileoa";

	public static int getResourseIdByName(String className, String name) {
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");

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
	
	public static int getLayoutInt(String layoutname){
		return Res.getResourseIdByName(Res.layout,layoutname);
	}
	
	public static int getStringInt(String stringname){
		return Res.getResourseIdByName(Res.string,stringname);
	}
	
	public static int getIdInt(String idname){
		return Res.getResourseIdByName(Res.id,idname);
	}
	
	public static int getDrawableInt(String drawablename){
		return Res.getResourseIdByName(Res.drawable,drawablename);
	}
	
	public static int getAttrInt(String attrname){
		return Res.getResourseIdByName(Res.attr,attrname);
	}
	
	public static int getAnimInt(String animname){
		return Res.getResourseIdByName(Res.anim,animname);
	}
	
	public static int getRawInt(String rawname){
		return Res.getResourseIdByName(Res.raw,rawname);
	}
	
	public static int getColorInt(String colorname){
		return Res.getResourseIdByName(Res.color,colorname);
	}
}
