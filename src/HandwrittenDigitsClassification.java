import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import mnist.MnistReader;
import tnn.NeuralNetwork;
import writingboard.WritingBoard;

public class HandwrittenDigitsClassification extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	int framesPerSecond = 90;
	static int screenSizeWidth = 2020;
	static int screenSizeHeight = 1620;
	static double metersPerPixel = 0.1;
	Timer timer = new Timer(1000/framesPerSecond, this);
	
	static WritingBoard writingBoard;
	static NeuralNetwork nn;
	static int guess = 0;
	static String networkFile = "TrainedNetwork.tnn";
	static double networkAccuracy;

	public static void main(String[] args) {
		writingBoard = new WritingBoard(28, 28, 200, 200, 1220, 1220);
		nn = new NeuralNetwork(new File(networkFile));
		nn.setLearningRate(0.025);
		
		JFrame jFrame = new JFrame();
		jFrame.setTitle("Handwriting Digits Neural Network");
		jFrame.setSize(screenSizeWidth, screenSizeHeight);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HandwrittenDigitsClassification handwriting = new HandwrittenDigitsClassification();
		handwriting.setPreferredSize(new Dimension(screenSizeWidth, screenSizeHeight));
		jFrame.addMouseListener(handwriting);
		jFrame.addMouseMotionListener(handwriting);
		jFrame.addKeyListener(handwriting);
		jFrame.add(handwriting);
		jFrame.pack();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0, 0, 0));
		g2d.fillRect(0, 0, screenSizeWidth, screenSizeHeight);
		
		writingBoard.draw(g2d);
		
		
		g2d.setFont(new Font("Courier", Font.PLAIN, 200));
		g2d.drawString(String.valueOf(guess), 1720, 700);
		
		g2d.setFont(new Font("Courier", Font.PLAIN, 100)); 
		g2d.drawString("CLEAR", 1620, 400);
		
		g2d.drawRect(1550, 300, 450, 140);
		
		timer.start();
	}
	
	private static void getNetworkAccuracy() {
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");
		ArrayList<double[]> inputs = new ArrayList<double[]>();
		for(int[][] image: images) {
			inputs.add(convertToInputs(image));
		}
		
		int numberCorrect = 0;
		for(int i = 0; i < inputs.size(); i++) {
			if(nn.compute(inputs.get(i)) == labels[i]) {
				numberCorrect++;
			}
		}
		
		System.out.println("The network is %" + ((Double.valueOf(numberCorrect)/Double.valueOf(inputs.size()))*100) + " accurate.");
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
	
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		writingBoard.writePixel(arg0.getX(), arg0.getY());
	}

	public void mouseMoved(MouseEvent arg0) {

	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
				
	}

	public void mousePressed(MouseEvent e) {
		if(e.getX() >= 1550 && e.getX() <= 1550 + 450 &&
				e.getY() >= 300 && e.getY() <= 300 + 140) {
			writingBoard.clearBoard();
		}
	}

	public void mouseReleased(MouseEvent e) {
		guess = nn.compute(writingBoard.getPixelArray());
	}
	
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_0:
			nn.train(writingBoard.getPixelArray(), 0);
			System.out.println("Trained on 0");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_1:
			nn.train(writingBoard.getPixelArray(), 1);
			System.out.println("Trained on 1");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_2:
			nn.train(writingBoard.getPixelArray(), 2);
			System.out.println("Trained on 2");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_3:
			nn.train(writingBoard.getPixelArray(), 3);
			System.out.println("Trained on 3");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_4:
			nn.train(writingBoard.getPixelArray(), 4);
			System.out.println("Trained on 4");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_5:
			nn.train(writingBoard.getPixelArray(), 5);
			System.out.println("Trained on 5");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_6:
			nn.train(writingBoard.getPixelArray(), 6);
			System.out.println("Trained on 6");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_7:
			nn.train(writingBoard.getPixelArray(), 7);
			System.out.println("Trained on 7");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_8:
			nn.train(writingBoard.getPixelArray(), 8);
			System.out.println("Trained on 8");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_9:
			nn.train(writingBoard.getPixelArray(), 9);
			System.out.println("Trained on 9");
			guess = nn.compute(writingBoard.getPixelArray());
			writingBoard.clearBoard();
			break;
		case KeyEvent.VK_S:
			System.out.println("SAVING NETWORK");
			nn.saveNeuralNetwork(networkFile);
			System.out.println("SAVED NETWORK");
			break;
		case KeyEvent.VK_T:
			getNetworkAccuracy();
			break;
		case KeyEvent.VK_BACK_SPACE:
			writingBoard.clearBoard();
			break;
		}
	}

	public void keyReleased(KeyEvent arg0) {
		
	}

	public void keyTyped(KeyEvent arg0) {

	}

}
