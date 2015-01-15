package com.dcy.psychology.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
	
	public static String convertStreamToString(InputStream is) {
		if(is == null)
			return "";
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "GBK"));
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stringBuilder.toString();
	}
	
	public static String readContent(File file){
		if(!file.exists())
			return "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return convertStreamToString(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
