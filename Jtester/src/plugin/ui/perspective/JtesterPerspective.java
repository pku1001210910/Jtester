package plugin.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import plugin.ui.view.data_flow.DataFlowView;

public class JtesterPerspective implements IPerspectiveFactory{
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		// 使用package explorer
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.2f, editorArea);
		left.addView("org.eclipse.jdt.ui.PackageExplorer");
		
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.7f, editorArea);
		// 由于我把视图的ID取成和视图类的全名一样，所以用下面这种方法得到类全名
		bottom.addView(DataFlowView.class.getName());
	}
}
