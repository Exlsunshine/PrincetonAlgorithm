import java.util.ArrayList;

public class WordNet
{
	private Digraph diagraph = null;
	private RedBlackBST<String, ArrayList<Integer>> redBST = null;
	private int size = 0;
	private RedBlackBST<Integer, ArrayList<String>> redBSTIntKey = null;

	private boolean hasSingleRoot(Digraph dig)
	{
		int temp = bfs(dig,0);
		
		//System.out.printf("%d\n", temp);
		
		if (temp > 1)
			return false;
		else
			return true;
	}	
	
	
	/**
	 * @return the number of the root (root is a node that has zero outdegree).
	 * */
	private int bfs(Digraph dig, int source)
	{
		int rootCnt = 0;
		int scanIndex = 0;
		boolean [] marked = new boolean[dig.V()];
		Queue<Integer> q = new Queue<Integer>();
		
		q.enqueue(source);
		marked[source] = true;
		
		while (true)
		{
			if (q.isEmpty())
			{
				if (scanIndex >= dig.V())
					break;
				for (int i = scanIndex; i < dig.V(); i++)
				{
					if (!marked[i])
					{
						q.enqueue(i);
						scanIndex = i + 1;
						break;
					}
				}
			}
			if (q.isEmpty())
				break;
			
			int seed = q.dequeue();
			boolean hasAdj = false;
			
			for (int i : dig.adj(seed))
			{
				hasAdj = true;
				if (!marked[i])
				{
					marked[i] = true;
					q.enqueue(i);
				}
			}
			
			if (!hasAdj)
				rootCnt++;
		}
		
		return rootCnt;
	}
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms)
	{
		if (synsets == null || hypernyms == null)
			throw new java.lang.NullPointerException("Null synsets or hypernyms params!");
		
		redBST = new RedBlackBST<String, ArrayList<Integer>>();
		redBSTIntKey = new RedBlackBST<Integer,ArrayList<String>>();
		readSynsetsData(synsets);
		diagraph = new Digraph(size);
		readHypernymsData(hypernyms);
		
		DirectedCycle dc = new DirectedCycle(diagraph);
		if (dc.hasCycle())
			throw new java.lang.IllegalArgumentException("Illegal argument.");
	
		boolean temp = hasSingleRoot(diagraph);
		//System.out.printf("%s", temp);
		if (!temp)
			throw new java.lang.IllegalArgumentException("Illegal argument.");
	}
	
	private void readHypernymsData(String filename)
	{
		In in = new In(filename);
		
		int id = 0;
		String line = null;
		String [] data = null;
		
		while (!in.isEmpty())
		{
			line = in.readLine();
			data = line.split(",");
			id = Integer.parseInt(data[0]);
			for (int i = 1; i < data.length; i++)
				diagraph.addEdge(id, Integer.parseInt(data[i]));
		}
	}
	
	/**
	 * Load synsets file into memory.
	 * */
	private void readSynsetsData(String filename)
	{
		In in = new In(filename);
		
		int id = 0;
		String line = null;
		String [] data = null;
		String [] words = null;
		
		while (!in.isEmpty())
		{
			line = in.readLine();
			data = line.split(",");
			
			id = Integer.parseInt(data[0]);
			words = data[1].split(" ");
			ArrayList<String> listStr = new ArrayList<String>();
			for (int i = 0; i < words.length; i++)
			{
				ArrayList<Integer> list = redBST.get(words[i]);
				if (list == null)
					list = new ArrayList<Integer>();
				list.add(id);
				redBST.put(words[i], list);
				listStr.add(words[i]);
			}
			redBSTIntKey.put(id, listStr);
		}
		
		size = id + 1;
	}
	
	/**
	 *  returns all WordNet nouns
	 * */
	public Iterable<String> nouns()
	{
		return redBST.keys();
	}
	
	/**
	 *  is the word a WordNet noun?
	 * */
	public boolean isNoun(String word)
	{
		if (word == null)
			throw new java.lang.NullPointerException("Null params.");
		
		return redBST.contains(word);	
	}
	
	/**
	 *  distance between nounA and nounB (defined below)
	 */
	public int distance(String nounA, String nounB)
	{
		if (nounA == null || nounB == null)
			throw new java.lang.NullPointerException("Null params.");
		
		if (!redBST.contains(nounA) || !redBST.contains(nounB))
			throw new java.lang.IllegalArgumentException("Illegal params");
		
		SAP sap = new SAP(diagraph);
		ArrayList<Integer> idA = redBST.get(nounA);
		ArrayList<Integer> idB = redBST.get(nounB);
		
		return sap.length(idA, idB);
	}
	
	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB)
	{
		if (nounA == null || nounB == null)
			throw new java.lang.NullPointerException("Null params");
		
		if (!redBST.contains(nounA) || !redBST.contains(nounB))
			throw new java.lang.IllegalArgumentException("Illegal params");
		
		SAP sap = new SAP(diagraph);
		ArrayList<Integer> idA = redBST.get(nounA);
		ArrayList<Integer> idB = redBST.get(nounB);
		int ancestor = sap.ancestor(idA, idB);
		
		String ans = "";
		for (String str : redBSTIntKey.get(ancestor))
			ans += str + " ";
		return ans;
	}
	
	public static void main(String[] args)
	{
		String synset = "D:\\myProgram\\java\\workspace\\WordNet\\dat\\wordnet\\synsets3.txt";
		String hypernyms = "D:\\myProgram\\java\\workspace\\WordNet\\dat\\wordnet\\hypernyms3InvalidTwoRoots.txt";
		
		WordNet wdn = new WordNet(synset, hypernyms);
		//System.out.printf("%d\n",	wdn.distance("Giannangelo_Braschi", "surrenderer"));
		//System.out.printf("%s\n",	wdn.sap("Giannangelo_Braschi", "surrenderer"));
	}
}