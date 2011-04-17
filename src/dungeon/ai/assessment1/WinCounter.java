package dungeon.ai.assessment1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

public class WinCounter {
	
	/** vector to count how many of the last 20 games have been won */
	private final static int WIN_LOSE_RANGE = 20;
	private static boolean[] won = null;
	private static int count = 0;	
	
	private static final String file = "results/results.txt";

	/**
	 * init the array to calc percentage of won games. 
	 */
	public static void init() {
		if( won == null) {
			won = new boolean[WIN_LOSE_RANGE];
			for(int i=0;i<won.length;i++) {
				won[i] = false;
			}
			// create output file
			try{
				File results = new File(file);
			    if (results.exists())
			    	results.delete();
			    results.createNewFile();
	         } catch(Exception e){
	            System.err.println(e.toString());
	         }   
		}		
	}
	
	
	/**
	 * Store if last game was won or lost. 
	 * @param winLose false for lost, true for won.
	 */
	public static void setWinLos(boolean winLose) {
		won[count] = winLose;
		count++;
		count%=WIN_LOSE_RANGE;
	}
	
	/**
	 * @return Returns the percentage of the won games out of the last WIN_LOSE_RANGE games.
	 */
	public static double getPercentageWon() {
		int cnt = 0;
		for(int i=0;i<won.length;i++) {
			if (won[i]) {
				cnt++;
			}
		}
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(((double)cnt)/WIN_LOSE_RANGE * 100));
	}
	
	public static void out() {
		if (count == WIN_LOSE_RANGE - 1) {
			Double percentageWon = WinCounter.getPercentageWon();
			System.out.println("Percentage of won games out of last " + WinCounter.WIN_LOSE_RANGE + " games = " + percentageWon + "%");
			
			// write results to file
	        try{
	            FileWriter out = new FileWriter(file, true);
	            BufferedWriter writer = new BufferedWriter(out);
	            writer.write(percentageWon.toString());
	            writer.newLine();
	            writer.close();
	            out.close();
	         } catch(Exception e){
	            System.err.println(e.toString());
	         }   
		} 
	}
}
