package core.common.model.test;

import java.util.ArrayList;
import java.util.List;

public class TestResultItem {
	private String filePath;
	private String rule;
	private String title;

	private List<String> contents;
	
	public TestResultItem(String filePath, String rule, String title) {
		this.filePath = filePath;
		this.rule = rule;
		this.title = title;
		contents = new ArrayList<String>();
	}

	public String getFilePath() {
		return filePath;
	}

	public String getRule() {
		return rule;
	}

	public String getTitle() {
		return title;
	}
	
	public void add(String detail){
		contents.add(detail);
	}
	
	public List<String> getDetail(){
		return contents;
	}
}