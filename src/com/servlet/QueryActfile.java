package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Servlet implementation class QueryActfile
 */
@WebServlet("/queryactfile")
public class QueryActfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryActfile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			int nPages = 0, nPagenum = 10;
			String strPage = request.getParameter("page");
			String strPagenum = request.getParameter("pnum");	
			String strID = request.getParameter("videoid");
			if(null != strPage){
				nPages = Integer.parseInt(strPage);
			}
			if(null != strPagenum){
				nPagenum = Integer.parseInt(strPagenum);
				if(nPagenum > 100)
					nPagenum = 100;
			}
			
			if(null == strID)
			{
				response.getOutputStream().write("{\"code\":201, \"msg\":\"parameter invalided.\"}".toString().getBytes("UTF-8"));
				return;
			}
			
			
			InitialContext ctx = new InitialContext();
			DataSource datasource = (DataSource) ctx.lookup("java:comp/env/jdbc/OtaDB");
			Connection connection = datasource.getConnection();
			String strSql = "select * from t_actfile where video_id =" + strID + " limit " + nPages*nPagenum + "," + nPagenum;
			rs = connection.createStatement().executeQuery(strSql);
			int count = 0;
			
			
			String str_id           = "";
			String str_name         = "";
			String str_category     = "";
			String str_scategory    = "";
			String str_videoid      = "";
			String str_actfile_url  = "";
			String str_date         = "";
			String str_filesize     = "";
			String str_author       = "";
			String str_md5          = "";
			String str_version      = "";
			String str_description  = "";
			JSONStringer stringer = new JSONStringer();
			  stringer.array();
			  while (rs.next()) {
				  count++;
				str_id              = rs.getString("actfile_id"             );
				str_name            = rs.getString("actfile_name"           );
				str_category        = rs.getString("category"       );
				str_scategory       = rs.getString("scategory"      );
				str_videoid         = rs.getString("video_id"        );
				str_actfile_url     = rs.getString("url"    );
				str_date            = rs.getString("lasttime"           );
				str_filesize        = rs.getString("filesize"       );
				str_author          = rs.getString("author"         );
				str_md5             = rs.getString("md5"            );
				str_version         = rs.getString("version"        );
				str_description     = rs.getString("description"    );
				
				if(null == str_category || str_category.equals("")){
					str_category = "0";  
				}
				
				if(null ==str_filesize || str_filesize.equals("")){
					str_filesize = "0";  
				}
				
				
				stringer.object().key("id"         ).value(Long.parseLong(str_id)          )
                .key("name"       ).value(str_name        )
                .key("category"   ).value(Integer.parseInt(str_category))
                .key("scategory"  ).value(str_scategory   )
                .key("videoid"    ).value(Long.parseLong(str_videoid)     )
                .key("actfile_url").value(str_actfile_url )
                .key("date"       ).value(str_date        )
                .key("filesize"   ).value(Integer.parseInt(str_filesize    ))
                .key("author"     ).value(str_author      )
                .key("md5"        ).value(str_md5         )
                .key("version"    ).value(str_version     )
                .key("description").value(str_description )
                .endObject();
			}	
			stringer.endArray();
			if(null != rs) rs.close();
			if(null != ps) ps.close();
			connection.close();
			
			
			if(0 == count){
				response.getOutputStream().write("{\"code\":205, \"msg\":\"no data.\"}".toString().getBytes("UTF-8"));
				return;
			}
			response.getOutputStream().write("{\"code\":0, \"msg\":\"it is successful\", \"object\":".toString().getBytes("UTF-8"));
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
			response.getOutputStream().write(" }".toString().getBytes("UTF-8"));
			response.setContentType("text/json; charset=UTF-8");
			

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
