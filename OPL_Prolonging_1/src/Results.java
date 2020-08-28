import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Results {
	
	ArrayList<Double> vD = null;
	ArrayList<Double> energyPrediction = null;
	ArrayList<Double> numNodesPrediction = null;
	ArrayList<Double> totalEnergy = null;
	ArrayList<Double> numInteractions = null;
	ArrayList<Double> numNodesPlus = null; //Number of nodes on the positive side
	ArrayList<Double> numNodesminus = null; //Number of nodes on the negative side.
	ArrayList<Double> lifeTime = null;
	ArrayList<Double> energyVd = null;
	ArrayList<Double> intervd = null;
	HashMap<Integer, Double> energyVD = new LinkedHashMap<Integer, Double>();
	HashMap<Integer, Double> interVD = new LinkedHashMap<Integer, Double>();
	
	
	Utilities util = new Utilities();
	int time = 0;
	int N = 0;
	int numRecords = 0;
	
	
	
	public  Results(int initialize, Utilities util)
	{
		// ArrayList<Double> totalEnergy_temp_poa = new ArrayList<>();
		this.util = util;
		this.N = util.N;
		this.time = PARAMETERS.simTime;
		this.numRecords = PARAMETERS.recordTime;
		
				vD = new ArrayList<>();
				util.init_DArray(vD, (time/ numRecords) + 1);
				
				totalEnergy = new ArrayList<>();
				util.init_DArray(totalEnergy, (time/numRecords) + 1);
				
				numInteractions = new ArrayList<>();
				util.init_DArray(numInteractions, (time/numRecords) + 1);
				
				numNodesPlus = new ArrayList<>();
				util.init_DArray(numNodesPlus, (time/numRecords) + 1);
				
				numNodesminus = new ArrayList<>();
				util.init_DArray(numNodesminus, (time/numRecords) + 1);
				
				
				energyPrediction = new ArrayList<>();
				util.init_DArray(energyPrediction, (time/numRecords) + 1);
				
				
				numNodesPrediction = new ArrayList<>();
				util.init_DArray(numNodesPrediction, (N));
				
				lifeTime = new ArrayList<>();
				util.init_DArray(lifeTime, (time/numRecords) + 1);
				
				energyVd = new ArrayList<>();
				util.init_DArray(energyVd, (50) );
				
				intervd = new ArrayList<>();
				util.init_DArray(intervd, (50) );
				
	}

	public Results(Utilities util)
	{
		this.util = util;
		this.N = util.N;
		this.time = PARAMETERS.simTime;
		
		
		vD = new ArrayList<>();
		
		
		totalEnergy = new ArrayList<>();
		
		
		numInteractions = new ArrayList<>();
		
		
		numNodesPlus = new ArrayList<>();
		
		
		numNodesminus = new ArrayList<>();
		
		
		
		energyPrediction = new ArrayList<>();
		
		
		
		numNodesPrediction = new ArrayList<>();
		
		lifeTime = new ArrayList<Double>();
		energyVd = new ArrayList<Double>();
		intervd = new ArrayList<Double>();
		
		
	}

	public ArrayList<Double> getvD() {
		return vD;
	}


	public void setvD(double value) {
		this.vD.add(value);
	}
	
	public void setEnergyVd(double value) {
		this.energyVd.add(value);
	}
	
	public void setInterVd(double value) {
		this.intervd.add(value);
	}


	public ArrayList<Double> getEnergyPrediction() {
		return energyPrediction;
	}


	public void setEnergyPrediction(double energyPrediction) {
		this.energyPrediction.add(energyPrediction);
	}


	public ArrayList<Double> getNumNodesPrediction() {
		return numNodesPrediction;
	}


	public void setNumNodesPrediction(double numNodesPrediction) {
		this.numNodesPrediction.add(numNodesPrediction);
	}


	public ArrayList<Double> getTotalEnergy() {
		return totalEnergy;
	}


	public void setTotalEnergy(double totalEnergy) {
		this.totalEnergy.add(totalEnergy);
	}


	public ArrayList<Double> getNumInteractions() {
		return numInteractions;
	}


	public void setNumInteractions(double numInteractions) {
		this.numInteractions .add(numInteractions);
	}
	
	
	public ArrayList<Double> getNumNodesPlus() {
		return numNodesPlus;
	}


	public void setNumNodesPlus(double numNodesPlus) {
		this.numNodesPlus.add(numNodesPlus);
	}


	public ArrayList<Double> getNumNodesminus() {
		return numNodesminus;
	}


	public void setNumNodesminus(double numNodesminus) {
		this.numNodesminus.add(numNodesminus);
	}

	public ArrayList<Double> getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(double lifeTime) {
		this.lifeTime.add(lifeTime);
	}
	

}
