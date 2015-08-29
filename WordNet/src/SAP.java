import java.util.ArrayList;

public class SAP 
{
	private Digraph digraph = null;
	
	/**
	 * Variables for bfs.
	 * */
	private int E = 0;
	private int V = 0;
	private int [] marked = null;
	private int [] edgeTo = null;
	private int [] distTo = null;
	private int preAncestor = 0; //The vertex befor the common ancestor between v & w.
	private int SEPERATE_FLAG = -1;
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G)
	{
		if (G == null)
			throw new java.lang.NullPointerException("Params null.");
		
		this.digraph = new Digraph(G);
		
		this.E = G.E();
		this.V = G.V();
	}
	
	/**
	 *  length of shortest ancestral path between v and w;
	 *  -1 if no such path
	 */
	public int length(int v, int w)
	{
		if (v < 0 || w < 0 || v > V || w > V)
			throw new java.lang.IndexOutOfBoundsException("Params out of bound.");
		
		if (v == w)
			return 0;
		
		int ancestor = ancestor(v, w);
		if (ancestor == -1)
			return -1;
		
		int distance = distTo[ancestor] + distTo[preAncestor] + 1;
		
		return distance;
	}
	
	/**
	 *  a common ancestor of v and w that participates in a shortest ancestral path; 
	 *  -1 if no such path
	 */
	public int ancestor(int v, int w)
	{
		if (v < 0 || w < 0 || v > V || w > V)
			throw new java.lang.IndexOutOfBoundsException("Params out of bound.");
	
		if (v == w)
			return v;
		
		int ancestor1 = preProcessAncestor(v,w);
		if (ancestor1 == -1)
			return -1;
		int distance1 = distTo[ancestor1] + distTo[preAncestor] + 1;
		int temp = preAncestor;
		
		int ancestor2 = preProcessAncestor(w, v);
		int distance2 = distTo[ancestor2] + distTo[preAncestor] + 1;
		
		if (distance1 < distance2)
		{
			preAncestor = temp;
			return ancestor1;
		}
		else
			return ancestor2;
	}
	
	private int preProcessAncestor(int v, int w)
	{
		this.marked = new int[V];
		this.edgeTo = new int[V];
		this.distTo = new int[V];
		for (int i = 0; i < V; i++)
			distTo[i] = Integer.MAX_VALUE;
		
		Stack<Integer> stk = new Stack<Integer>();
		stk.push(v);
		stk.push(SEPERATE_FLAG);
		stk.push(w);
		return bfs(digraph, stk);
	}
	  
	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{
		if (v == null || w == null)
			throw new java.lang.NullPointerException("Params null.");
		
		for (int i : v)
		{
			for (int j : w)
			{
				if (i == j)
					return 0;
			}
		}
		
		int ancestor = ancestor(v, w);
		if (ancestor == -1)
			return -1;
		int distance = distTo[ancestor] + distTo[preAncestor] + 1;
		
		return distance;
	}
	
	private int preProcessAncestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		this.marked = new int[V];
		this.edgeTo = new int[V];
		this.distTo = new int[V];
		for (int i = 0; i < V; i++)
			distTo[i] = Integer.MAX_VALUE;
		
		Stack<Integer> stk = new Stack<Integer>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		for (int id : v)
		{
			if (id > V || id < 0)
				throw new java.lang.IndexOutOfBoundsException("Index out of bound.");
			stk.push(id);
			tempList.add(id);
		}
		
		stk.push(SEPERATE_FLAG);
		for (int id : w)
		{
			if (id > V || id < 0)
				throw new java.lang.IndexOutOfBoundsException("Index out of bound.");
			stk.push(id);
			
			if (tempList.contains(id))
				return id;
		}
		return bfs(digraph, stk);
	}
	
	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		if (v == null || w == null)
			throw new java.lang.NullPointerException("Params null.");
		
		int ancestor1 = preProcessAncestor(v, w);
		if (ancestor1 == -1)
			return -1;
		int distance1 = distTo[ancestor1] + distTo[preAncestor] + 1;
		int temp = preAncestor;
		
		int ancestor2 = preProcessAncestor(w, v);
		int distance2 = distTo[ancestor2] + distTo[preAncestor] + 1;
		
		if (distance1 < distance2)
		{
			preAncestor = temp;
			return ancestor1;
		}
		else
			return ancestor2;
	}
	   
	
	
	/**
	 * Test : For inner use.
	 * */
	private int bfs(Digraph G, Iterable<Integer> sources)
	{
		int minDistance = Integer.MAX_VALUE;
		int ancestor = -1;
		int minAncestor = -1;
		int minPreAncestor = -1;
		
		Queue<Integer> vertexQue = new Queue<Integer>();
		
		int index = 1;
		for (int s : sources)
		{
			if (s == SEPERATE_FLAG)
			{
				index++;
				continue;
			}
			else
			{
				vertexQue.enqueue(s);
				marked[s] = index;
				distTo[s] = 0;
			}
		}

		while (!vertexQue.isEmpty())
		{
			int seed = vertexQue.dequeue();
			
			for (int w : G.adj(seed))
			{
				if (marked[w] != 0 && marked[w] != marked[seed])
				{
					preAncestor = seed;
					ancestor = w;
					if (distTo[ancestor] + distTo[preAncestor] + 1 < minDistance)
					{
						minAncestor = ancestor;
						minPreAncestor = preAncestor;
						minDistance = distTo[ancestor] + distTo[preAncestor] + 1;
						//marked[w] = marked[seed];
					}
				}
				else if (marked[w] == 0)
				{
					marked[w] = marked[seed];
					edgeTo[w] = seed;
					distTo[w] = distTo[seed] + 1;
					vertexQue.enqueue(w);
				}
			}
		}
		
		preAncestor = minPreAncestor;
		return minAncestor;
	}
	
	// do unit testing of this class
	public static void main(String[] args)
	{
		String filename = "D:\\myProgram\\java\\workspace\\WordNet\\dat\\wordnet\\digraph-wordnet.txt";
		In in = new In(filename);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		
		while (!StdIn.isEmpty()) 
		{/*
			String v = StdIn.readLine();
	        String w = StdIn.readLine();
	        String [] l1 = v.split(" ");
	        String [] l2 = w.split(" ");
	        
	        ArrayList<Integer> i1 = new ArrayList<Integer>();
	        ArrayList<Integer> i2 = new ArrayList<Integer>();
	        for (int i = 0; i < l1.length; i++)
	        {
	        	i1.add(Integer.parseInt(l1[i]));
	        	i2.add(Integer.parseInt(l2[i]));
	        }
	        
	        int length = sap.length(i1, i2);
	        System.out.printf("%d\n", length);
			*/
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length = sap.length(v, w);
	        System.out.printf("%d\n", length);
	    }
	}
}