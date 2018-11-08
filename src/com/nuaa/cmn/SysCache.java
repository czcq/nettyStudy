package com.nuaa.cmn;

import java.util.HashMap;

public class SysCache {
	
	private static HashMap<String,Object> sysCache= new HashMap();
	
	public static void put(String key,Object value){
		sysCache.put(key, value);
	}
	
	public static Object get(String key){
		return sysCache.get(key);
	}
	
}
