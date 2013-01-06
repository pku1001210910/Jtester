package core.analysis.staticanalysis.available_exp;

import core.common.cfg.interfaces.IBasicBlock;
import core.common.model.functionblock.AnalysisBlockImpl;

public class AvbExpBlock extends AnalysisBlockImpl {

	public AvbExpBlock(IBasicBlock bb, int lable) {
		super(bb, lable);
	}

	// 这个地方还要好好考虑一下，现在的想法是不论是向前分析还是向后分析，先将传入的表达式放入_temp，然后对_temp进行kill和gen的操作
	public void tansfer() {
		// 清空_temp
		this._temp.clear();
		// 将_entry的元素加到_temp
		if (this._entry.size() != 0) {
			for (int i = 0; i < this._entry.size(); i++) {
				this._temp.add(this._entry.get(i));
			}
		}
		this.gen();
		this.kill();

		// 将_temp的元素赋给_exit
		if (this._temp.size() != 0) {
			this._exit.clear();
			for (int i = 0; i < this._temp.size(); i++) {
				this._exit.add(this._temp.get(i));
			}
		}
	}
}
