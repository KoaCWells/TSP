import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealing 
{	
	static void setupCosts(int[][] Costs,int numCities)
	{
		Random r = new Random();
		int cost = 0;
		int low = 100;
		int high = 2501;
		for(int i = 0; i < numCities; i++)
		{
			for(int j = i; j < numCities; j++)
			{
				if(j == i) {Costs[i][j] = 0;} 
				else
				{
					cost = r.nextInt(high - low) + low;
					Costs[i][j] = cost;
					Costs[j][i] = cost;
				}
			}
		}	
	}
	static int[] generatePath(int numCities)
	{
		boolean[] chosen = new boolean[numCities]; 
		boolean found = false;
		int index = 0;
		int[] path = new int[numCities + 1];
		for(int i = 0; i < numCities; i++)
		{
			found = false;
			while(!found)
			{
				index = (int)(numCities * Math.random());
				if(chosen[index] == false)
				{ 
					found = true;
					chosen[index] = true;
				} 
			}
			path[i] = index;
		}
		path[numCities] = path[0];
		return path;
	}
	
	static int[] swap(int[] array, int numCities, Random rand)
	{
		int[] path = Arrays.copyOf(array, array.length);
		boolean idxCheck = false;
		while(!idxCheck)
		{
			int idx1 = rand.nextInt(numCities - 1) + 1;
			int idx2 = rand.nextInt(numCities - 1) + 1;
			if(idx1 == idx2)
			{
				idxCheck = false;
			}
			else 
			{
				idxCheck = true;
				//System.out.println("Swap index 1: " + idx1 + "\nSwap index 2: " + idx2);
				int temp1 = path[idx1];
				int temp2 = path[idx2];
				path[idx2] = temp1;
				path[idx1] = temp2;
			}
		}
		return path;
	}
	
	static int pathCost(int[] path, int[][] costMat, int numCities)
	{
		int cost = 0; 
		for(int i = 0; i < numCities; i++)
		{
			cost += costMat[path[i]][path[i + 1]];
		}
		return cost;
	}
	
	static double acceptance(int currCost, int newCost, double temp)
	{
		if(currCost - newCost > 0)
		{
			return 1.0;
		}
		return Math.exp((currCost - newCost) / temp);
	}
	
	static void printPath(int[] array)
	{
		System.out.println("Path:");
		for(int j = 0; j < array.length; j++)
		{ System.out.print(array[j] + " "); }
	}
	
	public static void main(String[] args)
	{
		int NUM_CITIES = 10;
		int c = 0;
		int BestPath[] = new int[NUM_CITIES + 1]; 
		int current[]= new int[NUM_CITIES + 1];
		int next[]= new int[NUM_CITIES + 1];
		int[][] TravelCosts = new int[NUM_CITIES][NUM_CITIES];
		double T = 10000; 
		double cooling = 0.5;
		Random r = new Random();
		
	    setupCosts(TravelCosts, NUM_CITIES);
	    System.out.println("Cost adjacency matrix:");
		for(int i = 0; i < NUM_CITIES; i++)
		{
			for(int j = 0; j < NUM_CITIES; j++)
			{
				System.out.print(TravelCosts[i][j] + "\t");
			}
			System.out.println();
		}
		
		current = generatePath(NUM_CITIES);
		System.out.println("\nInitial path:");
		for(int i = 0; i < current.length; i++)
		{ System.out.print(current[i] + " "); }
		System.out.println();
		int currCost = pathCost(current, TravelCosts, NUM_CITIES);
		System.out.println("Initial Cost: " + currCost);
		System.out.println("Initial T: " + T + "\n");
		
		double accept = 0.0;
		for(int i = 0; i < 10; i++)
		{
			System.out.println("T: " + T);
			next = swap(current,NUM_CITIES,r);
			c = pathCost(next, TravelCosts, NUM_CITIES);
			accept = acceptance(currCost, c, T);
			printPath(next);
			System.out.println("\nNext path cost: " + c);
			System.out.println("Acceptance prob: " + accept +"\n");
			
			T *= 1 - cooling;
		}
	}
}
