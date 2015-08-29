import java.util.Arrays;

public class Fast 
{	
	public static void main(String[] args)
	{
		int arr [] = In.readInts(args[0]);
		int num = arr[0];
		Point [] pointSet = new Point[num];
		Point cp [] = new Point[num];
		
		StdDraw.setPenRadius(0.01);
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (int i = 0;i < num; i++)
		{
			cp[i] = new Point(arr[1 + i * 2], arr[2 + i * 2]);
			pointSet[i] = new Point(arr[1 + i * 2], arr[2 + i * 2]);
			pointSet[i].draw();
		}
		StdDraw.setPenRadius();

		Arrays.sort(cp);
		double slopes [] = new double [num];
		for (int i = 0; i < num; i++)
		{
			//restore
			for (int j = 0; j < num; j++)
				pointSet[j] = cp[j];
			
			//calc slope to i
			for (int j = 0; j < num; j++)
				slopes[j] = pointSet[i].slopeTo(pointSet[j]);
			
			//sort
			InsertionSort(slopes,pointSet,num,i);
			//for (int j = 0; j < num; j++)
			//	System.out.printf("%d:%f,%s\t", i, slopes[j], pointSet[j]);
			//System.out.printf("\n");
			
			//check lines
			int cnt = 1;
			for (int j = i; j < num - 1; j++)
			{
				if (slopes[j] == slopes[j + 1])
					cnt++;
				else if (j == i)
					continue;
				else
				{
					boolean exist = false;
					for (int s = 0; s < i; s++)
					{
						if (slopes[s] == slopes[j])
						{
							exist = true;
							break;
						}
					}
					
					if (cnt >= 3 && !exist)
					{
						System.out.printf("%s -> ", cp[i].toString());
						for (int m = j - cnt + 1; m < j; m++)
							System.out.printf("%s -> ", pointSet[m]);
						System.out.printf("%s\n", pointSet[j].toString());
						
						cp[i].drawTo(pointSet[j]);
						cnt = 1;
					}
					else
						cnt = 1;
				}
			} 
			boolean exist = false;
			for (int s = 0; s < i; s++)
			{
				if (slopes[s] == slopes[num - 1])
				{
					exist = true;
					break;
				}
			}
			if (cnt >= 3 && !exist)
			{
				System.out.printf("%s -> ", cp[i].toString());
				for (int m = num - cnt; m < num - 1; m++)
					System.out.printf("%s -> ", pointSet[m]);
				System.out.printf("%s\n", pointSet[num - 1].toString());
				
				cp[i].drawTo(pointSet[num - 1]);
			}
		}
		//System.out.printf("over!");
	}
	
	
	private static void InsertionSort(double a [],Point ap [], int n,int low)
	{
		for (int i = low; i < n; i++)
		{
			int j = i;
			
			while (j >= low + 1)
			{
				if (a[j] < a[j - 1])
				{
					double td = a[j];
					a[j] = a[j - 1];
					a[j - 1] = td;
					
					Point tp = ap[j];
					ap[j] = ap[j - 1];
					ap[j - 1] = tp;		
				}
				j--;
			}
		}
	}
	
	private static void selectSort(double a [],Point ap [], int n)
	{
		for (int i = 0; i < n; i++)
		{
			int min = i;
			for (int j = i + 1; j < n; j++)
			{
				if (a[min] > a[j])
					min = j;
			}
			double td = a[min];
			a[min] = a[i];
			a[i] = td;
			
			Point tp = ap[min];
			ap[min] = ap[i];
			ap[i] = tp;			
		}
	}
}