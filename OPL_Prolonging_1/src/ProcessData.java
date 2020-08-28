import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessData {
	
	//Cambridge-School data
	
	int N = 36;
	int totalduration = PARAMETERS.simTime;
	String filename = "";
	//Utilities util = new Utilities();
	
	public int[][][] readData(BufferedReader reader, String delm) throws IOException
	{
		String line = "";
		int[][][] meetingInfo = new int[N][N][totalduration];
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				for(int t=0;t<totalduration;t++)
				{
					meetingInfo[i][j][t] = 0;
				}
			}
		}
		
		while((line=reader.readLine())!=null)
		{
			String[] content = line.split(delm);
			
			
			String time = content[0];
			//String action = content[1]; //we dont need it
			String node1 = content[2];
			String node2 = content[3];
			String type = content[4];
			
			int t = Integer.parseInt(time) + 1;
			int i = Integer.parseInt(node1);
			int j = Integer.parseInt(node2);
			int disconnected = type.compareTo("up");
			
			if(t>totalduration)
				break;
			
			if(disconnected == 0)
			{
				meetingInfo[i][j][t] = 1;
				//System.out.println(i + ", " + j + " meets at " + t);
			}
			
		}
		
		
		//Set meeting times as a list, each value containing next meeting time.
		int[][][] meetingTimes = new int[N][N][PARAMETERS.numOfMeetingsToGenerate];
	
;		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				int c = 0;
				for(int x=0;x<PARAMETERS.simTime;x++)
				{
					if(meetingInfo[i][j][x]==1)
					{
						meetingTimes[i][j][c] = x;
						meetingTimes[j][i][c] = x;
						//System.out.println(i + ", " + j + " meets at " + x);
						c++;
						if(c>=PARAMETERS.numOfMeetingsToGenerate)
						{
							break;
						}
					}
				}
				Arrays.sort(meetingTimes[i][j]);
			}
		
		}
		
		return meetingTimes;
				
	}
	
	public int getFirstMeetingTime(int i, int j, int[][][] meetingInfo)
	{
		int count=0;
		int time = 0;
		for(int t=1;t<totalduration;t++)
		{
			if(meetingInfo[i][j][t] == 1)
			{
			
				if(count==0)
				{
					time = t;
					
				}
				count++;
				
				
				//Graph ko connections haru. 1 din ko data le pugena.
			
				
			}
		}
		if(count>0) return time; else return 0;
	}
	public int[][] setNetworkInfo(int[][][] meetingInfo)
	{
		//Only get the first Meeting. Wew just need one meeting per pair.
		int[][] connectionInfo = new int[N][N];
		
		for(int i=0;i<N;i++)
		{
			for(int j=0; j<N;j++)
			{
				connectionInfo[i][j] = 0;
			}
		}
		
		int count =0;
		for(int i=0;i<N;i++)
		{
			count =0;
			for(int j=0; j<N;j++)
			{
				
				if(i!=j)
				{
					int time = getFirstMeetingTime(i, j, meetingInfo);
				
						connectionInfo[i][j] = time;
						if(time>0)
						count++;
						//break;
			}
			}
			
			//System.out.println(i + "is connected  to " + count + " nodes");
		}
		//System.exit(0);
		return connectionInfo;
	}
	
public int[][][] run() throws IOException
{
	BufferedReader reader = new BufferedReader(new FileReader("D:/University Laptop-Backup 6-26-2019/Research/Energy Balancing Social Metric/Dataset/haggle-one-cambridge-city-students/haggle-one-cambridge-city-students.txt"));
	

	int[][][] meetings =readData(reader, "\t");
	//int[][] connections = setNetworkInfo(meetings);
	return meetings;

	

	}

	public static void main(String[] args) throws IOException
	{
		
		ProcessData main = new ProcessData();
		main.run();
		
		
	}

}
