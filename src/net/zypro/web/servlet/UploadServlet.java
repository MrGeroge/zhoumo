package net.zypro.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

@WebServlet("/upload")
// 设置内存缓冲区的阀值为2M，设置临时存储文件的目录为E:\temp，
// 设置上传文件的最大数据量为10M（默认值为-1，表示没有限制）
@MultipartConfig(fileSizeThreshold=0x200000,
    location="F:\\J2eeworkplace\\zhoumo\\WebContent\\uploadFiles\\temp",
    maxFileSize=0xA00000)
public class UploadServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  protected void service(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("application/json;charset=GBK");
    
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    
    PrintWriter out = response.getWriter();
    JSONObject fileList = new JSONObject(); 
    // 得到请求中所有的Part对象
    Collection<Part> parts = request.getParts();
    int count=0;
    
    for(Part part : parts)
    {
    	count++;
    	JSONObject fileObj= new JSONObject(); 
        if(part.getContentType() != null)
        {
    	
        String fileName = getFileName(part);
        
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
   
        // 将文件保存到Web应用程序根目录下的uploadFiles目录下
        File file = new File(getServletContext().getRealPath("/uploadFiles"));
        if(!file.exists())
          file.mkdir();
        
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
        String filePath=file.getPath() + "/"+newFileName;
        part.write(filePath);
        fileObj.put("status", "success");
        fileObj.put("filePath", basePath+"uploadFiles/"+newFileName);
      }else {
    	  fileObj.put("status", "failed");
    	  fileObj.put("message", "非图像类型");
	  }
      fileList.put(String.valueOf(count), fileObj);      
    }
    System.out.print(fileList.toString());
    out.print(fileList.toString());
    out.close();
    
  }
  
  /**
   * 解析Part对象的报头值，得到上传文件的文件名
   * @param part 代表文件的Part对象
   * @return 文件名
   */
  private String getFileName(Part part)
  {
    String headerValue = part.getHeader("content-disposition");
    int index = headerValue.lastIndexOf("filename");
    String fileName = null;
    if(index != -1)
    {
      // 截取 filename=" 之后的内容 
      fileName = headerValue.substring("filename=".length() + index);
      // 去掉最后的双引号
      fileName = fileName.substring(0, fileName.length() - 1); 
      // 去掉文件名前面的路径部分
      fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
      
    }	
    return fileName;
  }
}
