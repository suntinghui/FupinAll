package com.jkrm.fupin.util;

import android.app.Activity;

import java.util.ArrayList;

/**
 * 继承模板页面的Activity对象管理类
 * 
 * @author Joey.Huang
 */
public class ActivityCollectorUtil
{
	/**
	 * 保存所有继承自BaseAct类的Activity集合对象
	 */
	public static ArrayList<Activity> activitys = new ArrayList<Activity>();

	/**
	 * 向集合中添加Activity对象
	 */
	public static void registry(Activity activity)
	{
		if(!activitys.contains(activity))
			activitys.add(activity);
	}

	/**
	 * 释放所有Activity对象
	 */
	public static void release()
	{
		for (int i = 0, size = activitys.size(); i < size; i++)
			activitys.get(i).finish();
		activitys.clear();
	}

	/**
	 * 释放所有Activity对象
	 * @param begin 从第一个开始删除activity
	 */
	public static void releaseII(int begin)
	{
		for (int i = begin, size = activitys.size(); i < size; i++)
			activitys.get(i).finish();
		activitys.clear();
	}
	
	/**
	 * 释放集合中指定的Activity对象
	 */
	public static void deRegistry(Activity activity)
	{
		activitys.remove(activity);
	}

	/**
	 * 根据索引获取集合中指定的Activity对象
	 */
	public static Activity getActivity(int index)
	{
		int size = activitys.size();
		if (size == 0 || index >= size || index < 0) return null;
		return activitys.get(index);
	}

}
