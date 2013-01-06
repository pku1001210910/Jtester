package core.common.model.jobflow;

import java.util.List;
import core.common.model.test.TestData;
import core.common.model.test.TestResult;

public class JobFlow {
	private List<IJob> jobs;
	private ICaller caller;

	public JobFlow(ICaller caller, TestData data) {
		this.jobs = data.getTestConfiguration().getJobs();
		this.caller = caller;
	}

	public TestResult run(TestData data) {
		TestResult result = data.getTestResult();
		result.setTotalFileNum(data.size());
		
		// file loop
		for (int fileNum = 0; fileNum < data.size(); fileNum++) {
			result.setCurrentFilePath(data.getCurrentTestFile().getPath());
			// rule loop
			for (int step = 0; step < jobs.size(); step++) {
				result.setCurrentRule(jobs.get(step).getName());
				IJob job = jobs.get(step);
				boolean succeed = job.run(data);
				
				if(!callback(result)){
					return result;
				}
				
				if (!succeed) {
					break;
				}
			}
			
			try {
				// Slow the file checking process 
				// Only for Demo
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			data.next();
			result.increaseFileCheckNum();
			
			if(!callback(result)){
				return result;
			}
		}
		return result;
	}

	private boolean callback(TestResult result) {
		// TODO
		// Open a new thread to callback
		if(caller != null)
			return caller.update(result);
		return true;
	}
}
