package org.wzy.page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fland.wang
 * @date Jul 23, 2015
 *
 */
public class ReflectHelper {

	/**
	 * 根据Field Name获取Field
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {

		Field field = null;
		Class<?> clazz = obj.getClass();
		do {
			try {
				field = clazz.getDeclaredField(fieldName);
				if (field != null)
					return field;
			} catch (Exception e) {
				//log.info(clazz.getName() + " no such field " + fieldName);
				// e.printStackTrace();
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);

		return field;
	}

	/**
	 * <p>
	 * 	从一个对象中 获取 某属性的值
	 * <p>
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object obj, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object result = null;
		condition: {
			if (field == null)
				break condition;
			if (field.isAccessible())
				result = field.get(obj);
			else {
				field.setAccessible(Boolean.TRUE);
				result = field.get(obj);
				field.setAccessible(Boolean.FALSE);
			}
		}
		return result;
	}
	
	/**
	 * <p>
	 * 		设置对象中 某属性的值
	 * </p>
	 * @param obj
	 * @param fieldName
	 * @param newValue
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * 	-wang.zeyan -2016年12月6日
	 */
	public static void setFieldValue(Object obj, String fieldName, Object newValue) throws IllegalArgumentException, IllegalAccessException{
		Field field = getFieldByFieldName(obj, fieldName);
		
		if (field == null) return ;
		if (field.isAccessible())
			field.set(obj, newValue);
		else {
			field.setAccessible(Boolean.TRUE);
			field.set(obj, newValue);
			field.setAccessible(Boolean.FALSE);
		}
	}

	/**
	 * 
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static List<Field> getFieldByFieldType(Object obj, Class<?> clazz) {
		if (obj == null)
			return null;
		List<Field> listField = new ArrayList<Field>();
		Class<?> currentClazz = obj.getClass();
		do {
			Field [] fields = currentClazz.getDeclaredFields();
			for (Field field : fields) {
				if (field.getType().getSimpleName().equals(clazz.getSimpleName())){
					listField.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != null);
		return listField;
	}
}
