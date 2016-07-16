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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

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
	
	/**
	 * Create the frame.
	 */
	public GROWUI() {
		initComponents();
		remy = new Recommender();
		remy.loadengine();
		
	}
	
	
	private void initComponents(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 626, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblAge = new JLabel("Age");
		lblAge.setBounds(29, 21, 46, 14);
		panel.add(lblAge);
		
		tfAge = new JTextField();
		tfAge.setBounds(29, 46, 108, 20);
		panel.add(tfAge);
		tfAge.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Gender");
		lblNewLabel.setBounds(29, 77, 46, 14);
		panel.add(lblNewLabel);
		
		JComboBox cbGender = new JComboBox();
		cbGender.setModel(new DefaultComboBoxModel(new String[] {"Male", "Female"}));
		cbGender.setBounds(29, 102, 108, 20);
		panel.add(cbGender);
		
		JLabel lblHeight = new JLabel("Height");
		lblHeight.setBounds(29, 128, 46, 14);
		panel.add(lblHeight);
		
		tfHeight = new JTextField();
		tfHeight.setColumns(10);
		tfHeight.setBounds(29, 153, 108, 20);
		panel.add(tfHeight);
		
		JLabel lblWeight = new JLabel("Weight");
		lblWeight.setBounds(29, 184, 46, 14);
		panel.add(lblWeight);
		
		tfWeight = new JTextField();
		tfWeight.setColumns(10);
		tfWeight.setBounds(29, 209, 108, 20);
		panel.add(tfWeight);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(289, 21, 275, 389);
		panel.add(scrollPane);
		
		JEditorPane epQueryResult = new JEditorPane();
		epQueryResult.setContentType("text/html");
		scrollPane.setViewportView(epQueryResult);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<AmalgamationFct> funcs = remy.myConcept.getAvailableAmalgamFcts();
				
				
				for(int i=0;i<funcs.size();i++){
					if((funcs.get(i).getName()).equals("default function"))
						remy.myConcept.setActiveAmalgamFct(funcs.get(i));
				}
				
				/**
				 * Regular information
				 */
				Integer gender;
				Integer age= null;
				Integer height= null;
				Integer weight= null;

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
						
				} catch(NumberFormatException e){
					//missing logger
				}
				
				
				
				try{
					
					/**
					 * GUI OUTPUT
					 */
					
					String similarCases = remy.solveOuery(age,gender,height,weight,5);
					epQueryResult.setText(similarCases);
					
					
					
					
					
					
					
				}catch(Exception e){
					
				}

				
			}
		});
		

		btnSearch.setBounds(48, 430, 89, 23);
		panel.add(btnSearch);
		

	}
	
/*	private ActionListener btnSearchActionListener(){
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<AmalgamationFct> funcs = remy.myConcept.getAvailableAmalgamFcts();
				
				
				for(int i=0;i<funcs.size();i++){
					if((funcs.get(i).getName()).equals("default function"))
						remy.myConcept.setActiveAmalgamFct(funcs.get(i));
				}
				
				*//**
				 * Regular information
				 *//*
				int age;
				boolean gender;
				int height;
				int weight;

				*//**
				 * Measurements
				 *//*
				int rArm;
				int lArm;
				
				
				
				try{
					String similarCases = remy.solveOuery();
					epQueryResult.setText(similarCases);
				}catch(Exception e){
					
				}

				
			}
		};
		return al;
	}*/
}
