public class Board
{ 
	private int dimention = 0;
	private int [][] map = null;
	
	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks)          
	{
		dimention = blocks.length;
		map = new int [dimention][dimention];
		
		for (int i = 0; i < dimention; i++)
			for (int j = 0; j < dimention; j++)
				map[i][j] = blocks[i][j];
	}
	
	public int dimension()                 // board dimension N
	{
		return dimention;
	}
	
	public int hamming()                   // number of blocks out of place
	{
		int count = 0;
		
		for (int i = 0; i < dimention; i++)
			for (int j = 0; j < dimention; j++)
				if ((map[i][j] != i * dimention + j + 1) && (map[i][j] != 0))
					count++;

		return count;
	}
	
	public int manhattan()                 // sum of Manhattan distances between blocks and goal
	{
		int count = 0;
		
		for (int i = 0; i < dimention; i++)
		{
			for (int j = 0; j < dimention; j++)
			{
				if ((map[i][j] != i * dimention + j + 1) && (map[i][j] != 0))
				{
					int x = (map[i][j] - 1) / dimention;
					int y = (map[i][j] - 1) % dimention;
					
					count += Math.abs(x - i) + Math.abs(y - j);
				}
			}
		}
		return count;
	}
	
	public boolean isGoal()                // is this board the goal board?
	{
		for (int i = 0; i < dimention; i++)
		{
			for (int j = 0; j < dimention; j++)
			{
				//System.out.printf("%d,%d\n", map[i][j],i * dimention + j + 1);
				if (this.map[i][j] != i * dimention + j + 1
						 && (i * dimention + j + 1 < dimention * dimention))
					 return false;
			}
		}
		return true;
	}
	
	// a board obtained by exchanging two adjacent blocks in the same row
	public Board twin()                    
	{
		for (int i = 0; i < dimention; i++)
		{
			for (int j = 0; j < dimention - 1; j++)
			{
				if (map[i][j] != 0 && map[i][j + 1] != 0)
				{
					int temp = map[i][j];
					map[i][j] = map[i][j + 1];
					map[i][j + 1] = temp;
					
					Board bd = new Board(map);
					
					temp = map[i][j];
					map[i][j] = map[i][j + 1];
					map[i][j + 1] = temp;
					
					return bd;
				}
			}
		}
		return null;
	}
	
	// does this board equal y?
	public boolean equals(Object y)        
	{
		if (this == y)
			return true;
		if (y == null)
			return false;
		if (this.getClass() != y.getClass())
			return false;
		
		Board that = (Board) y;
		
		if (this.dimention != that.dimention)
			return false;
		
		for (int i = 0; i < this.dimention; i++)
		{
			for (int j = 0; j < this.dimention; j++)
			{
				if (this.map[i][j] != that.map[i][j])
					return false;
			}
		}
		return true;
	}
	
	private void swap(int arr [][], int i, int j, int x, int y)
	{
		int temp = map[i][j];
		map[i][j] = map[x][y];
		map[x][y] = temp;
	}
	
	// all neighboring boards
	public Iterable<Board> neighbors()
	{
		Stack<Board> neighbors = new Stack<Board>();
		
		for (int i = 0; i < dimention; i++)
		{	
			for (int j = 0; j < dimention; j++)
			{
				if (map[i][j] == 0)
				{
					if (i + 1 < dimention)
					{
						swap(map, i, j, i + 1, j);
						Board bd = new Board(map);
						neighbors.push(bd);
						swap(map, i, j, i + 1, j);
					}
					if (i - 1 >= 0)
					{
						swap(map, i, j, i - 1, j);
						Board bd = new Board(map);
						neighbors.push(bd);
						swap(map, i, j, i - 1, j);
					}
					if (j + 1 < dimention)
					{
						swap(map, i, j, i, j + 1);
						Board bd = new Board(map);
						neighbors.push(bd);
						swap(map, i, j, i, j + 1);
					}
					if (j - 1 >= 0)
					{
						swap(map, i, j, i, j - 1);
						Board bd = new Board(map);
						neighbors.push(bd);
						swap(map, i, j, i, j - 1);
					}
				}
			}
		}
		
		return neighbors;
	}
	
	// string representation of the board (in the output format specified below)
	public String toString()              
	{
		String str;
		str = "\n" + String.valueOf(dimention) + "\n";
		
		for (int i = 0; i < dimention; i++)
		{	
			for (int j = 0; j < dimention; j++)
				str += " " + String.format("%2d ", map[i][j]);
			str += "\n";
		}
		return str;
	}
}