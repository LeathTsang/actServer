package com.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONStringer;

/**
 * Servlet implementation class deleteActfile
 */
@WebServlet("/deleteactfile")
public class deleteActfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteActfile() {
        super();
        // TODO Auto-generated constructor stub
    }

public  boolean deleteFile(String strFilePath){
		
		if(null == strFilePath || strFilePath.equals(""))
		{
			return false;
		}
				
		if(null != strFilePath){
			File file = new File(strFilePath);  
			  // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除  
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strSql = "";
		try {
			int nType = 0;
			String strType = request.getParameter("type");
			String strID = request.getParameter("id");
			
			if(null == strType || null == strID){
				response.getOutputStream().write("{\"code\":201, \"msg\":\"parameter invalided.\"}".toString().getBytes("UTF-8"));
				return;
			
			}

			if(strType.equals("1")){ 
			   //1:按动作文件ID删除单个文件  2:按videoid删除所有动作文件
				
				 strSql = "select org_url from t_actfile where actfile_id =" + strID ;
			}
			else
			{
				 strSql = "select org_url from t_actfile where video_id =" + strID ;
				
			}
						
			
			InitialContext ctx = new InitialContext();
			DataSource datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/OtaDB");
			Connection connection = datasource.getConnection();
			rs = connection.createStatement().executeQuery(strSql);
			int count = 0;
			
			
			String str_id           = "";
			String str_videoid      = "";
			String str_org_url  = "";
			  while (rs.next()) {
				count++;
				str_org_url     = rs.getString("org_url"    );
				deleteFile(str_org_url); //delete actfile				
			}	

			
			System.out.println("There are " + count + " rows deleted relate with id(" + strID + ") and type(" + strType + ")");
			// 删除数据表里面的动作文件记录
			if (strType.equals("1")) {
				// 1:按动作文件ID删除单个文件 2:按videoid删除所有动作文件

				strSql = "delete from t_actfile where actfile_id =" + strID;
			} else {
				strSql = "delete from t_actfile where video_id =" + strID;

			}
			  
			
			count = connection.createStatement().executeUpdate(strSql);
			System.out.println("rows affected for delete is " + count );

			if(null != rs) rs.close();
			if(null != ps) ps.close();			
			connection.close();
							
			response.getOutputStream().write("{\"code\":0, \"msg\":\"it is successful\"}".toString().getBytes("UTF-8"));
			

		} catch (Exception ex) {
			ex.printStackTrace();
			response.getOutputStream().write("{\"code\":203, \"msg\":\"system internal-db error.\"}".toString().getBytes("UTF-8"));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getOutputStream().write("{\"code\":204, \"msg\":\"Post request not supported.\"}".toString().getBytes("UTF-8"));
	}

}
