package com.dcy.psychology.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dcy.psychology.MyApplication;

import android.os.Environment;
import android.widget.Toast;

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
	
	public static String readInputStream(InputStream inputStream)
			throws IOException {
		if(inputStream == null)
			return "";
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] buffer = new byte[8 * 1024];
		int n = 0;
		while ((n = inputStream.read(buffer)) >= 0)
			stream.write(buffer, 0, n);
		inputStream.close();
		return stream.toString();
	}
	
	public static File getExternalDir() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(MyApplication.getInstance(), "«Î≤Â»ÎSDø®", Toast.LENGTH_SHORT).show();
            return null;
        }
        File external = Environment.getExternalStorageDirectory();
        if (external == null)
            return null;
        File ext = new File(Environment.getExternalStorageDirectory(),"Psychology");
        if(ext != null && !ext.exists()){
            ext.mkdirs();
        }
        return ext;
    }
	
	public static File getExternalTempFile(){
        File dir = getExternalDir();
        if(dir == null){
            return null;
        }
        return new File(dir, System.currentTimeMillis() + ".png");
    }
}
