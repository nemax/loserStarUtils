package com.loserstar.utils.ObjectMapConvert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;


/**
 * 
 * author: loserStar
 * date: 2018年12月7日上午9:53:22
 * remarks:对象map转换工具类
 */
public class LoserStarObjMapConvertUtil {

	/**
	 * 根据对象列表和对象的某个属性返回属性的List集合
	 * 
	 * @param objList
	 *            对象列表
	 * @param propertyName
	 *            要操作的属性名称
	 * @return <pre>
	 * 	指定属性的List集合;
	 * 	如果objList为null或者size等于0抛出 IllegalArgumentException异常;
	 *  如果propertyName为null抛出 IllegalArgumentException异常
	 * </pre>
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static <T> List<Object> getPropertyList(List<T> objList, String propertyName) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (objList == null || objList.size() == 0)
			throw new IllegalArgumentException("No objList specified");
		if (propertyName == null || "".equals(propertyName)) {
			throw new IllegalArgumentException("No propertyName specified for bean class '" + objList.get(0).getClass() + "'");
		}
		PropertyUtilsBean p = new PropertyUtilsBean();
		List<Object> propList = new LinkedList<Object>();
		for (int i = 0; i < objList.size(); i++) {
			T obj = objList.get(i);
			propList.add(p.getProperty(obj, propertyName));
		}
		return propList;
	}

	/**
	 * 将List列表中的对象的某个属性封装成一个Map对象，key值是属性名，value值是对象列表中对象属性值的列表
	 * 
	 * @param objList
	 *            对象列表
	 * @param propertyName
	 *            属性名称,可以是一个或者多个
	 * @return 返回封装了属性名称和属性值列表的Map对象，如果参数非法则抛出异常信息
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static <T> Map<String, List<Object>> getPropertiesMap(List<T> objList, String... propertyName)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (objList == null || objList.size() == 0)
			throw new IllegalArgumentException("No objList specified");
		if (propertyName == null || propertyName.length == 0) {
			throw new IllegalArgumentException("No propertyName specified for bean class '" + objList.get(0).getClass() + "'");
		}
		Map<String, List<Object>> maps = new HashMap<String, List<Object>>();
		for (int i = 0; i < propertyName.length; i++) {
			maps.put(propertyName[i], getPropertyList(objList, propertyName[i]));
		}
		return maps;
	}
	
	/**
	 * 把某种Map类型的父或子对象转为Map
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String,T> ConvertToMap(T t) throws Exception {
		if (t instanceof Map) {
			return (Map<String, T>) t;
		}else {
			throw new Exception("对象非Map类型，不能转换");
		}
	}

	/**
	 * 把某种Map的父对象或子对象的的List转为List<Map>
	 * @param objectList
	 * @return
	 * @throws Exception
	 */
	public static <T> List<Map<String, T>> ConvertListToMapList(List<T> list) throws Exception{
		List<Map<String, T>> resultList = new ArrayList<Map<String,T>>();
		resultList = (List<Map<String, T>>) list;
		//遍历转换貌似慢着一些，直接整个强制转换应该稍快点（猜测的，没具体考究过）
/*		for (T t: list) {
			resultList.add(ConvertToMap(t));
		}*/
		return resultList;
	}
	
	/**
	 * 某个List的父或子对象转为List（返回值的泛型基于形参的实际类型，调用时就可明确知道返回值类型）
	 * @param t
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> ConvertToList(T t) throws Exception{
		if (t instanceof List) {
			return (List<T>)t;
		}else {
			throw new Exception("对象非List类型，不能转换");
		}
	}
	
	/**
	 * 某个List的父或子对象转为List（返回值的泛型基于返回值实际类型，所以该方法并不知道返回值类型，调用时需强制转换）
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static List<?> ConvertObjectToList(Object object) throws Exception {
		if (object instanceof List) {
			return (List<?>)object;
		}else {
			throw new Exception("对象非List类型，不能转换");
		}
	}
}