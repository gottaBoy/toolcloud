package siddur.tool.core;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import siddur.common.miscellaneous.Constants;
import siddur.common.miscellaneous.FileSystemUtil;
import siddur.tool.core.data.ToolDescriptor;
import siddur.tool.core.data.XmlUtil;

public class ToolLoader {

	private static final Logger log4j = Logger.getLogger(ToolLoader.class);
	
	public ToolDescriptor loadConfig(File toolDir)throws Exception{
		File config = new File(toolDir, Constants.TOOL_PLUGIN_FILENAME);
		if(!config.exists()){
			throw new Exception("config.xml not found");
		}
		
		ToolDescriptor td = XmlUtil.fromXml(config);
		
		return td;
	}
	
	public IToolWrapper loadTool(String toolID) throws Exception{
		File toolDir = new File(FileSystemUtil.getToolDir(), toolID);
		return loadTool(toolDir);
	}
	
	public IToolWrapper loadTool(File toolDir) throws Exception{
		ToolDescriptor td = loadConfig(toolDir);
		
		if("java".equals(td.getLang())){
			return loadJavaTool(toolDir, td);
		}else{
			return loadScriptTool(toolDir, td);
		}
	}
	
	private JavaToolWrapper loadJavaTool(File toolDir, ToolDescriptor td) throws Exception{
		JavaToolWrapper jtw = new JavaToolWrapper();
		File[] tools = toolDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jar");
			}
		});
		
		try {
			URL[] urls = new URL[tools.length];
			for (int i = 0; i < urls.length; i++) {
				urls[i] = tools[i].toURI().toURL();
			}
			URLClassLoader rcl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
			
			for(File j : tools){
				JarFile jarFile = new JarFile(j);
				Enumeration<JarEntry> en = jarFile.entries();
				while(en.hasMoreElements()){
					JarEntry entry = en.nextElement();
					String classname = entry.getName();
					if(classname.endsWith(".class")){
						classname = classname.substring(0, classname.length() - 6);
						classname = classname.replace("/", ".").replace("\\", ".");
						Class<?> claz = rcl.loadClass(classname);
						
						if(ITool.class.isAssignableFrom(claz)){
							jtw.setClassname(claz.getName());
							jtw.setToolfile(j.getCanonicalPath());
							jtw.setDescriptor(td);
							return jtw;
						}
					}
				}
			}
		} catch (Exception e) {
			log4j.warn(e);
		} 
		throw new Exception("Cannot find tool class from directory " + toolDir.getName());
	}
	
	private ScriptToolWrapper loadScriptTool(File toolDir, ToolDescriptor td) throws Exception{
		ScriptToolWrapper stw = new ScriptToolWrapper();
		File[] files = toolDir.listFiles();
		for (File file : files) {
			if(!file.getName().equals(Constants.TOOL_PLUGIN_FILENAME)){
				stw.setToolfile(file.getCanonicalPath());
				return stw;
			}
		}
		throw new Exception("Cannot find script file from directory " + toolDir.getName());
	}
}
