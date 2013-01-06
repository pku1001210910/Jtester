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

import core.common.cfg.interfaces.IBranchNode;
import core.common.cfg.interfaces.IConnectorNode;
import core.common.cfg.interfaces.IControlFlowGraph;
import core.common.cfg.interfaces.IDecisionNode;
import core.common.cfg.interfaces.IExitNode;
import core.common.cfg.interfaces.IJumpNode;
import core.common.cfg.interfaces.INodeFactory;
import core.common.cfg.interfaces.IPlainNode;
import core.common.cfg.interfaces.IStartNode;

/**
 * Factory that creates cfg nodes
 */
public class NodeFactory implements INodeFactory {
	IControlFlowGraph graph;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.cdt.codan.provisional.core.model.cfg.INodeFactory#
	 * getControlFlowGraph()
	 */
	public IControlFlowGraph getControlFlowGraph() {
		return graph;
	}

	public NodeFactory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see analysis.cfg.interfaces.INodeFactory#createPlainNode ()
	 */
	public IPlainNode createPlainNode() {
		return new PlainNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see analysis.cfg.interfaces.INodeFactory#createJumpNode ()
	 */
	public IJumpNode createJumpNode() {
		return new JumpNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.cdt.codan.provisional.core.model.cfg.INodeFactory#
	 * createDecisionNode()
	 */
	public IDecisionNode createDecisionNode() {
		return new DecisionNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.cdt.codan.provisional.core.model.cfg.INodeFactory#
	 * createConnectiorNode()
	 */
	public IConnectorNode createConnectorNode() {
		return new ConnectorNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see analysis.cfg.interfaces.INodeFactory#createStartNode ()
	 */
	public IStartNode createStartNode() {
		return new StartNode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see analysis.cfg.interfaces.INodeFactory#createExitNode ()
	 */
	public IExitNode createExitNode() {
		return new ExitNode();
	}

	public IBranchNode createBranchNode(String label) {
		return new BranchNode(label);
	}
}
