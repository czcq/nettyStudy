package com.nuaa.cmn;

import java.util.HashMap;

import com.nuaa.MainActivity;
import com.nuaa.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UIHandler {

	private static HashMap<String, Handler> handlerMap = new HashMap<>();

	public static void init() {
		Activity activity = (Activity) SysCache.get(Constants.MAIN_ACTIVITY);
//		final TextView task = (TextView) activity.findViewById(R.id.task);
//		final TextView info = (TextView) activity.findViewById(R.id.info);
//		final ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
//		final Handler taskHandler = new Handler() {
//			public void handleMessage(Message msg) {
//				if(msg.what == Constants.BACKUP_IS_COMPLETED)progressBar.setVisibility(View.INVISIBLE);
//			};
//		};
//		handlerMap.put(Constants.HANDLER_TASK, taskHandler);
//
//		final Handler infoHandle = new Handler() {
//			public void handleMessage(Message msg) {
//				if(msg.what == Constants.NO_PICTURES_BACKUP){
//					progressBar.setVisibility(View.INVISIBLE);
//				}
//				
//				if(msg.what == Constants.BACKUP_IS_DOING){
//					info.setText("" + msg.obj);
//				}
//				
//
//			};
//		};
//		handlerMap.put(Constants.HANDLER_INFO, infoHandle);
//
//		final Handler errorHandle = new Handler() {
//			public void handleMessage(Message msg) {
//				task.setText("");
//				info.setText("请检查网络情况！");
//				info.setTextColor(Color.RED);
//			};
//		};
//		handlerMap.put(Constants.HANDLER_ERROR, errorHandle);
//
//		final Handler progressHandler = new Handler() {
//			public void handleMessage(Message msg) {
//				progressBar.setProgress(Integer.parseInt("" + msg.obj));
//			};
//		};
//		handlerMap.put(Constants.HANDLER_PROGRESS_BAR, progressHandler);
		
		
		//通知栏
		Context ctx = (Context) SysCache.get(Constants.CONTEXT);
		final NotificationManager manager = (NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(activity);
        PendingIntent intent = PendingIntent.getActivity(activity,100, new Intent(activity, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intent);
        builder.setSmallIcon(R.drawable.app);
        builder.setContentTitle("暂无数据需要同步");
        builder.setProgress(0, 0 , false);
        final Notification notification = builder.build();
        manager.notify(0, notification);
        final Handler notificationHandler = new Handler() {
			public void handleMessage(Message msg) {
				String str = (String) msg.obj;
				String[] strs = str.split("/");
				if(msg.what == Constants.BACKUP_IS_DOING){
					builder.setContentTitle("正在备份 "+str);
					builder.setProgress(100, 100*Integer.parseInt(strs[0])/Integer.parseInt(strs[1]), false);
					manager.notify(0, notification);
				}else if(msg.what == Constants.BACKUP_ERR_OCCURED){
					builder.setContentTitle("请检查网络情况");
					builder.setProgress(0, 0 , false);
					manager.notify(0, notification);
				}else if(msg.what == Constants.BACKUP_IS_COMPLETED){
					builder.setContentTitle(str);
					 builder.setProgress(0, 0 , false);
					manager.notify(0, notification);
				}
			};
		};
		handlerMap.put(Constants.HANDLER_NOTIFICATION, notificationHandler);
	}
	
	
	public static HashMap<String, Handler> getHandlerMap(){
		return handlerMap;
	}

}

