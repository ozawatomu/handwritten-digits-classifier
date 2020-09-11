import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mnist.MnistReader;
import tnn.NeuralNetwork;

public class TrainNetwork {

	public static void main(String[] args) {
		trainNetwork();
		getNetworkAccuracy();
		//testNetwork();
	}
	
	private static void getNetworkAccuracy() {
		int[] labels = MnistReader.getLabels("t10k-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("t10k-images.idx3-ubyte");
		ArrayList<double[]> inputs = new ArrayList<double[]>();
		for(int[][] image: images) {
			inputs.add(convertToInputs(image));
		}

		System.out.println("LOADING NETWORK");
		NeuralNetwork nn2 = new NeuralNetwork(new File("FirstTraining.tnn"));
		
		int numberCorrect = 0;
		for(int i = 0; i < inputs.size(); i++) {
			if(nn2.compute(inputs.get(i)) == labels[i]) {
				numberCorrect++;
			}
		}
		
		System.out.println("The network is %" + ((Double.valueOf(numberCorrect)/Double.valueOf(inputs.size()))*100) + " accurate.");
	}

	public static void trainNetwork() {
		System.out.println("LOADING INPUT");
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");
		ArrayList<double[]> inputs = new ArrayList<double[]>();
		for(int[][] image: images) {
			inputs.add(convertToInputs(image));
		}
		
		System.out.println("CONSTRUCTING NETWORK");
		NeuralNetwork nn = new NeuralNetwork(inputs.get(0).length, 89, 10, 0.02);
		nn.addHiddenLayer(32);
		//nn.addHiddenLayer(32);
		
		System.out.println("TRAINING NETWORK");
		int trainCount = 60000;
		for(int i = 0; i < trainCount; i++) {
			System.out.println(i + 1 + "/" + trainCount);
			nn.train(inputs.get(i), labels[i]);
		}
		
		System.out.println("SAVING NETWORK");
		nn.saveNeuralNetwork("FirstTraining");
		System.out.println("SAVED.");
	}
	
	public static void testNetwork() {
		System.out.println("LOADING NETWORK");
		NeuralNetwork nn2 = new NeuralNetwork(new File("FirstTraining.tnn"));
		
		//CREATE TESTS
		int[] testLabels = MnistReader.getLabels("t10k-labels.idx1-ubyte");
		List<int[][]> testImages = MnistReader.getImages("t10k-images.idx3-ubyte");
		Random r = new Random();
		int testIndex = r.nextInt(testLabels.length - 1);
		int[][] image2 = testImages.get(testIndex);
		System.out.println("COMPUTING NETWORK");
		System.out.println("Input:");
		printImage(image2);
		System.out.println("Guess Number: " + nn2.compute(convertToInputs(testImages.get(testIndex))));
	}
	
	public static double[] convertToInputs(int[][] image){
		double[] returnArray = new double[image.length*image[0].length];
		int c = 0;
		for(int y = 0; y < image.length; y++) {
			for(int x = 0; x < image[y].length; x++) {
				returnArray[c++] = ((double) image[y][x])/255;
			}
		}
		return returnArray;
	}
	
	public static void printImage(int[][] image) {
		for(int y = 0; y < image.length; y++) {
			for(int x = 0; x < image[y].length; x++) {
				if(image[y][x] > 0) {
					if(image[y][x] < 170) {
						System.out.print("- ");
					}else if(image[y][x] < 250) {
						System.out.print("+ ");
					}else{
						System.out.print("# ");
					}
				}else {
					System.out.print(". ");
				}
			}
			System.out.println();
		}
	}
}
