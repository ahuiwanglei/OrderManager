package com.ordermanger.online.ordermanager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.widget.TextView;

public class StringUtil {
	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty() || str.length() == 0 || str.equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
	
	/**
	 * 
	 * @param str
	 *            is null or ""
	 * @return true is not null
	 */
	public static final boolean isNotEmpty(String str) {
		return !(str == null || str.equals(""));
	}


	public static boolean isEmpty(TextView tv) {
		return isEmpty(tv.getText().toString());
	}

	public static String convertDate(String date) {
		if (isEmpty(date)) {
			return "";
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d;
			try {
				d = df.parse(date);
				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
				return df2.format(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return "";
		}

	}

	public static String convertNull(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str;
	}

	public static float convertFloat(String per) {
		if (isEmpty(per)) {
			return 0f;
		} else {
			return Float.parseFloat(per);
		}
	}
}
