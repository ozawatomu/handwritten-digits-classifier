package matrix;

import java.util.Arrays;
import java.util.Random;

public class Matrix {
	private double[][] data;
	
	public Matrix(int numberOfRows, int numberOfColumns) {
		data = new double[numberOfRows][numberOfColumns];
	}
	
	public Matrix(double[] array) {
		data = new double[array.length][1];
		for(int row = 0; row < numberOfRows(); row++) {
			data[row][0] = array[row];
		}
	}
	
	public void add(Matrix otherMatrix) {
		if(otherMatrix.numberOfColumns() == numberOfColumns() && otherMatrix.numberOfRows() == numberOfRows()) {
			for(int row = 0; row < numberOfRows(); row++) {
				for(int col = 0; col < numberOfColumns(); col++) {
					data[row][col] += otherMatrix.get(row, col);
				}
			}
		}else {
			throw new IllegalArgumentException("Matrices are different dimensions");
		}
	}
	
	public void addSubMatrix(Matrix subMatrix) {
		for(int row = 0; row < subMatrix.numberOfRows(); row++) {
			for(int col = 0; col < subMatrix.numberOfColumns(); col++) {
				data[row][col] += subMatrix.get(row, col);
			}
		}
	}
	
	public void subtract(Matrix otherMatrix) {
		if(otherMatrix.numberOfColumns() == numberOfColumns() && otherMatrix.numberOfRows() == numberOfRows()) {
			for(int row = 0; row < numberOfRows(); row++) {
				for(int col = 0; col < numberOfColumns(); col++) {
					data[row][col] -= otherMatrix.get(row, col);
				}
			}
		}else {
			throw new IllegalArgumentException("Matrices are different dimensions");
		}
	}
	
	public static Matrix getSubtractMatrix(Matrix matrix1, Matrix matrix2) {
		if(matrix2.numberOfColumns() == matrix1.numberOfColumns() && matrix2.numberOfRows() == matrix1.numberOfRows()) {
			Matrix resultMatrix = new Matrix(matrix1.numberOfRows(), matrix1.numberOfColumns());
			for(int row = 0; row < matrix1.numberOfRows(); row++) {
				for(int col = 0; col < matrix1.numberOfColumns(); col++) {
					resultMatrix.data[row][col] = matrix1.get(row, col) - matrix2.get(row, col);
				}
			}
			return resultMatrix;
		}else {
			throw new IllegalArgumentException("Matrices are different dimensions");
		}
	}
	
	public void product(Matrix otherMatrix) {
		if(otherMatrix.numberOfColumns() == numberOfRows()) {
			Matrix resultMatrix = new Matrix(otherMatrix.numberOfRows(), numberOfColumns());
			for(int row = 0; row < resultMatrix.numberOfRows(); row++) {
				for(int col = 0; col < resultMatrix.numberOfColumns(); col++) {
					double sumOfProducts = 0.0;
					for(int i = 0; i < otherMatrix.numberOfColumns(); i++) {
						sumOfProducts += otherMatrix.get(row, i)*get(i, col);
					}
					//resultMatrix.set(row, col, sumOfProducts);
					resultMatrix.data[row][col] = sumOfProducts;
				}
			}
			data = resultMatrix.data;
		}else {
			throw new IllegalArgumentException("Matrices of these dimensions cannot be multiplied");
		}
	}
	
	public static Matrix getProductMatrix(Matrix matrix1, Matrix matrix2) {
		if(matrix1.numberOfColumns() == matrix2.numberOfRows()) {
			Matrix resultMatrix = new Matrix(matrix1.numberOfRows(), matrix2.numberOfColumns());
			for(int row = 0; row < resultMatrix.numberOfRows(); row++) {
				for(int col = 0; col < resultMatrix.numberOfColumns(); col++) {
					double sumOfProducts = 0.0;
					for(int i = 0; i < matrix1.numberOfColumns(); i++) {
						sumOfProducts += matrix1.get(row, i)*matrix2.get(i, col);
					}
					//resultMatrix.set(row, col, sumOfProducts);
					resultMatrix.data[row][col] = sumOfProducts;
				}
			}
			return resultMatrix;
		}else {
			throw new IllegalArgumentException("Matrices of these dimensions cannot be multiplied");
		}
	}
	
	public static Matrix getEWMultiplyMatrix(Matrix matrix1, Matrix matrix2) {
		Matrix resultMatrix = new Matrix(matrix1.numberOfRows(), matrix1.numberOfColumns());
		for(int row = 0; row < resultMatrix.numberOfRows(); row++) {
			for(int col = 0; col < resultMatrix.numberOfColumns(); col++) {
				resultMatrix.data[row][col] = matrix1.get(row, col) * matrix2.get(row, col);
			}
		}
		return resultMatrix;
	}
	
	public static Matrix getMultiplyMatrix(Matrix matrix, double x) {
		Matrix resultMatrix = new Matrix(matrix.numberOfRows(), matrix.numberOfColumns());
		for(int row = 0; row < resultMatrix.numberOfRows(); row++) {
			for(int col = 0; col < resultMatrix.numberOfColumns(); col++) {
				resultMatrix.data[row][col] = matrix.get(row, col) * x;
			}
		}
		return resultMatrix;
	}
	
	public static Matrix getPowMatrix(Matrix matrix, double x) {
		Matrix resultMatrix = new Matrix(matrix.numberOfRows(), matrix.numberOfColumns());
		for(int row = 0; row < resultMatrix.numberOfRows(); row++) {
			for(int col = 0; col < resultMatrix.numberOfColumns(); col++) {
				resultMatrix.data[row][col] = Math.pow(matrix.get(row, col), x);
			}
		}
		return resultMatrix;
	}
	
	public void multiply(double x) {
		for(int row = 0; row < numberOfRows(); row++) {
			for(int col = 0; col < numberOfColumns(); col++) {
				data[row][col] *= x;
			}
		}
	}
	
	public Matrix getSubMatrix(int numberOfRows, int numberOfColumns) {
		if(numberOfRows > numberOfRows() || numberOfColumns > numberOfColumns()) {
			throw new IllegalArgumentException("There is no sub matrix of this size");
		}else {
			Matrix resultMatrix = new Matrix(numberOfRows, numberOfColumns);
			for(int row = 0; row < numberOfRows; row++) {
				for(int col = 0; col < numberOfColumns; col++) {
					resultMatrix.data[row][col] = data[row][col];
				}
			}
			return resultMatrix;
		}
	}
	
	public void transpose() {
		Matrix resultMatrix = new Matrix(numberOfColumns(), numberOfRows());
		for(int row = 0; row < numberOfRows(); row++) {
			for(int col = 0; col < numberOfColumns(); col++) {
				//resultMatrix.set(col, row, data[row][col]);
				resultMatrix.data[col][row] = data[row][col];
			}
		}
		data = resultMatrix.data;
	}
	
	public static Matrix getTransposedMatrix(Matrix matrix) {
		Matrix resultMatrix = new Matrix(matrix.numberOfColumns(), matrix.numberOfRows());
		for(int row = 0; row < matrix.numberOfRows(); row++) {
			for(int col = 0; col < matrix.numberOfColumns(); col++) {
				resultMatrix.data[col][row] = matrix.data[row][col];
			}
		}
		return resultMatrix;
	}
	
	public void randomise(double lowerBound, double upperBound) {
		Random r = new Random();
		for(int row = 0; row < numberOfRows(); row++) {
			for(int col = 0; col < numberOfColumns(); col++) {
				data[row][col] = lowerBound + r.nextDouble()*(upperBound - lowerBound);
			}
		}
	}
	
	public void addRow() {
		Matrix newMatrix = new Matrix(numberOfRows() + 1, numberOfColumns());
		for(int row = 0; row < numberOfRows(); row++) {
			for(int col = 0; col < numberOfColumns(); col++) {
				//newMatrix.set(row, col, data[row][col]);
				newMatrix.data[row][col] = data[row][col];
			}
		}
		data = newMatrix.getData();
	}
	
	public double get(int row, int column) {
		return data[row][column];
	}
	
	public void set(int row, int column, double x) {
		data[row][column] = x;
	}
	
	public double[][] getData() {
		return data;
	}
	
	public double[] getRow(int rowIndex) {
		return data[rowIndex];
	}
	
	public double[] getRow(int rowIndex, int startIndex, int endIndex) {
		return Arrays.copyOfRange(data[rowIndex], startIndex, endIndex);
	}
	
	public double[] getColumn(int columnIndex) {
		double[] column = new double[numberOfRows()];
		for(int i = 0; i < numberOfRows(); i++) {
			column[i] = data[i][columnIndex];
		}
		return column;
	}
	
	public double[] getColumn(int columnIndex, int startIndex, int endIndex) {
		double[] column = new double[endIndex - startIndex];
		for(int i = startIndex; i < endIndex; i++) {
			column[i] = data[i][columnIndex];
		}
		return column;
	}
	
	public String toString() {
		String string = "";
		for(int row = 0; row < numberOfRows(); row++) {
			string += Arrays.toString(data[row]) + "\n";
		}
		return string;
	}
	
	public int numberOfColumns() {
		return data[0].length;
	}
	
	public int numberOfRows() {
		return data.length;
	}
}
