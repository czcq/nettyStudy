package com.nuaa;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.nuaa.cmn.Constants;
import com.nuaa.cmn.UIHandler;
import com.nuaa.cmn.Utils;
import com.nuaa.handler.FileClientHandler;

public class FileClient extends TimerTask{
	
	@Override
	public void run() {
		
		final ArrayList<String> pathList = Utils.getPicturesPathList();
		
		if(pathList.size()<=0){
//			Utils.sndMsg(handlerMap.get(Constants.HANDLER_INFO), "暂无图片需要同步");
			Log.i(Constants.LOG_TAG, "暂无图片需要同步");
			return;
		}
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<SocketChannel>(){
			@Override
			protected void initChannel(SocketChannel ch)throws Exception {
				ByteBuf delimiter = Unpooled.copiedBuffer(com.nuaa.cmn.Constants.DELIMTER.getBytes());
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(com.nuaa.cmn.Constants.MAX_FRAME_LENGTH,delimiter));
				ch.pipeline().addLast(new FileClientHandler(pathList,UIHandler.getHandlerMap()));
			}
		});
		try{
			ChannelFuture channelFuture =  b.connect("134.175.123.97",8080).sync();
			channelFuture.channel().closeFuture().sync();
		}catch(Exception e){
			e.printStackTrace();
			Map<String,Handler> handlerMap = UIHandler.getHandlerMap();
			Handler handler = handlerMap.get(Constants.HANDLER_NOTIFICATION);
        	Utils.sndMsg(handler, Constants.BACKUP_ERR_OCCURED,"");
		}
	
	}
}
