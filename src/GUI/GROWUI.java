package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CBR.Recommender;
import de.dfki.mycbr.core.similarity.AmalgamationFct;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;

public class GROWUI extends JFrame {

	private JPanel contentPane;
	private JTextField tfAge;
	private JTextField tfHeight;
	private JTextField tfWeight;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GROWUI frame = new GROWUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Recommender remy;
	private JTextField tfShoulders;
	private JTextField tfChest;
	private JTextField tfWaist;
	private JTextField tfArms;
	private JTextField tfLegs;
	
	/**
	 * Create the frame.
	 */
	public GROWUI() {
		setTitle("GROW");
		initComponents();
		remy = new Recommender();
		remy.loadengine();
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 614, 509);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Measurements", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblAge = new JLabel("Age");
		lblAge.setBounds(29, 21, 46, 14);
		panel.add(lblAge);
		
		tfAge = new JTextField();
		tfAge.setText("34");
		tfAge.setBounds(29, 46, 122, 28);
		panel.add(tfAge);
		tfAge.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Gender");
		lblNewLabel.setBounds(29, 77, 46, 14);
		panel.add(lblNewLabel);
		
		JComboBox cbGender = new JComboBox();
		cbGender.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female"}));
		cbGender.setBounds(29, 102, 122, 26);
		panel.add(cbGender);
		
		JLabel lblHeight = new JLabel("Height (mm)");
		lblHeight.setBounds(29, 143, 108, 14);
		panel.add(lblHeight);
		
		tfHeight = new JTextField();
		tfHeight.setText("1800");
		tfHeight.setColumns(10);
		tfHeight.setBounds(29, 168, 122, 28);
		panel.add(tfHeight);
		
		JLabel lblWeight = new JLabel("Weight (hg)");
		lblWeight.setBounds(29, 199, 63, 16);
		panel.add(lblWeight);
		
		tfWeight = new JTextField();
		tfWeight.setText("800");
		tfWeight.setColumns(10);
		tfWeight.setBounds(29, 224, 122, 28);
		panel.add(tfWeight);
		
		
		JLabel lblShoulders = new JLabel("Shoulders");
		lblShoulders.setBounds(239, 21, 67, 14);
		panel.add(lblShoulders);
		
		tfShoulders = new JTextField();
		tfShoulders.setText("1200");
		tfShoulders.setColumns(10);
		tfShoulders.setBounds(239, 46, 122, 28);
		panel.add(tfShoulders);
		
		tfChest = new JTextField();
		tfChest.setText("1000");
		tfChest.setColumns(10);
		tfChest.setBounds(239, 102, 122, 28);
		panel.add(tfChest);
		
		tfWaist = new JTextField();
		tfWaist.setText("950");
		tfWaist.setColumns(10);
		tfWaist.setBounds(239, 163, 122, 28);
		panel.add(tfWaist);
		
		JLabel lblChest = new JLabel("Chest");
		lblChest.setBounds(239, 77, 46, 14);
		panel.add(lblChest);
		
		JLabel lblWaist = new JLabel("Waist");
		lblWaist.setBounds(239, 138, 46, 14);
		panel.add(lblWaist);
		
		JLabel lblArms = new JLabel("Arms");
		lblArms.setBounds(239, 199, 67, 14);
		panel.add(lblArms);
		
		tfArms = new JTextField();
		tfArms.setText("350");
		tfArms.setColumns(10);
		tfArms.setBounds(239, 224, 122, 28);
		panel.add(tfArms);
		
		JLabel lblLegs = new JLabel("Legs");
		lblLegs.setBounds(239, 255, 46, 14);
		panel.add(lblLegs);
		
		tfLegs = new JTextField();
		tfLegs.setText("600");
		tfLegs.setColumns(10);
		tfLegs.setBounds(239, 280, 122, 28);
		panel.add(tfLegs);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Similar Cases", null, panel_1, null);
		panel_1.setLayout(null);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(42, 16, 518, 450);
		panel_1.add(scrollPane);
		
		JEditorPane epQueryResult = new JEditorPane();
		scrollPane.setViewportView(epQueryResult);
		epQueryResult.setContentType("text/html");
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("E:\\Documents\\myCBRProjects\\GROW\\images\\chest_br_10th_rib.jpg"));
		lblNewLabel_1.setBounds(367, 21, 200, 403);
		panel.add(lblNewLabel_1);
		
		JComboBox cbAmalgmfct = new JComboBox();
		cbAmalgmfct.setModel(new DefaultComboBoxModel(new String[] {"mass gain", "fat loss"}));
		cbAmalgmfct.setBounds(239, 342, 122, 25);
		panel.add(cbAmalgmfct);
		
		JLabel lblNewLabel_2 = new JLabel("Goal");
		lblNewLabel_2.setBounds(239, 320, 25, 16);
		panel.add(lblNewLabel_2);
		
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<AmalgamationFct> funcs = remy.myConcept.getAvailableAmalgamFcts();
				remy.cleanUp();
				
				for(int i=0;i<funcs.size();i++){
					if((funcs.get(i).getName()).equals("default function")) //default function
						remy.myConcept.setActiveAmalgamFct(funcs.get(i));
				}
				
				/**
				 * Regular information
				 */
				Integer gender;
				Integer age= null;
				Integer height= null;
				Integer weight= null;
				Integer shoulders= null;
				Integer chest= null;
				Integer waist= null;
				Integer arms= null;
				Integer legs= null;


				/**
				 * Measurements
				 */

				
				String sGender = cbGender.getSelectedItem().toString();
				if("Male".equals(sGender)){
					gender = 1;
				} else{
					gender = 0;
				}
				
				try{					
					age = Integer.parseInt(tfAge.getText());
				
					height = Integer.parseInt(tfHeight.getText());
					weight = Integer.parseInt(tfWeight.getText());
					
					
					shoulders = Integer.parseInt(tfShoulders.getText());
					chest = Integer.parseInt(tfChest.getText());
					waist = Integer.parseInt(tfWaist.getText());
					arms = Integer.parseInt(tfArms.getText());
					legs = Integer.parseInt(tfLegs.getText());

						
				} catch(NumberFormatException e){
					//missing logger
				}
				
				
				
				try{
					
					/**
					 * GUI OUTPUT
					 */
					
					List<AmalgamationFct> funcs2 = remy.myConcept.getAvailableAmalgamFcts();
					
					
					for(int i=0;i<funcs2.size();i++){
						if((funcs2.get(i).getName()).equals(cbAmalgmfct.getSelectedItem().toString()))
							remy.myConcept.setActiveAmalgamFct(funcs2.get(i));
					}
					
					String similarCases = remy.solveOuery(age,gender,height,weight,shoulders,chest,waist,arms,legs,5);
					epQueryResult.setText(similarCases);
					
					
					JPanel panel_1 = new JTableWorkoutPlans(remy.getWorkoutPlans());
					tabbedPane.addTab("Workout Plans", null, panel_1, null);
					
			
					/*						
					CSVWriter writer = new CSVWriter(new FileWriter("E:\\Documents\\myCBRProjects\\GROW\\Clients\\exercises_men.csv"), '\t');
				     	
					PrintWriter pw = new PrintWriter(new File("E:\\Documents\\myCBRProjects\\GROW\\Clients\\exercises_men.csv"));
			       
					FileWriter writer = new FileWriter("E:\\Documents\\myCBRProjects\\GROW\\Clients\\exercises_men.csv",true);							
					 */	
					
				}catch(Exception e){
					
				}	
			}		
		});
		

		btnSearch.setBounds(286, 398, 75, 26);
		panel.add(btnSearch);
		

	}
	
}
