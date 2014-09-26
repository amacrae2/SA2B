package chao;

import java.util.ArrayList;
import java.util.List;

public class StateNode {

	private int timesVisited = 0;
	private double totalScore = 0;
	private List<MoveNode> children = null;
	private MoveNode parent = null;
	private MemoryState state;
	private Move moveToState = null;
	private double currScore = 0;

	public StateNode(MoveNode parent, MemoryState state, Move move) {
		this.parent = parent;
		this.state = state;
		this.moveToState = move;
	}

	// for creating the top node
	public StateNode(MemoryState state) {
		this.state = state;
	}

	public void addScore(double score) {
		timesVisited = timesVisited+1;
		totalScore = totalScore + score;
		if (parent != null) {
			parent.addScore(score);
		}
	}

	public void setChildren(List<MoveNode> children) {
		this.children = children;
	}

	public void setParent(MoveNode parent) {
		this.parent = parent;
	}

	public double getNodeScore() {
		return totalScore/timesVisited;
	}

	public int getTimesVisited() {
		return timesVisited;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public List<MoveNode> getChildren() {
		return children;
	}

	public MoveNode getParent() {
		return parent;
	}

	public MemoryState getState() {
		return state;
	}

	public Move getMoveToState() {
		return moveToState;
	}

	public void addChild(MoveNode child) {
		if (children == null) {
			children = new ArrayList<MoveNode>();
		}
		children.add(child);
	}

	public StateNode updateParent(Move move) {
		for (MoveNode mNode : children) {
			for (StateNode sNode : mNode.getChildren()) {
				if (sNode.getMoveToState().equals(move)) {
					return sNode;
				}
			}
		}
		return null;
	}

	public StateNode getNextState(Move move) {
		for (MoveNode child : children) {
			if (child.getMove().equals(move)) {
				return child.getChildren().get(0);
			}
		}
		System.err.println("StateNode error: no match when comparing children");
		return null;
	}

	public double getCurrScore() {
		return currScore;
	}

	public void setCurrScore(double currScore) {
		this.currScore = currScore;
	}

}