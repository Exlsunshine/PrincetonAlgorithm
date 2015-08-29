import java.util.ArrayList;


public class BaseballElimination
{
	private class Team
	{
		public String name;
		public int noOfWins;
		public int noOfLosses;
		public int noOfRemain;
		public int index;
		
		public Team(String name, int wins, int losses, int remain, int index)
		{
			this.name = name;
			this.noOfWins = wins;
			this.noOfLosses = losses;
			this.noOfRemain = remain;
			this.index = index;
		}
	}
	
	private int noOfTeams = 0;
	private int [][] remainMatrix = null;
	private RedBlackBST<String, Team> redBST = null;
	private ArrayList<String> nameList = null;
	
	/**
	 * create a baseball division from given filename in format specified below
	 * */
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		noOfTeams = Integer.parseInt(in.readLine());
		remainMatrix = new int[noOfTeams][noOfTeams];
		redBST = new RedBlackBST<String, Team>();
		nameList = new ArrayList<String>();
		
		for (int i = 0; i < noOfTeams; i++)
		{
			String name = in.readString();
			int win = in.readInt();
			int loss = in.readInt();
			int remain = in.readInt();
			
			for (int j = 0; j < noOfTeams; j++)
				remainMatrix[i][j] = in.readInt();
			
			Team team = new Team(name, win, loss, remain, i);
			redBST.put(name, team);
			nameList.add(name);
			/*
			System.out.print(name + " " + String.valueOf(win) + " " + String.valueOf(loss) + " " + String.valueOf(remain) + " ");
			for (int j = 0; j < noOfTeams; j++)
				System.out.print(remainMatrix[i][j] + " ");*/
		}
		
		in.close();
	}
	
	/**
	 * Number of teams
	 */
	public int numberOfTeams()
	{
		return noOfTeams;
	}
	
	/**
	 * all teams
	 * */
	public Iterable<String> teams()
	{
		return redBST.keys();
	}
	
	/**
	 * number of wins for given team
	 * */
	public int wins(String team)
	{
		if (!redBST.contains(team))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		return redBST.get(team).noOfWins;
	}
	
	/**
	 * number of losses for given team
	 * */
	public int losses(String team)
	{
		if (!redBST.contains(team))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		return redBST.get(team).noOfLosses;
	}
	
	/**
	 * number of remaining games for given team
	 * */
	public int remaining(String team)
	{
		if (!redBST.contains(team))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		return redBST.get(team).noOfRemain;
	}
	
	/**
	 * number of remaining games between team1 and team2
	 * */
	public int against(String team1, String team2)
	{
		if (!redBST.contains(team1) || !redBST.contains(team2))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		return remainMatrix[redBST.get(team1).index][redBST.get(team2).index];
	}
	
	
	private int idxTrivialElimination = -1;
	public boolean isEliminated(String team)              // is given team eliminated?
	{
		if (!redBST.contains(team))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		
		idxTrivialElimination = -1;
		int seed = redBST.get(team).index;
		int score = redBST.get(team).noOfRemain + redBST.get(team).noOfWins;
		for (int i = 0; i < noOfTeams; i++)
		{
			if (i == seed)
				continue;
			if (redBST.get(nameList.get(i)).noOfWins > score)
			{
				idxTrivialElimination = i;
				return true;
			}
		}
		
		
		generateNetwork(team);
		
		int middleCnt = (noOfTeams - 1) * noOfTeams / 2 - (noOfTeams - 1);
		ff = new FordFulkerson(fn,0,noOfTeams + middleCnt);
		//System.out.print(fn.toString() + "\nValue is " + String.valueOf(ff.value()));
		
		for (FlowEdge e : fn.adj(0))
		{
			if (e.flow() != e.capacity())
			{
				//System.out.print("\n" + e.toString() + "\n");
				return true;
			}
		}

		return false;
	}
	
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		if (!redBST.contains(team))
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		
		if (!isEliminated(team))
			return null;
		if (idxTrivialElimination != -1)
		{

			Stack<String> stk = new Stack<String>();
			stk.push(nameList.get(idxTrivialElimination));
			return stk;
		}
		
		Stack<String> stk = new Stack<String>();
		
		int middleCnt = (noOfTeams - 1) * noOfTeams / 2 - (noOfTeams - 1);
		int teamIndex = middleCnt + 1;
		
		int offset = 0;
		int seed = redBST.get(team).index;
		//System.out.println();
		for (int i = 0; i < noOfTeams; i++)
		{
			if (i == seed)
			{
				offset = -1;
				continue;
			}
			if (ff.inCut(teamIndex + i + offset))
			{
				stk.push(nameList.get(i));
				//System.out.println(i);
			}
		}
		//System.out.println();
		
		return stk;
	}
	
	private FlowNetwork fn = null;
	private FordFulkerson ff = null;
	
	//0 : source
	//1 -- middleCnt : vertex i-j
	//middleCnt + 1 -- middleCnt + (noOfTeams - 1) : vertex i
	//(noOfTeams - 1) + middleCnt + 2 - 1 : target
	private void generateNetwork(String source)
	{
		int seed = redBST.get(source).index;
		int middleCnt = (noOfTeams - 1) * noOfTeams / 2 - (noOfTeams - 1);
		fn = new FlowNetwork((noOfTeams - 1) + middleCnt + 2);
		int midIndex = 1;
		int teamIndex = middleCnt + 1;

		//printMatrix(remainMatrix);

		int offseti = 0;
		for (int i = 0; i < noOfTeams; i++)
		{
			if (i == seed)
			{
				offseti = -1;
				continue;
			}
			
			int offsetj = 0;
			for (int j = i + 1; j < noOfTeams; j++)
			{
				if (j == seed)
				{
					offsetj = -1;
					continue;
				}
				FlowEdge e = new FlowEdge(0, midIndex, remainMatrix[i][j]);
				//System.out.print(String.valueOf(i) + "," + String.valueOf(j) + " " + e.toString() + "\n");
				fn.addEdge(e);
				
				FlowEdge e2 = new FlowEdge(midIndex, teamIndex + i + offseti, Double.POSITIVE_INFINITY);
				fn.addEdge(e2);
				FlowEdge e3 = new FlowEdge(midIndex, teamIndex + j + offsetj + offseti, Double.POSITIVE_INFINITY);
				fn.addEdge(e3);
				midIndex++;
			}
		}
		
		int offset = 0;
		for (int i = 0; i < noOfTeams; i++)
		{
			if (i == seed)
			{
				offset--;
				continue;
			}
			FlowEdge e = new FlowEdge(teamIndex + i + offset, noOfTeams + middleCnt, redBST.get(source).noOfWins + redBST.get(source).noOfRemain - redBST.get(nameList.get(i)).noOfWins);
			fn.addEdge(e);
			//System.out.print(String.valueOf(i) + "," + String.valueOf(redBST.get(source).noOfWins + redBST.get(source).noOfRemain - redBST.get(nameList.get(i)).noOfWins) + "\n");
		}
		
		//System.out.print(fn.toString());
	}
	
	private void printMatrix(int [][] array)
	{
		for (int i = 0; i < array.length; i++)
		{
			for (int j = 0; j < array.length; j++)
				System.out.print(array[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args)
	{
		String path = "D:\\myProgram\\java\\workspace\\BaseballElimination\\dat\\baseball\\teams4a.txt";
		BaseballElimination be = new BaseballElimination(path);
		
		for (String str : be.certificateOfElimination("Ghaddafi"))
			System.out.println(str);
		
		
		/*
		String path = "D:\\myProgram\\java\\workspace\\BaseballElimination\\dat\\baseball\\teams4.txt";
		BaseballElimination be = new BaseballElimination(path);
		
		System.out.print(be.numberOfTeams() + "\n");
		ArrayList<String> names = new ArrayList<String>();
		
		for (String str : be.teams())
			names.add(str);
		for (int i = 0; i < be.numberOfTeams(); i++)
		{
			System.out.print(names.get(i) + "\t" + be.wins(names.get(i)) + "\t" + be.losses(names.get(i))
					+ "\t" + be.remaining(names.get(i)) + "\n");
		}
		
		for (int i = 0; i < be.numberOfTeams(); i++)
		{
			for (int j = 0; j < be.numberOfTeams(); j++)
				System.out.print(String.valueOf(be.remainMatrix[i][j]) + " ");

			System.out.print("\n");
		}
		*/
	}
}