package art.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import art.controller.ArtController;

public class ShapeCanvas extends JPanel 
{
	private ArrayList<Polygon> triangleList;
	private ArrayList<Polygon> polygonList;
	private ArrayList<Ellipse2D> ellipseList;
	private ArrayList<Rectangle> rectangleList;
	private ArtController app;
	private int previousX;
	private int previousY;
	
	private BufferedImage canvasImage;
	
	public ShapeCanvas(ArtController app)
	{
		super();
		this.app = app;
		
		previousX = Integer.MIN_VALUE;
		previousY = Integer.MIN_VALUE;
		triangleList = new ArrayList<Polygon>();
		polygonList = new ArrayList<Polygon>();
		ellipseList = new ArrayList<Ellipse2D>();
		rectangleList = new ArrayList<Rectangle>();
		
		canvasImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		this.setMinimumSize(new Dimension(600, 600));
		this.setPreferredSize(new Dimension(600,600));
		this.setMaximumSize(getPreferredSize());
	}
	
	public void addShape(Shape current)
	{
		if (current instanceof Polygon)
		{
			if (((Polygon)current).xpoints.length == 3)
			{
				triangleList.add((Polygon) current);
			}
			else
			{
				polygonList.add((Polygon)current);
			}
		}
		else if (current instanceof Ellipse2D)
		{
			ellipseList.add((Ellipse2D) current);
		}
		else
		{
			rectangleList.add((Rectangle) current);
		}
		updateImage();
	}
	
	public void clear()
	{
		canvasImage = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		ellipseList.clear();
		triangleList.clear();
		polygonList.clear();
		rectangleList.clear();
		updateImage();
	}
	
	public void changeBackground()
	{
		Graphics2D current = canvasImage.createGraphics();
		current.setPaint(randomColor());
		current.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
		updateImage();
	}
	
	public void drawOnCanvas(int xPosition, int yPosition, int lineWidth)
	{
		Graphics2D current = canvasImage.createGraphics();
		current.setPaint(Color.DARK_GRAY);
		current.setStroke(new BasicStroke(3));
		
		if (previousX == Integer.MIN_VALUE)
		{
			current.drawLine(xPosition,  yPosition,  xPosition,  yPosition);
		}
		else
		{
			current.drawLine(previousX,  previousY,  xPosition,  yPosition);
		}
		previousX = xPosition;
		previousY = yPosition;
		updateImage();
	}
	
	public void resetLine()
	{
		previousX = Integer.MIN_VALUE;
		previousY = Integer.MIN_VALUE;
	}
	
	public void save()
	{
		try
		{
			JFileChooser saveDialog = new JFileChooser();
			saveDialog.showSaveDialog(app.getFrame());
			String savePath = saveDialog.getSelectedFile().getPath();
			ImageIO.write(canvasImage,  "PNG", new File(savePath));
		}
		catch (IOException error)
		{
			app.handleErrors(error);
		}
	}
	
	private Color randomColor()
	{
		int red = (int)(Math.random() * 256);
		int green = (int)(Math.random() * 256);
		int blue = (int)(Math.random() * 256);
		int alpha = (int)(Math.random() * 256);
		
		return new Color(red, green, blue, alpha);
	}
	
	private void updateImage()
	{
		Graphics2D currentGraphics = (Graphics2D) canvasImage.getGraphics();
		
		for (Ellipse2D current : ellipseList)
		{
			currentGraphics.setColor(randomColor());
			currentGraphics.setStroke(new BasicStroke(2));
			currentGraphics.fill(current);
			currentGraphics.setColor(randomColor());
			currentGraphics.draw(current);
		}
		
		for (Polygon currentTriangle : triangleList)
		{
			currentGraphics.setColor(randomColor());
			currentGraphics.fill(currentTriangle);
		}
		
		for (Polygon currentPolygon : polygonList)
		{
			currentGraphics.setColor(randomColor());
			currentGraphics.setStroke(new BasicStroke(4));
			currentGraphics.draw(currentPolygon);
		}
		
		for (Rectangle currentRectangle : rectangleList)
		{
			currentGraphics.setColor(randomColor());
			currentGraphics.fill(currentRectangle);
		}
		currentGraphics.dispose();
		repaint();
		// paint is only called indirectly
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		graphics.drawImage(canvasImage, 0, 0, null);
	}
	
}
