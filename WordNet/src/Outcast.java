public class Outcast 
{
	private WordNet wordnet = null;
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet)
	{
		this.wordnet = wordnet;
	}
	
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns)
	{
		int length = nouns.length;
		double [] distance = new double[length];
		double maxDistance = -1;
		int maxIndex = -1;
		
		for (int i = 0; i < length; i++)
		{
			for (int j = 0; j < length; j++)
			{
				if (i != j)
					distance[i] += wordnet.distance(nouns[i], nouns[j]);

				//System.out.printf("%d,%d:%f\n", i,j,distance[i]);
			}
			
			if (distance[i] > maxDistance)
			{
				maxDistance = distance[i];
				maxIndex = i;
			}
		}
		
		//System.out.printf("\n%f,%d\n", maxDistance,maxIndex);
		
		return nouns[maxIndex];
	}

	// see test client below
	public static void main(String[] args)
	{
		String synset = "D:\\myProgram\\java\\workspace\\WordNet\\dat\\wordnet\\synsets.txt";
		String hypernyms = "D:\\myProgram\\java\\workspace\\WordNet\\dat\\wordnet\\hypernyms.txt";
		
		WordNet wordnet = new WordNet(synset, hypernyms);
	    Outcast outcast = new Outcast(wordnet);


	    In in = new In();
	    String line = in.readLine();
	    String [] nouns = line.split(" ");
	    StdOut.println(line + ":\t" + outcast.outcast(nouns));
	}
}