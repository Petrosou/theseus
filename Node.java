import java.util.ArrayList;
public class Node {
	Node parent;
	ArrayList<Node> children;
	int nodeDepth;
	int[] nodeMove;
	PlayerBoard nodeBoard;
	double nodeEvaluation;
	
	public Node() {
		parent = null;
		children = null;
		nodeDepth = 0;
		nodeMove = new int[3];
		nodeBoard = null;
		nodeEvaluation = 0;
	}
	
	public Node(Node parent, ArrayList<Node> children, int nodeDepth, int[] nodeMove, PlayerBoard nodeBoard, double nodeEvaluation) {
		this.parent = parent;
		this.children = children;
		this.nodeDepth = nodeDepth;
		this.nodeMove = nodeMove;
		this.nodeBoard = nodeBoard;
		this.nodeEvaluation = nodeEvaluation;
	}
	
	public Node(Node node) {
		parent = node.parent;
		children = node.children;
		nodeDepth = node.nodeDepth;
		nodeMove = node.nodeMove;
		nodeBoard = node.nodeBoard;
		nodeEvaluation = node.nodeEvaluation;
	}
	
}
