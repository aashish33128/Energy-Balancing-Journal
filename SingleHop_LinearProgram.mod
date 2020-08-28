/*********************************************
 * OPL 12.9.0.0 Model
 * Author: dhunganaa
 * Creation Date: Feb 4, 2020 at 11:42:10 AM
 *********************************************/
/*********************************************
 * OPL 12.9.0.0 Model
 * Author: dhunganaa
 * Creation Date: Nov 21, 2019 at 12:24:02 PM
 *********************************************/
/*********************************************
 * OPL 12.9.0.0 Model
 * Author: dhunganaa
 * Creation Date: Oct 17, 2019 at 1:48:36 PM
 *********************************************/
 
 //No self consumption
 
 int numNodes = 30;
 range index = 1..numNodes;

dvar float+ sent[index][index];
 dvar float+ enext[index];
 float energies[index]=...;

  float edges[index][index]=...;
   dvar int+ K[index][index];
   dvar float+ loss;
   float beta = 0.6;
   int threshold = 1260;
   
   
   
   
  
   
//maximize (min(i in index)enext[i]);//(sum(t in 1..time)(Lifetime[t]));
/**
	Objective function for energy balancing
*/
minimize 100 * (sum(i in index)(abs(enext[i] - sum(i in index)enext[i]/numNodes))) + loss;
subject to
{
		
		  
	equationConstraint3:
		forall(i in index, j in index)
		  sent[i][j] <= (energies[i] * (edges[i][j] >= 1 && edges[i][j] <= threshold));
	
	equationConstrainttest:
		forall(i in index)
		  sum(j in index)sent[i][j] <= (energies[i]);
		//equationConstrainttest:
		//forall(i in index)
		  //sum(j in index)sent[i][j] <= (energies[i]);
	
	
	equationConstraint3Lower:
		forall(i in index, j in index)
			sent[i][j] >=0;        
	
		//equationConstraint3Upper:
		//forall(i in index, j in index)
			//sent[i][j] <= 100;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	
	
	equationConstraint31:
		forall(i in index, j in index)
			K[i][j] == 1- (sent[i][j] ==0);
			 
	equationConstraintMain:
	forall(i in index)
		enext[i]==energies[i] - sum(j in index)sent[i][j] + (sum(j in index)sent[j][i] * (1-beta));
	
	equationConstraint32:
			forall(i in index, j in index)
			  	K[i][j] + K[j][i] <=1 ;
			  	
	
	
	equationConstraint4:		
		loss == sum(i in index, j in index)sent[i][j] * beta;
			
	
	equationConstraint5:
		forall(i in index)
			0<=enext[i]<=100;
		  	

	
}

 