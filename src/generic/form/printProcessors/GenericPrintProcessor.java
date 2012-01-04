package generic.form.printProcessors;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import util.ReflectUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class GenericPrintProcessor implements JRDataSource, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int currentIndex = -1;
	private List<Object> entities = null;
	
	public GenericPrintProcessor(List<Object> entities){
		this.entities = entities;
	}
	
	@Override
	public Object getFieldValue(JRField jasperField) throws JRException {
		String[] getters = jasperField.getDescription().split("\\.");
		String getter = "";
		Method method = null;
		Object result = entities.get(currentIndex);
		for(String getStr : getters){
			getter = "get"+getStr;
			method = ReflectUtil.getMethod(result, getter);
			if(method!=null){
				try {
					result = method.invoke(result);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return result == null ? "" : result;
	}

	@Override
	public boolean next() throws JRException {
		if(currentIndex < entities.size()-1){
			currentIndex++;
			return true;
		}
		return false;
	}

}
