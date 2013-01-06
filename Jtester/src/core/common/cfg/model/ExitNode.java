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
import core.common.cfg.interfaces.IExitNode;
import core.common.cfg.interfaces.IStartNode;

/**
 * Plain node has one prev one jump
 * 
 */
public class ExitNode extends AbstractSingleIncomingNode implements IExitNode {
	private IStartNode start;

	protected ExitNode() {
		super();
	}

	public IBasicBlock[] getOutgoingNodes() {
		return EMPTY_LIST;
	}

	public int getOutgoingSize() {
		return 0;
	}

	public IStartNode getStartNode() {
		return start;
	}

	public void setStartNode(IStartNode start) {
		this.start = start;
	}

	@Override
	public void addOutgoing(IBasicBlock node) {
		throw new UnsupportedOperationException();
	}
}
