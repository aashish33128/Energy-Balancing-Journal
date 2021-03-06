import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Utilities {

	/**
	 * Intermeeting Times
	 */
	int[][][] Mij = null;
	Random rand = new Random();
	int N = 0;
	int[][][] meetingInfo = null;
	int simTime = 0;
	int numOfMeetingsToGenerate = PARAMETERS.numOfMeetingsToGenerate;
	
	
	public Utilities(int simTime, int N)
	{
		this.simTime = simTime;
		this.N = N;
	}

	
	/**
	 * This sets one meeting time for each pair. Each time is different, so the
	 * pairs won't miss a chance to communicate.
	 * 
	 * @param edges
	 */
	public int[][][] setMeetingTimes(int[][] edges) {
		int[][][] meetingTimes = new int[N][N][numOfMeetingsToGenerate];
		
		

		for (int i = 0; i < N; i++){
			for (int j = i + 1; j < N; j++) {
		
				float u = 0;
				Random r = new Random();
				int t = 0;
				int[] times = new int[numOfMeetingsToGenerate];
				for(int k=0;k<numOfMeetingsToGenerate;k++)
				{
				if (edges[i][j] > 0) {
					u = r.nextFloat();
					double mean = edges[i][j];
					if (u != 0) {
						t = (int) (-mean * Math.log(u));
						// System.out.println(i + ", " + j + "meets at " + t + " with " + mean + "u " + u);
						if(k==0)
						{
							times[k] = t;
						}
						else
						{
							times[k] =   t;
						}
						//addedTime.add(t);
						// System.out.println(i + " , " + j + " meets at " + times[k]);
					} else {
						while (u == 0) {
							u = r.nextFloat();
							t = (int) (-mean * Math.log(u));
							// System.out.println(i + ", " + j + "meets at " + t);
							if(k==0)
							{
								times[k] = t;
								
							}
							else
							{
								times[k] =   t;
							}
							//addedTime.add(t);
							
							// System.out.println(i + " , " + j + " meets at " + times[k]);

						}
						
					}
					
					//System.out.println("Time for " + i + "," + j + " is " + times[k]);
				}
				}
				
				//Lets try this:
				 Arrays.parallelSort(times);
				 int[] times_cloned = new int[numOfMeetingsToGenerate];
				 
				
				 for(int a =0;a<numOfMeetingsToGenerate;a++)
				 {
					 
					 if(a==0)
					 {
						 times_cloned[a] = times[a];
					 }
					 else
					 {
						 times_cloned[a] = times_cloned[a-1] + times[a];
					 }
					 
					// System.out.println(times_cloned[a]);
					 
				 }
				meetingTimes[i][j] = times_cloned;
				meetingTimes[j][i] = times_cloned;
				//System.out.println("Meeting times for "+ i + ", " + j + "is " + times);
				/*
				 * for(int a=0;a<times.length;a++) { System.out.println(times[a]); }
				 */
			}
			
		
		}
		
		//System.exit(0);
		return meetingTimes;

	}

	public int[] getExponentialTimes(int i, int j, float mean) {
		// float mean = 480;

		// Generate a non-zero uniformly-distributed random value.
		float u;
		int[] nextMeetingTime = new int[this.numOfMeetingsToGenerate];
		Random r = new Random();
		for (int a = 0; a < this.numOfMeetingsToGenerate; a++) {

			if (i == j)
				nextMeetingTime[a] = 0;
			else {
				u = r.nextFloat();
				if (u != 0) {
					int t = (int) (-mean * Math.log(u));

					nextMeetingTime[a] = t;
					// System.out.println(i + " , " + j + " meets at " + t);
				} else {
					while (u == 0) {
						u = r.nextFloat();
						int t = (int) (-mean * Math.log(u));

						nextMeetingTime[a] = t;

					}
				}

			}
		}
		Arrays.sort(nextMeetingTime);

		return ((nextMeetingTime));
	}

	/**
	 * Returns 1 if i and j meets at startTime
	 * 
	 * @param i
	 * @param j
	 * @param startTime
	 * @param meetingInfo
	 * @return
	 */
	public int meets(int i, int j, float startTime, int[][][] meetingInfo) {
		/*
		 * for (int a = 0; a < PARAMETERS.numOfMeetingsToGenerate; a++) {
		 * 
		 * if(meetingInfo[i][j]==null) return 0; if (meetingInfo[i][j][a] == startTime)
		 * return 1; }
		 * 
		 * return 0;
		 */

		for(int x=0;x<meetingInfo[i][j].length;x++)
		{
		
		if (meetingInfo[i][j][x] == startTime) {
			return 1;
		}
		}
		return 0;
	}

	/**
	 * Uniform energy distribution
	 * 
	 * @param energy
	 * @return
	 */
	public double[] setEnergyLevels(double[] energy) {

		double sum = 0;
		// Initialize energy
		for (int i = 0; i < (N); i++) {
			energy[i] = 0;
		}

		// for(int t=0;t<1;t++)
		// {
		for (int i = 0; i < N; i++) {
			energy[i] = rand.nextInt(101);
			sum = sum + energy[i];

			// }
		}

		/*
		 * //Get the average for(int i=0;i<(N+1);i++) { energy[i] = energy[i]/100;
		 * System.out.println(energy[i]); }
		 */

		// System.out.println("Total Energy " + sum);
		return energy;

	}

	public Utilities() {

	}

	/**
	 * 
	 * @param currentTime
	 * @param schedularType 0 for Probabilistic, 1 for known Mij
	 * @return
	 */
	public String getInteractingPairs(int currentTime, int schedularType, int[][][] meetingTimes) {

		
		String pairs = "";
		int first = 0;
		int second = 0;

		if (schedularType == 0) {

			while (first == second) {
				first = rand.nextInt(N);
				second = rand.nextInt(N);
			}

			pairs = first + "," + second;

		} else if (schedularType == 1) {
			if (this.meetingInfo == null) {
				System.out.println(
						"Error calling MIJ based protocols : Meeting Info not set. Call setNetworkInfo() method");
				System.exit(0);
			}

			ArrayList<String> selectedPairs = new ArrayList<>();
			for (int i = 0; i < (N); i++) {
				for (int j = 0; j < (N); j++) {
				
					if (meets(i, j, currentTime, meetingTimes) == 1) {

						pairs = i + "," + j;

						selectedPairs.add(pairs);
					}
				}
			}

			// Pick one of the pairs randomly

			if (selectedPairs.size() == 0) {
				pairs = 0 + "," + 0;
			}

			Random r = new Random();

			if (selectedPairs.size() > 0) {
				int index = r.nextInt(selectedPairs.size());
				pairs = selectedPairs.get(index);
			}
		}
		

		return pairs;

	}

	/**
	 * 
	 * @param currentTime
	 * @param schedularType 0 for Probabilistic, 1 for known Mij
	 * @return
	 */
	public String getInteractingPairs_minEnergy(int currentTime, int schedularType, double[] energy, double[] avg) {
		String pairs = "";
		int first = 0;
		int second = 0;

		if (schedularType == 0) {

			while (first == second) {
				first = rand.nextInt(N);
				second = rand.nextInt(N);
			}

			pairs = first + "," + second;

		} else if (schedularType == 1) {
			if (this.meetingInfo == null) {
				System.out.println(
						"Error calling MIJ based protocols : Meeting Info not set. Call setNetworkInfo() method");
				System.exit(0);
			}
			ArrayList<String> selectedPairs = new ArrayList<>();
			for (int i = 0; i < (N); i++) {
				for (int j = i + 1; j < (N ); j++) {
					if (meets(i, j, currentTime, meetingInfo) == 1) {
						pairs = i + "," + j;
						selectedPairs.add(pairs);
					}
				}
			}

			// Pick one of the pairs randomly

			if (selectedPairs.size() == 0) {
				pairs = 0 + "," + 0;
			}

			Random r = new Random();
			double min = 0;
			int selectedIndex = 0;

			if (selectedPairs.size() > 0) {

				for (int i = 0; i < selectedPairs.size(); i++) {
					int node1 = Integer.parseInt(selectedPairs.get(i).split(",")[0]);

					int node2 = Integer.parseInt(selectedPairs.get(i).split(",")[1]);
					double energyToExchange = 0;

					double delta1 = Math.abs(energy[node1] - avg[node1]);
					double delta2 = Math.abs(energy[node2] - avg[node2]);

					if (delta1 > min) {
						min = delta1;
						selectedIndex = i;
					}

					if (delta2 > min) {
						min = delta2;
						selectedIndex = i;
					}

				}

				int index = selectedIndex;
				// System.out.println(" selected Index is with " + selectedIndex + " , " + min)
				// ;
				pairs = selectedPairs.get(index);
			}
		}

		return pairs;

	}

	/**
	 * 
	 * @param currentTime
	 * @param schedularType 0 for Probabilistic, 1 for known Mij
	 * @return
	 */
	public ArrayList<String> getInteractingPairs_multiple(int currentTime, int schedularType) {
		String pairs = "";
		ArrayList<String> selectedPairs = new ArrayList<>();
		int first = 0;
		int second = 0;

		if (schedularType == 0) {

			while (first == second) {
				first = rand.nextInt(N);
				second = rand.nextInt(N);
			}

			pairs = first + "," + second;

		} else if (schedularType == 1) {
			if (this.meetingInfo == null) {
				System.out.println(
						"Error calling MIJ based protocols : Meeting Info not set. Call setNetworkInfo() method");
				System.exit(0);
			}

			for (int i = 0; i < (N); i++) {
				for (int j = 0; j < (N ); j++) {
					if (meets(i, j, currentTime, meetingInfo) == 1) {
						pairs = i + "," + j;
						if (!redundant(selectedPairs, pairs)) {
							selectedPairs.add(pairs);
						}

					}
				}
			}

		}

		return selectedPairs;

	}
	


	public String writeEnergies(float[] energies,  String filename) {
	//	String filename = "energies.dat";

		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter(filename));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String content = "energies=[";
		for (int a = 0; a < this.N; a++) {
			content = content + energies[a] + ",";
		}

		content = content.substring(0, content.length() - 1) + "];";

		try {
			write.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;

	}
	
	public String writeEdges(int[][] edges,String filename) {

	
		// int[][][] meetings = new int[numNodes][numNodes][time];
		int s = 0;
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			String content = "edges=[";
			for (int i = 0; i < N; i++) {
				content = content + "[";

				for (int j = 0; j < N; j++) {

					// System.out.println(i+","+j+"," + t + "," + meetings[i][j].length);
					content = content + (edges[i][j] + ",");

					// content = content.substring(0,content.length()-1) + "],";
				}

				content = content.substring(0, content.length() - 1) + "],";
			}
			content = content.substring(0, content.length() - 1) + "];";
			w.write(content);
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Edges Created");

		return filename;

	}

	public String writeHops(int[][] hops, String filename) {

		
		// int[][][] meetings = new int[numNodes][numNodes][time];
		int s = 0;
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			String content = "hops=[";
			for (int i = 0; i < N; i++) {
				content = content + "[";

				for (int j = 0; j < N; j++) {

					// System.out.println(i+","+j+"," + t + "," + meetings[i][j].length);
					content = content + (hops[i][j] + ",");

				}

				content = content.substring(0, content.length() - 1) + "],";
			}
			content = content.substring(0, content.length() - 1) + "];";
			w.write(content);
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Hops Created");

		return filename;

	}


	public boolean redundant(ArrayList<String> selectedPairs, String pairs) {
		if (selectedPairs.isEmpty()) {
			return false;
		} else {
			String nodea = pairs.split(",")[0];
			String nodeb = pairs.split(",")[1];
			for (int i = 0; i < selectedPairs.size(); i++) {
				String pair = selectedPairs.get(i);
				String node1 = pair.split(",")[0];
				String node2 = pair.split(",")[1];

				if (node1.compareTo(nodea) == 0 || node2.compareTo(nodea) == 0 || node1.compareTo(nodeb) == 0
						|| node2.compareTo(nodeb) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public void init_DArray(ArrayList<Double> array, int size) {
		for (int i = 0; i < size; i++) {
			array.add(0.0);
		}
	}

	public void writeToFile(Results rs, boolean print, String filename) throws IOException {
		/**
		 * Write Separate Files ?? Not required for now
		 */

		BufferedWriter w = new BufferedWriter(new FileWriter(filename + ".csv"));

		String content = "Time, Variation Distance, Total Energy, Number of Interactions, Plus Nodes, Minus Nodes, Energyexchanged, LifeTime";
		w.write(content);
		w.newLine();
		for (int i = 0; i < rs.vD.size(); i++) {
			String time = String.valueOf((i * PARAMETERS.recordTime));
			String vd = String.valueOf(rs.vD.get(i) / (double) PARAMETERS.numSim);
			String te = String.valueOf(rs.totalEnergy.get(i) / (double) PARAMETERS.numSim);
			String ex = String.valueOf(rs.energyPrediction.get(i) / (double)  PARAMETERS.numSim);
			String ni = String.valueOf(rs.numInteractions.get(i) / (double)  PARAMETERS.numSim);
			String plus = String.valueOf(rs.numNodesPlus.get(i) / (double)  PARAMETERS.numSim);
			String minus = String.valueOf(rs.numNodesminus.get(i) / (double)  PARAMETERS.numSim);
			String lifetime = String.valueOf(rs.lifeTime.get(i) / (double)  PARAMETERS.numSim);
			content = time + ", " + vd + " , " + te + " , " + ni + ", " + plus + " , " + minus + ", " + ex + ", " + lifetime;

			w.write(content);
			w.newLine();

			// System.out.println("PARAMETERS");
			if (print) {

				// System.out.println(vd);
				// System.out.println(rs.energyPrediction.get(i)/(double)PARAMETERS.simTime);
			}
		}

		w.close();

	}

	/**
	 * Calculate Intermeeting degrees between nodes using statistical expectation.
	 * Imd(n1,n2) = sum(lambda n1 n2/(lambda n1 n2 + lamda n1 k) for all k!=n2)
	 * 
	 * @param n1
	 * @param n2
	 * @param Mij
	 * @return
	 */
	public double caluclateIMDUsingLamda(int n1, int n2, double[][] Mij) {
		double sum = 0;
		for (int k = 0; k < (N); k++) {
			if (k != n2) {
				double value = Mij[n1][n2] / (Mij[n1][n2] + Mij[n1][k]);
				sum = sum + value;
			}
		}

		// System.out.println("IMD between " + n1 + " and " + n2 + " is " + sum);
		return sum;
	}

	/**
	 * calculate Intermeeting degrees seeing actual count
	 * 
	 * @param n1
	 * @param n2
	 * @param time
	 * @param meet
	 * @return
	 */

	public double getIntermeetingDegree(int n1, int n2, int[] time, int[][][] meet) {

		double sum = 0;
		int t0 = time[0];
		int t1 = time[1];
		int t = 1;

		while (true) {

			for (int j = 0; j < (N ); j++) {
				if (j == n2)
					continue;

				for (int i = 0; i < time.length; i++) {
					if (meet[n1][j][i] > t0 && meet[n1][j][i] < t1) {
						sum = sum + 1;
						break;
					}
				}
			}
			t++;
			if (t >= this.numOfMeetingsToGenerate) {
				break;
			}

			t0 = t1;
			t1 = time[t];
		}

		sum = sum / time.length;
		return sum;

	}

	public Queue<String> processQueue(double element, Queue<String> mem, int node, int time, int K) {

		String content = node + "_" + element + "_" + time;
		Queue<String> q = new LinkedList<>();

		if (mem.size() > 0) {

			// Check your own queue
			for (int i = 0; i < mem.size(); i++) {
				// Node already present in memory:
				// update with latest info. remove the old one

				String item = mem.remove();
				if (item.split("_")[0] != String.valueOf(node)) {
					q.add(item);
				}
			}

			q.add(content);

			if (mem.size() > K) {
				mem.remove();
			}

		} else {

			q.add(content);
		}

		return q;

	}

	public Queue<String> updateQueue(Queue<String> mem, String content, int K) {

		// String content = node + "_"+ element + "_"+time;
		String node = content.split("_")[0];
		Queue<String> q = new LinkedList<>();

		if (mem.size() > 0) {

			// Check your own queue
			for (int i = 0; i < mem.size(); i++) {
				// Node already present in memory:
				// update with latest info. remove the old one

				String item = mem.remove();
				if (item.split("_")[0] != String.valueOf(node)) {
					q.add(item);
				}
			}

			q.add(content);

			if (mem.size() > K) {
				mem.remove();
			}

		} else {
			return q;
		}

		return q;

	}

	/**
	 * check if the queue has info about the requested node
	 * 
	 * @param q
	 * @param node
	 * @return
	 */
	public String getNodeInfo(Queue<String> q, String node) {
		for (String item : q) {
			if (item.split("_")[0].compareTo(node) == 0) {
				return item;
			}

		}

		return "";
	}

	public Queue<String> syncQueue(Queue<String> mem1, Queue<String> mem2, int K) {
		Queue<String> queue = new LinkedList<>();

		for (String item : mem1) {
			String hasinfo = getNodeInfo(mem2, item.split("_")[0]);
			if (!hasinfo.isEmpty()) {
				int stime = Integer.parseInt(item.split("_")[2]);
				int dtime = Integer.parseInt(hasinfo.split("_")[2]);

				if (dtime > stime) {
					// Content is outdated, update
					queue = updateQueue(mem1, hasinfo, K);

				}
			} else {
				queue = mem1;
			}
		}

		return queue;
	}

	public double getCurrentAverage(Queue<String> q, double energy) {
		double sum = energy;
		int count = 1;

		for (String item : q) {

			sum = sum + Double.parseDouble(item.split("_")[1]);

			count++;
		}

		double avg = sum / (double) count;
		return avg;
	}

	public Queue<String> mergeQueues(Queue<String> q1, Queue<String> q2, int K) {
		for (String item : q2) {
			if (!q1.contains(item)) {
				q1.add(item);
			}

			if (q1.size() > K) {
				q1.remove();
			}
		}

		return q1;
	}
	
	public int isNetworkConnected(int[][] edges)
	{
		
		int countNodes = 0;
		
		for(int i=0;i<N;i++)
		{
			int count = 0;
			//Check for each node if it has neighbors. if has, its connected
			for(int j=0; j<N;j++)
			{
				if(edges[i][j] > 0 && edges[i][j]<=PARAMETERS.threshold)
				{
					count = count + 1;
					break;
				}
				
			}
			if(count>0)
			{
				countNodes = countNodes + 1;
			}
			
		}
		
		System.out.println("---------------------------Total Nodes connected" + countNodes);
		if(countNodes==N)
		{
			return 1;
		}
		return 0;
	}

}
