package util;

import java.lang.reflect.Method;

public class ReflectUtil {
	public static Method getMethod(Object obj, String methodName){
		Method[] methods = obj.getClass().getMethods();
		for(Method m: methods){
			if(m.getName().equals(methodName)){
				return m;
			}
		}
		return null;
	}
}
