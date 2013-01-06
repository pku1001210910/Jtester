package core.analysis.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.common.model.jobflow.ICaller;
import core.common.model.jobflow.JobFlow;
import core.common.model.test.TestData;
import core.common.model.test.TestResult;
import core.common.model.test.TestResultItem;

public class API {
	public static TestResult analyze(List<String> filePaths, List<String> rules, ICaller caller){
		if(filePaths == null || rules == null){
			System.err.println("Null filePaths or methods");
			return null;
		}
		
		TestData data = new TestData();
		try {
			data.accept(filePaths);
		} catch (IOException e) {
			System.err.println("Invalid filePaths");
		}
		
		try {
			data.getTestConfiguration().accept(rules);
		} catch (ClassNotFoundException e) {
			System.err.println("undefined testing rules");
		} catch (InstantiationException e) {
			System.err.println("rule can't create an instant :"+e);
		} catch (IllegalAccessException e) {
			System.err.println("rule can't be accessed :"+e);
		}
		
		JobFlow flow = new JobFlow(caller, data);
		flow.run(data);
		return data.getTestResult();
	}
	
	public static void main(String args[]) {
		List<String> paths = new ArrayList<String>();
//		paths.add("D:/JavaTest.java");
//		paths.add("D:/JavaTest1.java");
//		paths.add("D:/JavaTest2.java");
		paths.add("D:/JavaTest3.java");
		List<String> rules = new ArrayList<String>();
		rules.add(RuleSet.FUNCTION_INFO_VISITOR);
//		rules.add(RuleSet.AVAILABLE_EXP);
//		rules.add(RuleSet.VERY_BUSY_EXP);
//		rules.add(RuleSet.LIVE_VAR);
//		rules.add(RuleSet.REACHING_DEF);
		rules.add(RuleSet.CONST_PROPAGATION);
		TestResult result = analyze(paths, rules, null);
	
		ArrayList<TestResultItem> items = result.getResult().get("D:/JavaTest3.java");
		String lastRule = "";
		for (int i = 0; i < items.size(); i++) {
			if (lastRule != items.get(i).getRule()) {
				System.out.println("\npath: " + items.get(i).getFilePath() + " rule: " + items.get(i).getRule() + " title: " + items.get(i).getTitle());
				lastRule = items.get(i).getRule();
			}
			System.out.println("contents: " + items.get(i).getDetail());
		}
		
//		items = result.getResult().get("D:/JavaTest1.java");
//		lastRule = "";
//		for (int i = 0; i < items.size(); i++) {
//			if (lastRule != items.get(i).getRule()) {
//				System.out.println("\npath: " + items.get(i).getFilePath() + " rule: " + items.get(i).getRule() + " title: " + items.get(i).getTitle());
//				lastRule = items.get(i).getRule();
//			}
//			System.out.println("contents: " + items.get(i).getDetail());
//		}
//		
//		items = result.getResult().get("D:/JavaTest2.java");
//		lastRule = "";
//		for (int i = 0; i < items.size(); i++) {
//			if (lastRule != items.get(i).getRule()) {
//				System.out.println("\npath: " + items.get(i).getFilePath() + " rule: " + items.get(i).getRule() + " title: " + items.get(i).getTitle());
//				lastRule = items.get(i).getRule();
//			}
//			System.out.println("contents: " + items.get(i).getDetail());
//		}
	}
}
