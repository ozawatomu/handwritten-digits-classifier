package writingboard;
import java.awt.Color;
import java.awt.Graphics2D;

public class Pixel {
	int intensity;
	
	public Pixel() {
		intensity = 0;
	}
	
	public void addIntensity(int intensity) {
		if(intensity > 255) {
			this.intensity = 255;
		}else if(intensity < 0) {
			this.intensity = 0;
		}else {
			this.intensity += intensity;
			if(this.intensity > 255) {
				this.intensity = 255;
			}
		}
	}
	
	public int getIntensity() {
		return intensity;
	}
	
	public void clear() {
		intensity = 0;
	}
	
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		g.setColor(new Color((int) intensity, (int) intensity, (int) intensity));
		g.fillRect(x, y, width, height);
	}
}
