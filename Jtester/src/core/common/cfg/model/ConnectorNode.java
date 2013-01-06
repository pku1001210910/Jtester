/*******************************************************************************
 * Copyright (c) 2009, 2010 Alena Laskavaia 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alena Laskavaia  - initial API and implementation
 *******************************************************************************/
package core.common.cfg.model;

import java.util.ArrayList;

import core.common.cfg.interfaces.IBasicBlock;
import core.common.cfg.interfaces.IConnectorNode;
import core.common.cfg.interfaces.IJumpNode;



/**
 * TODO: add description
 */
public class ConnectorNode extends AbstractSingleOutgoingNode implements IConnectorNode {
	protected ArrayList<IBasicBlock> incoming = new ArrayList<IBasicBlock>(2);

	protected ConnectorNode() {
		super();
	}

	@Override
	public void addIncoming(IBasicBlock node) {
		incoming.add(node);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.cdt.codan.provisional.core.model.cfg.IBasicBlock#
	 * getIncomingIterator()
	 */
	public IBasicBlock[] getIncomingNodes() {
		return incoming.toArray(new IBasicBlock[incoming.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see analysis.cfg.interfaces.IBasicBlock#getIncomingSize ()
	 */
	public int getIncomingSize() {
		return incoming.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.cdt.codan.provisional.core.model.cfg.IConnectorNode#
	 * hasBackwardIncoming()
	 */
	public boolean hasBackwardIncoming() {
		for (IBasicBlock node : incoming) {
			if (node instanceof IJumpNode) {
				if (((IJumpNode) node).isBackwardArc())
					return true;
			}
		}
		return false;
	}
}
