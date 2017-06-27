import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;

public class Pi
{
    	public static void main(String[] args)
	{
		if(args.length < 2)
		{
			System.out.println("Please enter two arguments.");
			System.exit(1);
		}
		else
		{
			try
			{
				long numThreads = Long.parseLong(args[0]);
				long numIterations = Long.parseLong(args[1]);
				Pi pi = new Pi();
				pi.calc(numThreads, numIterations);
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter 'long' arguments.");
				System.exit(1);
			}
		}
    	}
    
    	public void calc(long numThreads, long numIterations)
	{
		Thread threads[] = new Thread[(int) numThreads];
		AtomicLong count = new AtomicLong(0);
		final long NUM_ITERATIONS = numIterations / numThreads;
		
		for (int i = 0; i < threads.length; i++)
		{

			threads[i] = new Thread(() -> 
			{
				int currIteration = 0;
				int inCircle = 0;
				for (int j = currIteration; j < NUM_ITERATIONS; j++)
				{
					double x = ThreadLocalRandom.current().nextDouble(1);
					double y = ThreadLocalRandom.current().nextDouble(1);
					if ((x*x + y*y) < 1) inCircle++;
				}
				count.addAndGet(inCircle);
			});
		}	
	
		//start the threads
		for (int i = 0; i < threads.length; i++) threads[i].start();
		
		//join the threads
		for (int i = 0; i < threads.length; i++)
		{
			try
			{
				threads[i].join();
			}
			catch (Exception e)
			{
				System.out.println("Failed to join threads.");
				System.exit(1);
			}
		}

		double ratio = ((double) count.get() / numIterations);
		double pi = ratio*4;

		System.out.println("\nTotal = " + numIterations);
		System.out.println("Inside = " + count.get());
		System.out.println("Ratio = " + ratio);
		System.out.println("Pi = " + pi);
    	}
}
