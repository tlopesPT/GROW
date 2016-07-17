package GUI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

public class JTableWorkoutPlans extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean DEBUG = false;
	private ArrayList<String[]> workoutPlans;

	public JTableWorkoutPlans(ArrayList<String[]> workoutPlans) {
		super(new GridLayout(1, 0));

		this.workoutPlans = new ArrayList<String[]>(workoutPlans);

		JTable table = new JTable(new MyTableModel());
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);
	}

	class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private String[] columnNames = workoutPlans.get(0); // headers

		private Object[][] data;
		public MyTableModel() {
			fillData();
		}

		public void fillData() {
			workoutPlans.remove(0); // remove headers

			/*
			 * Gets the number of rows in the csv
			 */
			int i = 0;
			for (String[] arr : workoutPlans) {
				i++;
			}

			/*
			 * Gets the number of columns in the csv
			 */
			int j = 0;
			for (String[] arr : workoutPlans) {
				for (String str : arr) {
					j++;
				}
				break;
			}

			/**
			 * Allocating for both rows and columns
			 */
			data = new Object[i][j]; 
			i = 0;
			j = 0;

			/**
			 * Generating the workout plans with checkboxes (do/don't)
			 * the string is the name of the workout
			 */
			for (String[] arr : workoutPlans) {
				j=0;
				for (String str : arr) {
					if (j > 0) {
						data[i][j] = new Boolean(str);
					} else {
						data[i][j] = str;
					}
					j++;
				}
				i++;
			}

		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			if (col < 1) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col + " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}
		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
	}

}
