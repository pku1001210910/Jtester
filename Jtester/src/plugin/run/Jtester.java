package plugin.run;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import core.analysis.api.RuleSet;

import plugin.ui.dialog.progress.ProgressDialog;
import plugin.util.IOUtil;

public class Jtester implements IWorkbenchWindowActionDelegate{
	private ISelection selection;
	private IWorkbenchWindow window;
	
	public void run(IAction action) {
		List<String> filePaths = IOUtil.getSelectionPath(selection);
		List<String> rules = retrieveRules();
		
		JtesterProgress progress = new JtesterProgress(filePaths.size());
		
		JtesterCaller caller = new JtesterCaller(filePaths, rules, progress);
		caller.start();
		
		ProgressDialog dialog = new ProgressDialog(null, progress);
		progress.register(dialog);
		dialog.run();
	
	}

	public List<String> retrieveRules(){
		List<String> rules = new ArrayList<String>();
		rules.add(RuleSet.FUNCTION_INFO_VISITOR);
		rules.add(RuleSet.AVAILABLE_EXP);
		rules.add(RuleSet.VERY_BUSY_EXP);
		rules.add(RuleSet.REACHING_DEF);
		rules.add(RuleSet.LIVE_VAR);
		rules.add(RuleSet.CONST_PROPAGATION);
		return rules;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		 this.selection = selection;
	}
	
	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
