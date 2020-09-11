package writingboard;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class WritingBoard {
	public int locX;
	public int locY;
	public int resX;
	public int resY;
	public int width;
	public int height;
	Pixel[][] pixels;
	
	public WritingBoard(int resX, int resY, int locX, int locY, int width, int height) {
		this.locX = locX;
		this.locY = locY;
		this.resX = resX;
		this.resY = resY;
		this.width = width;
		this.height = height;
		pixels = new Pixel[resY][resX];
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				pixels[y][x] = new Pixel();
			}
		}
	}
	
	public double[] getPixelArray() {
		WritingBoard shiftedBoard = new WritingBoard(resX, resY, locX, locY, width, height);
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				shiftedBoard.pixels[y][x].intensity = pixels[y][x].intensity;
			}
		}
		int lowestXPixel = resX;
		int lowestYPixel = resY;
		int highestXPixel = 0;
		int highestYPixel = 0;
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				if(pixels[y][x].intensity > 0) {
					if(x < lowestXPixel) {
						lowestXPixel = x;
					}
					if(y < lowestYPixel) {
						lowestYPixel = y;
					}
					if(x > highestXPixel) {
						highestXPixel = x;
					}
					if(y > highestYPixel) {
						highestYPixel = y;
					}
				}
			}
		}
		double centerX = ((double) highestXPixel + (double) lowestXPixel)/2;
		double centerY = ((double) highestYPixel + (double) lowestYPixel)/2;
		
		int shiftX = (int) Math.round(centerX - (double) resX/2.0);
		int shiftY = (int) Math.round(centerY - (double) resY/2.0);
		
		if(shiftX < 0) {
			shiftX *= -1;
			while(shiftX > 0) {
				shiftedBoard.shiftRight();
				shiftX--;
			}
		}else {
			while(shiftX > 0) {
				shiftedBoard.shiftLeft();
				shiftX--;
			}
		}
		if(shiftY < 0) {
			shiftY *= -1;
			while(shiftY > 0) {
				shiftedBoard.shiftDown();
				shiftY--;
			}
		}else {
			while(shiftY > 0) {
				shiftedBoard.shiftUp();
				shiftY--;
			}
		}
		
		double[] returnArray = new double[resX*resY];
		int i = 0;
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				returnArray[i++] = ((double) shiftedBoard.pixels[y][x].getIntensity())/255;
			}
		}
		return returnArray;
	}
	
	public void shiftUp() {
		for(int y = 0; y < resY - 1; y++) {
			for(int x = 0; x < resX; x++) {
				pixels[y][x] = pixels[y + 1][x];
			}
		}
		for(int x = 0; x < resX; x++) {
			pixels[resY - 1][x] = new Pixel();
		}
	}
	
	public void shiftDown() {
		for(int y = resY - 1; y > 0; y--) {
			for(int x = 0; x < resX; x++) {
				pixels[y][x] = pixels[y - 1][x];
			}
		}
		for(int x = 0; x < resX; x++) {
			pixels[0][x] = new Pixel();
		}
	}
	
	public void shiftLeft() {
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX - 1; x++) {
				pixels[y][x] = pixels[y][x + 1];
			}
		}
		for(int y = 0; y < resY; y++) {
			pixels[y][resX - 1] = new Pixel();
		}
	}
	
	public void shiftRight() {
		for(int y = 0; y < resY; y++) {
			for(int x = resX - 1; x > 0; x--) {
				pixels[y][x] = pixels[y][x - 1];
			}
		}
		for(int y = 0; y < resY; y++) {
			pixels[y][0] = new Pixel();
		}
	}
	
	public void clearBoard() {
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				pixels[y][x].clear();
			}
		}
	}
	
	public boolean isClear() {
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				if(pixels[y][x].getIntensity() > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void writePixel(int x, int y) {
		int xPix = (int) Math.floor((((double) x - (double) locX)/(double) width)*(double) resX);
		int yPix = (int) Math.floor((((double) y - (double) locY)/(double) height)*(double) resY);
		if(yPix >= 0 && yPix < resY && xPix >= 0 && xPix < resX) {
			pixels[yPix][xPix].addIntensity(255);
			if(yPix - 1 >= 0) {
				pixels[yPix - 1][xPix].addIntensity(64);
			}
			if(yPix + 1 < resY) {
				pixels[yPix + 1][xPix].addIntensity(64);
			}
			if(xPix - 1 >= 0) {
				pixels[yPix][xPix - 1].addIntensity(64);
			}
			if(xPix + 1 < resX) {
				pixels[yPix][xPix + 1].addIntensity(64);
			}
		}
	}
	
	public void draw(Graphics2D g) {
		for(int y = 0; y < resY; y++) {
			for(int x = 0; x < resX; x++) {
				pixels[y][x].draw(g,
						(int) Math.ceil((double) locX + ((double) width/(double) resX)*(double) x),
						(int) Math.ceil((double) locY + ((double) height/(double) resY)*(double) y),
						(int) Math.ceil((double) width/(double) resX),
						(int) Math.ceil((double) height/(double) resY));
			}
		}
		g.setColor(new Color(255, 255, 255));
		g.drawRect(locX, locY, width, height);
	}
}
