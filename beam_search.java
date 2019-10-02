// Name: William Ma
// ID: 260740719

public class beam_search{
	
	public static void main(String[] args){
		System.out.println("a)");
		gradient();
		System.out.println("\nb)");
		beam();
	}
	
	public static double[][] nei (double x, double y, double step){
		double[][] nei = new double[9][4];
		double[] neix = {x-step, x, x+step, x-step, x, x+step, x-step, x, x+step};
		double[] neiy = {y-step, y-step, y-step, y, y, y, y+step, y+step, y+step};
		for (int i = 0; i < 9; i++){
			nei[i][0] = neix[i];
			nei[i][1] = neiy[i];
			if (nei[i][0] >= 0 && nei[i][0] <= 10 && nei[i][1] >= 0 && nei[i][1] <= 10){
				nei[i][2] = f1(nei[i][0], nei[i][1]);
				nei[i][3] = f2(nei[i][0], nei[i][1]);
			}
			else {
				nei[i][2] = -1 * Double.POSITIVE_INFINITY;
				nei[i][3] = -1 * Double.POSITIVE_INFINITY;
			}
		}
		return nei;
	}
	
	public static double f1 (double x, double y){
		return Math.sin(x/2) + Math.cos(y*2);
	}
	
	public static double[] maxf1 (double[][] nei){
		double max = nei[4][2];
		int index = 4;
		for (int i = 0; i < 9; i++){
			double temp = nei[i][2];
			if (temp > max){
				max = temp;
				index = i;
			}
		}
		double[] best = {nei[index][0], nei[index][1]};
		return best;
	}
	
	public static double[][] maxf1 (double[][] nei, int beam){
		boolean check = true;
		double[] less;
		while (check){
			check = false;
			for (int i = 0; i < nei.length-1; i++){
				if (nei[i][2] < nei[i+1][2]){
					less = nei[i];
					nei[i] = nei[i+1];
					nei[i+1] = less;
					check = true;
				}
			}
		}
		double[][] result = new double[8][4];
		for (int k = 0; k < beam; k++){
			result[k] = nei[k];
		}
		return result;
	}
	
	public static double[] steps1 (double x, double y, double size){
		int steps = 0;
		while (maxf1(nei(x, y, size))[0] != x || maxf1(nei(x, y, size))[1] != y){
			steps++;
			x = maxf1(nei(x, y, size))[0];
			y = maxf1(nei(x, y, size))[1];
		}
		double[] result = {f1(x, y), steps};
		return result;
	}
	
	public static double[] steps1 (double[][] set, int beam, double size){
		int steps = 0;
		double[][] max = maxf1(set, beam);
		double[][] neigh = new double[beam*9][4];
		double[][][] temp = new double[beam][9][4];
		for (int k = 0; k < beam; k++){
			temp = new double[beam][9][4];
			temp[k] = nei(set[k][0], set[k][1], size);
			for (int i = 0; i < 9; i++) {
				neigh[k*9+i] = temp[k][i];
			}
		}
		while (true){
			boolean stop = true;
			for (int i = 0; i < beam; i++) {
				if (maxf1(neigh, beam)[i][2] != max[i][2]) {
					stop = false;
				}
			}
			if (stop) {
				break;
			}
			steps++;
			max = maxf1(neigh, beam);
			for (int k = 0; k < beam; k++){
				temp [k] = nei(max[k][0], max[k][1], size);
				for (int i = 0; i < 9; i++) {
					neigh[k*9+i] = temp[k][i];
				}
			}
		}
		double[] result = {max[0][2], steps};
		return result;
	}
	
	public static void gradient(){
		double[] size = {0.01, 0.05, 0.1, 0.2};
		double[][] f1val = new double[4][100];
		int[][] results1 = new int[4][100];
		double[][] f2val = new double[4][100];
		int[][] results2 = new int[4][100];
		double[] fmean1 = new double[4];
		double[] fstdev1 = new double[4];
		double[] smean1 = new double[4];
		double[] sstdev1 = new double[4];
		double[] fmean2 = new double[4];
		double[] fstdev2 = new double[4];
		double[] smean2 = new double[4];
		double[] sstdev2 = new double[4];
	 	for (int j = 0; j < 4; j++){
	 		double f1sum = 0;
			double f1dev = 0;
			double stepsum = 0;
			double stepdev = 0;
			for (int i = 0; i < 100; i++){
				double x = Math.random()*10;
				double y = Math.random()*10;
				f1val[j][i] = steps1(x, y, size[j])[0];
				results1[j][i] = (int) steps1(x, y, size[j])[1];
				f1sum += f1val[j][i];
				stepsum += results1[j][i];
			}
			fmean1[j] = f1sum/100;
			smean1[j] = stepsum/100;
			for (int i = 0; i < 100; i++){
				f1dev += Math.pow(f1val[j][i] - fmean1[j], 2);
				stepdev += Math.pow(results1[j][i] - smean1[j], 2);
			}
			fstdev1[j] = Math.sqrt(f1dev/100);
			sstdev1[j] = Math.sqrt(stepdev/100);
	 	}
		for (int j = 0; j < 4; j++){
			double f2sum = 0;
			double f2dev = 0;
			double stepsum = 0;
			double stepdev = 0;
			for (int i = 0; i < 100; i++){
				double x = Math.random()*10;
				double y = Math.random()*10;
				f2val[j][i] = steps2(x, y, size[j])[0];
				results2[j][i] = (int) steps2(x, y, size[j])[1];
				f2sum += f2val[j][i];
				stepsum += results2[j][i];
			}
			fmean2[j] = f2sum/100;
			smean2[j] = stepsum/100;
			for (int i = 0; i < 100; i++){
				f2dev += Math.pow(f2val[j][i] - fmean2[j], 2);
				stepdev += Math.pow(results2[j][i] - smean2[j], 2);
			}
			fstdev2[j] = Math.sqrt(f2dev/100);
			sstdev2[j] = Math.sqrt(stepdev/100);
		}
		System.out.println("\nf1: ");
		for (int j = 0; j < 4; j++){
			System.out.println("Mean of f1 for " + size[j] + ": " + String.format("%.2f", fmean1[j]));
			System.out.println("Stdev of f1 for " + size[j] + ": " + String.format("%.2f", fstdev1[j]));
			System.out.println("Mean of steps for " + size[j] + ": " + String.format("%.2f", smean1[j]));
			System.out.println("Stdev of steps for " + size[j] + ": " + String.format("%.2f", sstdev1[j]));
		}
		System.out.println("\nf2: ");
		for (int j = 0; j < 4; j++){
			System.out.println("Mean of f2 for " + size[j] + ": " + String.format("%.2f", fmean2[j]));
			System.out.println("Stdev of f2 for " + size[j] + ": " + String.format("%.2f", fstdev2[j]));
			System.out.println("Mean of steps for " + size[j] + ": " + String.format("%.2f", smean2[j]));
			System.out.println("Stdev of steps for " + size[j] + ": " + String.format("%.2f", sstdev2[j]));
		}
	}
	
	public static double f2 (double x, double y){
		return -Math.abs(x-2) - Math.abs(y/2+1) + 3;
	}
	
	public static double[] maxf2 (double[][] nei){
		double max = nei[4][3];
		int index = 4;
		for (int i = 0; i < 9; i++){
			double temp = nei[i][3];
			if (temp > max){
				max = temp;
				index = i;
			}
		}
		double[] best = {nei[index][0], nei[index][1]};
		return best;
	}
	
	public static double[][] maxf2 (double[][] nei, int beam){
		boolean check = true;
		double[] less;
		while (check){
			check = false;
			for (int i = 0; i < nei.length-1; i++){
				if (nei[i][3] < nei[i+1][3]){
					less = nei[i];
					nei[i] = nei[i+1];
					nei[i+1] = less;
					check = true;
				}
			}
		}
		double[][] result = new double[8][4];
		for (int k = 0; k < beam; k++){
			result[k] = nei[k];
		}
		return result;
	}

	public static double[] steps2 (double x, double y, double size){
		int steps = 0;
		while (maxf2(nei(x, y, size))[0] != x || maxf2(nei(x, y, size))[1] != y){
			steps++;
			x = maxf2(nei(x, y, size))[0];
			y = maxf2(nei(x, y, size))[1];
		}
		double[] result = {f2(x, y), steps};
		return result;
	}

	public static double[] steps2 (double[][] set, int beam, double size){
		int steps = 0;
		double[][] max = maxf2(set, beam);
		double[][] neigh = new double[beam*9][4];
		double[][][] temp = new double[beam][9][4];
		for (int k = 0; k < beam; k++){
			temp = new double[beam][9][4];
			temp[k] = nei(set[k][0], set[k][1], size);
			for (int i = 0; i < 9; i++) {
				neigh[k*9+i] = temp[k][i];
			}
		}
		while (true){
			boolean stop = true;
			for (int i = 0; i < beam; i++) {
				if (maxf2(neigh, beam)[i][3] != max[i][3]) {
					stop = false;
				}
			}
			if (stop) {
				break;
			}
			steps++;
			max = maxf2(neigh, beam);
			for (int k = 0; k < beam; k++){
				temp [k] = nei(max[k][0], max[k][1], size);
				for (int i = 0; i < 9; i++) {
					neigh[k*9+i] = temp[k][i];
				}
			}
		}
		double[] result = {max[0][3], steps};
		return result;
	}
	
	public static void beam(){
		double size = 0.1;
		int[] beam = {2, 4, 6, 8};
		double[][] f1val = new double[4][100];
		int[][] results1 = new int[4][100];
		double[][] f2val = new double[4][100];
		int[][] results2 = new int[4][100];
		double[] fmean1 = new double[4];
		double[] fstdev1 = new double[4];
		double[] smean1 = new double[4];
		double[] sstdev1 = new double[4];
		double[] fmean2 = new double[4];
		double[] fstdev2 = new double[4];
		double[] smean2 = new double[4];
		double[] sstdev2 = new double[4];
		for (int j = 0; j < 4; j++){
			double[][] set = new double[beam[j]][4];
			double x;
			double y;
			double f1sum = 0;
			double f1dev = 0;
			double stepsum = 0;
			double stepdev = 0;
			for (int i = 0; i < 100; i++){
				for (int k = 0; k < beam[j]; k++){
					x = Math.random()*10;
					set[k][0] = x;
					y = Math.random()*10;
					set[k][1] = y;
					set[k][2] = f1(x, y);
					set[k][3] = f2(x, y);
				}
				f1val[j][i] = steps1(set, beam[j], size)[0];
				results1[j][i] = (int) steps1(set, beam[j], size)[1];
				f1sum += f1val[j][i];
				stepsum += results1[j][i];
			}
			fmean1[j] = f1sum/100;
			smean1[j] = stepsum/100;
			for (int i = 0; i < 100; i++){
				f1dev += Math.pow(f1val[j][i] - fmean1[j], 2);
				stepdev += Math.pow(results1[j][i] - smean1[j], 2);
			}
			fstdev1[j] = Math.sqrt(f1dev/100);
			sstdev1[j] = Math.sqrt(stepdev/100);
		}
		
		System.out.println("\nf1: ");
		for (int j = 0; j < 4; j++){
			System.out.println("Mean of f1 for " + beam[j] + ": " + String.format("%.2f", fmean1[j]));
			System.out.println("Stdev of f1 for " + beam[j] + ": " + String.format("%.2f", fstdev1[j]));
			System.out.println("Mean of steps for " + beam[j] + ": " + String.format("%.2f", smean1[j]));
			System.out.println("Stdev of steps for " + beam[j] + ": " + String.format("%.2f", sstdev1[j]));
		}
		
		for (int j = 0; j < 4; j++){
			double[][] set = new double[beam[j]][4];
			double x;
			double y;
			double f2sum = 0;
			double f2dev = 0;
			double stepsum = 0;
			double stepdev = 0;
			for (int i = 0; i < 100; i++){
				for (int k = 0; k < beam[j]; k++){
					x = Math.random()*10;
					set[k][0] = x;
					y = Math.random()*10;
					set[k][1] = y;
					set[k][2] = f1(x, y);
					set[k][3] = f2(x, y);
				}
				f2val[j][i] = steps2(set, beam[j], size)[0];
				results2[j][i] = (int) steps2(set, beam[j], size)[1];
				f2sum += f1val[j][i];
				stepsum += results2[j][i];
			}
			fmean2[j] = f2sum/100;
			smean2[j] = stepsum/100;
			for (int i = 0; i < 100; i++){
				f2dev += Math.pow(f2val[j][i] - fmean2[j], 2);
				stepdev += Math.pow(results2[j][i] - smean2[j], 2);
			}
			fstdev2[j] = Math.sqrt(f2dev/100);
			sstdev2[j] = Math.sqrt(stepdev/100);
		}
		System.out.println("\nf2: ");
		for (int j = 0; j < 4; j++){
			System.out.println("Mean of f2 for " + beam[j] + ": " + String.format("%.2f", fmean2[j]));
			System.out.println("Stdev of f2 for " + beam[j] + ": " + String.format("%.2f", fstdev2[j]));
			System.out.println("Mean of steps for " + beam[j] + ": " + String.format("%.2f", smean2[j]));
			System.out.println("Stdev of steps for " + beam[j] + ": " + String.format("%.2f", sstdev2[j]));
		}
	}
}