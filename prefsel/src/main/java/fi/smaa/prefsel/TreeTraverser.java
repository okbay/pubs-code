package fi.smaa.prefsel;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class TreeTraverser {
	
	public static int nrNodes(Node root) {
		int count = 1;
		
		for (Node qn : root.getChildren()) {
			count += nrNodes(qn);
		}
		return count;
	}
	
	public static String toDOT(Node root) {
		String res = "graph G {\n";
		res += nodeToDOT(root, new Sequencer(), new HashMap<Node, Integer>());
		res += "}\n";
		return res;
	}
	
	private static String nodeToDOT(Node root, Sequencer sequencer, Map<Node, Integer> sequences) {
		String nodeS = nodeString(root, sequencer, sequences);
		
		String res = nodeS + "\t[label=\"" + nodeLabel(root) + "\"];\n";
		for (Node qn : root.getChildren()) {
			res += "\t" + nodeS + " -- " + nodeString(qn, sequencer, sequences) + ";\n";
		}
		for (Node qn : root.getChildren()) {
			res += nodeToDOT(qn, sequencer, sequences);
		}
		
		return res;
	}
	
	private static String nodeLabel(Node n) {
		if (n instanceof UnexpandedNode) {
			return "un";
		} else if (n instanceof ConcreteAnswerNode) {
			ConcreteAnswerNode a = (ConcreteAnswerNode) n;
			return Integer.toString(a.getAnswer());
		} else if (n instanceof QuestionNode){
			QuestionNode q = (QuestionNode) n;
			return q.getQuestion().getA1() + " ? " + q.getQuestion().getA2();
		} else {
			throw new IllegalArgumentException("strange node");
		}
	}

	private static String nodeString(Node root, Sequencer sequencer,
			Map<Node, Integer> sequences) {
		int seq = -1;
		if (sequences.containsKey(root)) {
			seq = sequences.get(root);
		} else {
			seq = sequencer.next();
			sequences.put(root, seq);
		}	
		String rootS = root.toString();
		if (rootS.equals("a-1")) {
			rootS = "R";
		}
	
		return rootS + "seq" + seq;
	}
}
