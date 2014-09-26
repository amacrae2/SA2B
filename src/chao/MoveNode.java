package chao;


import java.util.ArrayList;
import java.util.List;

public class MoveNode {

	private int timesVisited = 0;
	private double totalScore = 0;
	private List<StateNode> children = null;
	private StateNode parent = null;
	private Move move;

	public MoveNode(StateNode parent, Move move) {
		super();
		this.parent = parent;
		this.move = move;
	}

	public void addScore(double score) {
		timesVisited = timesVisited+1;
		totalScore = totalScore + score;
		parent.addScore(score);
	}

	public int getTimesVisited() {
		return timesVisited;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public List<StateNode> getChildren() {
		return children;
	}

	public StateNode getParent() {
		return parent;
	}

	public Move getMove() {
		return move;
	}

	public void addChild(StateNode child) {
		if (children == null) {
			children = new ArrayList<StateNode>();
		}
		children.add(child);
	}

	public double getExpectedScore() {
		return totalScore/timesVisited;
	}


}
