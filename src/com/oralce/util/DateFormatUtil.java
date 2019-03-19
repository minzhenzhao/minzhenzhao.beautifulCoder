package com.oralce.util;

import java.text.SimpleDateFormat;
import java.util.Date;

//获取时间格式的方法
public class DateFormatUtil {
	public static String getFormatDate(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}
