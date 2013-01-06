package core.common.model.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestResult {
	private int totalFileNum;
	private int filesCheckNum;
	private String filePath;
	private String rule;
	
	private HashMap<String, ArrayList<TestResultItem>> result;

	public TestResult() {
		result = new HashMap<String, ArrayList<TestResultItem>>();
	}

	public void add(String filePath, TestResultItem item) {
		ArrayList<TestResultItem> list = result.get(filePath);
		if(list == null){
			list = new ArrayList<TestResultItem>();
		}

		list.add(item);
		result.put(filePath, list);
	}
	
	public HashMap<String, ArrayList<TestResultItem>> getResult(){
		return result;
	}
	
	public int getTotalFileNum(){
		return totalFileNum;
	}
	
	public int getFilesCheckNum(){
		return filesCheckNum;
	}
	
	public void increaseFileCheckNum(){
		filesCheckNum++;
	}
	
	public void setTotalFileNum(int num){
		totalFileNum = num;
	}
	
	public void setCurrentFilePath(String path){
		filePath = path;
	}
	
	public String getCurrentFilePath(){
		return filePath;
	}
	
	public void setCurrentRule(String name){
		rule = name;
	}
	public String getCurrentRule(){
		return rule;
	}
}
