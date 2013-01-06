package core.analysis.staticanalysis.verybusy_exp;

import core.common.cfg.interfaces.IBasicBlock;
import core.common.model.functionblock.AnalysisBlockImpl;

public class VeyBsyExpBlock extends AnalysisBlockImpl{
	
	public VeyBsyExpBlock(IBasicBlock bb, int lable) {
		super(bb, lable);
	}

	// 这个地方还要好好考虑一下，现在的想法是不论是向前分析还是向后分析，先将传入的表达式放入_temp，然后对_temp进行kill和gen的操作
	public void tansfer() 	{
		//清空_temp
		this._temp.clear();
		//将_entry的元素加到_temp
		if (this._exit.size() != 0){
			for (int i = 0; i < this._exit.size(); i++){
				this._temp.add(this._exit.get(i));
			}
		}
		
		this.gen();
		this.kill();

		//将_temp的元素赋给_exit
		if (this._temp.size() != 0){
			this._entry.clear();
			for (int i = 0; i < this._temp.size(); i++){
				this._entry.add(this._temp.get(i));
			}
		}
		
	}
}
