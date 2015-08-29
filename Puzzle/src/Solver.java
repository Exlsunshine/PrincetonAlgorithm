public class Solver
{
	private MinPQ<SearchNode> mpq = null;
	private SearchNode answer = null;

	private MinPQ<SearchNode> mpqTwin = null;
	private boolean isSolvable = false;
	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial)            
    {
		mpq = new MinPQ<SearchNode>();
		SearchNode sn = new SearchNode(0, initial, null);
		mpq.insert(sn);
		
		//for the twin
		mpqTwin = new MinPQ<SearchNode>();
		SearchNode snTwin = new SearchNode(0, initial.twin(), null);
		mpqTwin.insert(snTwin);
    }
	
	private SearchNode findGoal()
	{
		while (!mpq.isEmpty() && !mpqTwin.isEmpty())
		{
			SearchNode currentNode = mpq.delMin();
			//System.out.printf("%s\n",currentNode.board.toString());
			//StdIn.readChar();
			
			if (currentNode.board.isGoal())
			{
				isSolvable = true;
				answer = currentNode;
				return currentNode;
			}
			
			for (Board bd : currentNode.board.neighbors())
			{
				if ((currentNode.prevNode == null) || !bd.equals(currentNode.prevNode.board))
				{
					SearchNode sn = new SearchNode(currentNode.moves + 1, bd, currentNode);
					mpq.insert(sn);
				}
			}
			
			
			//for the twin
			SearchNode currentNodeTwin = mpqTwin.delMin();
			if (currentNodeTwin.board.isGoal())
			{
				answer = currentNodeTwin;
				return currentNodeTwin;
			}
			for (Board bd : currentNodeTwin.board.neighbors())
			{
				if ((currentNodeTwin.prevNode == null) || !bd.equals(currentNodeTwin.prevNode.board))
				{
					SearchNode sn = new SearchNode(currentNodeTwin.moves + 1, bd, currentNodeTwin);
					mpqTwin.insert(sn);
				}
			}
		}
		return null;
	}
	
	public boolean isSolvable()             // is the initial board solvable?
	{
		if (answer == null)
			answer = findGoal();
		
		return isSolvable;
	}
	
	// min number of moves to solve initial board; -1 if no solution
	public int moves()
	{
		if (answer == null)
			answer = findGoal();
		
		if (isSolvable)
			return answer.moves;
		else
			return -1;
	}
	
	 // sequence of boards in a shortest solution; null if no solution
	public Iterable<Board> solution()
    {
		if (answer == null)
			answer = findGoal();
		if (!isSolvable)
			return null;
		
		Stack<Board> snStk = new Stack<Board>();

		SearchNode node = answer;
		while (node != null)
		{
			snStk.push(node.board);
			node = node.prevNode;
		}
		
		return snStk;
	}
	
	private class SearchNode implements Comparable<SearchNode>
	{
		public int moves = 0;
		public Board board = null;
		public SearchNode prevNode = null;
		public int priority = 0;
		
		public SearchNode(int moves, Board board, SearchNode prevNode)
		{
			this.moves = moves;
			this.board = board;
			this.prevNode = prevNode;
			this.priority = this.moves + this.board.manhattan();
		}

		@Override
		public int compareTo(SearchNode o) 
		{
			if (this.priority > o.priority)
				return 1;
			else if (this.priority < o.priority)
				return -1;
			else
				return 0;
		}
	}
	
	public static void main(String[] args)  // solve a slider puzzle (given below)
	{
		//System.out.printf("Begin 0...\n");
		// for each command-line argument
        for (String filename : args)
        {
            // read in the board specified in the filename
            In in = new In(filename);
            int N = in.readInt();
    		//System.out.printf("Begin 1... %s,%d\n",filename,N);
            int[][] tiles = new int[N][N];
            for (int i = 0; i < N; i++) 
            	for (int j = 0; j < N; j++) 
                	tiles[i][j] = in.readInt();


    		//System.out.printf("Begin 2...\n");
            // solve the slider puzzle
            Board initial = new Board(tiles);
    		//System.out.printf("Begin 3...\n");
            Solver solver = new Solver(initial);
    		//System.out.printf("Begin 4...\n");
            System.out.println("Minimum number of moves = " + solver.moves());
            System.out.println(solver.solution());
        }
		//System.out.printf("Over\n");
	}
}
