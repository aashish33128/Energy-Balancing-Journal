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
  int hops[index][index] = ...;
   dvar int+ K[index][index];
   dvar float+ loss;
   float beta = 0.2;
   int threshold =1260;
   dvar int hopsused_single[index][index];
    dvar int hopsused_multi[index][index];
   
   
   
   
   
  
   
//maximize (min(i in index)enext[i]);//(sum(t in 1..time)(Lifetime[t]));
/**
	Objective function for energy balancing
*/

//forall(i in index)
	dexpr  int hopscount_single = (sum(i in index, j in index)(hopsused_single[i][j]));
	dexpr int hopscount_multi =  (sum(i in index, j in index)(hopsused_multi[i][j]));
//((sent[i][j] >=0.00000001)=> (hops[i][j]));
minimize (numNodes^2 * (numNodes^2 * (100 * (sum(i in index)(abs(enext[i] - sum(i in index)enext[i]/numNodes))) + loss) + (hopscount_single))+hopscount_multi/100);
//maximize (min(i in index)enext[i]);
subject to
{
		
		  
	equationConstraint3:
		forall(i in index, j in index)
		  sent[i][j] <= (energies[i] * (edges[i][j] >= 1 && edges[i][j] <= threshold));
	
	//multihop : Node cannot transfer more than it has.
	
		forall(i in index)
		  equationConstrainttest:
		  sum(j in index)sent[i][j] <= (energies[i]);
	
	
		forall(i in index, j in index)
		  equationConstraint3Lower:
			sent[i][j] >=0;        
	
		//equationConstraint3Upper:
		//forall(i in index, j in index)
			//sent[i][j] <= 100;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
	
	forall(i in index,j in index)
	  hopsConstraint_single:
		  hopsused_single[i][j] == hops[i][j] * (sent[i][j]>=0.001 && hops[i][j] == 1);//()sum(j in index)(hops[i][j] * (abs(sent[i][j] - 1) >= 0 && hops[i][j]>=2));  
	
	forall(i in index,j in index)
	  hopsConstraint_multi:
		  hopsused_multi[i][j] == hops[i][j] * (sent[i][j]>=0.001 && hops[i][j] >=2);//()sum(j in index)(hops[i][j] * (abs(sent[i][j] - 1) >= 0 && hops[i][j]>=2)); 
	
	forall(i in index, j in index)
		equationConstraint31:
			K[i][j] == 1- (sent[i][j] ==0);
	
	//multihop
	
	forall(i in index)
	  equationConstraintMain:
		enext[i]==energies[i] - sum(j in index)sent[i][j] + (sum(j in index)sent[j][i] * (1-beta)^hops[i][j]);
		
	//singlehop
	//equationConstraintMain:
		//forall(i in index)
		//enext[i]==energies[i] - sum(j in index)sent[i][j] + (sum(j in index)sent[j][i] * (1-beta));
	
	equationConstraint32:
			forall(i in index, j in index)
			  	K[i][j] + K[j][i] <=1 ;
			  	
	
	//multihop
	equationConstraint4:		
		loss ==  sum(i in index, j in index)(sent[i][j] - sent[i][j] * (1-beta)^hops[i][j]);
	
	
	//singlehop
	//equationConstraint4:		
		//loss ==  sum(i in index, j in index)(sent[i][j] * (beta));
			
	
	
		forall(i in index)
		  equationConstraint5:
			0<=enext[i]<=100;
		  	

	
}

/**main {
  
  // for(var sim=1;sim<=simTime;sim++)
   {
      var source = new IloOplModelSource("MultiHopLinearProgram.mod");
      var cplex = new IloCplex();
      var def = new IloOplModelDefinition(source);     
      var opl = new IloOplModel(def,cplex);
      
   
      
 var data1= new IloOplDataSource("energies.dat");      
    opl.addDataSource(data1);      
    
  
	var data2= new IloOplDataSource("edges_multiple.dat");      
    opl.addDataSource(data2);
    
    var data2= new IloOplDataSource("hops.dat");      
    opl.addDataSource(data2);     
    opl.generate();
	
      if (cplex.solve()) {  
         opl.postProcess();
         
         writeln("OBJ = " + cplex.getObjValue());
         
         
         for(var x=1;x<=thisOplModel.numNodes;x++)
         {
         	for(var y=1; y<=thisOplModel.numNodes;y++)
         	{

         		{
         			if(opl.sent[x][y] > 0)
         			{
         				writeln(x+"->" + y + "at : " + " = " + opl.sent[x][y]);         			
         			}         		
         		}         	
         	}         
         }
       //  writeln("Lifetime " + opl.Lifetime);
         writeln("Final Energy " + opl.enext);
         writeln("Hops used  " + opl.hopscount);
      } else {
         writeln("No solution");
      }
      
      
      //Optimal value is the final obtained lifetime.
    // opl.printSolution();
       var x = opl.end();
       writeln("Exited with status " + x);
      // writeln("Total Life Time is " + totalLifeTime);
	
    }
         
	//writeln("Average Final LifeTime " + totalLifeTime/simTime);
 
 //	
 
 	
 	 

}**/



