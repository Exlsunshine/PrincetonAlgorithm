import java.awt.Color;

public class SeamCarver 
{
	private Picture picture = null;
	private int width = 0;
	private int height = 0;

	/**
	 *  Create a seam carver object based on the given picture
	 */
	public SeamCarver(Picture picture)
	{
		this.picture = new Picture(picture);
		width = this.picture.width();
		height = this.picture.height();
	}
	
	/**
	 * Current picture
	 */
	public Picture picture()
	{
		return picture;
	}   
	
	/**
	 * Width of current picture
	 */
	public int width()
	{
		return width;
	}
	
	/**
	 * Height of current picture
	 */
	public int height()
	{
		return height;
	}
	
	/**
	 * Energy of pixel at column x and row y
	 */
	public double energy(int x, int y)
	{
		if (x >= width || y >= height || x < 0 || y < 0)
			throw new java.lang.IndexOutOfBoundsException("IndexOut Of Bound");
		
		if (x == width - 1 || x == 0 || y == height - 1 || y == 0)
			return 195075.0;
		else
		{
			double energy = 0;
			
			Color left = picture.get(x - 1, y);
			Color right = picture.get(x + 1, y);
			Color up = picture.get(x, y - 1);
			Color bottom = picture.get(x, y + 1);
			
			energy += Math.pow(Math.abs(left.getRed() - right.getRed()),2);
			energy += Math.pow(Math.abs(left.getGreen() - right.getGreen()),2);
			energy += Math.pow(Math.abs(left.getBlue() - right.getBlue()),2);
			
			energy += Math.pow(Math.abs(up.getRed() - bottom.getRed()),2);
			energy += Math.pow(Math.abs(up.getGreen() - bottom.getGreen()),2);
			energy += Math.pow(Math.abs(up.getBlue() - bottom.getBlue()),2);
			
			return energy;
		}
	}
	
	public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
	{
		double minDist = Integer.MAX_VALUE * 1.0;
		int[] minPath =  new int[width];
		
		int minIndex = getFromSeedHorizional();

		minDist = distTo[width - 1][minIndex];
		minPath[width - 1] = minIndex;
		for (int j = width - 2; j >= 0; j--)
			minPath[j] = edgeToRow[j + 1][minPath[j + 1]];
			
		/*
		for (int i = 0; i < width; i++)
			System.out.printf("%d,", minPath[i]);
		*/
		
		return minPath;
	}
	
	private int getFromSeedHorizional()
	{
		double [][] energyMatrix = new double[width][height];
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				energyMatrix[i][j] = energy(i,j);

		distTo = new double[width][height];
		//edgeTo = new int[width][height];
		edgeToRow = new int[width][height];
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				distTo[i][j] = Double.POSITIVE_INFINITY;
				//edgeTo[i][j] = -2;
				edgeToRow[i][j] = -2;
			}
		}
		for (int i = 0; i < height; i++)
		{
			distTo[0][i] = energyMatrix[0][i];
			//edgeTo[0][i] = -2;
			edgeToRow[0][i] = -2;
		}		
	
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				dfsHorizional(energyMatrix, i, j);
		
		/*
		
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
				System.out.printf("%9.0f\t",distTo[i][j]);
			System.out.printf("\n");
		}
		System.out.printf("\n\n");

		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
				System.out.printf("(%d,%d)\t",edgeToRow[i][j],edgeTo[i][j]);
			System.out.printf("\n");
		}
		*/
		
		//get min distance
		double minDist = Integer.MAX_VALUE;
		int minRowIndex = -1;
		for (int i = 0; i < height; i++)
		{
			if (minDist > distTo[width - 1][i])
			{
				minDist = distTo[width - 1][i];
				minRowIndex = i;
			}
		}
		
		return minRowIndex;
	}
	
	private void dfsHorizional(double [][] energyMatrix, int col, int row)
	{
		if ( col + 1 < width)
		{
			if (   distTo[col + 1][row] > distTo[col][row] + energyMatrix[col + 1][row])
			{
				   distTo[col + 1][row] = distTo[col][row] + energyMatrix[col + 1][row];
				   //edgeTo[col + 1][row] = col;
				edgeToRow[col + 1][row] = row;
			}
			
			if (row - 1 >= 0)
			{
				if (   distTo[col + 1][row - 1] > distTo[col][row] + energyMatrix[col + 1][row - 1])
				{
					   distTo[col + 1][row - 1] = distTo[col][row] + energyMatrix[col + 1][row - 1];
					   //edgeTo[col + 1][row - 1] = col;
					edgeToRow[col + 1][row - 1] = row;
				}
			}
			if (row + 1 < height)
			{
				if (   distTo[col + 1][row + 1] > distTo[col][row] + energyMatrix[col + 1][row + 1])
				{
					   distTo[col + 1][row + 1] = distTo[col][row] + energyMatrix[col + 1][row + 1];
					   //edgeTo[col + 1][row + 1] = col;
					edgeToRow[col + 1][row + 1] = row;
				}
			}
		}
	}
	
	/**
	 * Sequence of indices for vertical seam
	 */
	public int[] findVerticalSeam()
	{
		double minDist = Integer.MAX_VALUE * 1.0;
		int[] minPath =  new int[height];
		
		int minIndex = getFromSeed();
		
		//System.out.printf("Min Index is %d\n", minIndex);
		
		minDist = distTo[minIndex][height - 1];
		minPath[height - 1] = minIndex;
		for (int j = height - 2; j >= 0; j--)
			minPath[j] = edgeTo[minPath[j + 1]][j + 1];
		
		/*
		System.out.printf("\n\n");
		for (int i = 0; i < height; i++)
			System.out.printf("%d,", minPath[i]);
		*/
		
		return minPath;
	}
	
	private double[][] distTo = null;
	private int[][] edgeTo = null;
	private int[][] edgeToRow = null;
	
	private int getFromSeed()
	{
		double [][] energyMatrix = new double[width][height];
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				energyMatrix[i][j] = energy(i,j);

		/*
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
				System.out.printf("%9.0f", energyMatrix[j][i]);
			System.out.printf("\n");
		}
		*/
		
		distTo = new double[width][height];
		edgeTo = new int[width][height];
		//edgeToRow = new int[width][height];
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				distTo[i][j] = Double.POSITIVE_INFINITY;
				edgeTo[i][j] = -2;
				//edgeToRow[i][j] = -2;
			}
		}
		for (int i = 0; i < width; i++)
		{
			distTo[i][0] = energyMatrix[i][0];
			edgeTo[i][0] = -2;
			//edgeToRow[i][0] = -2;
		}
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				dfs(energyMatrix,j, i);
		
		
		/*
		for (int j = 0; j < height; j++)//row
		{
			for (int i = 0; i < width; i++)//coloumn
				System.out.printf("%9.0f\t",distTo[i][j]);
			System.out.printf("\n");
		}
		System.out.printf("\n\n");

		for (int j = 0; j < height; j++)//row
		{
			for (int i = 0; i < width; i++)//coloumn
				System.out.printf("(%d,%d)\t",edgeToRow[i][j],edgeTo[i][j]);
			System.out.printf("\n");
		}
		*/
		
		//get min distance
		double minDist = Integer.MAX_VALUE;
		int minColIndex = -1;
		for (int i = 0; i < width; i++)
		{
			if (minDist > distTo[i][height - 1])
			{
				minDist = distTo[i][height - 1];
				minColIndex = i;
			}
		}
		
		return minColIndex;
	}
	
	

	private void dfs(double [][] energyMatrix, int col, int row)
	{
		if ( row + 1 < height)
		{
			if (distTo[col][row + 1] > distTo[col][row] + energyMatrix[col][row + 1])
			{
				distTo[col][row + 1] = distTo[col][row] + energyMatrix[col][row + 1];
				edgeTo[col][row + 1] = col;
				//edgeToRow[col][row + 1] = row;
			}
			
			if (col - 1 >= 0)
			{
				if (distTo[col - 1][row + 1] > distTo[col][row] + energyMatrix[col - 1][row + 1])
				{
					distTo[col - 1][row + 1] = distTo[col][row] + energyMatrix[col - 1][row + 1];
					edgeTo[col - 1][row + 1] = col;
					//edgeToRow[col - 1][row + 1] = row;
				}
			}
			if (col + 1 < width)
			{
				if (distTo[col + 1][row + 1] > distTo[col][row] + energyMatrix[col + 1][row + 1])
				{
					distTo[col + 1][row + 1] = distTo[col][row] + energyMatrix[col + 1][row + 1];
					edgeTo[col + 1][row + 1] = col;
					//edgeToRow[col + 1][row + 1] = row;
				}
			}
		}
	}
	
	public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
	{
		if (width <= 1 || height <= 1 || seam.length > width)
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		
		if (seam == null)
			throw new java.lang.NullPointerException("Null pointer");
		
		
		Picture pic = new Picture(width, height - 1);
		
		for (int i = 0; i < width; i++)
		{
			int offset = 0;
			for (int j = 0; j < height; j++)
			{
				if (seam[i] == j)
				{
					offset = 1;
					continue;
				}
				else
					pic.set(i, j - offset, picture.get(i, j));
			}
		}
		
		height--;
		picture = pic;
	}
	
	public void removeVerticalSeam(int[] seam) 
	{
		if (width <= 1 || height <= 1 || seam.length > height)
			throw new java.lang.IllegalArgumentException("IllegalArgumentException");
		
		if (seam == null)
			throw new java.lang.NullPointerException("Null pointer");
		
		Picture pic = new Picture(width - 1, height);
		
		for (int i = 0; i < height; i++)
		{
			int offset = 0;
			for (int j = 0; j < width; j++)
			{
				if (seam[i] == j)
				{
					offset = 1;
					continue;
				}
				else
					pic.set(j - offset, i, picture.get(j, i));
			}
		}
		
		width--;
		picture = pic;
	}
	
	
	/**
	 * Private helper.
	 * Can be deleted.
	 */
	private void PrintEnergy (String path)
	{
		Picture inputImg = new Picture(path);
		System.out.printf("", 1);
        System.out.printf("image is %d pixels wide by %d pixels high.\n", inputImg.width(), inputImg.height());
        
        SeamCarver sc = new SeamCarver(inputImg);
        
        System.out.printf("Printing energy calculated for each pixel.\n");        

        for (int j = 0; j < sc.height(); j++)
        {
            for (int i = 0; i < sc.width(); i++)
                System.out.printf("%9.0f ", sc.energy(i, j));

            System.out.println();
        }
	}
	
	public static void main(String[] args) 
	{
		Picture inputImg = new Picture("D:\\myProgram\\java\\workspace\\SeamCarver\\dat\\seamCarving\\10x12.png");
		System.out.printf("", 1);
        System.out.printf("image is %d pixels wide by %d pixels high.\n", inputImg.width(), inputImg.height());
        
        SeamCarver sc = new SeamCarver(inputImg);
        
        System.out.printf("Printing energy calculated for each pixel.\n");        

        /*
        for (int j = 0; j < sc.height(); j++)
        {
            for (int i = 0; i < sc.width(); i++)
                System.out.printf("%9.0f ", sc.energy(i, j));

            System.out.println();
        }*/
        
        sc.removeVerticalSeam(sc.findVerticalSeam());
	}
}