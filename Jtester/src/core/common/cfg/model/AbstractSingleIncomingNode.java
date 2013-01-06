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
import core.common.cfg.interfaces.ISingleIncoming;

/**
 * Abstract node with one incoming arc (node)
 * 
 */
public abstract class AbstractSingleIncomingNode extends AbstractBasicBlock implements ISingleIncoming {
	private IBasicBlock prev;

	/**
	 * Default constructor
	 */
	public AbstractSingleIncomingNode() {
		super();
	}

	public IBasicBlock[] getIncomingNodes() {
		return new IBasicBlock[] { prev };
	}

	public int getIncomingSize() {
		return 1;
	}

	public IBasicBlock getIncoming() {
		return prev;
	}

	/**
	 * Sets the incoming node
	 * 
	 * @param prev
	 */
	public void setIncoming(IBasicBlock prev) {
		this.prev = prev;
	}

	@Override
	public void addIncoming(IBasicBlock node) {
		setIncoming(node);
	}
}
