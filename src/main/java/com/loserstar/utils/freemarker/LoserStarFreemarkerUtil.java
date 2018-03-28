package com.loserstar.utils.freemarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.loserstar.utils.file.LoserStarFileUtil;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class LoserStarFreemarkerUtil {
/*	Freemarker提供了3种加载模板目录的方法。 它使用Configuration类加载模板。
	三种方法分别是：
	public void setClassForTemplateLoading(Class clazz, String pathPrefix);
	public void setDirectoryForTemplateLoading(File dir) throws IOException;
	public void setServletContextForTemplateLoading(Object servletContext, String path);

	第一种：基于类路径，HttpWeb包下的framemaker.ftl文件
	  configuration.setClassForTemplateLoading(this.getClass(), "/HttpWeb");
	configuration.getTemplate("framemaker.ftl"); //framemaker.ftl为要装载的模板 
	第二种：基于文件系统
	configuration.setDirectoryForTemplateLoading(new File("/template"))
	configuration.getTemplate("framemaker.ftl"); //framemaker.ftl为要装载的模板
	第三种：基于Servlet Context，指的是基于WebRoot下的template下的framemaker.ftl文件
	HttpServletRequest request = ServletActionContext.getRequest();
	configuration.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/template");
	configuration.getTemplate("framemaker.ftl"); //framemaker.ftl为要装载的模板
*/
	



	public static void main(String[] args) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setDefaultEncoding("UTF-8");
		/**
		 * https://www.cnblogs.com/yasepix/p/6283726.html
		 * 其实这个方法是根据类加载路径来判断的，最终会执行以下代码：
			FreemarkerUtil.class.getClassLoader().getResource("/template/");
			打出出来发现路径是
			file:/E:/loserStarWorkSpace/loserStarUtils/target/classes/
			所以第二个参数得加上包路径，但是mvn编译并不会把src目录下的除*.java之外的文件打包过去，所以我们的ftl文件得放到resources目录的同包名目录下
		 */
		cfg.setClassForTemplateLoading(LoserStarFreemarkerUtil.class, "/com/loserstar/utils/freemarker");
		System.out.println(LoserStarFreemarkerUtil.class.getResource("/"));
//		cfg.setDirectoryForTemplateLoading(new File("E:\\loserStarWorkSpace\\loserStarUtils\\src\\main\\java\\com\\loserstar\\utils\\freemarker"));
		Map<String, Object> map = new HashMap<>();
		try {
			Template temp =cfg.getTemplate("test.ftl");
			map.put("data", "loserStar freemakrer!!!!!");
			StringWriter stringWriter = new StringWriter();
			temp.process(map,stringWriter);
			System.out.println(stringWriter.toString());
			String filePath = "c://loserStarFreemarkerTemplate.txt";
			LoserStarFileUtil.WriterForFile(filePath, stringWriter.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}