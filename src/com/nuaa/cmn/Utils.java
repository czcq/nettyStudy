package com.nuaa.cmn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

public class Utils {
	
	private static ArrayList<String> md5Cache = new ArrayList<String>();

	public static void loadMd52Cache(){
		
		if(null!=md5Cache)md5Cache.clear();
		
		File file = new File(Constants.MD5_FILE);
		if(!file.exists()){
			try {
				File dir = new File(Constants.MD5_DIR);
				if(!dir.exists()){
					dir.mkdir();
				}
				if(file.createNewFile()){
					Log.i(Constants.LOG_TAG, "created md5 file successfully!");
					return;
				}
			} catch (IOException e) {
				Log.i(Constants.LOG_TAG, "failed to create md5 file ,"+e);
				return;
			}
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while(null!=(line = br.readLine())){
				md5Cache.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void savePath2CacheAndFile(String path){
		String md5 = MD5(path);
		md5Cache.add(md5);
		File file = new File(Constants.MD5_FILE);
		if(!file.exists()){
			try {
				File dir = new File(Constants.MD5_DIR);
				if(!dir.exists()){
					dir.mkdir();
				}
				if(file.createNewFile()){
					Log.i(Constants.LOG_TAG, "created md5 file successfully!");
				}
			} catch (IOException e) {
				Log.i(Constants.LOG_TAG, "failed to create md5 file ,"+e);
				return;
			}
		}
		try {
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
			br.write(md5);
			br.write("\n");
			br.flush();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isBackuped(String path){
		String md5 = MD5(path);
		for(String s:md5Cache){
			if(s.equals(md5)){
				Log.i(Constants.LOG_TAG, path + " backuped");
				return  true;
			}
		}
		return false;
	}
	
	public static void sndMsg(Handler handler , int what , String msg){
		Message message = new Message();
		message.what = what;
		message.obj = msg;
		handler.sendMessage(message);
	}
	
	public static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	public static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	
	public static  ArrayList<String> getPicturesPathList() {
		
		ArrayList<String> pathList = new ArrayList<String>();
		Context ctx = (Context) SysCache.get(Constants.CONTEXT);
		Cursor cursor = ctx.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,null);
		while (cursor.moveToNext()) {
			byte[] data = cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			String path = new String(data, 0, data.length - 1);
			if(!isBackuped(path))pathList.add(path);
		}
		return pathList;
	}
}
