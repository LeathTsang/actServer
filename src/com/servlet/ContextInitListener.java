package com.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.util.Env;

public class ContextInitListener implements ServletContextListener {

	static String stStr_download_file_rootdir;
	static String stStr_nginx_upload_rootdir;
	static String stStr_internet_download_address;
	public ContextInitListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static synchronized Connection getConn(){
String driverClassName = Env.getInstance().getProperty("driver");
String url = Env.getInstance().getProperty("url");
String user = Env.getInstance().getProperty("user");
String password = Env.getInstance().getProperty("password");

try {
Class.forName(driverClassName);
Connection conn = DriverManager.getConnection(url, user, password);
return conn;
} catch (Exception ex) {
ex.printStackTrace();
}
return null;
}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		Properties props = new Properties(); 
        InputStream inputStream = null; 
        try { 
            inputStream = getClass().getResourceAsStream("/config.properties"); 
            props.load(inputStream); 
            stStr_download_file_rootdir = (String) props.get("download_file_rootdir"); 
            stStr_nginx_upload_rootdir = (String) props.get("nginx_upload_rootdir"); 
            stStr_internet_download_address = (String)props.get("internet_download_address");
            
            System.err.println("download_file_rootdir:" + stStr_download_file_rootdir + " nginx_upload_rootdir: " + stStr_nginx_upload_rootdir); 
            System.err.println("internet_download_address:" + stStr_internet_download_address); 
            
/*            File afile = new File("D:\\java_test_dir\\zuidaima_com2.txt");  
            
            BufferedWriter output = new BufferedWriter(new FileWriter(afile));  
            output.write("it test writing file");  
            output.close();  
            
            
            System.out.println("afile.getName(): " + afile.getName());  
            if (afile.renameTo(new File("d:\\" + afile.getName()))) {  
                System.out.println("File is moved successful!");  
            } else {  
                System.out.println("File is failed to move!");  
            }  */
            
            
        } catch (IOException ex) { 
            ex.printStackTrace(); 
        } 
	}

}
