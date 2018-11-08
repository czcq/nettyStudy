package com.nuaa.handler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import android.os.Handler;
import android.util.Log;
import com.nuaa.cmn.Constants;
import com.nuaa.cmn.PictureTypeEnum;
import com.nuaa.cmn.Utils;

@SuppressWarnings("rawtypes")
public class FileClientHandler  extends  SimpleChannelInboundHandler{
	
	private ArrayList<String> pathList;
	private Map<String,Handler> handlerMap;
	private static HashMap<ChannelFuture, String> futurePathMap = new HashMap<ChannelFuture, String>();
	
	public FileClientHandler(ArrayList<String> pathList , Map<String,Handler> handlerMap){
		this.pathList = pathList;
		this.handlerMap = handlerMap;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws IOException {
		final long start = System.currentTimeMillis();
		final AtomicInteger i = new AtomicInteger(0);
		for(final String path:pathList){
			try{
					FileInputStream fileInputStream = new FileInputStream(new File(path));
					int fileLength = fileInputStream.available();
					ByteBuf buf = ctx.alloc().buffer(1+fileLength+4);
					//write picture type
					String name = path.substring(path.lastIndexOf(".")+1);
					buf.writeByte(PictureTypeEnum.getCode(name));
					//write picture data
					byte[] data = new byte[fileLength];
					fileInputStream.read(data);
					buf.writeBytes(data);
					//write message delimter
					buf.writeBytes(Constants.DELIMTER.getBytes());
					ChannelFuture f1 = ctx.writeAndFlush(buf); // (3)
					futurePathMap.put(f1, path);
					Log.i(Constants.LOG_TAG, "len="+fileLength);
			        f1.addListener(new ChannelFutureListener() {
			            @Override
			            public void operationComplete(ChannelFuture future) {
			            	i.incrementAndGet();
			            	String backupedPath = futurePathMap.get(future);
			            	Utils.savePath2CacheAndFile(backupedPath);
			            	//update UI
			            	Handler handler = handlerMap.get(Constants.HANDLER_NOTIFICATION);
			            	Utils.sndMsg(handler, Constants.BACKUP_IS_DOING,i.get()+"/"+pathList.size());
							
							if(i.get() == pathList.size()){//work is over
								long endTime = System.currentTimeMillis();
								long cost = endTime - start;
								//update UI
								Utils.sndMsg(handler, Constants.BACKUP_IS_COMPLETED , pathList.size()+"张照片备份完成，耗时"+cost/1000.0+"s");
								ctx.close();
							}
			            }
			        }); 
			}catch(Exception e){
				Log.e(Constants.LOG_TAG, "failed to backup "+path);
				i.incrementAndGet();
				Log.i("Constants.LOG_TAG", "i="+i.get()+",size="+pathList.size()+",发生异常，"+e);
				e.printStackTrace();
			}
		}
}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, Object arg1)
			throws Exception {
	}
}