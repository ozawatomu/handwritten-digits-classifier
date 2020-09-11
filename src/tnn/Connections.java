package tnn;
import matrix.Matrix;

public class Connections {
	private Matrix weights;
	
	public Connections(int inputCount, int outputCount) {
		weights = new Matrix(outputCount, inputCount + 1);
		weights.randomise(-1.0, 1.0);
		//System.out.println("Synapses");
		//System.out.println(weights);
	}
	
	public void setWeights(Matrix newWeights) {
		weights = newWeights;
	}
	
	public Matrix getWeights() {
		return weights;
	}
	
	public Matrix getSynapseWeights() {
		return weights.getSubMatrix(weights.numberOfRows(), weights.numberOfColumns() - 1);
	}
	
	public void updateWeights(Matrix deltaWeights) {
		weights.addSubMatrix(deltaWeights);
	}
	
	public void updateBiases(Matrix biases) {
		for(int row = 0; row < weights.numberOfRows(); row++) {
			weights.set(row, weights.numberOfColumns() - 1, weights.get(row, weights.numberOfColumns() - 1) + biases.get(row, 0));
		}
	}
}
