package client;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas{
	
	private List<Point> pList = new ArrayList<>();
	
	public void setList(Point p) {
		pList.add(p);
	}
	
	public void newList() {
		pList = new ArrayList<>();
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawRect(20, 20, 300, 350); // game area
		for(int i=0;i<pList.size()-1;i++) {
			int x = (int)pList.get(i).getX();
			int y = (int)pList.get(i).getY();
			int x2 = (int)pList.get(i+1).getX();
			int y2 = (int)pList.get(i+1).getY();
			g.drawLine(x, y, x2, y2);
		}
	}

	@Override
	public void update(Graphics g) {
		this.paint(g);
	}
	

}
