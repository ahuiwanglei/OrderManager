package com.ordermanger.online.ordermanager.util.http;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class HttpCommon {

	public static final class HTTP {
		public static final int TIMEOUT = 120000;
		public static final String BASEURL = "";
		public static final String IP = "218.4.189.147:8099";
		public static final String HOST = "http://" + IP + "/I/";
		public static final String IMAGE_HOST = "http://" + IP + "/Content/attachment/tp/";
		public static final String MINA_IMAAGe_HOST = "http://" + IP + "/Content/images/ad/";
		public static final String Image_CX_HOST = "http://" + IP + "/Content/attachment/cx/";
		public static final int HTTP_OK = 0;
		public static final int HTTP_NO = 1;

	}

}
