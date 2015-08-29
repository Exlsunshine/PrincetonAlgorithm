import java.util.Arrays;



public class Brute
{
	public static void main(String[] args)
	{
		String fileName = null;
		fileName = args[0];//StdIn.readString();
		int arr [] = In.readInts(fileName);
		
		int num = arr[0];
		Point [] pointSet = new Point[num];
		
		for (int i = 0;i < num; i++)
			pointSet[i] = new Point(arr[1 + i * 2], arr[2 + i * 2]);
		
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		
		Arrays.sort(pointSet);
		for (int i = 0; i < num; i++)
		{
			pointSet[i].draw();
			for (int j = i + 1; j < num; j++)
			{
				for (int k = j + 1; k < num; k++)
				{
					if (pointSet[i].slopeTo(pointSet[j]) != pointSet[i].slopeTo(pointSet[k]))
						continue;
					
					for (int l = k + 1; l < num; l++)
					{
						if ( (pointSet[i].slopeTo(pointSet[j]) == pointSet[i].slopeTo(pointSet[k]))
							&& (pointSet[i].slopeTo(pointSet[j]) == pointSet[i].slopeTo(pointSet[l])))
						{
							System.out.printf("%s -> %s -> %s -> %s\n",
									pointSet[i].toString(), pointSet[j].toString(),
									pointSet[k].toString(), pointSet[l].toString());
							pointSet[i].drawTo(pointSet[l]);
						}
					}
				}
			}
		}
	}
}
