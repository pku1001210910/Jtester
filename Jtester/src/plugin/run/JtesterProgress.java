package plugin.run;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import plugin.util.ConsoleFactory;

import core.common.model.jobflow.ICaller;
import core.common.model.test.TestResult;
import core.common.model.test.TestResultItem;

public class JtesterProgress implements IRunnableWithProgress, ICaller{
	private boolean stop = false;
	private int worked = 0;
	private int lastTimeNum = 0;
	
	private int progressSize;
	
	private List<ICaller> callers = new ArrayList<ICaller>();
	
	public JtesterProgress(int size) {
		progressSize = size;
	}

	public void run(IProgressMonitor monitor) {
		monitor.beginTask("开始执行......", progressSize);
		while(true){
			if (monitor.isCanceled()) // 随时监控是否选择了对话框的“取消”按钮
				return;// 中断处理
			try {
				if(!stop){
					if(worked > 0){
						monitor.worked(worked);// 进度条前进一步
						worked = 0;
					}
				}else{
					Thread.sleep(1000);
					continue;
				}
			} catch (Throwable t) {
			}
		}
	}
	
	static String lastRule="";
	static String lastFile="";
	public boolean update(TestResult result) {
		for(int i=0;i<callers.size();i++){
			if(!callers.get(i).update(result)){
				System.err.println("caller "+ callers.get(i).getClass() + " updates failed!");
				return false;
			}
		}
		
		if(result.getFilesCheckNum() > lastTimeNum){
			lastTimeNum = result.getFilesCheckNum();
			worked = 1;
		}
		
		// TODO 
		// use UI for information output instead of console
		if(lastFile != result.getCurrentFilePath()){
			lastFile = result.getCurrentFilePath();
		}
		
		if(lastRule != result.getCurrentRule()){
			lastRule = result.getCurrentRule();
		}
		
		ArrayList<TestResultItem> items = result.getResult().get(lastFile);
		if(items != null){
			for (int i = 0; i < items.size(); i++) {
				if (lastRule != items.get(i).getRule()) {
					ConsoleFactory.printToConsole("\npath: " + items.get(i).getFilePath() + " rule: " + items.get(i).getRule() + " title: " + items.get(i).getTitle());
					lastRule = items.get(i).getRule();
				}
				ConsoleFactory.printToConsole("contents: " + items.get(i).getDetail());
			}
		}
		
		return true;
	}	
	
	public void stop(){
		stop = true;
	}

	public void register(ICaller caller) {
		callers.add(caller);
	}
}
