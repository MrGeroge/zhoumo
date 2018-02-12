package net.zypro.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.sun.tracing.dtrace.ArgsAttributes;

/**
 * Servlet implementation class OutputServlet
 */
@WebServlet("/Output")
public class OutputServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;
    
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		HttpServletRequest request=(HttpServletRequest)arg0;
		
		// TODO Auto-generated method stub
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		JSONObject fileList = new JSONObject();
		
		if(name.equals("zy")&&password.equals("1234"))
		{
			fileList.put("status", "success");
		}else {
			fileList.put("status", "failed");
		}
		
		
		arg1.setContentType("application/json;charset=GBK");
	       
	    PrintWriter out =arg1.getWriter();
	    fileList.put("name", request.getParameter("name"));
	    out.print(fileList.toString());
	    out.close();
	}
}
