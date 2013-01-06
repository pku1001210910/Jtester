package plugin.run;

import java.util.List;

import core.analysis.api.API;
import core.common.model.jobflow.ICaller;

public class JtesterCaller extends Thread {
	private boolean isDelay;
	private int delay;

	private List<String> paths;
	private List<String> rules;
	private ICaller caller;

	public JtesterCaller(List<String> paths, List<String> rules, ICaller caller) {
		this.paths = paths;
		this.rules = rules;
		this.caller = caller;

		isDelay = false;
		delay = 1500;
	}

	public void run() {
		try {
			if (isDelay) {
				sleep(delay);
			}
			API.analyze(paths, rules, caller);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
