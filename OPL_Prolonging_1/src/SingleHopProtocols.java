
import java.util.ArrayList;
public class SingleHopProtocols {

	float[] energy = null;
	int[][][] meetingInfo = null;
	//double totalEnergy = 0;
	int N = 0;
	int timeOfSimulation = PARAMETERS.simTime;
	int numRecords = PARAMETERS.recordTime;
	Results rs = null;
	Utilities util = null;
	float beta = PARAMETERS.beta;
	float eOpt = 0;
	public float target = 0;
	double[] usage = null;
	double avgUsage = 0;
	

	public SingleHopProtocols(float[] energies, int[][][] meetingInfo, Results result, int n,
			float beta, float eOpt) {

		System.out.println("----Single Hop Protocol Initialized---");
		this.energy = energies;
		this.meetingInfo = meetingInfo;
	//	this.totalEnergy = totalenergy2;
		this.rs = result;
		this.N = n;
		this.eOpt = eOpt;
		util = new Utilities(this.timeOfSimulation, this.N);
		util.meetingInfo = meetingInfo;
		this.target = eOpt;
		this.usage = new double[N];

	}

	public void setTarget(float target) {
		this.target = target;
	}

	public Results Protocol_OptimalCloser() {

		System.out.println("---------Running Optimal Closer----------" + eOpt);
		int currentTime = 0;
		float[] energy = this.energy;
		int numInteractions = 0;
		int numNodesPlus = 0;
		int numNodesMinus = 0;

		this.target = this.eOpt;
		while (true) {
			currentTime++;

			double lifetime = 100;

			/**
			 * set target as continuous mean.
			 */
			double currentMean = 0.0;
			for (int a = 0; a < N; a++) {
				currentMean = currentMean + energy[a];
			}

			double sumPrediction = 0;
			double sum = 0;
			double totalenergy = 0;
			for (int i = 0; i < N; i++) {

				sum = sum + Math.abs(energy[i] / currentMean - target / currentMean);
				totalenergy = totalenergy + energy[i];

				if (energy[i] < lifetime) {
					lifetime = energy[i];
				}

			}
			sum = sum / (N);

			if ((currentTime - 1) % numRecords == 0) {

				rs.setTotalEnergy(totalenergy);
				rs.setvD(sum * (N));
				rs.setNumInteractions(numInteractions);
				rs.setNumNodesPlus(numNodesPlus);
				rs.setNumNodesminus(numNodesMinus);

				rs.setEnergyPrediction(eOpt);
				rs.setLifeTime(lifetime);

			}
			String pairs = util.getInteractingPairs(currentTime, 1, meetingInfo);
			int i = Integer.parseInt(pairs.split(",")[0]);
			int j = Integer.parseInt(pairs.split(",")[1]);

			// System.out.println("Interacting " + i + "," + energy[i] + ", " + j + "," +
			// energy[j]);
			if (i != j) {
				// See if nodes interact

				// if (mijs[i][j] > randomvalue)
				{
					// System.out.println(i + " and " + j + " meets at " + currentTime);
					// Protocol
					if (energy[i] > target && energy[j] < target) {
						float diff1 = Math.abs(energy[i] - target);
						float diff2 = Math.abs(energy[j] - target);

						if (diff1 * (1 - beta) > diff2) {
							energy[i] = energy[i] - (diff2 / (1 - beta));
							energy[j] = target;
							numInteractions++;
						} else {
							energy[j] = energy[j] + (1 - beta) * diff1;
							energy[i] = target;
							numInteractions++;
						}

					}

					else if (energy[j] > target && energy[i] < target) {
						double diff1 = Math.abs(energy[i] - target);
						double diff2 = Math.abs(energy[j] - target);

						if (diff2 * (1 - beta) > diff1) {
							energy[j] = (float) (energy[j] - (diff1 / (1 - beta)));
							energy[i] = target;
							numInteractions++;
						} else {
							energy[i] = (float) (energy[i] + (1 - beta) * diff2);
							energy[j] = target;
							numInteractions++;
						}

					}
				}
			}

			// System.out.println(currentTime);
			if (currentTime > PARAMETERS.simTime) {

				break;
			}
		}
		return rs;
	}

	public Results protocol_POA() {

		System.out.println("-----------RUNNING POA  ALGORITHM--------------");

		double avg[] = new double[N];
		double sum = 0;
		int currentTime = 0;
		int numInteractions = 0;
		double totalenergyex = 0;
		double lifetime = 0;
		while (true) {

			currentTime++;
			sum = 0;

			double totalenergy = 0;
			int numNodesPlus = 0;
			int numNodesMinus = 0;
			double target = 0;
			lifetime = 1000;
			
			/**
			 * set target as continuous mean.
			 */
			double currentMean = 0.0;
			for (int a = 0; a < N; a++) {
				currentMean = currentMean + energy[a];
			}

			target = currentMean / (N);
			double sumPrediction = 0;
			for (int i = 0; i < N; i++) {

				sum = sum + Math.abs(energy[i] / currentMean - target / currentMean);
				totalenergy = totalenergy + energy[i];

				if (energy[i] > target) {
					numNodesPlus++;
				} else if (energy[i] < target) {
					numNodesMinus++;
				}

				if (energy[i] < lifetime) {
					lifetime = energy[i];
				}

			}
			sum = sum / (N);

			if ((currentTime - 1) % numRecords == 0) {

				rs.setTotalEnergy(totalenergy);
				rs.setvD(sum * (N));
				rs.setNumInteractions(numInteractions);
				rs.setNumNodesPlus(numNodesPlus);
				rs.setNumNodesminus(numNodesMinus);
				rs.setEnergyPrediction(eOpt);
				rs.setLifeTime(lifetime);

			}

			String pairs = util.getInteractingPairs(currentTime, 1, meetingInfo);

			int first = Integer.parseInt(pairs.split(",")[0]);
			int second = Integer.parseInt(pairs.split(",")[1]);

			/*
			 * avg[first] = ((avg[first] * num[first] + energy[second]) / (num[first] + 1));
			 * avg[second] = ((avg[second] * num[second] + energy[first]) / (num[second] +
			 * 1));
			 * 
			 * num[first] = num[first] + 1; num[second] = num[second] + 1;
			 */

			/**
			 * Add Global Knowledge
			 */
			
			
			/*
			 * if(numInteractions>200) { System.out.println("Num of interactions " +
			 * numInteractions); System.exit(0); }
			 */
			 

			avg[first] = target;
			avg[second] = target;

			float x = (energy[first]);
			float y = (energy[second]);
			
			if(numInteractions <100)
			{

			if ((x > avg[first] && y <= avg[second]) || (x <= avg[first] && y > avg[second])) {

				if (x > y) {

					energy[first] = (x + y) / 2; // energyToGive;
					energy[second] = (y + x) / 2 - (beta * (x - y) / 2);
					if(Math.abs((x-y)/2) > 0)
					{
					numInteractions++;
					}
					totalenergyex = totalenergyex + (Math.abs(x - y / 2));

				} else if (y >= x ) {

					energy[second] = (x + y) / 2;// energyToGive;
					energy[first] = ((y + x) / 2) - (beta * (y - x) / 2);

					if(Math.abs((x-y)/2) > 0)
					{
					numInteractions++;
					}

				}
			}

			}

			if (currentTime > (timeOfSimulation)) {

				break;
			}
		}

		/*
		 * // Store Energy Prediction values for (int i = 0; i < (N + 1); i++) {
		 * rs.setEnergyPrediction(avg[i]); rs.setNumNodesPrediction(num[i]); }
		 */
		System.out.println("--------COMPLETE------------");
		// System.out.println("Size is " + rs.getvD().size());

		return rs;

	}

	public Results protocol_SingleHopLinearExact(float[][] sent) {

		// (Mij);
		System.out.println("---STARTING Single Hop Linear Exact -----------" + this.eOpt);
		// Set Network Info Here
		double sum = 0;
		int currentTime = 0;
		int numInteractions = 0;

		
		while (true) {

			currentTime++;
			sum = 0;

			double totalenergy = 0;
			int numNodesPlus = 0;
			int numNodesMinus = 0;
			double target = this.eOpt;
			// System.out.println("Target is " + this.eOpt);

			/**
			 * set target as continuous mean.
			 */
			double currentMean = 0.0;
			for (int a = 0; a < N; a++) {
				currentMean = currentMean + energy[a];
			}

			double sumPrediction = 0;
			double lifetime = 1000;
			for (int i = 0; i < N; i++) {

				sum = sum + Math.abs(energy[i] / currentMean - target / currentMean);
				totalenergy = totalenergy + energy[i];

				if (energy[i] > target) {
					numNodesPlus++;
				} else if (energy[i] < target) {
					numNodesMinus++;
				}

				if (energy[i] < lifetime) {
					lifetime = energy[i];
				}

			}
			sum = sum/(double)N;
		

			if ((currentTime - 1) % numRecords == 0) {
				//System.out.println("Variation Distance is " + sum+ "," + currentTime  );
				rs.setTotalEnergy(totalenergy);
				rs.setvD(sum * (N));
				rs.setNumInteractions(numInteractions);
				rs.setNumNodesPlus(numNodesPlus);
				rs.setNumNodesminus(numNodesMinus);
				rs.setEnergyPrediction(eOpt);
				rs.setLifeTime(lifetime);

			}

			String pairs = util.getInteractingPairs(currentTime, 1, meetingInfo);

			//ArrayList<String> pairs_temp = new ArrayList<String>();
			/*
			 * pairs_temp.add("0,1"); pairs_temp.add("1,4"); pairs_temp.add("1,3");
			 * pairs_temp.add("2,4"); pairs_temp.add("5,6"); pairs_temp.add("0,1");
			 * pairs_temp.add("1,4"); pairs_temp.add("4,5");
			 */
			// if(select > 7) { break; } pairs = pairs_temp.get(select);

			// pairs = pairs_temp.get(select);

			if (pairs.compareTo(0 + "," + 0) != 0) {
			
				int i = Integer.parseInt(pairs.split(",")[0]);
				int j = Integer.parseInt(pairs.split(",")[1]);

				// If meets, send sent_self>0 && if(p(node j meeting with any other nodes) >
				// node i) for each j. Give energy as hop.
				// Next protocol could be only with EOPT

				// If node i has to give energy to j (either for j or through j)
				if (sent[i][j] > 0 || sent[j][i] > 0) {
					if (sent[j][i] > 0) {
						int temp = i;
						i = j;
						j = temp;

					}
				} else {
					continue;
				}

			
				/*
				 * System.out.println("Interacting " + i + " - > " + j + " sent = " + sent[i][j]
				 * + "," + energy[i] + ", " + energy[j]);
				 */
				
				float sent_i = sent[i][j];
				
				
				if(energy[i]- sent_i >= 0 && energy[j] + (sent_i * (1-beta)) <=100)
				{
					energy[i] = energy[i] - sent_i;
					energy[j] = energy[j] + (sent_i * (1 - beta));
					if (sent_i > 0) {
						numInteractions++;
						sent[i][j] = sent[i][j] - sent_i;
						usage[i] = usage[i] + sent_i;
						usage[j] = usage[j] + (sent_i * (1-beta));
					}

				}
				
				//System.out.println("Final Energy " + energy[i] + "," + energy[j]);
				
				if(energy[j] >100)
				{
					System.out.println(energy[i] + ", " + energy[j] + "," + sent[i][j] + ", " + sent[j][i]);
					System.exit(0);
				}
				
				

			}

			if (currentTime > (timeOfSimulation)) {

				break;
			}
		}

		System.out.println("Final Energy levels");
		for (int i = 0; i < N; i++) {
			System.out.print(energy[i] + ",");
		}
		System.out.println();
		
		
		//Compute final usage
		int count = 0;
		for(int i=0; i<N;i++)
		{
			if(usage[i] > 0)
			{
			avgUsage = avgUsage + usage[i];
			count++;
			}
		}
		
		this.avgUsage = avgUsage/N;
		

		return rs;

	}

}
