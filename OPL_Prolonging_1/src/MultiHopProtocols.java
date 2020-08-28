import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class MultiHopProtocols {

	float[] energy = null;
	int[][][] meetingInfo = null;
	int N = 0;
	int timeOfSimulation = PARAMETERS.simTime;
	int numRecords = PARAMETERS.recordTime;
	Results rs = null;
	Utilities util = null;
	float beta = PARAMETERS.beta;
	float[][] sent_individual;
	float[][] sent_ILP;
	float[][] others_energy;
	float[] to_receive;
	float eOpt = 0;
	double K = 0;
	public float target = 0;
	List<Integer>[][] paths;
	double[] usage = null;
	double avgUsage = 0;
	
	

	public MultiHopProtocols(float[] energies, int[][][] meetingInfo,  Results result, int n, float beta,
			List<Integer>[][] paths, float[][] sent_individual, float[][] sent_ILP, float[] to_receive, float eOpt) {
		this.energy = energies;
		this.meetingInfo = meetingInfo;
	
		this.rs = result;
		this.N = n;
		this.eOpt = eOpt;
		util = new Utilities(this.timeOfSimulation, this.N);
		util.meetingInfo = meetingInfo;
		this.paths = paths;
		this.sent_individual = sent_individual;
		this.sent_ILP = sent_ILP;
		this.others_energy = new float[N][N];
		this.to_receive = to_receive;
		this.usage = new double[N];

		
	}


	public void setTarget(float target) {
		this.target = target;
	}
	

	public Results protocol_LinearExact_Multihop(float[][] sent_self, float[][] sent_others) {

		// (Mij);
		System.out.println("---STARTING Linear Exact MultiHop -----------" + this.eOpt);

		// Set Network Info Here
		
		
		double sum = 0;
		int currentTime = 0;
		int numInteractions = 0;
	
		

		int select = 0;
		float[] remaining_energy = new float[N]; //Remainining received energy:
		while (true) {

			currentTime++;
			sum = 0;

			double totalenergy = 0;
			int numNodesPlus = 0;
			int numNodesMinus = 0;
			double target = this.eOpt;
			//System.out.println("Target is " + this.eOpt);

			/**
			 * set target as continuous mean.
			 */
			double currentMean = 0.0;
			for (int a = 0; a < N; a++) {
				currentMean = currentMean + energy[a];
			}

			double sumPrediction = 0;
			double lifetime= 1000;
			for (int i = 0; i < N ; i++) {

				sum = sum + Math.abs(energy[i] / currentMean - target / currentMean);
				totalenergy = totalenergy + energy[i];

				if (energy[i] > target) {
					numNodesPlus++;
				} else if (energy[i] < target) {
					numNodesMinus++;
				}
				
				if(energy[i] < lifetime)
				{
					lifetime = energy[i];
				}

			}
			sum = sum /(double)N;

			if ((currentTime - 1) % numRecords == 0) {
				//System.out.println("Variation Distance is " + sum + "," + currentTime );
				rs.setTotalEnergy(totalenergy);
				rs.setvD(sum * (N ));
				rs.setNumInteractions(numInteractions);
				rs.setNumNodesPlus(numNodesPlus);
				rs.setNumNodesminus(numNodesMinus);
				rs.setEnergyPrediction(eOpt);
				rs.setLifeTime(lifetime);

			}
			
		

			String pairs = util.getInteractingPairs(currentTime, 1, meetingInfo);
			
		
			 ArrayList<String> pairs_temp = new ArrayList<String>();
			 pairs_temp.add("0,1");
			 pairs_temp.add("1,4"); pairs_temp.add("1,3"); pairs_temp.add("2,4");
			 pairs_temp.add("5,6"); pairs_temp.add("0,1"); pairs_temp.add("1,4");pairs_temp.add("4,5");
			 

			// if(select > 7) { break; } pairs = pairs_temp.get(select);
			 
			// pairs = pairs_temp.get(select);
			
			if (pairs.compareTo(0 + "," + 0) != 0) {
			select++;
				int i = Integer.parseInt(pairs.split(",")[0]);
				int j = Integer.parseInt(pairs.split(",")[1]);
				
				//If node i has to give energy to j (either for j or through j)
				if(sent_individual[i][j] > 0 )
				{
				//System.out.println("Interacting " + i + " - >  " + j);
				
				
					
							float togiveSelf = sent_self[i][j];//Math.max(0, ]);
						//	System.out.println("Self : " + togiveSelf);
						
							
							//double sent = togiveSelf + togiveOthers;
							
						//	sent = Math.min(sent_individual[i][j], sent);
							
							//Give self
							float sent = togiveSelf;
							if(sent > 0)
							{
						
							if(energy[i] -sent < 0)
							{
								sent = energy[i];
							}
							
							if(energy[j] + (sent * (1-beta)) > 100)
							{
								sent = (100 - energy[j])/(1-beta);
							}
							
							energy[i] = energy[i]-sent;
							energy[j] = energy[j] + (sent * (1-beta));
							
							
							to_receive[j] = to_receive[j] - (sent*(1-beta));
							remaining_energy[j] = remaining_energy[j] + (sent * (1-beta));
							
							sent_self[i][j] = sent_self[i][j] - sent;
							//System.out.println("Sent is " + sent);
							if(sent > 0)
							{
								numInteractions++;
								
								usage[i]=usage[i] + sent;
								usage[j] = usage[j] + (sent * (1-beta));
								
							}
							
							}
							
							
							//Send for others
							float togiveOthers = Math.min(remaining_energy[i], sent_others[i][j]); 	
							
							
							//if(togiveOthers)
							//System.out.println("To give others " + togiveOthers);
							sent = togiveOthers;
							if(sent > 0)
							{
								if(energy[j] + (sent * (1-beta)) > 100)
								{
									sent = (100 - energy[j])/(1-beta);
								}
								
								if(energy[i] - sent < 0)
								{
									sent = energy[i];
								}
								
								//Here sent cant be more than sent_individual as well.
								if(sent > sent_individual[i][j])
								{
									System.err.println("Invalid");
									System.exit(0);
								}
								
								energy[i] = energy[i]-sent;
								energy[j] = energy[j] + (sent * (1-beta));
								remaining_energy[i] = remaining_energy[i] - sent;
								
								to_receive[j] = to_receive[j] - (sent*(1-beta)); 
								remaining_energy[j] = remaining_energy[j] + (sent * (1-beta));
								
								sent_others[i][j] = sent_others[i][j] - sent;
								//System.out.println("Sent is " + sent);
								if(sent > 0)              
								{                         //sent_individual[i][j] = sent_individual[i][j]-sent;
									numInteractions++;    
									usage[i] = usage[i] + sent;
									usage[j] = usage[j] + (sent * (1-beta));
                                }                         
								                          
								
							}
				
			
							//System.out.println(i + "->" + j + " = " + sent + " at" + currentTime);
				/*
				 * System.out.println("Final Energy" + i + "&" + j + " =" + energy[i] + ", " +
				 * energy[j]); System.out.println();
				 */
			
							if(energy[i] > 100 || energy[j] > 100)
							{
								System.out.println("Excess energy " + energy[i] + "," + energy[j]);
								System.exit(0);
							}
							
							
							
						
							
							
											}
				
				
				
				
				
				if(sent_individual[j][i] > 0)
				{
					int temp = i;
					i = j;
					j = temp;
					

					float togiveSelf = sent_self[i][j];//Math.max(0, ]);
				//	System.out.println("Self : " + togiveSelf);
				
					
					//double sent = togiveSelf + togiveOthers;
					
				//	sent = Math.min(sent_individual[i][j], sent);
					
					//Give self
					float sent = togiveSelf;
					if(sent > 0)
					{
				
					if(energy[i] -sent < 0)
					{
						sent = energy[i];
					}
					
					if(energy[j] + (sent * (1-beta)) > 100)
					{
						sent = (100 - energy[j])/(1-beta);
					}
					
					energy[i] = energy[i]-sent;
					energy[j] = energy[j] + (sent * (1-beta));
					
					
					to_receive[j] = to_receive[j] - (sent*(1-beta));
					remaining_energy[j] = remaining_energy[j] + (sent * (1-beta));
					
					sent_self[i][j] = sent_self[i][j] - sent;
					//System.out.println("Sent is " + sent);
					if(sent > 0)
					{
						numInteractions++;
						usage[i] = usage[i] + sent;
						usage[j] = usage[j] + (sent * (1-beta));
					}
					
					}
					
					
					//Send for others
					float togiveOthers = Math.min(remaining_energy[i], sent_others[i][j]); 	
					
					
					//if(togiveOthers)
					//System.out.println("To give others " + togiveOthers);
					sent = togiveOthers;
					if(sent > 0)
					{
						if(energy[j] + (sent * (1-beta)) > 100)
						{
							sent = (100 - energy[j])/(1-beta);
						}
						
						if(energy[i] - sent < 0)
						{
							sent = energy[i];
						}
						
						//Here sent cant be more than sent_individual as well.
						if(sent > sent_individual[i][j])
						{
							System.err.println("Invalid");
							System.exit(0);
						}
						
						energy[i] = energy[i]-sent;
						energy[j] = energy[j] + (sent * (1-beta));
						remaining_energy[i] = remaining_energy[i] - sent;
						
						to_receive[j] = to_receive[j] - (sent*(1-beta)); 
						remaining_energy[j] = remaining_energy[j] + (sent * (1-beta));
						
						sent_others[i][j] = sent_others[i][j] - sent;
						//System.out.println("Sent is " + sent);
						if(sent > 0)              
						{                         //sent_individual[i][j] = sent_individual[i][j]-sent;
							numInteractions++;  
							usage[i] = usage[i] - sent;
							usage[j] = usage[j] + (sent * (1-beta));
                        }                         
						                          
						
					}
		
	
					//System.out.println(i + "->" + j + " = " + sent + " at" + currentTime);
		/*
		 * System.out.println("Final Energy" + i + "&" + j + " =" + energy[i] + ", " +
		 * energy[j]); System.out.println();
		 */
	
					if(energy[i] > 100 || energy[j] > 100)
					{
						System.out.println("Excess energy " + energy[i] + "," + energy[j]);
						System.exit(0);
					}
					
					
					
				
	
					
					}
				}
			
		
			
			if (currentTime > (timeOfSimulation)) {

				break;
			}
		}
		
		
		//compute total usage
		double s = 0;
		int c = 0;
		for(int i=0; i<N; i++)
		{
			if(usage[i] > 0)
			{
			s = s + usage[i];
			c++;
			}
		}
		s= s/N;
		this.avgUsage = s;
		return rs;

	}
	
	public Results Protocol_OptimalCloser() {
		
		/*
		 * System.out.println("New Energy Levels " ); for (int i = 0; i < N; i++) {
		 * System.out.print(energyLevels[i] + " , " ); }
		 */
		
		System.out.println("---------Running Optimal Closer----------" + eOpt);
		int currentTime = 0;
		float[] energy = this.energy;
		int numInteractions = 0;
		int numNodesPlus = 0;
		int numNodesMinus = 0;
		double[] life = new double[(PARAMETERS.simTime / PARAMETERS.recordTime) + 1];
		int index = 0;
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
			for (int i = 0; i < N ; i++) {

				sum = sum + Math.abs(energy[i] / currentMean - target / currentMean);
				totalenergy = totalenergy + energy[i];

				
				if(energy[i] < lifetime)
				{
					lifetime = energy[i];
				}

			}
			sum = sum / (N );

			if ((currentTime - 1) % numRecords == 0) {

				rs.setTotalEnergy(totalenergy);
				rs.setvD(sum * (N ));
				rs.setNumInteractions(numInteractions);
				rs.setNumNodesPlus(numNodesPlus);
				rs.setNumNodesminus(numNodesMinus);
				
				rs.setEnergyPrediction(sumPrediction / ((double) N ));
				rs.setLifeTime(lifetime);

			}
			String pairs = util.getInteractingPairs(currentTime, 1, meetingInfo);
			int i = Integer.parseInt(pairs.split(",")[0]);
			int j = Integer.parseInt(pairs.split(",")[1]);
		
			//System.out.println("Interacting " + i + "," + energy[i] + ", " + j + "," + energy[j]);
			if (i != j) {
				// See if nodes interact
			

				//if (mijs[i][j] > randomvalue)
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
						float diff1 = Math.abs(energy[i] - target);
						float diff2 = Math.abs(energy[j] - target);

						if (diff2 * (1 - beta) > diff1) {
							energy[j] = energy[j] - (diff1 / (1 - beta));
							energy[i] = target;
							 numInteractions++;
						} else {
							energy[i] = energy[i] + (1 - beta) * diff2;
							energy[j] = target;
							 numInteractions++;
						}

					}
				}
			}
		

			
//System.out.println(currentTime);
			if (currentTime > PARAMETERS.simTime) {

				break;
			}
		}
		return rs;
		}
	

	
}
