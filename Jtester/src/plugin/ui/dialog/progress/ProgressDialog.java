package plugin.ui.dialog.progress;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import plugin.configuration.Const;
import plugin.run.JtesterProgress;
import plugin.util.ConsoleFactory;

import core.common.model.jobflow.ICaller;
import core.common.model.test.TestResult;

public class ProgressDialog extends Dialog implements ICaller, Runnable {
	private static String DEFAULT_TASKNAME = JFaceResources
			.getString("ProgressMonitorDialog.message");

	private static int LABEL_DLUS = 21;
	private static int BAR_DLUS = 9;

	protected ProgressIndicator progressIndicator;
	protected Button stop;
	protected Button close;
	protected boolean operationClosableState = false;
	protected boolean operationStoppableState = true;
	protected boolean enableCloseButton;
	protected boolean enableStopButton;

	private ProgressMonitor progressMonitor = new ProgressMonitor();
	private String task;
	private int nestingDepth;
	private boolean openOnRun = true;

	private JtesterProgress jtesterProgress;
	private Label files_check_v;
	private Label files_skipped_v;
	private Label failed_runs_v;
	private Label violations_found_v;
	private Label violations_suppressed_v;
	private Label rules_violated_v;
	private Label file_under_check_v;

	private class ProgressMonitor implements IProgressMonitorWithBlocking {
		private String fSubTask = "";

		private volatile boolean fIsStopped;
		protected boolean forked = false;
		protected boolean locked = false;

		public void beginTask(String name, int totalWork) {
			if (progressIndicator.isDisposed()) {
				return;
			}

			setTaskName(name);

			if (totalWork == UNKNOWN) {
				progressIndicator.beginAnimatedTask();
			} else {
				progressIndicator.beginTask(totalWork);
			}
		}

		public void done() {
			if (!progressIndicator.isDisposed()) {
				progressIndicator.sendRemainingWork();
				progressIndicator.done();
			}
		}

		public void setTaskName(String name) {
			if (name == null) {
				task = "";
			} else {
				task = name;
			}
			String s = task;
			if (s.length() <= 0) {
				s = DEFAULT_TASKNAME;
			}
		}

		public boolean isCanceled() {
			return fIsStopped;
		}

		public void setCanceled(boolean b) {
			fIsStopped = b;
			if (locked) {
				clearBlocked();
			}
		}

		public void subTask(String name) {
			if (name == null) {
				fSubTask = "";
			} else {
				fSubTask = name;
			}
		}

		public void worked(int work) {
			internalWorked(work);
		}

		public void internalWorked(double work) {
			if (!progressIndicator.isDisposed()) {
				progressIndicator.worked(work);
			}
		}

		public void clearBlocked() {
			if (getShell() == null || getShell().isDisposed())
				return;
			locked = false;
			updateForClearBlocked();
		}

		public void setBlocked(IStatus reason) {
			if (getShell() == null || getShell().isDisposed())
				return;
			locked = true;
			updateForSetBlocked(reason);
		}
	}

	protected void updateForClearBlocked() {
		progressIndicator.showNormal();

	}

	protected void updateForSetBlocked(IStatus reason) {
		progressIndicator.showPaused();
	}

	public ProgressDialog(Shell parent, JtesterProgress progress) {
		super(parent);
		if (isResizable()) {
			setShellStyle(getDefaultOrientation() | SWT.BORDER | SWT.TITLE
					| SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.MAX);
		} else {
			setShellStyle(getDefaultOrientation() | SWT.BORDER | SWT.TITLE
					| SWT.APPLICATION_MODAL);
		}
		setBlockOnOpen(false);

		jtesterProgress = progress;
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.STOP_ID == buttonId) {
			stopPressed();
			setClosable(true);
		} else if (IDialogConstants.CLOSE_ID == buttonId) {
			closePressed();
		}
	}

	protected void stopPressed() {
		asyncSetOperationStopButton(false);
		jtesterProgress.stop();
	}

	protected void closePressed() {
		close.setEnabled(false);
		super.close();
	}

	public boolean close() {
		if (getNestingDepth() <= 0) {
			clearCursors();
			return super.close();
		}
		return false;
	}

	protected void clearCursors() {
		if (close != null && !close.isDisposed()) {
			close.setCursor(null);
		}
		Shell shell = getShell();
		if (shell != null && !shell.isDisposed()) {
			shell.setCursor(null);
		}
	}

	protected void configureShell(final Shell shell) {
		super.configureShell(shell);

		// TODO
		// set shell text using configuration
		shell.setText(Const.DATA_FLOW);
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createStopButton(parent);
		createCloseButton(parent);
	}

	protected void createStopButton(Composite parent) {
		stop = createButton(parent, IDialogConstants.STOP_ID,
				IDialogConstants.STOP_LABEL, true);

		setOperationStopButton(enableStopButton);
	}

	public void setStoppable(boolean stoppable) {
		if (stop == null) {
			enableStopButton = stoppable;
		} else {
			asyncSetOperationStopButton(stoppable);
		}
	}

	private void asyncSetOperationStopButton(final boolean b) {
		if (getShell() != null) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					setOperationStopButton(b);
				}
			});
		}
	}

	protected void setOperationStopButton(boolean b) {
		operationStoppableState = b;
		if (stop != null && !stop.isDisposed()) {
			stop.setEnabled(b);
		}
	}

	protected void createCloseButton(Composite parent) {
		close = createButton(parent, IDialogConstants.CLOSE_ID,
				IDialogConstants.CLOSE_LABEL, true);

		setOperationCloseButton(enableCloseButton);
	}

	public void setClosable(boolean closable) {
		if (close == null) {
			enableCloseButton = closable;
		} else {
			asyncSetOperationCloseButton(closable);
		}
	}

	private void asyncSetOperationCloseButton(final boolean b) {
		if (getShell() != null) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					setOperationCloseButton(b);
				}
			});
		}
	}

	protected void setOperationCloseButton(boolean b) {
		operationClosableState = b;
		if (close != null && !close.isDisposed()) {
			close.setEnabled(b);
		}
	}

	protected Control createDialogArea(Composite parent) {
		Composite comp = new Composite(parent, SWT.BORDER);
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setLayout(new GridLayout(2, false));

		// Labels
		createLabels(comp);

		// empty line
		createEmptyLine(comp);
		
		// check line
		createCheckLine(comp);

		// progress indicator
		progressIndicator = new ProgressIndicator(comp);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		gd.heightHint = convertVerticalDLUsToPixels(BAR_DLUS);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		progressIndicator.setLayoutData(gd);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = convertVerticalDLUsToPixels(LABEL_DLUS);
		gd.horizontalSpan = 2;
		return parent;
	}
	
	protected void createCheckLine(Composite comp){
		Label file_under_check = new Label(comp, SWT.NONE);
		file_under_check.setText(Const.FILE_UNDER_CHECK);
		file_under_check_v = new Label(comp, SWT.NONE);
		file_under_check_v.setText(Const.EMPTY_LINE);
	}

	protected void createLabels(Composite comp) {
		Label files_check = new Label(comp, SWT.NONE);
		files_check.setText(Const.STATIC_FILES_CHEKCED);
		files_check_v = new Label(comp, SWT.NONE);
		files_check_v.setText(Const.EMPTY_LINE);

		Label files_skipped = new Label(comp, SWT.NONE);
		files_skipped.setText(Const.STATIC_FILES_SKIPPED);
		files_skipped_v = new Label(comp, SWT.NONE);
		files_skipped_v.setText(Const.EMPTY_LINE);

		Label failed_runs = new Label(comp, SWT.NONE);
		failed_runs.setText(Const.STATIC_FAILED_RUNS);
		failed_runs_v = new Label(comp, SWT.NONE);
		failed_runs_v.setText(Const.EMPTY_LINE);

		Label violations_found = new Label(comp, SWT.NONE);
		violations_found.setText(Const.STATIC_VIOLATIONS_FOUND);
		violations_found_v = new Label(comp, SWT.NONE);
		violations_found_v.setText(Const.EMPTY_LINE);

		Label violations_suppressed = new Label(comp, SWT.NONE);
		violations_suppressed.setText(Const.STATIC_VIOLATIONS_SUPPRESSED);
		violations_suppressed_v = new Label(comp, SWT.NONE);
		violations_suppressed_v.setText(Const.EMPTY_LINE);

		Label rules_violated = new Label(comp, SWT.NONE);
		rules_violated.setText(Const.STATIC_NUM_OF_RULES_VIOLATED);
		rules_violated_v = new Label(comp, SWT.NONE);
		rules_violated_v.setText(Const.EMPTY_LINE);
	}

	protected void createEmptyLine(Composite comp) {
		for (int i = 0; i < 8; i++) {
			new Label(comp, SWT.NONE);
		}
	}

	protected Point getInitialSize() {
		Point calculatedSize = super.getInitialSize();
		if (calculatedSize.x < 400) {
			calculatedSize.x = 400;
		}

		if (calculatedSize.y < 400) {
			calculatedSize.y = 400;
		}

		return calculatedSize;
	}

	public IProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public void run() {
		setStoppable(true);
		setClosable(false);
		try {
			aboutToRun();
			// Let the progress monitor know if they need to update in UI Thread
			progressMonitor.forked = true;
			try {
				ModalContext.run((IRunnableWithProgress) jtesterProgress, true,
						getProgressMonitor(), getShell().getDisplay());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			finishedRun();
		}
	}

	public boolean getOpenOnRun() {
		return openOnRun;
	}

	public void setOpenOnRun(boolean openOnRun) {
		this.openOnRun = openOnRun;
	}

	protected int getNestingDepth() {
		return nestingDepth;
	}

	protected void incrementNestingDepth() {
		nestingDepth++;
	}

	protected void decrementNestingDepth() {
		nestingDepth--;
	}

	protected void aboutToRun() {
		if (getOpenOnRun()) {
			open();
		} else {
			create();
		}
		incrementNestingDepth();
	}

	protected void finishedRun() {
		decrementNestingDepth();
		close();
	}

	public int open() {
		if (!getOpenOnRun()) {
			if (getNestingDepth() == 0) {
				return OK;
			}
		}
    
		int result = super.open();
		return result;
	}
	
	public boolean update(final TestResult result) {
		if (getShell() != null && operationStoppableState) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					files_check_v.setText(result.getFilesCheckNum() + "/" + result.getTotalFileNum());
					files_skipped_v.setText("0");
					failed_runs_v.setText("0");
					violations_found_v.setText("0");
					violations_suppressed_v.setText("0");
					rules_violated_v.setText("0");
					
					// update UI
					if(result.getFilesCheckNum() == result.getTotalFileNum()){
						stop.setEnabled(false);
						setClosable(true);
					}
				}
			});
			return true;
		}
		return false;
	}
}
