package com.oralce.util;

public class SnGenerateUtil {
	//自动生成学号的方法
	public static String generateSn(int clazzId) {
		String sn = "";
		sn = "A" + clazzId + System.currentTimeMillis();
		return sn;
	}
	
	public static String generateTeacherSn(int clazzId){
		String sn = "";
		sn = "T" + clazzId + System.currentTimeMillis();
		return sn;
	}

}
