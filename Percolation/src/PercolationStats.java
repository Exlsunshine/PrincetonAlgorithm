import java.util.Random;


public class PercolationStats 
{
    private double [] fractions;
    private int T;
    public PercolationStats(int N, int T)    // perform T independent computational experiments on an N-by-N grid
	{
		this.T = T;
		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException("N<=0");
		
		fractions = new double [T];
		
		
		for (int i = 0; i < T; i++)
		{
			Random rand = new Random();
			int cnt = 0;
			Percolation pl = new Percolation(N);
			while (!pl.percolates())
			{
				int x = rand.nextInt(N);
				int y = rand.nextInt(N);
				x++;
				y++;
				
				if (!pl.isOpen(x, y))
				{	
					pl.open(x , y);
					cnt++;
				}
			}
			fractions[i] = 1.0 * cnt / (N * N);
		}
	}
	public double mean()                      // sample mean of percolation threshold
	{
		double mean = 0;
		for (int i = 0; i < T; i++)
		{
			mean += fractions[i];
		}
		mean /= T;
		return mean;
	}
	public double stddev()                    // sample standard deviation of percolation threshold
	{
		double mean = mean();
		double dev = 0;
		for (int i = 0; i < T; i++)
		{
			dev += (fractions[i] - mean) * (fractions[i] - mean);
		}
		dev /= (T - 1);
		return dev;
	}
	public double confidenceLo()             // returns lower bound of the 95% confidence interval
	{
		double mean = mean();
		double dev = stddev();
		double lowConf = mean - 1.96 * dev / Math.sqrt(T);
		return lowConf;
	}
	public double confidenceHi()             // returns upper bound of the 95% confidence interval
	{
		double mean = mean();
		double dev = stddev();
		double upConf = mean + 1.96 * dev / Math.sqrt(T);
		return upConf;
	}
	public static void main(String[] args)   // test client, described below
	{
		
	}
}
