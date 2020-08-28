
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ilog.concert.*;

import ilog.opl.*;

public class ApplicationMain {

	// int time = 50;
	int numNodes = PARAMETERS.numNodes;
	float[] energies = new float[numNodes];
	int[][] edges = new int[numNodes][numNodes];
	float[] neighbours = new float[numNodes];

	/*
	 * float[] energies = { 90, 80, 45, 25, 5, 5, 5 }; int[][] edges = { { 0, 10, 0,
	 * 0, 0, 0, 0 }, { 10, 0, 30, 5, 50, 0, 0 }, { 0, 30, 0, 0, 15, 0, 0 }, { 0, 5,
	 * 0, 0, 25, 0, 0 }, { 0, 50, 15, 25, 0, 10, 0 }, { 0, 0, 0, 0, 10, 0, 10 }, {
	 * 0, 0, 0, 0, 0, 10, 0 } };
	 */
	// New Example

	// double[] energies = { 15, 25, 55, 90 };
	// int[][] edges = { { 0, 30, 15, 0 }, { 30, 0, 20, 50 }, { 15, 20, 0, 35 }, {
	// 0, 50, 35, 0 } };

	int[][] hops = new int[numNodes][numNodes];
	int threshold = PARAMETERS.threshold;
	int simTime = PARAMETERS.simTime;
	float beta = PARAMETERS.beta;
	float[] to_receive = new float[numNodes];
	ArrayList<Node> Nodes = new ArrayList<Node>();

	/**
	 * Need to separate high energy nodes and lower energy nodes so divided into two
	 * 
	 * @return
	 */
	public float[] setEnergies_Distributed() {
		this.energies = new float[numNodes];
		Random rand = new Random();
		int high = (int) numNodes / 2;
		int low = numNodes - high;

		float[] energies_high = new float[high];
		float[] energies_low = new float[low];

		for (int i = 0; i < high; i++) {
			energies_high[i] = 50 + rand.nextInt(50);
		}
		for (int i = 0; i < low; i++) {
			energies_low[i] = 0 + rand.nextInt(50);
		}

		System.arraycopy(energies_high, 0, this.energies, 0, high);
		System.arraycopy(energies_low, 0, this.energies, high, low);
		return this.energies;
	}

	public int[][] setEdges_Distributed() {
		this.edges = new int[numNodes][numNodes];
		int high = (int) numNodes / 2;
		int low = numNodes - high;
		float dd = PARAMETERS.sparsity_internal;

		// high to low
		for (int i = 0; i < high; i++) {
			for (int j = high; j < numNodes; j++) {
				Random r = new Random();
				float value = r.nextFloat();
				if (dd >= value && i != j) {
					edges[i][j] = PARAMETERS.minIntermeetingTime
							+ r.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);
					edges[j][i] = edges[i][j];
				} else {
					edges[i][j] = 0;
					edges[j][i] = 0;
				}
			}
		}

		// high to high
		for (int i = 0; i < high; i++) {

			for (int j = 0; j < high; j++) {
				Random r = new Random();
				if (i != j) {
					float value = r.nextFloat();
					if (PARAMETERS.sparsity >= value) {
						edges[i][j] = PARAMETERS.minIntermeetingTime
								+ r.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);

						edges[j][i] = edges[i][j];
					}
				} else {
					edges[i][j] = 0;
					edges[j][i] = 0;
				}
			}
		}

		// low to low
		for (int i = high; i < numNodes; i++) {

			for (int j = high; j < numNodes; j++) {
				Random r = new Random();
				if (i != j) {
					float value = r.nextFloat();
					if (PARAMETERS.sparsity >= value) {
						edges[i][j] = PARAMETERS.minIntermeetingTime
								+ r.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);
						edges[j][i] = edges[i][j];
					}
				} else {
					edges[i][j] = 0;
					edges[j][i] = 0;
				}
			}
		}
		return edges;
	}

	public int[][] setEdges_Disconnected() {
		this.edges = new int[numNodes][numNodes];
		int high = (int) numNodes / 2;
		int low = numNodes - high;
		float dd = PARAMETERS.sparsity_internal;

		// high to low
		for (int i = 0; i < high; i++) {
			for (int j = high; j < numNodes; j++) {
				Random r = new Random();
				float value = r.nextFloat();
				if (dd >= value && i != j) {
					edges[i][j] = PARAMETERS.minIntermeetingTime
							+ r.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);
					edges[j][i] = edges[i][j];
				} else {
					edges[i][j] = 0;
					edges[j][i] = 0;
				}
			}
		}

		// high to high
		for (int i = 0; i < high; i++) {

			for (int j = 0; j < high; j++) {
				Random r = new Random();
				if (i != j) {
					float value = r.nextFloat();
					if (PARAMETERS.sparsity >= value) {
						edges[i][j] = PARAMETERS.minIntermeetingTime
								+ r.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);

						edges[j][i] = edges[i][j];
					}
				} else {
					edges[i][j] = 0;
					edges[j][i] = 0;
				}
			}
		}

		// low to low
		for (int i = high; i < numNodes; i++) {

			for (int j = high; j < numNodes; j++) {
				Random r = new Random();
				if (i != j) {
					float value = r.nextFloat();
					if (PARAMETERS.sparsity >= value) {
						edges[i][j] = PARAMETERS.minIntermeetingTime
								+ r.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);
						edges[j][i] = edges[i][j];
					}
				} else {
					edges[i][j] = 0;
					edges[j][i] = 0;
				}
			}
		}
		return edges;
	}

	public float[] setEnergies() {
		Random rand = new Random();
		for (int i = 0; i < numNodes; i++) {
			energies[i] = 0 + rand.nextInt(100);
		}
		return energies;
	}
	

	public int[][] setEdges() {
		for (int i = 0; i < numNodes; i++) {
			Random rand = new Random();
			for (int j = i + 1; j < numNodes; j++) {
				if (i == j) {
					edges[i][j] = 0;
				} else {

					Random r = new Random();
					float value = r.nextFloat();
					// loat sparsity = PARAMETERS.sparsity_min +
					// rand.nextFloat()*(PARAMETERS.sparisity_max-PARAMETERS.sparsity_min);
					float sparsity = PARAMETERS.sparsity;
					if (value <= sparsity) {
//						/System.out.println(value + "----" + sparsity);
						edges[i][j] = PARAMETERS.minIntermeetingTime
								+ rand.nextInt(PARAMETERS.maxIntermeetingTime - PARAMETERS.minIntermeetingTime);
						edges[j][i] = edges[i][j];
					}

					else {
						edges[i][j] = 0;
						edges[j][i] = 0;
					}

				}
			}
		}
		return edges;
	}

	
	public int[][] setEdges_Data(int[][][] meetingTimes) {
		for (int i = 0; i < numNodes; i++) {
			Random rand = new Random();
			for (int j = i + 1; j < numNodes; j++) {
				if (i == j) {
					edges[i][j] = 0;
					edges[j][i] = 0;
				} else {
					int intermeeting = 0;
					int count = 0;
					// System.out.println(meetingTimes[i][j]);
					for (int x = 0; x < meetingTimes[i][j].length - 1; x++) {
						Arrays.parallelSort(meetingTimes[i][j]);
						if (meetingTimes[i][j][x + 1] > 0) {

							intermeeting = intermeeting + Math.abs(meetingTimes[i][j][x + 1] - meetingTimes[i][j][x]);
							count++;
						}
					}

					if (count > 1) {
						intermeeting = (int) intermeeting / count;
						System.out.println(intermeeting);
					} else {
						intermeeting = 0;// PARAMETERS.minIntermeetingTime + rand.nextInt(PARAMETERS.maxIntermeetingTime
											// - PARAMETERS.minIntermeetingTime);
					}
					edges[i][j] = intermeeting;
					edges[j][i] = intermeeting;

					// System.out.println(i + ", " + j + "intermeets " + intermeeting);
				}
			}
		}
		return edges;
	}

	public int[][] setEdges_Data_distributed(int[][][] meetingTimes) {
		this.edges = new int[numNodes][numNodes];
		int high = (int) numNodes / 2;
		int low = numNodes - high;
		float dd = PARAMETERS.sparsity_internal;

		// high to high
		for (int i = 0; i < high; i++) {
			Random rand = new Random();
			for (int j = i + 1; j < high; j++) {
				if (i == j) {
					edges[i][j] = 0;
				} else {
					int intermeeting = 0;
					int count = 0;
					// System.out.println(meetingTimes[i][j]);
					for (int x = 0; x < meetingTimes[i][j].length - 1; x++) {
						Arrays.parallelSort(meetingTimes[i][j]);
						if (meetingTimes[i][j][x + 1] > 0) {

							intermeeting = intermeeting + Math.abs(meetingTimes[i][j][x + 1] - meetingTimes[i][j][x]);
							count++;
						}
					}

					if (count > 1) {
						intermeeting = (int) intermeeting / count;
					} else {
						intermeeting = 0;
					}
					edges[i][j] = intermeeting;
					edges[j][i] = intermeeting;
					// System.out.println(i + ", " + j + "intermeets " + intermeeting);
				}
			}
		}
		// high to low
		for (int i = 0; i < high; i++) {
			Random rand = new Random();
			for (int j = high; j < numNodes; j++) {
				if (i == j) {
					edges[i][j] = 0;
					edges[j][i] = 0;
				} else {
					int intermeeting = 0;
					int count = 0;
					// System.out.println(meetingTimes[i][j]);
					for (int x = 0; x < meetingTimes[i][j].length - 1; x++) {
						Arrays.parallelSort(meetingTimes[i][j]);
						if (meetingTimes[i][j][x + 1] > 0) {

							intermeeting = intermeeting + Math.abs(meetingTimes[i][j][x + 1] - meetingTimes[i][j][x]);
							count++;
						}
					}

					if (count > 1) {
						intermeeting = (int) intermeeting / count;
					} else {
						intermeeting = 0;
					}

					double value = rand.nextDouble();
					if (dd >= value) {
						edges[i][j] = intermeeting;
						edges[j][i] = intermeeting;
					} else {
						edges[i][j] = 0;
						edges[j][i] = 0;
					}

					// System.ou t.println(i + ", " + j + "intermeets " + intermeeting);
				}
			}
		}

		// low to low
		for (int i = high; i < numNodes; i++) {
			Random rand = new Random();
			for (int j = i + 1; j < numNodes; j++) {
				if (i == j) {
					edges[i][j] = 0;
					edges[j][i] = 0;
				} else {
					int intermeeting = 0;
					int count = 0;
					// System.out.println(meetingTimes[i][j]);
					for (int x = 0; x < meetingTimes[i][j].length - 1; x++) {
						Arrays.parallelSort(meetingTimes[i][j]);
						if (meetingTimes[i][j][x + 1] > 0) {

							intermeeting = intermeeting + Math.abs(meetingTimes[i][j][x + 1] - meetingTimes[i][j][x]);
							count++;
						}
					}

					if (count > 1) {
						intermeeting = (int) intermeeting / count;
					} else {
						intermeeting = 0;
					}

					double value = rand.nextDouble();

					edges[i][j] = intermeeting;
					edges[j][i] = intermeeting;

					// System.out.println(i + ", " + j + "intermeets " + intermeeting);
				}
			}
		}
		return edges;
	}

	public List<Integer>[][] getMinimumPaths(int[][] distance, int[][] hops) {
		@SuppressWarnings("unchecked")
		List<Integer>[][] minimumPaths = new ArrayList[numNodes][numNodes];

		/**
		 * Intitialize Graph and store the shortest path from each source to dest *.
		 */

		Graph G = new Graph(numNodes);

		for (int i = 0; i < numNodes; i++) {
			for (int j = 0; j < numNodes; j++) {
				if (edges[i][j] > 0) {
					G.addEdge(i, j);
					// G.addEdge(j, i);
				}
			}
		}

		// Finds all paths within threshold and return all paths
		SearchPath path = new SearchPath(numNodes, G, edges, threshold);

		// From each source to every destination
		for (int i = 0; i < numNodes; i++) {
			ArrayList<List<Integer>> allPaths = new ArrayList<List<Integer>>();

			for (int j = 0; j < numNodes; j++) {
				// ArrayList<ArrayList<Integer>> sourcePath = new ArrayList<Integer>();
				if (i != j) {
					allPaths.addAll(path.getAllPaths(i, j));
				}
				// System.out.println("Source Path size " + allPaths.size());

			}

			// populate distance and hop matrix
			int cost = 0;
			for (int dest = 0; dest < numNodes; dest++) {
				List<Integer> minimum = new ArrayList<Integer>();
				int minHop = 1000;

				for (int d = 0; d < allPaths.size(); d++) {

					if (allPaths.get(d).get(allPaths.get(d).size() - 1) == dest) {

						cost = 0;
						for (int m = 0; m < allPaths.get(d).size() - 1; m++) {
							cost = cost + edges[allPaths.get(d).get(m)][allPaths.get(d).get(m + 1)];
						}

						if (allPaths.get(d).size() == minHop) {
							if (cost < distance[i][dest] && cost > 0) {

								minHop = allPaths.get(d).size();
								minimum = allPaths.get(d);
								distance[i][dest] = cost;
								hops[i][dest] = minimum.size() - 1;
								minimumPaths[i][dest] = minimum;
								// System.out.println("Path Switched due to distance" + i + ", " + dest + "," +
								// cost + ","+ distance[i][dest] +"," + allPaths.get(d) + ", " +
								// minimum.size());
							}

						} else {
							if (allPaths.get(d).size() < minHop) {
								minHop = allPaths.get(d).size();
								minimum = allPaths.get(d);
								distance[i][dest] = cost;
								hops[i][dest] = minimum.size() - 1;
								minimumPaths[i][dest] = minimum;
							}
						}

					}

				}

			}

		}

		// Print final data

		/*
		 * System.out.println("?????????????????????????????????"); for(int
		 * k=0;k<numNodes;k++) { for(int k1=0; k1<numNodes;k1++) { System.out.println(k
		 * + " and " + k1 + " = " + distance[k][k1] + ", " + hops[k][k1]); } }
		 */

		return minimumPaths;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationMain main = new ApplicationMain();

		/*
		 * main.energies = main.setEnergies(); main.edges = main.setEdges();
		 */
		Utilities util = new Utilities(main.simTime, main.numNodes);

		ArrayList<Results> results_multihop_linearExact = new ArrayList<>();
		ArrayList<Results> results_optimalCloser = new ArrayList<>();
		ArrayList<Results> results_POA_multi = new ArrayList<>();
		ArrayList<Results> results_POA = new ArrayList<>();
		ArrayList<Results> results_sLE = new ArrayList<Results>();
		ArrayList<Results> results_sOC = new ArrayList<Results>();

		int count = 0;
		
	
		double totalhops_final = 0;

		// main.edges = main.setEdges_Data(meetingTimes);
		double[] deviation_single = new double[PARAMETERS.numSim];
		int cdev = 0;
		double[] deviation_multi = new double[PARAMETERS.numSim];
		int cdev1 = 0;
		double[] variation_single = new double[PARAMETERS.numSim];
		int cvar1 = 0;
		double[] loss_single = new double[PARAMETERS.numSim];
		int closs1 = 0;


		double usage_multi = 0;
		double usage_single = 0;
		double[] usageSingle = new double[main.numNodes];
		double[] usageMulti = new double[main.numNodes];
		double[] usageMulti_relay = new double[main.numNodes];

		for (int sim = 0; sim < PARAMETERS.numSim; sim++) {
			//main.energies = main.setEnergies_Distributed();
			 main.energies = main.setEnergies();
			// main.neighbours = main.setNeighbours();

				//uses cambridge dataset; 
				//make sure the path for the data set is updated.
			
			ProcessData data = new ProcessData();
			int[][][] meetingTimes = null;
			try {
				meetingTimes = data.run();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // util.setMeetingTimes(main.edges);

			System.out.println("RUNNING SIMULATION--------------" + sim);
			// Final hops and distance information
			//main.edges = main.setEdges_Data(meetingTimes);// ();
			main.edges = main.setEdges();
			// main.edges = main.setEdges_Data(meetingTimes);
			//main.edges = main.setEdges_Distributed();

			// main.edges = main.setEdges_Disconnected();//(meetingTimes);

			util.isNetworkConnected(main.edges);
			// main.edges = main.setEdges();
			meetingTimes = util.setMeetingTimes(main.edges);

			/**
			 * ***********SINGLE HOP IMPLEMENTATION*********************************
			 */

			System.out.println("------Single Hop------");

			//location to single hop lineaer program.
			String modelfilename = "C:\\Users\\dhunganaa\\opl\\NoSelfConsumption_LIfetime\\SingleHop_LinearProgram.mod";
			float[][] sent_single = new float[main.numNodes][main.numNodes];
			float[] finalEnergy_single = new float[main.numNodes];
			sent_single = main.model_SingleHop(modelfilename, finalEnergy_single, main.edges, util);

			// Calculate E_opt:
			float eOpt_Single = 0;
			for (int i = 0; i < finalEnergy_single.length; i++) {
				eOpt_Single = eOpt_Single + finalEnergy_single[i];

				if (PARAMETERS.debugMode) {
					System.out.println("Node " + i + " = " + finalEnergy_single[i] + " , ");
				}
			}

			eOpt_Single /= main.numNodes;
			deviation_single[cdev] = eOpt_Single;

			cdev++;
			System.out.println("EOPT for single hop " + eOpt_Single);

			if (PARAMETERS.debugMode) {
				System.out.println("Sent =  [ ");
				for (int a = 0; a < main.numNodes; a++) {
					System.out.println();
					for (int a1 = 0; a1 < main.numNodes; a1++) {
						System.out.print(sent_single[a][a1] + " ,");

					}

				}

			}

			
			//Calculate Node usage
			double[] nodeUsage = new double[main.numNodes];
			for(int i=0;i<main.numNodes;i++)
			{
				double sum = 0;
				for(int j = 0; j<main.numNodes && j!=i; j++)
				{
					sum = sum + sent_single[i][j] + sent_single[j][i];
				}
				nodeUsage[i] = sum;
			}
			
			Arrays.parallelSort(nodeUsage);
			
			for(int i=0;i<main.numNodes;i++)
			{
				usageSingle[i] = usageSingle[i] + nodeUsage[i]; 
			}
			// **************SINGLE HOP PROTOCOLS************//

			/**
			 * Clone Initial Matrix before calling protocols:
			 */

			float[] energies_cloned = main.energies.clone();
			float[][] sent_single_cloned = sent_single.clone();
			for (int i = 0; i < main.numNodes; i++) {

				sent_single_cloned[i] = sent_single[i].clone();
			}

			// System.out.println("Energy of node 23 and 10 " + main.energies[23] + ", " +
			// main.energies[10]);
			Results result1 = new Results(util);
			SingleHopProtocols protocols_singleHop = new SingleHopProtocols(energies_cloned, meetingTimes, result1,
					main.numNodes, main.beta, eOpt_Single);
			result1 = protocols_singleHop.protocol_SingleHopLinearExact(sent_single_cloned);
			results_sLE.add(result1);
			
			//check usage
			double usg = protocols_singleHop.avgUsage;
			usage_single = usage_single + usg;
			

			variation_single[cvar1] = result1.getvD().get(result1.getvD().size() - 1);
			cvar1++;
			loss_single[closs1] = result1.getTotalEnergy().get(0)
					- result1.getTotalEnergy().get(result1.getTotalEnergy().size() - 1);
			closs1++;
			energies_cloned = main.energies.clone();
			Results result2 = new Results(util);
			protocols_singleHop = new SingleHopProtocols(energies_cloned, meetingTimes, result2, main.numNodes,
					main.beta, eOpt_Single);
			result2 = protocols_singleHop.Protocol_OptimalCloser();
			results_sOC.add(result2);
			// double[] deviation_var_sOC =
			// main.calculateStandardDeviationTimeWise(results_sOC);

			energies_cloned = main.energies.clone();
			Results result3 = new Results(util);
			protocols_singleHop = new SingleHopProtocols(energies_cloned, meetingTimes, result3, main.numNodes,
					main.beta, eOpt_Single);
			result3 = protocols_singleHop.protocol_POA();
			results_POA.add(result3);
			// double[] deviation_var_POA =
			// main.calculateStandardDeviationTimeWise(results_POA);

			// System.exit(0);
			/**
			 * **********STARTING MULTIHOPS******************
			 */
			int[][] hops = new int[main.numNodes][main.numNodes];
			int[][] distance = new int[main.numNodes][main.numNodes];

			// Initialize distance Matrix
			for (int a = 0; a < main.numNodes; a++) {
				for (int b = 0; b < main.numNodes; b++) {
					distance[a][b] = 10000;
				}
			}

			// Get Minimum Paths and distance
			@SuppressWarnings("unchecked")
			List<Integer>[][] minimumPaths = new ArrayList[main.numNodes][main.numNodes];
			 minimumPaths = main.getMinimumPaths(distance, hops);

			System.out.println("Minimum Paths found");

			// Reset distance = 1000 to 0;required for linear program.
			// Initialize distance Matrix
			for (int a = 0; a < main.numNodes; a++) {
				for (int b = 0; b < main.numNodes; b++) {
					if (distance[a][b] > main.threshold)
						distance[a][b] = 0;
				}
			}

			/**
			 * After calculating hop and distance info: Provide it to ILP to calculate
			 * optimal values
			 */

			String modelFileName = "C:\\\\Users\\\\dhunganaa\\\\opl\\\\NoSelfConsumption_LIfetime\\\\MultiHopLinearProgram.mod";
			float[] finalEnergy = new float[main.numNodes];
			float[][] sent =new float[main.numNodes][main.numNodes];//main.model_MultiHop(modelFileName, distance,hops, util, finalEnergy);

			System.out.println("***************************************");

			double c1 = 0;
			double totalhops = 0;
			for (int x1 = 0; x1 < main.numNodes; x1++) {
				System.out.println();
				for (int y1 = 0; y1 < main.numNodes; y1++) {

					if (sent[x1][y1] > 0) {
						System.out.println("hops used between " + x1 + "," + y1 + "=" + hops[x1][y1]);
						//System.out.print(sent[x1][y1] + " ");
						totalhops = totalhops + hops[x1][y1];
						c1++;
					}
				}
			}
			totalhops_final += (totalhops / c1);

			totalhops = totalhops / c1;
			System.out.println();

			
		
			// Calculate Eopt
			float eOpt_multi = 0;
			for (int i = 0; i < main.numNodes; i++) {
				eOpt_multi = eOpt_multi + finalEnergy[i];
			}

			eOpt_multi /= main.numNodes;
			deviation_multi[cdev1] = eOpt_multi;
			cdev1++;
			System.out.println("Eopt for  multi hop " + eOpt_multi);

			if (eOpt_multi != eOpt_Single) {
				System.out.println("Values " + eOpt_multi + "," + eOpt_Single);// System.exit(0);
			}
			if (PARAMETERS.debugMode) {
				System.out.println("Sent =  [ ");
				for (int a = 0; a < main.numNodes; a++) {
					System.out.println();
					for (int a1 = 0; a1 < main.numNodes; a1++) {
						System.out.print(sent[a][a1] + " ,");
					}

				}
			}

			System.out.println();
			System.out.println("***************MULTI HOP ILP FINISHED ************************");

			/**
			 * ILP FINISHED
			 */
			// Now we have all distances, hops as well as minimum path selected for each
			// pair.

			System.out.println("--_STARTING PROTOCOLS MULTIHOP---------");
			// Calculate total nodes
			/*
			 * double sum = 0; double sum_final = 0; for (int i = 0; i < main.numNodes; i++)
			 * { sum = sum + main.energies[i]; sum_final = sum_final+ finalEnergy[i]; }
			 */

			float[][] sent_individual = new float[main.numNodes][main.numNodes];
			float[][] sent_self = new float[main.numNodes][main.numNodes];
			sent_individual = main.calculateSentPerNode(sent, sent_individual, minimumPaths);
			sent_self = main.calculateSelf(sent, sent_individual, minimumPaths);

			float[][] sent_others = main.calculateSent_Others(sent_self, sent_individual);
			
			
			//Calculate Node usage (Without Relay energy)
			double[] nodeUsage1 = new double[main.numNodes];
			for(int i=0;i<main.numNodes;i++)
			{
				double sum = 0;
				for(int j = 0; j<main.numNodes && j!=i; j++)
				{
					sum = sum + sent_self[i][j] + sent_self[j][i];
				}
				nodeUsage1[i] = sum;
			}
			
			Arrays.parallelSort(nodeUsage1);
			
			for(int i=0;i<main.numNodes;i++)
			{
				usageMulti[i] = usageMulti[i] + nodeUsage1[i]; 
			}
			
			//Calculate Node usage (including Relay energy)
			double[] nodeUsage2 = new double[main.numNodes];
			for(int i=0;i<main.numNodes;i++)
			{
				double sum = 0;
				for(int j = 0; j<main.numNodes && j!=i; j++)
				{
					sum = sum + sent_individual[i][j] + sent_individual[j][i];
				}
				nodeUsage2[i] = sum;
			}
			
			Arrays.parallelSort(nodeUsage2);
			
			for(int i=0;i<main.numNodes;i++)
			{
				usageMulti_relay[i] = usageMulti_relay[i] + nodeUsage2[i]; 
			}
			
			
			main.to_receive = main.calculateto_receive(sent_individual);

			if ((int) eOpt_multi != (int) eOpt_Single) {
				count++;
			}

			energies_cloned = main.energies.clone();
			float[][] sent_individual_cloned = sent_individual.clone();
			float[][] sent_self_cloned = sent_self.clone();
			float[][] sent_others_cloned = sent_others.clone();
			for (int k = 0; k < main.numNodes; k++) {
				sent_individual_cloned[k] = sent_individual[k].clone();
				sent_self_cloned[k] = sent_self[k].clone();
				sent_others_cloned[k] = sent_others[k].clone();
			}
			float[][] sent_cloned = sent.clone();
			for (int k = 0; k < main.numNodes; k++) {
				sent_cloned[k] = sent[k].clone();
			}
			float[] to_received_cloned = main.to_receive.clone();

			Results result4 = new Results(util);
			MultiHopProtocols protocols_multiHop = new MultiHopProtocols(energies_cloned, meetingTimes, result4,
					main.numNodes, main.beta, minimumPaths, sent_individual_cloned, sent_cloned, to_received_cloned,
					eOpt_multi);
			result4 = protocols_multiHop.protocol_LinearExact_Multihop(sent_self_cloned, sent_others_cloned);// (sent_single_cloned);
			results_multihop_linearExact.add(result4);
			
			//check node usage
			double usage = protocols_multiHop.avgUsage;
			usage_multi = usage_multi + usage;
			
			energies_cloned = main.energies.clone();
			Results result5 = new Results(util);
			protocols_singleHop = new SingleHopProtocols(energies_cloned, meetingTimes, result5, main.numNodes,
					main.beta, eOpt_multi);
			result5 = protocols_singleHop.Protocol_OptimalCloser();
			results_optimalCloser.add(result5);

			energies_cloned = main.energies.clone();
			Results result6 = new Results(util);
			protocols_singleHop = new SingleHopProtocols(energies_cloned, meetingTimes, result6, main.numNodes,
					main.beta, eOpt_multi);
			result6 = protocols_singleHop.protocol_POA();
			results_POA_multi.add(result6);

		}

		
		
		System.out.println("Usage for Single Hop LE " + usage_single);
		System.out.println("Usage for Multi Hop LE " + usage_multi);
		
		totalhops_final = totalhops_final / PARAMETERS.numSim;
		

		System.out.println("Average hop path used : " + totalhops_final);
		
		
		try
		{
		BufferedWriter w = new BufferedWriter(new FileWriter("vd.csv"));
		
		String ctnt = "";
		
		for(int i=0;i<PARAMETERS.numSim;i++)
		{
			ctnt = i + "," + main.getAverage(results_sLE.get(i).getvD());
			w.write(ctnt);
			w.newLine();
		}
		
		
	w.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		double[] deviation_var_PLE = main.calculateStandardDeviationTimeWiseVariation(results_sLE);
		double[] deviation_interaction_PLE = main.calculateStandardDeviationTimeWiseInteraction(results_sLE);
		double[] deviation_energy_PLE = main.calculateStandardDeviationTimeWiseEnergy(results_sLE);

		double[] deviation_var_sOC = main.calculateStandardDeviationTimeWiseVariation(results_sOC);
		double[] deviation_interaction_sOC = main.calculateStandardDeviationTimeWiseInteraction(results_sOC);
		double[] deviation_energy_sOC = main.calculateStandardDeviationTimeWiseEnergy(results_sOC);

		double[] deviation_var_POA = main.calculateStandardDeviationTimeWiseVariation(results_POA);
		double[] deviation_interaction_POA = main.calculateStandardDeviationTimeWiseInteraction(results_POA);
		double[] deviation_energy_POA = main.calculateStandardDeviationTimeWiseEnergy(results_POA);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("TimewiseDeviations.csv"));
		
		String content = "Time, LE_var, LE_int, LE_Energy, OC_var, OC_int, OC_energy, OA_var, OA_int, OA_energy";
		
			writer.write(content);
	
		
			writer.newLine();
		
		int time = 0;
		for (int i = 0; i < deviation_var_PLE.length; i++) {
			content =  time + "," + deviation_var_PLE[i] + "," + deviation_interaction_PLE[i] + ","
					+ deviation_energy_PLE[i] + "," + deviation_var_sOC[i] + "," + deviation_interaction_sOC[i] + ","
					+ deviation_energy_sOC[i] + "," + deviation_var_POA[i] + "," + deviation_interaction_POA[i] + ","
					+ deviation_energy_POA[i];
			writer.write(content);
			writer.newLine();
			time = time + 200;

		}
		writer.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		/**
		 * Generate Final Results
		 */

		Results finalResult_multihop_linearExact = new Results(util);
		finalResult_multihop_linearExact = main.calculateFinalResult(results_multihop_linearExact, util);

		Results finalResult_OptimalCloser = new Results(util);
		finalResult_OptimalCloser = main.calculateFinalResult(results_optimalCloser, util);

		Results finalResult_shop_LE = new Results(util);
		finalResult_shop_LE = main.calculateFinalResult(results_sLE, util);

		Results finalResult_shop_OC = new Results(util);
		finalResult_shop_OC = main.calculateFinalResult(results_sOC, util);

		Results finalResult_shop_POA = new Results(util);
		finalResult_shop_POA = main.calculateFinalResult(results_POA, util);

		Results finalResult_shop_POA_multi = new Results(util);
		finalResult_shop_POA_multi = main.calculateFinalResult(results_POA_multi, util);

		try {
			util.writeToFile(finalResult_multihop_linearExact, true, "MultiHop_LinearExact");
			util.writeToFile(finalResult_OptimalCloser, true, "OptimalCloser");
			util.writeToFile(finalResult_shop_LE, true, "SingleHop_LinearExact");
			util.writeToFile(finalResult_shop_OC, true, "SingleHop_OC");
			util.writeToFile(finalResult_shop_POA, true, "POA_single");
			util.writeToFile(finalResult_shop_POA_multi, true, "POA_multi");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // boolean flag to print data on console

		// s.prolongNetworkLifeTime_LinearExact_MultiHop();
		System.out.println("Total Diff " + count);

		String newContent = "Threshold, deviation_opt, deviation_variation, deviation_loss" + "\n";
		// Print individual EOPT VALUES
		System.out.println("Single Hop");
		double dev_opt = 0;
		double avg_opt = main.getAverage(deviation_single);
		System.out.println(avg_opt);
		for (int i = 0; i < PARAMETERS.numSim; i++) {
			dev_opt = dev_opt + Math.pow((deviation_single[i] - avg_opt), 2);
			// System.out.println(dev_opt);
		}

		dev_opt = Math.sqrt(dev_opt / PARAMETERS.numSim);

		double dev_var = 0;
		double avg_var = main.getAverage(variation_single);
		for (int i = 0; i < PARAMETERS.numSim; i++) {
			dev_var = dev_var + Math.pow((variation_single[i] - avg_var), 2);
			// System.out.println(deviation_single[i]);
		}

		dev_var = Math.sqrt(dev_var / PARAMETERS.numSim);

		double dev_loss = 0;
		double avg_loss = main.getAverage(loss_single);
		for (int i = 0; i < PARAMETERS.numSim; i++) {
			dev_loss = dev_loss + Math.pow((loss_single[i] - avg_loss), 2);
			// System.out.println(deviation_single[i]);
		}

		dev_loss = Math.sqrt(dev_loss / PARAMETERS.numSim);

		newContent = newContent + PARAMETERS.threshold + "," + dev_opt + "," + dev_var + "," + dev_loss;

		// write to file

		try {
			BufferedWriter write = new BufferedWriter(new FileWriter("deviation.csv"));
			BufferedWriter w = new BufferedWriter(new FileWriter("usage.csv"));
			String content = "Node, Single(Without Relay), Single(with Relay), Multi (Without Relay), Single(With Relay)";
			w.write(content);
			w.newLine();
			for(int i=0;i<main.numNodes;i++)
			{
				w.write(i + "," + (usageSingle[i]/PARAMETERS.numSim) + "," + (usageSingle[i]/PARAMETERS.numSim) + "," + (usageMulti[i]/PARAMETERS.numSim) + "," + (usageMulti_relay[i]/PARAMETERS.numSim));
				w.newLine();
			}
			write.write(newContent);
			write.newLine();
			write.close();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public double[] calculateStandardDeviationTimeWiseVariation(ArrayList<Results> variation) {
		// Variation Distance
		ArrayList<Double> variation_time = new ArrayList<Double>();
		double[] deviations = new double[PARAMETERS.simTime / 200];
		int c = 0;
		for (int t = 0; t < (PARAMETERS.simTime)/200; t = t + 1) {
			double sum = 0;
			double var_avg = getAverage_vd(variation, t);
			for (int a = 0; a < PARAMETERS.numSim; a++) {
				

				sum = sum + Math.pow((variation.get(a).getvD().get(t) - var_avg), 2);

			}

			sum = Math.sqrt(sum / PARAMETERS.numSim);
			deviations[c] = sum;
			c++;

		}

		return deviations;

	}

	public double[] calculateStandardDeviationTimeWiseInteraction(ArrayList<Results> interaction) {
		// Variation Distance
		// ArrayList<Double> variation_time = new ArrayList<Double>();
		double[] deviations = new double[PARAMETERS.simTime / 200];
		int c = 0;
		for (int t = 0; t < (PARAMETERS.simTime/200); t = t + 1) {
			double sum = 0;
			double var_avg = getAverage_inter(interaction, t);
			for (int a = 0; a < PARAMETERS.numSim; a++) {
				

				sum = sum + Math.pow((interaction.get(a).getNumInteractions().get(t) - var_avg), 2);

			}

			sum = Math.sqrt(sum / PARAMETERS.numSim);
			deviations[c] = sum;
			c++;

		}

		return deviations;

	}

	public double[] calculateStandardDeviationTimeWiseEnergy(ArrayList<Results> energy) {
		// Variation Distance
		ArrayList<Double> variation_time = new ArrayList<Double>();
		double[] deviations = new double[PARAMETERS.simTime / 200];
		int c = 0;
		for (int t = 0; t < (PARAMETERS.simTime/200); t = t + 1) {
			double sum = 0;
			double var_avg = getAverage_energy(energy, t);
			for (int a = 0; a < PARAMETERS.numSim; a++) {
			

				sum = sum + Math.pow((energy.get(a).getTotalEnergy().get(t) - var_avg), 2);

			}

			sum = Math.sqrt(sum / PARAMETERS.numSim);
			deviations[c] = sum;
			c++;

		}

		return deviations;

	}
	

	public double getAverage(ArrayList<Double> list) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i);
		}

		sum = sum / list.size();
		return sum;
	}

	public double getAverage(double[] list) {
		double sum = 0;
		for (int i = 0; i < list.length; i++) {
			sum = sum + list[i];
		}

		sum = sum / list.length;
		return sum;
	}

	public double getAverage_vd(ArrayList<Results> list, int time) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i).getvD().get(time);
		}

		sum = sum / list.size();
		return sum;
	}
	
	public double getAverage_inter(ArrayList<Results> list, int time) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i).getNumInteractions().get(time);
		}

		sum = sum / list.size();
		return sum;
	}
	
	public double getAverage_energy(ArrayList<Results> list, int time) {
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum = sum + list.get(i).getTotalEnergy().get(time);
		}

		sum = sum / list.size();
		return sum;
	}

	public float[] calculateto_receive(float[][] sent_individual) {
		float[] to_receive = new float[numNodes];

		for (int i = 0; i < numNodes; i++) {
			double sum = 0;

			for (int k = 0; k < numNodes; k++) {
				if (sent_individual[k][i] > 0) {
					sum = sum + (sent_individual[k][i] * 0.8);
				}

			}
			to_receive[i] = (float) sum;
			System.out.println("To Receive " + i + " is " + to_receive[i]);
		}

		return to_receive;
	}

	public float[][] calculateSelf(float[][] sent, float[][] sent_individual, List<Integer>[][] minimumPath) {

		float[][] sent_self = new float[numNodes][numNodes];
		// For each node i
		for (int i = 0; i < numNodes; i++) {

			// For each node j (dest)
			for (int k = 0; k < numNodes; k++) {
				List<Integer> selectedPath = minimumPath[i][k];
				// System.out.println("Selected Path is for " + i + " , " + k + "is " +
				// selectedPath);

				if (sent[i][k] > 0) {
					if (selectedPath.size() > 1) {/*
													 * if(i==1 && k==4) { System.out.println(sent[i][k]); }
													 */
						sent_self[selectedPath.get(0)][selectedPath
								.get(1)] = (float) sent_self[selectedPath.get(0)][selectedPath.get(1)] + sent[i][k];
						// System.out.println(selectedPath.get(0) + " , " + selectedPath.get(1) + ", " +
						// sent[i][k]);
					}

				}
			}

		}

		if (PARAMETERS.debugMode) {
			System.out.println("Final Energy to send per node_Self \n [");
			for (int a1 = 0; a1 < numNodes; a1++) {
				System.out.println();
				for (int a2 = 0; a2 < numNodes; a2++) {
					System.out.print(sent_self[a1][a2] + ",");
				}
			}
		}

		System.out.println("***************************************");
		return sent_self;
	}

	public float[][] calculateSent_Others(float[][] sent_self, float[][] sent_individual) {

		float[][] sent_others = new float[numNodes][numNodes];
		// For each node i
		for (int i = 0; i < numNodes; i++) {

			// For each node j (dest)
			for (int k = 0; k < numNodes; k++) {

				sent_others[i][k] = sent_individual[i][k] - sent_self[i][k];
			}
		}

		if (PARAMETERS.debugMode) {
			System.out.println("Final Energy to send per node_others \n [");
			for (int a1 = 0; a1 < numNodes; a1++) {
				System.out.println();
				for (int a2 = 0; a2 < numNodes; a2++) {
					System.out.print(sent_others[a1][a2] + ",");
				}
			}
		}
		System.out.println("***************************************");
		return sent_others;
	}

	public float[][] calculateSentPerNode(float[][] sent, float[][] sent_individual, List<Integer>[][] minimumPath) {
		for (int i = 0; i < numNodes; i++) {

			for (int k = 0; k < numNodes; k++) {
				List<Integer> selectedPath = minimumPath[i][k];

				// System.out.println("Selected Path is " + selectedPath);
				// System.out.println(i + ","+k + ", " + sent[i][k] + ", ");
				if (sent[i][k] > 0) {
					// if(i==0 && k==3)
					/*
					 * { System.out.println(selectedPath); }
					 */

					for (int j = 0; j < selectedPath.size() - 1; j++) {

						if (j == 0) {
							sent_individual[selectedPath.get(j)][selectedPath
									.get(j + 1)] = sent_individual[selectedPath.get(j)][selectedPath.get(j + 1)]
											+ sent[i][k];

						} else {

							sent_individual[selectedPath.get(j)][selectedPath.get(
									j + 1)] = (float) (sent_individual[selectedPath.get(j)][selectedPath.get(j + 1)]
											+ sent[i][k] * Math.pow(1 - beta, j));
						}
					}

				}

			}

			// sent_individual[i][j] = totalToSend;

		}

		System.out.println("***************************************");

		// System.out.println(sent_individual[1][4] + ", " + sent[1][4] + "," +
		// sent_individual[0][1]);
		if (PARAMETERS.debugMode) {
			System.out.println("Final Energy to send per node \n [");
			for (int a1 = 0; a1 < numNodes; a1++) {
				System.out.println();
				for (int a2 = 0; a2 < numNodes; a2++) {
					System.out.print(sent_individual[a1][a2] + ",");
				}
			}

			System.out.print("]");
			System.out.println();
		}
		System.out.println("***************************************");

		return sent_individual;
	}

	public float[][] model_SingleHop(String modelfilename, float[] finalEnergy, int[][] edges, Utilities util)// double[]
																												// finalEnergy)
	{
		File f = new File("edges.dat");
		f.delete();

		String fileName_energies = util.writeEnergies(energies, "energies.dat");
		String fileName_Meetings = util.writeEdges(edges, "edges.dat");
		List<String> dataFiles = new ArrayList<String>();
		dataFiles.add(fileName_energies);
		dataFiles.add(fileName_Meetings);

		RunCplex runcp = new RunCplex(this.numNodes, modelfilename, dataFiles);
		double value = runcp.run();
		float[][] sent = new float[numNodes][numNodes];

		System.out.println("Objective value is " + value);

		IloNumMap variables = runcp.getVariables("sent");
		for (int a = 0; a < this.numNodes; a++) {
			for (int b = 0; b < this.numNodes; b++) {
				float sentvalue = 0;
				try {
					sentvalue = (float) variables.getSub(a + 1).get(b + 1);
					sent[a][b] = sentvalue;
				} catch (IloException e) { //
					// TODO Auto-generated catch block e.printStackTrace(); } //
				}
			}
		}

		// set Final Energy Levels
		IloNumMap v1 = runcp.getVariables("enext");
		for (int a = 0; a < this.numNodes; a++) {
			float energy = 0;
			try {
				energy = (float) v1.get(a + 1);
				finalEnergy[a] = energy;
			} catch (IloException e) { // TODO
			}
		}

		runcp.close();

		return sent;

	}

	public float[][] model_MultiHop(String modelFileName, int[][] distances, int[][] hops, Utilities util,
			float[] finalEnergy) {

		// Write Energy, Hops, and Distance to file.

		File f = new File("distances.dat");
		f.delete();
		f = new File("hops.dat");
		f.delete();
		String fileName_energies = util.writeEnergies(energies, "energies.dat");
		String fileName_hops = util.writeHops(hops, "hops.dat");
		String fileName_Meetings = util.writeEdges(distances, "distances.dat");// "edges.dat";

		// C:\\Users\\dhunganaa\\opl\\NoSelfConsumption_LIfetime\\MultiHopLinearProgram.mod";
		String modelfilename = modelFileName;
		List<String> dataFiles = new ArrayList<String>();
		dataFiles.add(fileName_energies);
		dataFiles.add(fileName_hops);
		dataFiles.add(fileName_Meetings);

		float[][] sent = new float[numNodes][numNodes];
		// for (int sim = 0; sim < simTime; sim++) {

		// System.out.println("---Running Simulation--- " + sim);
		RunCplex runcp = new RunCplex(numNodes, modelfilename, dataFiles);
		double value = runcp.run();
		System.out.println("Objective value is " + value);
		IloNumMap variables = runcp.getVariables("sent");
		for (int a = 0; a < numNodes; a++) {
			for (int b = 0; b < numNodes; b++) {
				double sentvalue = 0;
				try {
					sentvalue = variables.getSub(a + 1).get(b + 1);
				} catch (IloException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("Sent is " + sentvalue);
				sent[a][b] = (float) sentvalue;
			}
		}

		IloNumMap v1 = runcp.getVariables("enext");
		for (int a = 0; a < numNodes; a++) {
			double energy = 0;
			try {
				energy = v1.get(a + 1);
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finalEnergy[a] = (float) energy;
			System.out.print(energy + ", ");
		}

		System.out.println();

		runcp.close();
		// cplex.end();
		// }

		return sent;
		// System.exit(status);
	}

	/**
	 * Final Result : Sum and take the average
	 * 
	 * @param results
	 * @return
	 */
	public Results calculateFinalResult(ArrayList<Results> results, Utilities util) {
		Results r = new Results(util);

		/**
		 * Initialize and Define
		 */
		ArrayList<Double> vD = new ArrayList<>();
		util.init_DArray(vD, results.get(0).vD.size());
		ArrayList<Double> energyPrediction = new ArrayList<>();
		util.init_DArray(energyPrediction, results.get(0).energyPrediction.size());
		ArrayList<Double> numNodesPrediction = new ArrayList<>();
		util.init_DArray(numNodesPrediction, results.get(0).numNodesPrediction.size());
		ArrayList<Double> totalEnergy = new ArrayList<>();
		util.init_DArray(totalEnergy, results.get(0).totalEnergy.size());
		ArrayList<Double> numInteractions = new ArrayList<>();
		util.init_DArray(numInteractions, results.get(0).numInteractions.size());
		ArrayList<Double> numNodesPlus = new ArrayList<>();
		util.init_DArray(numNodesPlus, results.get(0).numNodesPlus.size());
		ArrayList<Double> numNodesminus = new ArrayList<>();
		util.init_DArray(numNodesminus, results.get(0).numNodesminus.size());
		ArrayList<Double> lifetime = new ArrayList<>();
		util.init_DArray(lifetime, results.get(0).lifeTime.size());

		/**
		 * Add each result
		 */

		for (int i = 0; i < results.size(); i++) {

			for (int c = 0; c < results.get(i).vD.size(); c++) {

				vD.set(c, (results.get(i).vD.get(c) + vD.get(c)));

			}

			for (int b = 0; b < results.get(i).energyPrediction.size(); b++) {
				energyPrediction.set(b, (results.get(i).energyPrediction.get(b) + energyPrediction.get(b)));
			}

			for (int c = 0; c < results.get(i).numNodesPrediction.size(); c++) {
				numNodesPrediction.set(c, (results.get(i).numNodesPrediction.get(c) + numNodesPrediction.get(c)));
			}

			for (int c = 0; c < results.get(i).totalEnergy.size(); c++) {
				totalEnergy.set(c, (results.get(i).totalEnergy.get(c) + totalEnergy.get(c)));
			}

			for (int c = 0; c < results.get(i).numInteractions.size(); c++) {
				numInteractions.set(c, (results.get(i).numInteractions.get(c) + numInteractions.get(c)));
			}

			for (int c = 0; c < results.get(i).numNodesPlus.size(); c++) {
				numNodesPlus.set(c, (results.get(i).numNodesPlus.get(c) + numNodesPlus.get(c)));
			}

			for (int c = 0; c < results.get(i).numNodesminus.size(); c++) {
				numNodesminus.set(c, (results.get(i).numNodesminus.get(c) + numNodesminus.get(c)));
			}

			for (int c = 0; c < results.get(i).lifeTime.size(); c++) {
				lifetime.set(c, (results.get(i).lifeTime.get(c) + lifetime.get(c)));
			}

		}

		// Create Result Object

		r.vD = vD;

		// System.out.println(r.vD.get(0));
		r.energyPrediction = energyPrediction;
		r.numNodesPrediction = numNodesPrediction;
		r.totalEnergy = totalEnergy;
		r.numInteractions = numInteractions;
		r.numNodesPlus = numNodesPlus;
		r.numNodesminus = numNodesminus;
		r.lifeTime = lifetime;

		return r;
	}

}