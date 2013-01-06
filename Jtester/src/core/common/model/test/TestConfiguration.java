package core.common.model.test;

import java.util.ArrayList;
import java.util.List;

import core.common.model.jobflow.IJob;
import core.common.parser.JavaParser;

public class TestConfiguration {
	private List<IJob> jobs;
	
	public TestConfiguration(){
		jobs = new ArrayList<IJob>();
	}

	public void accept(List<String> methods) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		jobs.add(new JavaParser());
		for (int i = 0; i < methods.size(); i++) {
			Class<?> jobClass = Class.forName(methods.get(i));
			IJob job = (IJob) jobClass.newInstance();
			jobs.add(job);
		}
	}

	public List<IJob> getJobs() {
		return jobs;
	}
}
