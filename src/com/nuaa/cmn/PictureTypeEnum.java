package com.nuaa.cmn;

import android.util.Log;

public enum PictureTypeEnum {

	 JPG("jpg",0x00),
	 BMP("bmp",0x01),
	 PCX("pcx",0x02),
	 TIF("tif",0x03),
	 GIF("gif",0x04),
	 JPEG("jpeg",0x05),
	 TGA("tga",0x06),
	 EXIF("exif",0x07),
	 FPX("fpx",0x08),
	 SVG("svg",0x09),
	 PSD("psd",0x0A),
	 CDR("cdr",0x0B),
	 PCD("pcd",0x0C),
	 DXF("dxf",0x0D),
	 UFO("ufo",0x0E),
	 EPS("eps",0x0F),
	 AI("ai",0x10),
	 PNG("png",0x11),
	 HDRI("hdri",0x12),
	 RAW("raw",0x13),
	 WMF("wmf",0x14),
	 FLIC("flic",0x15),
	 EMF("emf",0x16),
	 ICO("ico",0x17),
	 WEBP("webp",0x18);
	 
	 



	 private String name;
	 private int code;
	 
	 private PictureTypeEnum(String name,int code){
		 this.name = name;
		 this.code = code;
	 }
	 
	 public String getName() {
		 return name;
	 }
	 
	 public int getCode() {
		 return code;
	 }
	 
	 public static int getCode(String name){
		 for(PictureTypeEnum p : PictureTypeEnum.values()){
			 if(p.getName().equalsIgnoreCase(name)){
				 return p.getCode();
			 }
		 }
		 Log.e(Constants.LOG_TAG,"not Found corret code , by "+name);
		 return PictureTypeEnum.PNG.getCode();
	 }
	
}
