package com.util;

import java.io.InputStream;
import java.util.Properties;

public class Env extends Properties {
	private static Env instance;

	public static Env getInstance() {
		if (instance != null)
			return instance;
		else {
			makeInstance();
			return instance;
		}
	}

	// 同步方法，保证在同一时间，只能被一个人调用
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new Env();
		}
	}

	private Env() {
		InputStream is = getClass().getResourceAsStream("/config.properties");// 加载db.properties文件
		try {
			load(is);// 取得属性列表
		} catch (Exception e) {
			System.err.println("错误：没有读取属性文件，请确认db.property文件是否存在");
		}
	}
}
