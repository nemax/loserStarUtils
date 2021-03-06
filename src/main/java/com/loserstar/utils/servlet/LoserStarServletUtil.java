/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * email:362527240@qq.com
 * github:https://github.com/xinxin321198
 * remarks:
 */
package com.loserstar.utils.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.loserstar.utils.file.LoserStarFileUtil;
import com.loserstar.utils.proerties.LoserStarPropertiesUtil;

/**
 * author: loserStar
 * date: 2018年4月25日下午6:22:05
 * remarks:
 */
@WebServlet("/aa")
public class LoserStarServletUtil extends HttpServlet{
protected HttpServletRequest request;
protected HttpServletResponse response;

	
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.request = req;
		this.response = resp;
		super.service(req, resp);
	}

	/**
	 * 获取文件上传路径
	 * @return
	 */
	protected String getFileLoadPath() {
		String filePath = getPropertiesPath()+"file-service.properties";
		Properties properties = new Properties(); 
		try {
			properties.load(new FileReader(filePath));
			String fileUploadDir = properties.getProperty("kaen.uploaddir","upload");
			return getRealPath()+fileUploadDir+File.separator;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 得到项目绝对路径（末尾带斜杠）
	 * @return
	 */
	protected String getRealPath() {
		String root = request.getServletContext().getRealPath(File.separator);
		 if (!root.endsWith(java.io.File.separator)) {
		        root = root + java.io.File.separator;
	        }
		return root;
	}
	/**
	 * 得到配置文件目录
	 * @return
	 */
	protected String getPropertiesPath() {
		return getRealPath()+"WEB-INF"+File.separator+"classes"+File.separator;
	}
	
	
	
	  // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // 上传配置
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	/**
	 * 上传文件，返回新的文件名
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileUploadException 
	 */
	protected String uploadFile( ) throws IOException, FileUploadException {
		// 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return "";
        }
     // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> formItems = upload.parseRequest(request);
		 if (formItems != null && formItems.size() > 0) {
             // 迭代表单数据
             for (FileItem item : formItems) {
                 // 处理不在表单中的字段
                 if (!item.isFormField()) {
            		 String uploadDir = LoserStarPropertiesUtil.getProperties(request.getServletContext().getRealPath("配置文件路径")).getProperty("kaen.uploaddir");//获取文件上传路径
            		 uploadFile(uploadDir,item);
                     request.setAttribute("message",
                         "文件上传成功!");
                 }
             }
         }
		 return "";
	}
	
	/**
	 * 上传文件到某个绝对路径
	 * @param uploadDir 系统的绝对路径
	 * @param file 文件对象
	 * @return
	 * @throws IOException
	 */
	protected String uploadFile(String uploadDir,FileItem file) throws IOException {
		String fileRealName = file.getName();                   //获得原始文件名
		LoserStarFileUtil.createDir(uploadDir);//创建上传路径
		String newFileName = LoserStarFileUtil.generateUUIDFileName(fileRealName);//生成新文件名
		LoserStarFileUtil.WriteInputStreamToFilePath(file.getInputStream(),uploadDir+newFileName, false);//输出文件
		return newFileName;
	}
	/**
	 * 下载配置文件配置的路径下的某个文件
	 * @param fileUrl 文件的uuid文件名（带后缀）
	 * @param downName 下载时的名称
	 * @throws Exception 
	 */
	protected void downloadPropertiesDirFile(String fileUrl,String downName) throws Exception {
		String uploadDir = LoserStarPropertiesUtil.getProperties(request.getServletContext().getRealPath("配置文件路径")).getProperty("kaen.uploaddir");//获取文件上传路径
		String downloadFilePath = uploadDir+fileUrl;
		downloadFile(downloadFilePath,downName);
	}
	
	/**
	 * 下载文件，指定一个文件的绝对路径以及下载时显示的文件名
	 * @param downloadFilePath 文件的绝对路径
	 * @param downName 下载时的名称,为null时默认取真实的该文件名称
	 * @throws Exception
	 */
	protected void downloadFile(String downloadFilePath,String downName) throws Exception {
		InputStream inputStream;
		OutputStream outputStream ;
		try {
			File file = new File(downloadFilePath);
			if (downName==null||downName.equals("")) {
				downName = file.getName();
			}
			inputStream = new FileInputStream(file);
			outputStream = response.getOutputStream();
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(java.net.URLEncoder.encode(downName, "UTF-8")));
			response.addHeader("Content-Length", "" + file.length());
			response.setContentType("application/octet-stream");
			LoserStarFileUtil.WriteInputStreamToOutputStream(inputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
