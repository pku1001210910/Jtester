/*******************************************************************************
 * Copyright (c) 2010 Alena Laskavaia and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alena Laskavaia - initial API and implementation
 *******************************************************************************/
package core.common.cfg.model;

import core.common.cfg.interfaces.IBasicBlock;
import core.common.cfg.interfaces.ICfgData;

/**
 * Abstract Basic Block for control flow graph.
 */
public abstract class AbstractBasicBlock implements IBasicBlock, ICfgData {
	/**
	 * Empty array of basic blocks
	 */
	public final static IBasicBlock[] EMPTY_LIST = new IBasicBlock[0];
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Add a node to list of outgoing nodes of this node
	 * 
	 * @param node - node to add
	 */
	public abstract void addOutgoing(IBasicBlock node);

	/**
	 * Add a node to list of incoming nodes of this node
	 * 
	 * @param node - node to add
	 */
	public abstract void addIncoming(IBasicBlock node);

	/**
	 * @return toString for data object
	 */
	public String toStringData() {
		if (getData() == null){
			return "0x" + Integer.toHexString(System.identityHashCode(this));}
		return getData().toString();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + toStringData();
	}
}
