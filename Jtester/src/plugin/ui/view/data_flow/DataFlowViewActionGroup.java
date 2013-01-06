package plugin.ui.view.data_flow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.actions.ActionGroup;

public class DataFlowViewActionGroup extends ActionGroup {
	private TableViewer tv;
	
	// 在Action要使用到TableViewer对象，所以通过构造函数把它传进来
	public DataFlowViewActionGroup(TableViewer tv){
		this.tv = tv;
	}
	
	// 自定义方法。生成Action对象，并通过工具栏管理器ToolBarManager填充进工具栏
	public void fillActionToolBars(ToolBarManager actionBarManager) {
		// 创建Action对象，一个按钮对应一个个的Action
		Action ClearScreenAction = new ClearScreenAction();
		actionBarManager.add(createContributionItem(ClearScreenAction));
		actionBarManager.update(true);// 更新工具栏，否则工具栏不显示任何按钮
	}
	
	// 把包装的处理过程写成了一个方法
	private IContributionItem createContributionItem(IAction action) {
		ActionContributionItem aci = new ActionContributionItem(action);
		aci.setMode(ActionContributionItem.MODE_FORCE_TEXT);// 显示图像+文字
		return aci;
	}
	
	private class ClearScreenAction extends Action{
		public ClearScreenAction(){
			setText("Clear");
		}
		public void run(){
		}
	}
}
