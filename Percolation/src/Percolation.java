
public class Percolation 
{
    private int [][] map; 
    private WeightedQuickUnionUF uf = null;
    private int N;
	
	public Percolation(int N)// create N-by-N grid, with all sites blocked
	{
		if (N <= 0)
			throw new IllegalArgumentException("N<=0");
		
		this.N = N;
		uf = new WeightedQuickUnionUF(N*N);
		map = new int [N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				map[i][j] = 1; // 1 means blocked!!!
	} 
	   
	public void open(int i, int j) // open site (row i, column j) if it is not already
	{
		int m = i - 1;
		int n = j - 1;
		if (m < 0 || n < 0 || m >= N || n >= N)
			throw new IndexOutOfBoundsException("Out of index");
		
		if (map[m][n] == 0)
			return;
		else	
		{
			map[m][n] = 0;
			if (m - 1 >= 0 && map[m - 1][n] == 0)
				uf.union(N * m + n, N * (m - 1) + n);
			if (m + 1 < N  && map[m + 1][n] == 0)
				uf.union(N * m + n, N * (m + 1) + n);
			if (n - 1 >= 0 && map[m][n-1] == 0)
				uf.union(N * m + n, N * m + n - 1);
			if (n + 1 < N  && map[m][n + 1] == 0)
				uf.union(N * m + n, N * m + n + 1);
		}
	}          
	   
	public boolean isOpen(int i, int j) // is site (row i, column j) open?
	{	   
		int m = i - 1;
		int n = j - 1;
		if (m < 0 || n < 0 || m >= N || n >= N)
			throw new IndexOutOfBoundsException("Out of index");
		
		return map[m][n] == 0;
	}      
	  
	public boolean isFull(int i, int j) // is site (row i, column j) full?
	{ 
		int m = i - 1;
		int n = j - 1;
		if (m < 0 || n < 0 || m >= N || n >= N)
			throw new IndexOutOfBoundsException("Out of index");
		
		int id = m * N + n;
		for (int k = 0; k < N; k++)
		{
			if (map[m][n] == 0 && uf.connected(id, k))
				return true;
		}
		return false;
	}     
	   
	public boolean percolates()  // does the system percolate?
	{
		for (int i = 0; i < N; i++)
		{
			for (int j = 0; j < N; j++)
			{
				if (uf.connected(i, (N - 1) * N + j) && map[0][i] == 0 && map[N-1][j] == 0)
					return true;
			}
		}
		return false;
	}             
	   //public static void main(String[] args)   // test client, optional

}
