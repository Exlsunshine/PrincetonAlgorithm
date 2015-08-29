public class BoggleSolver 
{
	private Trie26 trieSet;
	private SET<String> wordsSet;
	private int noOfRows = 0;
	private int noOfCols = 0;
	private char [][] map = null;
	
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
    	this.trieSet = new Trie26();
    	int len = dictionary.length;
    	
    	for (int i = 0; i < len; i++)
    		this.trieSet.add(dictionary[i]);
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
    	wordsSet = new SET<String>();
    	noOfRows = board.rows();
    	noOfCols = board.cols();
    	map = new char[noOfRows][noOfCols];
    	
    	for (int i = 0; i < noOfRows; i++)
    	{
    		for (int j = 0; j < noOfCols; j++)
    			map[i][j] = board.getLetter(i, j);
    	}
    	
    	
    	for (int i = 0; i < noOfRows; i++)
    	{
    		for (int j = 0; j < noOfCols; j++)
    		{
    	    	boolean [][] marked = new boolean[noOfRows][noOfCols];
    			dfs(marked, i, j, "", trieSet.root);
    		}
    	}
    	
    	return wordsSet;
    }
    
    private void dfs(boolean [][]marked, int x, int y, String str, Node root)
    {
    	if (x < 0 || x >= noOfRows || y < 0 || y >= noOfCols)
    		return ;
    	if (marked[x][y])
    		return ;
    	
    	char ch = map[x][y];
    	
    	Node node = null;
    	String strCopy = "";
    	if (root.next[ch - 'A'] == null)
    		return;
    	if (ch == 'Q')
    	{
    		node = root.next[ch - 'A'].next['U' - 'A'];
    		if (node == null)
    			return ;
    		strCopy = str + String.valueOf(ch) + "U";
    	}
    	else
    	{
    		node = root.next[ch - 'A'];
    		strCopy = str + String.valueOf(ch);
    	}
    	
    	if (strCopy.length() > 2)
    	{
    		if (trieSet.contains(strCopy))
    		{
    			if (!wordsSet.contains(strCopy))
    				wordsSet.add(strCopy);
    		}
    	}
    	
    	marked[x][y] = true;
    	
    	dfs(marked, x - 1, y, strCopy, node);
    	dfs(marked, x + 1, y, strCopy, node);
    	
    	dfs(marked, x, y - 1, strCopy, node);
    	dfs(marked, x, y + 1, strCopy, node);
    	
    	dfs(marked, x - 1, y - 1, strCopy, node);
    	dfs(marked, x - 1, y + 1, strCopy, node);
    	dfs(marked, x + 1, y - 1, strCopy, node);
    	dfs(marked, x + 1, y + 1, strCopy, node);
    	
    	marked[x][y] = false;
    }
    
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
	{
    	if (!trieSet.contains(word))
    		return 0;
    	
    	int len = word.length();
    	
    	if (len >= 0 && len <= 2)
    		return 0;
    	else if (len >= 3 && len <= 4)
    		return 1;
    	else if (len == 5)
    		return 2;
    	else if (len == 6)
    		return 3;
    	else if (len == 7)
    		return 5;
    	else
    		return 11;
	}
    
    public static void main(String[] args)
	{
    	String pathDic = "D:\\myProgram\\java\\workspace\\Boggle\\dat\\boggle\\dictionary-algs4.txt";
    	String pathBod = "D:\\myProgram\\java\\workspace\\Boggle\\dat\\boggle\\board-q.txt";
    	
    	In in = new In(pathDic);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(pathBod);
        
        long start = System.currentTimeMillis();
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            //StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        
    	long end = System.currentTimeMillis();
    	
    	System.out.print(String.format("\nTime consuming : %f\n",(end - start) * 1.0 / 1000));
	}
}