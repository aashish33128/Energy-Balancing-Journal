
public class OptimalStoppingTheory {
	int nodeCount = 0;
	double[][][][] P_with_K;
	double[][] Mij;
	int[][] hop;
	
	
	public OptimalStoppingTheory(int nodeCount, int[][] Mij, int[][] hop)
	{
		this.nodeCount = nodeCount;
		this.Mij = new double[nodeCount][nodeCount];
		for(int i=0;i<this.nodeCount;i++)
		{
			for(int j=0;j<this.nodeCount;j++)
			{
				if(Mij[i][j]!=0)
				{
				this.Mij[i][j] = 1.0/Mij[i][j];
				}
				else
				{
					this.Mij[i][j] = 0.0;
				}
			}
		}
		
		this.hop = hop;
		
	}
	
	public void backwardInductionNoSharing(int time, int source, int dest){
		
		// create matrix
				P_with_K=new double[nodeCount][nodeCount][hop[source][dest]+1][time+1];
				
				for(int t=1;t<=time;t++){
					for(int i=0;i<nodeCount;i++){ 
						for(int d=0;d<nodeCount;d++){
							if(i!=d){
								P_with_K[i][d][0][t] = (1-Math.pow(1-Mij[i][d], t)); // all zero remaining hop
							}
						}
					}
				}
				
				
				double [][] Mij_temp=new double[nodeCount][nodeCount];
				for(int i=0;i<nodeCount;i++){
					for(int j=i+1;j<nodeCount;j++){
						
						double rr = Math.random();
						/*if(rr<partialRatio){
							Mij_temp[i][j]=Mij[i][j];
							Mij_temp[j][i]=Mij[j][i];
						}
						else{
							// keep 0
						}*/
					}
				}
				
				// make sure it is not changed
				Mij_temp[source][dest] = Mij[source][dest];
				
				
				for(int t=1;t<=time;t++){
					for(int h=1;h<=hop[source][dest];h++){	
						for(int i=0;i<nodeCount;i++){ 
							for(int d=0;d<nodeCount;d++){
								if(i!=d){
									double [] Pj_best=new double[nodeCount]; 
									
									P_with_K[i][d][h][t] = Mij_temp[i][d]; // first process destination
									double remainingProb=1-Mij_temp[i][d];
									
									for(int j=0;j<nodeCount;j++){ 
										Pj_best[j] = P_with_K[j][d][h-1][(t-1)]; // best strategy for delivery through your neighbor 
										
										if(i!=j && d!=j && Mij_temp[i][j]!=0 && (P_with_K[i][d][h][t-1] < Pj_best[j])){ // only those who met (except j, if any)
										
											P_with_K[i][d][h][t] += remainingProb*Mij_temp[i][j]*Pj_best[j];
											remainingProb -= remainingProb*Mij_temp[i][j];
										}
									}
									
									P_with_K[i][d][h][t] += remainingProb*P_with_K[i][d][h][t-1];
									
								}
							}
						}
					}
				}
				
				System.out.println("Theoretical results - No sharing with HOP "+hop);
				for(int i=0;i<=time;i=i+10){
					System.out.println(P_with_K[source][dest][hop[source][dest]][i]); 
				}
	}

}
