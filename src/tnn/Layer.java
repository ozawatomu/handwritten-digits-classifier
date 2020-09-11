package tnn;
import java.util.Random;

import matrix.Matrix;

public class Layer {
	private Matrix values;
	
	public Layer(int neuronCount) {
		values = new Matrix(neuronCount + 1, 1);
		values.randomise(-1.0, 1.0);
		values.set(values.numberOfRows() - 1, 0, 1);
		//System.out.println("Neurons");
		//System.out.println(values);
	}
	
	public void setValues(Connections inputConnections, Layer previousLayer) {
		values = Matrix.getProductMatrix(inputConnections.getWeights(), previousLayer.getValues());
		for(int i = 0; i < values.numberOfRows(); i++) {
			values.set(i, 0, 1.0/(1 + Math.pow(Math.E, -values.get(i, 0))));
		}
		values.addRow();
		values.set(values.numberOfRows() - 1, 0, 1);
		//System.out.println("Neurons");
		//System.out.println(values);
	}
	
	public void setValues(double[] input) {
		for(int i = 0; i < values.numberOfRows() - 1; i++) {
			values.set(i, 0, input[i]);
		}
	}
	
	public double[] getValuesArray() {
		return values.getColumn(0, 0, values.numberOfRows() - 1);
	}
	
	public Matrix getValues() {
		return values;
	}
	
	public Matrix getNeuronValues() {
		return values.getSubMatrix(values.numberOfRows() - 1, 1);
	}
	
	public int getNeuronCount() {
		return values.numberOfRows() - 1;
	}
}
