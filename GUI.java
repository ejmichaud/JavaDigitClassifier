import networkclasses.*;

import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.lang.Integer;

public class GUI extends JFrame {
    private JFrame gui;
    private JPanel mainPanel;
    private JPanel drawingPanel;
    private JButton inputButton;
    private JButton resetButton;
    private JTextField outputField;
    private JLabel outputLabel;

    int x;
    int y;
    double[][] input = new double[28][28];
    int xInput = 0;
    int yInput = 0;
    int prevInputX = 0;
    int prevInputY = 0;
    int rectCount = 0;
    Dimension grid = new Dimension(720, 720);
    Dimension window = new Dimension(1460, 800);
		Network net = new Network("params/parameters9694.txt");

    public GUI()
    {
        showGui();
    }


    public static void main(String[] args)
    {
        GUI show = new GUI();
        show.showMouseListener();
    }


    public void showGui()
    {
        //GUI
        gui = new JFrame("Recognize It!");
        gui.setMinimumSize(window);
        gui.setLayout(new GridLayout(1, 2));

        //Panel for Everything other than Drawing Surface
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new GridLayout(2,1));

        gui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        inputButton = new JButton("Input Digit to Network");
        inputButton.setFont(new Font("Arial", Font.PLAIN, 30));
        inputButton.addActionListener(new inputButtonListener());
        mainPanel.add(inputButton);

        resetButton = new JButton("Reset Grid");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 40));
        resetButton.addActionListener(new resetButtonListener());
        mainPanel.add(resetButton);

        outputLabel = new JLabel("The Computer thinks it is:");
        outputLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        mainPanel.add(outputLabel);

				outputField = new JTextField();
				mainPanel.add(outputField);

        gui.add(mainPanel);
        mainPanel.setVisible(true);
        gui.pack();
        gui.setVisible(true);
    }

    public void showMouseListener()
    {
        drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.addMouseListener(new mouseListener());
        drawingPanel.addMouseMotionListener(new mouseListener());
        DrawPanel panel = new DrawPanel();
        panel.setMinimumSize(grid);
        drawingPanel.add(panel);

        //Make Panel Visisble
        gui.add(drawingPanel);
        gui.setVisible(true);
    }

    public class DrawPanel extends JPanel
    {
        public DrawPanel()
        {
            super();
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(740 , 780));
            System.out.print(getPreferredSize());
        }

        public void paintComponent(Graphics g)
        {
            int width = getWidth();
            int height = getHeight();
            //System.out.println(width + " " + height);

            super.paintComponent(g);
            drawGrid(g);
            drawRect(g);
        }

        public void drawGrid(Graphics g)
        {
            g.setColor(Color.BLACK);

            for (int row = 0; row <= 28; row++)
            {
                g.drawLine(20, 20 + (25 * row), 720, 20 + (25 * row));
            }
            for(int col = 0; col <= 28; col++)
            {
                g.drawLine(20 + (25 * col), 20, 20 + (25 * col), 720);
            }
        }

        public void drawRect(Graphics g)
        {
            for (int i = 0; i <= rectCount; i++)
            {
                for (int xSearch = 0; xSearch < 28; xSearch++)
                {
                    for(int ySearch = 0; ySearch <28; ySearch++)
                    {
                        if(input[xSearch][ySearch] == 1)
                        {
                            //System.out.println(xSearch + " " + ySearch);
                            g.setColor(Color.BLACK);
                            g.fillRect(20 + (25 * xSearch), 20 + (25 * ySearch) , 25, 25);
                        }
                    }
                }
            }
        }
    }

    class mouseListener implements MouseListener, MouseMotionListener
    {
        public void mousePressed(MouseEvent e)
        {

        }

        public void mouseDragged(MouseEvent e)
        {
            x = e.getX();
            y = e.getY();

						xInput = (x - 10) / 25;
						yInput = (y - 25) / 25;

						/*
						input[xInput][yInput] = 1.0;
						input[xInput-1][yInput] = 0.75;
						input[xInput+1][yInput] = 0.75;
						input[xInput][yInput-1] = 0.75;
						input[xInput][yInput+1] = 0.75;
						input[xInput-1][yInput-1] = 0.5;
						input[xInput-1][yInput+1] = 0.5;
						input[xInput+1][yInput-1] = 0.5;
						input[xInput+1][xInput+1] = 0.5;
						*/

            if (input[xInput][yInput] != 1)
            {
                input[xInput][yInput] = 1;
								input[xInput-1][yInput] = 1;
								input[xInput+1][yInput] = 1;
								input[xInput][yInput-1] = 1;
								input[xInput][yInput+1] = 1;
            }


            rectCount++;
            drawingPanel.repaint();
            prevInputX = xInput;
            prevInputY = yInput;
        }

        public void mouseReleased(MouseEvent e)
        {
            x = e.getX();
            y = e.getY();

						xInput = (x - 10) / 25;
						yInput = (y - 25) / 25;

						/*
						input[xInput][yInput] = 1.0;
						input[xInput-1][yInput] = 0.75;
						input[xInput+1][yInput] = 0.75;
						input[xInput][yInput-1] = 0.75;
						input[xInput][yInput+1] = 0.75;
						input[xInput-1][yInput-1] = 0.5;
						input[xInput-1][yInput+1] = 0.5;
						input[xInput+1][yInput-1] = 0.5;
						input[xInput+1][xInput+1] = 0.5;
						*/


            if (input[xInput][yInput] != 1)
            {
							input[xInput][yInput] = 1.0;
							input[xInput-1][yInput] = 1;
							input[xInput+1][yInput] = 1;
							input[xInput][yInput-1] = 1;
							input[xInput][yInput+1] = 1;

            }

            rectCount++;
            drawingPanel.repaint();
        }

        public void mouseEntered(MouseEvent e)
        {

        }

        public void mouseExited(MouseEvent e)
        {

        }

        public void mouseClicked(MouseEvent e)
        {

        }

        public void mouseMoved(MouseEvent e)
        {

        }
    }

    class inputButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
			Matrix in = new Matrix(28, 28, false);
			for (int i = 0; i < 28; i++) {
				for (int j = 0; j < 28; j++) {
						in.matrix[i][j] = input[i][j];
				}
			}
			System.out.println("X center --> " + Operations.getCenterX(in));
			System.out.println("Y center --> " + Operations.getCenterY(in));
			/*
			System.out.println("Converted to matrix!");
			in.printString();
	Matrix out = net.feedforward(in);
			System.out.println("Fed through network");
			int answer = Operations.argmax(out);
			System.out.println(answer);

			outputField.setText(Integer.toString(answer));
			*/
        }
    }

    class resetButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Reset Button Clicked!");
            for (int i = 0; i < input.length; i++)
            {
                for (int j = 0; j < input[i].length; j++)
                {
                    input[i][j] = 0;
                }
            }
            drawingPanel.repaint();
        }
    }
}
