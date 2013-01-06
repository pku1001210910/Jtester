package plugin.ui.view.data_flow;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

//标签器。标签器对数据集中的单个实体对象进行处理和转化，由标签器来决定实体对象中的字段显示在表格的哪一列中。
public class DataFlowViewLabelProvider implements ITableLabelProvider{
	
	// 由此方法决定数据记录在表格的每一列显示什么文字。 element参数是一个实体类对象。col是当前要设置的列的列号，0是第一列。
		public String getColumnText(Object element, int col) {
			return null; // 方法可以返回空值
		}
		
	// getColumnText方法用于显示文字，本方法用于显示图片。
	public Image getColumnImage(Object element, int col) {
		return null; // 方法可以返回空值
	}

	public void dispose() {
	}

	// -------------以下方法很少使用,先不用管，让它们空实现-----------------
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void removeListener(ILabelProviderListener listener) {
	}
}
