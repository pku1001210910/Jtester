package plugin.ui.view.data_flow;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

//内容器。由此类对输入到表格的数据进行筛选和转化。
public class DataFlowViewContentProvider implements IStructuredContentProvider{
	// 对输入到表格的数据集合进行筛选和转。输入的数据集全部要转化成数组，每一个数组元素就是一个实体类对象，也就是表格中的一条记录。
		public Object[] getElements(Object element) {
			// 参数element就是通过setInput(Object input)输入的对象input。
				return new Object[0]; // 如非List类型则返回一个空数组
		}

		// 当TableViewer对象被关闭时触发执行此方法
		public void dispose() {}

		// 当TableViewer再次调用setInput()时触发执行此方法
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {}
}
