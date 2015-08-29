
public class e2 {


	private int num;
	private ST<String, Integer> teams;
	private int []w;
	private int []l;
	private int []r;
	private int [][]g;
	private int max = 0;
	private int index_max = 0;
	private FordFulkerson ff;
	private SET<String> coe;
	public e2(String filename)                    // create a baseball division from given filename in format specified below
	{
		In in = new In(filename);
		num = in.readInt();
		teams = new ST();
		w = new int[num];
		l = new int[num];
		r = new int[num];
		g = new int[num][num];
		for (int i = 0; i < num; i++)
		{
			String str = in.readString();
			teams.put(str, i);
			w[i] = in.readInt();
			if (w[i] > max)
			{
				max = w[i];
				index_max = i;
			}
			l[i] = in.readInt();
			r[i] = in.readInt();
			for (int j = 0; j < num; j++)
				g[i][j] = in.readInt();
		}
	}
	public int numberOfTeams()                        // number of teams
	{
		return num;
	}
	public Iterable<String> teams()                                // all teams
	{
		return this.teams;
	}
	public int wins(String team)                      // number of wins for given team
	{
		Integer t = teams.get(team);
		if (t == null)
			throw new IllegalArgumentException();
		int index = (int)t;
		return w[index];
	}
	public int losses(String team)                    // number of losses for given team
	{
		Integer t = teams.get(team);
		if (t == null)
			throw new IllegalArgumentException();
		int index = (int)t;

		return l[index];
	}
	public int remaining(String team)                 // number of remaining games for given team
	{
		Integer t = teams.get(team);
		if (t == null)
			throw new IllegalArgumentException();
		int index = (int)t;
		return r[index];
	}
	public int against(String team1, String team2)    // number of remaining games between team1 and team2
	{
		Integer t1 = teams.get(team1);
		Integer t2 = teams.get(team2);
		if (t1 == null || t2 == null)
			throw new IllegalArgumentException();
		int i1 = (int)t1;
		int i2 = (int)t2;
		return g[i1][i2];
	}
	public boolean isEliminated(String team)              // is given team eliminated?
	{
		Integer ts = teams.get(team);
		if (ts == null)
			throw new IllegalArgumentException();
		int index = (int)ts;	
		coe = null;
		if (w[index] + r[index] < max)
		{
			for (String str : teams)
			{
				if (teams.get(str) == index_max)
				{
					coe = new SET<String>();
					coe.add(str);
					break;
				}
			}
			return true;
		}
		int num_lay1 = (num - 1) * (num - 2) / 2;
		FlowNetwork fnt = new FlowNetwork(num + 2 + num_lay1);
		int s = num + 2 + num_lay1 - 2;
		int t = s + 1;
		int lay = num;
		int sum = 0;
		for (int i = 0; i < num; i++)
		{
			if (i == index) continue; 
			fnt.addEdge(new FlowEdge(i, t, w[index] + r[index] - w[i]));
			for (int j = i + 1; j < num; j++)
			{
				if (j == index) continue;
				fnt.addEdge(new FlowEdge(s, lay, g[i][j]));
				sum += g[i][j];
				fnt.addEdge(new FlowEdge(lay, i, Double.POSITIVE_INFINITY));
				fnt.addEdge(new FlowEdge(lay++, j, Double.POSITIVE_INFINITY));
			}
		}
		

		ff = new FordFulkerson(fnt, s, t);
		System.out.print(fnt.toString() + "\nValue is " + String.valueOf(ff.value()));
		
		lay = num;
		if (ff.value() < sum)
		{
			coe = new SET<String>();
			for (int i = 0; i < num; i++)
			{
				if (i == index) continue;
				for (int j = i + 1; j < num; j++)
				{
					if (j == index) continue;
					if (ff.inCut(lay++))
					{
						System.out.print("hit:" + String.valueOf(lay - 1) + "\n");
						for (String str : teams)
						{
							if (teams.get(str) == i)
							{
								coe.add(str);
							}
							if (teams.get(str) == j)
							{
								coe.add(str);
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		Object t = teams.get(team);
		if (t == null)
			throw new IllegalArgumentException();
		if (!isEliminated(team))
			return null;
		return coe;
		
	}
	public static void main(String[] args)
	{
		String path = "D:\\myProgram\\java\\workspace\\BaseballElimination\\dat\\baseball\\teams4a.txt";
	    e2 division = new e2(path);
	    
	    for (String str : division.certificateOfElimination("Ghaddafi"))
			System.out.println(str);
	}
}
