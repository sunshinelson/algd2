import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.fhnw.algd2.treeeditor.base.Tree;

class TreeEditor extends JFrame {
	private static final long serialVersionUID = -1350806931333831745L;
	Tree<Integer, ?> treeModel = null;
	TreeComponent treeView = null;
	JTextField text;

	TreeEditor(Tree<Integer, ?> t) {
		setTitle("Tree Editor (" + t.getClass().getName() + ")");
		treeModel = t;
		treeView = new TreeComponent(treeModel);
		getContentPane().setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 1));
		text = new JTextField(3);
		panel.add(text);
		text.setMaximumSize(text.getPreferredSize());
		text.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					treeModel.insert(Integer.valueOf(text.getText()), null);
					treeView.repaint();
				}
				catch (Exception ex) {}
				text.setText("");
			}
		});
		JButton b;
		panel.add(b = new JButton("Insert"));
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					treeModel.insert(Integer.valueOf(text.getText()), null);
					treeView.repaint();
				}
				catch (Exception ex) {
					System.out.println(ex);
					ex.printStackTrace();
				}
				text.setText("");
			}
		});
		panel.add(b = new JButton("Remove"));
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (treeView.getSelection() != null) {
					treeModel.remove(treeView.getSelection());
					treeView.repaint();
				} else {
					JOptionPane.showMessageDialog(TreeEditor.this,
							"Please select node you want to remove first", "Removing Nodes",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		panel.add(b = new JButton("toString"));
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(treeModel.toString());
			}
		});
		/*
		 * panel.add(b = new JButton("RotateL")); b.addActionListener( new
		 * ActionListener(){ public void actionPerformed(ActionEvent e){
		 * if(treeView.getSelection() != null){ treeModel.rotate(BinaryTree.LEFT,
		 * treeView.getSelection()); treeView.repaint(); } } } ); panel.add(b = new
		 * JButton("RotateR")); b.addActionListener( new ActionListener(){ public
		 * void actionPerformed(ActionEvent e){ if(treeView.getSelection() != null){
		 * treeModel.rotate(BinaryTree.RIGHT, treeView.getSelection());
		 * treeView.repaint(); } } } );
		 */
		getContentPane().add("Center", treeView);
		getContentPane().add("East", panel);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		text.setFont(treeView.getFont());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("usage: " + TreeEditor.class.getName()
					+ " <tree class>");
			System.exit(1);
		}
		try {
			Constructor[] ctors = Class.forName(args[0]).getDeclaredConstructors();
			Constructor ctor = null;
			// Find default constructor
			for (int i = 0; i < ctors.length; i++) {
				ctor = ctors[i];
				if (ctor.getGenericParameterTypes().length == 0) break;
			}
			Object t = null;
			ctor.setAccessible(true);
			try {
				t = ctor.newInstance();
			}
			catch (InstantiationException e) {
				System.out
						.println("cound not instantiate instance of class " + args[0]);
				System.exit(1);
			}
			catch (IllegalAccessException e) {
				System.out.println("cound not access constructor of class " + args[0]);
				System.exit(1);
			}
			catch (IllegalArgumentException e) {
				System.out
						.println("Mismatch in number of argument parameters of class "
								+ args[0]);
				System.exit(1);
			}
			catch (InvocationTargetException e) {
				System.out.println("Given class is an abstract class: " + args[0]);
			}
			Frame f = new TreeEditor((Tree<Integer, ?>)t);
			f.setSize(400, 300);
			f.setVisible(true);
		}
		catch (SecurityException e) {
			System.out.println("can not access class " + args[0]
					+ " (Security Exception)");
			System.exit(1);
		}
		catch (ClassNotFoundException e) {
			System.out.println("class " + args[0] + " not found");
			System.exit(1);
		}
	}

	private static class TreeComponent extends JComponent {
		private static final long serialVersionUID = -3958752705443429232L;
		static final int MIN_HEIGHT = 3;
		Tree<Integer, ?> treeModel;
		Integer selection = null;
		Font font;

		TreeComponent(Tree<Integer, ?> t) {
			treeModel = t;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int w = getWidth();
					int h = getHeight();
					int dy = h / (1 + Math.max(MIN_HEIGHT, treeModel.height()));
					int y = dy / 2;
					int c = y / 2;
					int x = w / 2;
					int dx = (x - c) / 2;
					Object obj = findNode(treeModel.getRoot(), e.getX(), e.getY(), x, y,
							dx, dy, c);
					if (obj != selection) {
						selection = (Integer)obj;
						repaint();
					}
				}
			});
		}

		Integer getSelection() {
			return selection;
		}

		@Override
		public Font getFont() {
			return font;
		}

		Object findNode(Tree.Node<Integer, ?> t, int mx, int my, int x, int y,
				int dx, int dy, int c) {
			if (t == null) return null;
			else if ((mx - x) * (mx - x) + (my - y) * (my - y) <= 4 * c * c) return t
					.getKey();
			else if (mx < x) return findNode(t.getLeft(), mx, my, x - dx, y + dy,
					dx / 2, dy, c);
			else return findNode(t.getRight(), mx, my, x + dx, y + dy, dx / 2, dy, c);
		}

		void drawTree(Graphics g, Tree.Node<Integer, ?> t, int x, int y, int dx,
				int dy, int c) {
			if (t.getLeft() != null) {
				g.drawLine(x, y, x - dx, y + dy);
				drawTree(g, t.getLeft(), x - dx, y + dy, dx / 2, dy, c);
			}
			if (t.getRight() != null) {
				g.drawLine(x, y, x + dx, y + dy);
				drawTree(g, t.getRight(), x + dx, y + dy, dx / 2, dy, c);
			}
			if (t.getKey() == selection) g.setColor(Color.green);
			else g.setColor(Color.red);
			g.fillOval(x - c, y - c, 2 * c + 1, 2 * c + 1);
			g.setColor(Color.black);
			g.drawOval(x - c, y - c, 2 * c, 2 * c);
			String str = t.getKey().toString();
			FontMetrics m = g.getFontMetrics(font);
			g.drawString(str, x - m.stringWidth(str) / 2, y + m.getAscent() / 2 - 3);
		}

		@Override
		public void paint(Graphics g) {
			int w = getWidth();
			int h = getHeight();
			int dy = h / (1 + Math.max(MIN_HEIGHT, treeModel.height()));
			int y = dy / 2;
			int c = y / 2;
			int x = w / 2;
			int dx = (x - c) / 2;
			g.setFont(font = new Font(null, Font.BOLD, c * 3 / 2));
			if (treeModel.getRoot() != null) drawTree(g, treeModel.getRoot(), x, y,
					dx, dy, c);
		}
	}
}