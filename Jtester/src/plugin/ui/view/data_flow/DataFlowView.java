package plugin.ui.view.data_flow;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.swt.widgets.Label;

public class DataFlowView extends ViewPart{
	public DataFlowView() {
	}
	// 创建一个容器，所有界面在此基础上展示	
	private Composite topComp;
	
	// 创建一个TableViewer对象
	private static TableViewer tv;
	private static Label reportLabel;
	private static Combo combo; 
		
	public void createPartControl(Composite parent) {
		topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new FillLayout());
		
		ViewForm viewForm = new ViewForm(topComp, SWT.NONE);
		viewForm.setLayout(new FillLayout());
		
		// 式样：MULTI可多选、H_SCROLL有水平滚动条、V_SCROLL有垂直滚动条、BORDER有边框、FULL_SELECTION整行选择
		tv = new TableViewer(viewForm, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		
		// 创建工具栏
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT);
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);
		
		// 生成一个ReportViewActionGroup对象，并调用fillContextMenu方法将按钮注入到菜单对象中
		DataFlowViewActionGroup actionGroup = new DataFlowViewActionGroup(tv);
		actionGroup.fillActionToolBars(toolBarManager);
		
		// 设置表格和工具栏在布局中的位置
		viewForm.setContent(tv.getControl()); // 主题是表格
		viewForm.setTopRight(toolBar); // 顶端中间是工具栏
		
		// 设置通知栏
		reportLabel = new Label(viewForm, SWT.NONE);
		viewForm.setTopLeft(reportLabel);
		reportLabel.setText(" No report to display at this time");
		
		// 设置 报告内容选框
		combo = new Combo(viewForm, SWT.READ_ONLY);
		combo.setLayoutData(new FillLayout());
		combo.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
			}
		});
		viewForm.setTopCenter(combo);
		
		// 通过表格内含的Table对象设置布局方式
		Table table = tv.getTable();
		table.setHeaderVisible(true); // 显示表头
		table.setLinesVisible(true); // 显示表格线
		TableLayout layout = new TableLayout(); // 专用于表格的布局
		table.setLayout(layout);
		
		// 用TableColumn类创建表格列

		// source code line列
		layout.addColumnData(new ColumnWeightData(50));
		TableColumn line = new TableColumn(table, SWT.NONE);
		line.setText("line");

		// source code列
		layout.addColumnData(new ColumnWeightData(120));
		TableColumn column_Product = new TableColumn(table, SWT.NONE);
		column_Product.setText("source code");
		
		// entry列
		layout.addColumnData(new ColumnWeightData(120));
		new TableColumn(table, SWT.NONE).setText("entry");
		
		// exit列
		layout.addColumnData(new ColumnWeightData(120));
		new TableColumn(table, SWT.NONE).setText("exit");
		
		// 设置内容器和标签器
		tv.setContentProvider(new DataFlowViewContentProvider());
		tv.setLabelProvider(new DataFlowViewLabelProvider());
	}
	
	// 用于获取tv
	public static TableViewer getTableViewer(){
		return tv;
	}
	
	public static Label getReportLabel(){
		return reportLabel;
	}
		
	public void setFocus() {
	}

}
