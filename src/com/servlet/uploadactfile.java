package com.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONStringer;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Future;

import javax.naming.Context;
import javax.naming.InitialContext;



/**
 * Servlet implementation class uploadactfile
 */
@WebServlet("/uploadactfile")
public class uploadactfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public uploadactfile() {
		// TODO Auto-generated constructor stub
	}

	public String queryMaxID(){
	
		String str_ID = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {		
			InitialContext ctx = new InitialContext();
			DataSource datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/OtaDB");
			Connection connection = datasource.getConnection();
			String strSql = "select actfile_id from t_actfile where actfile_id = (select max(actfile_id) from t_actfile)";
			rs = connection.createStatement().executeQuery(strSql);
			while(rs.next())
			{
				str_ID = rs.getString(1);
				break;
			}
			
			if(null != rs) rs.close();
			if(null != ps) ps.close();
			connection.close();
			
		}catch (Exception ex) {
				ex.printStackTrace();
			}	
		
		return str_ID;
	}
	
	
	public String queryFilePathByID(String str_ID){
		String strFilePath = null;
		if(null == str_ID || str_ID.equals(""))
		{
			return null;
		}
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {		
			InitialContext ctx = new InitialContext();
			DataSource datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/OtaDB");
			Connection connection = datasource.getConnection();
			String strSql = " select org_url from t_actfile where actfile_id=" + str_ID;
			rs = connection.createStatement().executeQuery(strSql);
			while(rs.next())
			{
				strFilePath = rs.getString(1);
				break;
			}
			
			if(null != rs) rs.close();
			if(null != ps) ps.close();
			connection.close();
			
		}catch (Exception ex) {
				ex.printStackTrace();
			}	
		
		return strFilePath;
	}
	
	public boolean isIDValided(String str_ID){
		
		if(null == str_ID || str_ID.equals(""))
		{
			return false;
		}
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {		
			int count = 0;
			InitialContext ctx = new InitialContext();
			DataSource datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/OtaDB");
			Connection connection = datasource.getConnection();
			String strSql = "select * from t_actfile where actfile_id='" + str_ID + "'";
			rs = connection.createStatement().executeQuery(strSql);
			while(rs.next())
			{
				count++;
				break;
			}
			
			if(null != rs) rs.close();
			if(null != ps) ps.close();
			connection.close();
			
			if(0 == count){

				return true;
			}
			
			
			
		}catch (Exception ex) {
				ex.printStackTrace();

			}	
		
		return false;
	}
	
	
 public  boolean deleteFile(String strFilePath){
		
		if(null == strFilePath || strFilePath.equals(""))
		{
			return false;
		}
				
		if(null != strFilePath){
			File file = new File(strFilePath);  
			  // ����ļ�·������Ӧ���ļ����ڣ�������һ���ļ�����ֱ��ɾ��  
			  if (file.exists() && file.isFile()) {  
			   if (!file.delete()) {  
			    System.out.println("error: delete file:" + strFilePath + " failed.");  				
			   }
			   else{
				    System.out.println("info: delete old file:" + strFilePath + " succeeded.");  
				    return true;
			   }
		}else{
		    System.out.println("error: file:" + strFilePath + " not found.");  				
		}
		
	
	}
		return false;
 }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getOutputStream().write("{\"code\":204, \"msg\":\"Get request not supported.\"}".toString().getBytes("UTF-8"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (null == request) {
			return;
		}
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		
		String str_method = request.getParameter("method");
		String str_name = request.getParameter("name");
		String str_category = request.getParameter("category");
		String str_videoid = request.getParameter("videoid");
		String str_id = request.getParameter("id");
		String str_date = request.getParameter("date");
		String str_filesize = request.getParameter("filesize");
		String str_author = request.getParameter("author");
		String str_md5 = request.getParameter("md5");
		String str_version = request.getParameter("version");
		String str_description = request.getParameter("description");
        String str_upload_path = "/tmp";
        String str_upload_filename = "file1_name";
        String str_upload_filesize = "file1_size";
        String str_blockcount = "3";
        String str_actfile_url ="http://192.168.0.123:8080/download/";
        String str_scategory= "da pian";
        String strSql ="";

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");


		try {

			List<FileItem> list = upload
					.parseRequest(new ServletRequestContext(request));

			Iterator<FileItem> iter = list.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				// item.isFormField()�����жϵ�ǰ�����Ƿ���file��������� �������ֵ��true˵������
				// ������ͨ����
				if (item.isFormField()) {
					System.out.println("��ͨ����" + item.getFieldName());
					System.out.println(item.getString("utf-8"));

					if (item.getFieldName().equals("method")) {
						str_method = item.getString("utf-8");
					} else if (item.getFieldName().equals("name") ){
						str_name = item.getString("utf-8");
					} else if (item.getFieldName().equals("category") ){
						str_category = item.getString("utf-8");
					} else if (item.getFieldName().equals("videoid") ){
						str_videoid = item.getString("utf-8");
					} else if (item.getFieldName().equals("id") ){
						str_id = item.getString("utf-8");

					} else if (item.getFieldName().equals("author") ){
						str_author = item.getString("utf-8");

						} else if (item.getFieldName().equals("date") ){
						str_date = item.getString("utf-8");
					} else if (item.getFieldName().equals("filesize") ){
						str_filesize = item.getString("utf-8");

					} else if (item.getFieldName().equals("md5")) {
						str_md5 = item.getString("utf-8");

					} else if (item.getFieldName().equals("version") ){
						str_version = item.getString("utf-8");

					} else if (item.getFieldName().equals("description") ){
						str_description = item.getString("utf-8");
					}
					 else if (item.getFieldName().equals("file1_path") ){
						 str_upload_path = item.getString("utf-8");
						}
					 else if (item.getFieldName().equals("file1_name") ){
						 str_upload_filename = item.getString("utf-8");
						}else if (item.getFieldName().equals("file1_size") ){
							str_upload_filesize = item.getString("utf-8");
						}

				} else {
					// System.out.println("file����" + item.getFieldName());
					/*
					 * ֻ��file����Ž��ö����е�����д����ʵ�ļ�����
					 */
					String lastpath = item.getName();// ��ȡ�ϴ��ļ�������
					lastpath = lastpath.substring(lastpath.lastIndexOf("."));
					System.out.println("request mode is invalid, param: " + lastpath);
				
				}
			}
		} catch (FileUploadException e) {
			System.out.println("fileupload parse is error.");
		}
		
		if(str_method.equals("1")){ //1���½������ļ�  2���޸Ķ����ļ�
			//����½��ļ�����ѯ���ݿ��Ƿ������ͬ�Ķ����ļ�id�� ������ڷ���һ��id����ֵ
			//-----����nginx�᲻��ɾ���ѱ�����ϴ��ļ�---------------------
			if(isIDValided(str_id))
			{
				System.out.println("actfile_id:" + str_id + " is valid.");
			}
			else
			{
				String str_validID = queryMaxID();
				if(null != str_validID){ //id����ֵ�����IDֵ�� ��10�����ظ��ͻ���
					long nID = Long.parseLong(str_validID);
					nID += 10;
					response.getOutputStream().write(("{\"code\":210, \"msg\":\"actfile ID is invalided.\", \"id\":" + nID +"}").toString().getBytes("UTF-8"));
					deleteFile(str_upload_path); //����Ӧ���ɾ�����ϴ����ļ���������������ļ�
					return;
				}
				//����id=0��ָʾ����������idֵʧ�ܣ��ɿͻ����Լ�����id�������ύ���������ļ�����
				response.getOutputStream().write(("{\"code\":210, \"msg\":\"actfile ID is invalided.\", \"id\":0").toString().getBytes("UTF-8"));
				deleteFile(str_upload_path); //����Ӧ���ɾ�����ϴ����ļ���������������ļ�
				return;
			}
		}
		else{ //�޸Ķ����ļ����Ȳ���ϼ�¼���ļ���ŵ�ַ��ɾ�����ļ�����ִ�к���Ĳ�����¶����ļ�����
			String strFilePath = queryFilePathByID(str_id);
		    deleteFile(strFilePath);
		}
		
		 //str_upload_path = "D:\\java_test_dir\\zuidaima_com2.txt";  //for test
	    //�ƶ��ļ���ָ��������Ŀ¼, ��actfile_id+ʱ����Ϊ�ļ�������ֹ�ظ��ϴ�ͬһ�ļ� ����ͻ
		 Calendar calendar = Calendar.getInstance();
		 int hours = calendar.get(Calendar.HOUR_OF_DAY); // ʱ
		 int minutes = calendar.get(Calendar.MINUTE);    // ��
		 int seconds = calendar.get(Calendar.SECOND);    // ��
		 
		 File afile = new File(str_upload_path);  
         
     	 String h_m_s = String.format("%02d%02d%02d", hours, minutes, seconds);
		 String dstFile = ContextInitListener.stStr_download_file_rootdir + str_id + "_" + h_m_s + ".act";
         str_actfile_url = ContextInitListener.stStr_internet_download_address + str_id + "_" + h_m_s + ".act";
                
         System.out.println("prepare to move srcfile("+ str_upload_path + ") to dstfile(" + dstFile);  
         if (afile.renameTo(new File(dstFile))) {  
             System.out.println("It is succeeded to move file.srcfile:" + str_upload_path + " to dstfile:" + dstFile);  
             
         } else {  
             System.out.println("failed to move file.srcfile:" + str_upload_path + " to dstfile:" + dstFile);  
         } 
 
		
		
 		
		//���ݿ�������
		PreparedStatement ps = null;
		try {		
			InitialContext ctx = new InitialContext();
			DataSource datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/OtaDB");
			Connection connection = datasource.getConnection();
			strSql = "INSERT INTO `t_actfile` (id,actfile_id, actfile_name,video_id,category,scategory,url,org_url,author,lasttime,version,description,filesize, md5,blockcount)" +
					" VALUES (null, " + Long.parseLong(str_id) + ", '" + str_name + "', "+ Long.parseLong(str_videoid) + "," + Integer.parseInt(str_category)
					+ ", '" + str_scategory + "', '" +  str_actfile_url + "', '" + dstFile + "', '" + str_author + "', '" + str_date + "', '" + str_version 
					+ "', '" + str_description + "', " + Integer.parseInt(str_filesize) + ", '" + str_md5 + "', " + str_blockcount + ") on duplicate key update "
					+ "actfile_name='" + str_name + "', video_id=" + Long.parseLong(str_videoid) + ", category=" + Integer.parseInt(str_category) + ", scategory='"
					+ str_scategory + "', url='"+ str_actfile_url + "', org_url='"+dstFile +"', author='" + str_author + "', lasttime='" + str_date + "', version='"
					+ str_version + "', description='" + str_description + "', filesize="+ Integer.parseInt(str_filesize) + ", md5='" + str_md5 + "', blockcount=" + str_blockcount ;
		    
			int count = connection.createStatement().executeUpdate(strSql);
			System.out.println("executeUpdate sql: " + strSql);
			System.out.println("executeUpdate result rows: " + count);
			

			if(null != ps) ps.close();
			connection.close();

				if (0 == count) {
					response.getOutputStream()
							.write(("{\"code\":205, \"msg\":\"System error, errcode:11.\", \"id\":"
									+ str_id + "}").toString()
									.getBytes("UTF-8"));
					return;
				}
				response.getOutputStream().write(
						("{\"code\":0, \"msg\":\"It is successful.\", \"id\":"
								+ str_id + "}").toString().getBytes("UTF-8"));

				return;

			} catch (Exception ex) {
				ex.printStackTrace();
				response.getOutputStream()
						.write(("{\"code\":203, \"msg\":\"System error, errcode:10.\", \"id\":"
								+ str_id + "}").toString().getBytes("UTF-8"));
			}

		}

}

