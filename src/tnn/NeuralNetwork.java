package tnn;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import matrix.Matrix;

public class NeuralNetwork {
	private ArrayList<Layer> layers;
	private ArrayList<Connections> synapses;
	private double learningRate;
	
	public NeuralNetwork(int inputNeuronCount, int hiddenNeuronCount, int outputNeuronCount, double learningRate) {
		layers = new ArrayList<Layer>();
		layers.add(new Layer(inputNeuronCount));
		layers.add(new Layer(hiddenNeuronCount));
		layers.add(new Layer(outputNeuronCount));
		synapses = new ArrayList<Connections>();
		this.learningRate = learningRate;
		connectLayers();
	}
	
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	
	public int compute(double[] input) {
		//SET INPUT VALUES FOR INPUT LAYER
		layers.get(0).setValues(input);
		
		//SET VALUES FOR ALL LAYERS
		for(int i = 1; i < layers.size(); i++) {
			layers.get(i).setValues(synapses.get(i - 1), layers.get(i - 1));
		}
		
		//RETURN HIGHEST INDEX
		double[] outputs = layers.get(layers.size() - 1).getValuesArray();
		//System.out.println(Arrays.toString(outputs));
		int maxI = 0;
		for(int i = 1; i < outputs.length; i++) {
			if(outputs[i] > outputs[maxI]) {
				maxI = i;
			}
		}
		return maxI;
	}
	
	public void train(double[] input, int label) {
		//CREATE TARGETS MATRIX
		Matrix targets = new Matrix(layers.get(layers.size() - 1).getNeuronCount(), 1);
		targets.set(label, 0, 1);
		
		//SET ERRORS FOR OUTPUT LAYER
		compute(input);
		Matrix[] errors = new Matrix[layers.size() - 1];
		errors[errors.length - 1] = Matrix.getSubtractMatrix(targets, layers.get(layers.size() - 1).getNeuronValues());
		
		//SET ERRORS FOR HIDDEN LAYERS
		for(int i = errors.length - 2; i >= 0; i--) {
			errors[i] = Matrix.getProductMatrix(Matrix.getTransposedMatrix(synapses.get(i + 1).getSynapseWeights()), errors[i + 1]);
		}
		
		//CREATE WEIGHT AND BIAS CHANGE MATRIX
		Matrix[] deltaWeights = new Matrix[synapses.size()];
		Matrix[] deltaBiases = new Matrix[layers.size() - 1];
		for(int i = deltaWeights.length - 1; i >= 0; i--) {
			Matrix learningRateError = Matrix.getMultiplyMatrix(errors[i], learningRate);
			Matrix sigmoidDerivative = Matrix.getSubtractMatrix(layers.get(i + 1).getNeuronValues(), Matrix.getPowMatrix(layers.get(i + 1).getNeuronValues(), 2));
			deltaBiases[i] = Matrix.getEWMultiplyMatrix(learningRateError, sigmoidDerivative);
			deltaWeights[i] = Matrix.getProductMatrix(deltaBiases[i], Matrix.getTransposedMatrix(layers.get(i).getNeuronValues()));
		}
		
		//UPDATE WEIGHTS AND BIASES
		for(int i = 0; i < synapses.size(); i++) {
			synapses.get(i).updateWeights(deltaWeights[i]);
			synapses.get(i).updateBiases(deltaBiases[i]);
		}
	}
	
	public void addHiddenLayer(int neuronCount) {
		disconnectLayers();
		layers.add(layers.size() - 1, new Layer(neuronCount));
		connectLayers();
	}
	
	public void connectLayers() {
		disconnectLayers();
		for(int i = 0; i < layers.size() - 1; i++) {
			//System.out.println(layers.get(i).getNeuronCount());
			//System.out.println(layers.get(i + 1).getNeuronCount());
			synapses.add(new Connections(layers.get(i).getNeuronCount(), layers.get(i + 1).getNeuronCount()));
		}
	}
	
	public void disconnectLayers() {
		while(!synapses.isEmpty()) {
			synapses.remove(0);
		}
	}
	
	public NeuralNetwork(File file) {
		//SETUP SCANNER
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//CREATE LAYERS
		String[] line1 = scanner.nextLine().split(" ");
		synapses = new ArrayList<Connections>();
		layers = new ArrayList<Layer>();
		layers.add(new Layer(Integer.valueOf(line1[0])));
		layers.add(new Layer(Integer.valueOf(line1[line1.length - 2])));
		for(int i = 1; i < line1.length - 2; i++) {
			addHiddenLayer(Integer.valueOf(line1[i]));
		}
		learningRate = Double.valueOf(line1[line1.length - 1]);
		connectLayers();
		
		//UPDATE SYNPASE MATRICES
		for(int i = 0; i < synapses.size(); i++) {
			Matrix newWeights = new Matrix(layers.get(i + 1).getNeuronCount(), layers.get(i).getNeuronCount() + 1);
			for(int row = 0; row < newWeights.numberOfRows(); row++) {
				String[] line = scanner.nextLine().split(" ");
				for(int col = 0; col < newWeights.numberOfColumns(); col++) {
					newWeights.set(row, col, Double.valueOf(line[col]));
				}
			}
			synapses.get(i).setWeights(newWeights);
		}
		
		//CLOSE SCANNER
		scanner.close();
	}
	
	public void saveNeuralNetwork(String filename) {
		try {
			//SETUP WRITER
			File file = new File(filename + ".tnn");
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			BufferedWriter writer = null;
			try {
				FileWriter fileWriter = new FileWriter(file);
				writer = new BufferedWriter(fileWriter);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			//SAVE LAYERS
			for(Layer layer: layers) {
				writer.write(layer.getNeuronCount() + " ");
			}
			writer.write(String.valueOf(learningRate));
			writer.newLine();
			
			//SAVE WEIGHTS
			for(Connections connections: synapses) {
				for(int row = 0; row < connections.getWeights().numberOfRows(); row++) {
					for(int col = 0; col < connections.getWeights().numberOfColumns(); col++) {
						writer.write(connections.getWeights().get(row, col) + " ");
					}
					writer.newLine();
				}
			}
			
			//CLOSE WRITER
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
