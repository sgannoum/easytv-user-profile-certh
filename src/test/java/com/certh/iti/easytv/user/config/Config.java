package com.certh.iti.easytv.user.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

public class Config {
	
	public static final String path = "userModel.json";
	
	public static JSONObject getProfile(String fname) throws IOException {
		String line;
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(loader.getResource(fname).getFile())));
		StringBuffer buff = new StringBuffer();
		
		while((line = reader.readLine()) != null) 
				buff.append(line);
		
		
		JSONObject json = new JSONObject(buff.toString());		
		reader.close();
		
		return json;
		
	}

}
