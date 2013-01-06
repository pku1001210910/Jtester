package core.common.model.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestData {
	private List<TestFile> files;

	private TestConfiguration config;

	private TestResult result;

	private int currentFileNum;

	public TestData() {
		files = new ArrayList<TestFile>();
		config = new TestConfiguration();
		result = new TestResult();
		currentFileNum = 0;
	}

	public void accept(List<String> paths) throws IOException {
		for (int i = 0; i < paths.size(); i++) {
			addFile(paths.get(i));
		}
	}

	public void addFile(String filePath) throws IOException {
		TestFile file = new TestFile(filePath);
		file.accept(filePath);
		files.add(file);
	}

	public List<TestFile> getFiles() {
		return files;
	}

	public int size() {
		return files.size();
	}

	public TestConfiguration getTestConfiguration() {
		return config;
	}

	public TestResult getTestResult() {
		return result;
	}

	public TestFile getCurrentTestFile() {
		return files.get(currentFileNum);
	}

	public void next() {
		currentFileNum++;
	}
}
