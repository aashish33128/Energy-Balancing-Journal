import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloNumMap;
import ilog.concert.cppimpl.IloModel;
import ilog.opl.IloCplex;
import ilog.opl.IloOplDataSource;
import ilog.opl.IloOplErrorHandler;
import ilog.opl.IloOplFactory;
import ilog.opl.IloOplModel;
import ilog.opl.IloOplModelDefinition;
import ilog.opl.IloOplModelSource;
import ilog.opl.IloOplSettings;

public class RunCplex {

	int numNodes;
	String modelfilename;
	List<String> datafileName;
	IloOplModel opl;
	IloOplFactory oplF;
	
	public RunCplex(int numNodes, String modelfilename, List<String> datafileName)
	{
		this.numNodes = numNodes;
		this.modelfilename = modelfilename;
		this.datafileName = datafileName;
	}
	
	public void settings(IloCplex cplex)
	{
		//Set Optimization and output Parameters
		try {
			
			 //cplex.setParam(IloCplex.IntParam.RootAlg, IloCplex.Algorithm.Network);
			  //cplex.setParam(IloCplex.IntParam.NodeAlg, IloCplex.Algorithm.Network);
			 
			//cplex.setParam(IloCplex.IntParam.TuningDisplay, 2);
			//cplex.setParam(IloCplex.IntParam.Probe, 3);
			//cplex.setParam(IloCplex.IntParam.MIPEmphasis, 3);
			//cplex.setParam(IloCplex.IntParam.MIPDisplay, 1);
			cplex.setParam(IloCplex.DoubleParam.EpGap, 0.02);
			System.out.println("--Algorithm used to solve " + cplex.getAlgorithm());
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cplex.setOut(System.out);
		
		
	}
	
	public void setData()
	{
		
	}
	
	public IloNumMap getVariables(String name)
	{
		IloNumMap variables = null;
		if(opl == null)
		{
			System.out.println("-----ERROR--" + "Accessing variable from an empty model");
		}
		else
		{
		variables  = opl.getElement(name).asNumMap();
		}
		
		return variables;
	}
	
	public double run()
	{
		double objValue = -1;
		IloOplFactory.setDebugMode(false);
		
		IloOplFactory oplF = new IloOplFactory();
		IloOplErrorHandler errHandler = oplF.createOplErrorHandler();
	
		IloOplModelSource modelSource = oplF.createOplModelSource(this.modelfilename);
	
		IloOplSettings settings = oplF.createOplSettings(errHandler);
	
		IloOplModelDefinition def = oplF.createOplModelDefinition(modelSource, settings);
		IloCplex cplex = null;
		try {
			cplex = oplF.createCplex();
			settings(cplex);
			
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IloOplModel opl = oplF.createOplModel(def, cplex);
		this.oplF = oplF;
		this.opl = opl;
	
		// Create Data File:
		
		for(int a=0;a<this.datafileName.size();a++)
		{
			IloOplDataSource dataSource = oplF.createOplDataSource(this.datafileName.get(a));
			opl.addDataSource(dataSource);
		}
	

		opl.generate();
		System.out.println("Generating OPL Model");
		
		try {
			if (cplex.solve()) {
				
				
				System.out.println(cplex.getNcols() + ", " + cplex.getNrows());
				System.out.println(cplex.getAlgorithm());
				
				//System.exit(0);
				 objValue = opl.getCplex().getObjValue();
				// opl.
				System.out.println("OBJECTIVE: " + objValue);
				opl.postProcess();
				//totalLifeTime = totalLifeTime + objValue;

				// cplex.numVarArray(6, 0, 6);
			}
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return objValue;
		// cplex.end();
	}

	public void close()
	{
		this.opl.end();
		this.oplF.end();
	}
	}

