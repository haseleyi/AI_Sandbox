package searchclient;

import java.util.Comparator;
import java.util.Stack;
import java.util.ArrayList;

import searchclient.NotImplementedException;

public abstract class Heuristic implements Comparator<Node> {
	public Heuristic(Node initialState) {
		// Here's a chance to pre-process the static parts of the level.
	}

	public int h(Node n) {
		// Create stack of goal locations and list of box locations in map
		Stack<Pair<Character, Integer>> goals = new Stack<Pair<Character, Integer>>();
		ArrayList<Pair<Character, Integer>> boxes = new ArrayList<Pair<Character, Integer>>();
		for (int r = 0; r < Node.MAX_ROW; r++) {
			for (int c = 0; c < Node.MAX_COL; c++) {
				if (n.boxes[r][c] != 0) {
					boxes.add(new Pair<Character, Integer>(n.boxes[r][c], c));
				}
				if (n.goals[r][c] != 0) {
					goals.push(new Pair<Character, Integer>(n.goals[r][c], c));
				}
			}
		}

		// Match each goal with a box, sum up the column distances for each match
		int total = 0;
		while (!goals.isEmpty()) {
			Pair<Character, Integer> g = goals.pop();
			char c = g.first;
			int col = g.second;
			for (Pair<Character, Integer> box : boxes) {
				if (Character.toLowerCase(box.first) == c) {
					total += Math.abs(box.second - col);
					boxes.remove(box);
					break;
				}
			}
		}
		return total;
	}

	public abstract int f(Node n);

	@Override
	public int compare(Node n1, Node n2) {
		return this.f(n1) - this.f(n2);
	}

	public static class AStar extends Heuristic {
		public AStar(Node initialState) {
			super(initialState);
		}

		@Override
		public int f(Node n) {
			return n.g() + this.h(n);
		}

		@Override
		public String toString() {
			return "A* evaluation";
		}
	}

	public static class WeightedAStar extends Heuristic {
		private int W;

		public WeightedAStar(Node initialState, int W) {
			super(initialState);
			this.W = W;
		}

		@Override
		public int f(Node n) {
			return n.g() + this.W * this.h(n);
		}

		@Override
		public String toString() {
			return String.format("WA*(%d) evaluation", this.W);
		}
	}

	public static class Greedy extends Heuristic {
		public Greedy(Node initialState) {
			super(initialState);
		}

		@Override
		public int f(Node n) {
			return this.h(n);
		}

		@Override
		public String toString() {
			return "Greedy evaluation";
		}
	}

	public static class Pair<F, S> {
	    public final F first;
	    public final S second;

	    public Pair(F first, S second) {
	        this.first = first;
	        this.second = second;
	    }

	    // @Override
	    public boolean equals(Pair other) {
	    	return other.first == first && other.second == second;
	    }
	}
}

