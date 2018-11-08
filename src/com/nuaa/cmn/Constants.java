package com.nuaa.cmn;

public class Constants {
	
	public static final String LOG_TAG = "pictureTransfer";
	
	public static final String DELIMTER = "$_$_$_$_";
	
	public static final int MAX_FRAME_LENGTH = 1385381430;
	
	public static final String MD5_DIR = "data/data/com.nuaa/";
	
	public static final String MD5_FILE = "data/data/com.nuaa/md5.txt";
	
	public static final String HANDLER_TASK = "HANDLER_TASK";
	
	public static final String HANDLER_INFO = "HANDLER_INFO";
	
	public static final String HANDLER_PROGRESS_BAR = "HANDLER_PROGRESS_BAR";
	
	public static final String HANDLER_ERROR = "HANDLER_ERROR";
	
	public static final String HANDLER_NOTIFICATION = "HANDLER_NOTIFICATION";
	
	public static final int NO_PICTURES_BACKUP = 0;//没有照片需要同步
	
	public static final int BACKUP_IS_COMPLETED = 1;//同步任务结束
	
	public static final int BACKUP_IS_DOING = 2;//任务进行中
	
	public static final int BACKUP_ERR_OCCURED = 3;//任务出错
	
	public static final String CONTEXT = "CONTEXT";
	
	public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
	

}
