package art.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import art.controller.ArtController;

public class ArtPanel extends JPanel;
{
	private final int MINIMUM_EDGE = 5;
	private final int MAXIMUM_EDGE = 20;
	private final int MINIMUM_SCALE = 20;
	private final int MAXIMUM_SCALE = 100;
	
	private ArtController app;
	
	private SpringLayout appLayout;
	private ShapeCanvas canvas;
	private JPanel buttonPanel;
	private JPanel sliderPanel;
	private JSlider scaleSlider;
	private JSlider edgeSlider;
	private JButton triangleButton;
	private JButton rectangleButton;
	private JButton ellipseButton;
	private JButton polygonButton;
	private JButton clearButton;
	private JButton saveButton;
	private JButton colorButton;
	
	private int currentEdgeCount;
	private int currentScale;
	
	public ArtPanel(ArtController app)
	{
		super();
		this.app = app;
		appLayout = new SpringLayout();
		
		currentScale = MINIMUM_SCALE;
		currentEdgeCount = MINIMUM_EDGE;
		scaleSlider = new JSlider(MINIMUM_SCALE, MAXIMUM_SCALE);
		edgeSlider = new JSlider(MINIMUM_EDGE, MAXIMUM_EDGE);
		
		canvas = new ShapeCanvas(app);
		sliderPanel = new JPanel();
		buttonPanel = new JPanel(new GridLayout(0,1));
		
		triangleButton = new JButton("add triangle");
		rectangleButton = new JButton("add rectangle");
		ellipseButton = new JButton("add ellipse");
		polygonButton = new JButton("add polygon");
		clearButton = new JButton("clear image");
		saveButton = new JButton("save image");
		colorButton = new JButton("change color");
		
		setupSliders();
		setupPanel();
		setupLayout();
		setupListeners();
		
	}
	
	private void setupSliders()
	{
		Hashtable<Integer, JLabel> scaleLabels = new HashTable<Integer, JLabel>();
		Hashtable<Integer, JLabel> edgeLabels = new Hashtable<Integer, JLabel>();
		
		scaleLabels.put(MINIMUM_SCALE, new JLabel("<HTML>Small<BR>Shape></HTML>"));
		scaleLabels.put((MAXIMUM_SCALE + MINIMUM_SCALE) / 2, new JLabel("<HTML>Medium<BR>Shape</HTML>"));
		scaleLabels.put(MAXIMUM_SCALE, new JLabel("<HTML>Large<BR>Shape</HTML>"));
		
		edgeLabels.put(MINIMUM_EDGE,  new JLabel("Edges: " + MINIMUM_EDGE));
		edgeLabels.put(MAXIMUM_EDGE,  new JLabel("Edges: " + MAXIMUM_EDGE));
		
		scaleSlider.setLabelTable(scaleLabels);
		scaleSlider.setOrientation(JSlider.VERTICAL);
		scaleSlider.setSnapToTicks(true);
		scaleSlider.setMajorTickSpacing(10);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		scaleSlider.setValue(MINIMUM_SCALE);
		
		edgeSlider.setLabelTable(edgeLabels);
		edgeSlider.setOrientation(JSlider.VERTICAL);
		edgeSlider.setSnapToTicks(true);
		edgeSlider.setMajorTickSpacing(3);
		edgeSlider.setMinorTickSpacing(1);
		edgeSlider.setPaintTicks(true);
		edgeSlider.setPaintLabels(true);
	}
	
	private void setupPanel()
	{
		this.setLayout(appLayout);
		this.setBackground(Color.DARK_GRAY);
		this.setPreferredSize(new Dimension(1024, 768));
		this.add(canvas);
		
		buttonPanel.setPreferredSize(new Dimension(200, 450));
		buttonPanel.add(triangleButton);
		buttonPanel.add(rectangleButton);
		buttonPanel.add(ellipseButton);
		buttonPanel.add(polygonButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(colorButton);
		
		sliderPanel.setPreferredSize(new Dimension(250, 450));
		sliderPanel.add(scaleSlider);
		sliderPanel.add(edgeSlider);
		
		this.add(buttonPanel);
		this.add(sliderPanel);
	}
	
	private void setupLayout()
	{
		
	}
	
	private void setupListeners()
	{
		
		
		clearButton.addActionListeners(click -> canvas.clear());
		
		saveButton.addActionListeners(click -> canvas.save());
		
		colorButton.addActionListener(click -> canvas.changeBackground());
		
		scaleSlider.addChangeListener(new ChangeListener()
				{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				if (!scaleSlider.getValueIsAdjusting()) {
					currentScale = scaleSlider.getValue();
				}
			}
		});
		
		edgeSlider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				if (!edgeSlider.getValueIsAdjusting()) 
				{
					currentEdgeCount = edgeSlider.getValue();
				}
			}
		});
	}
	
	private boolean coinFlip()
	{
		return (int) (Math.random() * 2) == 0;
	}
	
	private Polygon createPolygon(int sides)
	{
		Polygon currentShape = new Polygon();
		
		int originX = (int) (Math.random() * 600);
		int originY = (int) (Math.random() * 600);
		
		for (int index = 0; index < sides; index++)
		{
			int minus = coinFlip() ? -1 : 1;
			int shiftX = (int) (Math.random() * currentScale) * minus;
			minus = coinFlip() ? -1 : 1;
			int shiftY = (int) (Math.random() * currentScale) * minus;
			currentShape.addPoint(originX + shiftX,  originY + shiftY);;
		}
		return currentShape;
	}
	
	
	private Rectangle createRectangle()
	{
		Rectangle currentRectangle;
		
		int cornerX = (int) (Math.random() * 600);
		int cornerY = (int) (Math.random() * 600);
		int width = (int) (Math.random() * currentScale ) + 1;
		if (coinFlip())
		{
			currentRectangle = new Rectangle(cornerX, cornerY, width, width);
		}
		else
		{
			int height = (int)(Math.random() * currentScale) + 1;
			currentRectangle = new Rectangle(cornerX, cornerY, width, height);
		}
		return currentRectangle;
	}
	private Ellipse2D createEllipse()
	{
		Ellipse2D ellipse = new Ellipse2D.Double();
		
		int cornerX = (int) (Math.random() * 600);
		int cornerY = (int) (Math.random() * 600);
		double width = Math.random() * currentScale +1;
		if (coinFlip())
		{
			ellipse.setFrame(cornerX, cornerY, width, width);
		}
		else
		{
			double height = Math.random() * currentScale + 1;
			ellipse.setFrame(cornerX,  cornerY,  width,  height);
		}
		
		return ellipse;
	}
	
	rectangleButton.addActionListener(new ActionListener()
	{
		public void actionPerfromed(ActionEvent click)
		{
			Rectangle rectangle = createRectangle();
			canvas.addShape(rectangle);
		}
	});
	
	triangleButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent click)
		{
			Polygon triangle = createPolygon(3);
			canvas.addShape(triangle);
		}
	});
	
	polygonButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent click)
		{
			Polygon polygon = createPolygon(currentEdgeCount);
			canvas.addShape(polygon);
		}
	});
	
	ellipseButton.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent click)
		{
			Ellipse2D ellipse = createEllipse();
			canvas.addShape(ellipse);
		}
	});
}




