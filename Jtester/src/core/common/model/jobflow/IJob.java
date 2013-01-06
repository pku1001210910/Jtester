package core.common.model.jobflow;

import core.common.model.test.TestData;

public interface IJob {
	public boolean run(TestData data);
	public String getName();
}