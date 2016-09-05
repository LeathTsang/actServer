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

	// ͬ����������֤��ͬһʱ�䣬ֻ�ܱ�һ���˵���
	private static synchronized void makeInstance() {
		if (instance == null) {
			instance = new Env();
		}
	}

	private Env() {
		InputStream is = getClass().getResourceAsStream("/config.properties");// ����db.properties�ļ�
		try {
			load(is);// ȡ�������б�
		} catch (Exception e) {
			System.err.println("����û�ж�ȡ�����ļ�����ȷ��db.property�ļ��Ƿ����");
		}
	}
}
