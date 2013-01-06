package core.common.model.test;

import java.io.IOException;
import java.util.HashMap;

import core.common.util.IOUtil;

public class TestFile {
	private String path;
	private String code;
	
	private HashMap<String, Object> attachments;

	public TestFile(String path) {
		this.path = path;
		this.code = "";
		attachments = new HashMap<String, Object>();
	}

	public void accept(String path) throws IOException{
		this.code = IOUtil.read(path);
	}
	
	public Object get(String key){
		return attachments.get(key);
	}
	
	public String getPath(){
		return path;
	}
	
	public void put(String key, Object value){
		attachments.put(key, value);
	}
	
	public String getCode(){
		return code;
	}
}