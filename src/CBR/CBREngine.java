package CBR;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.io.CSVImporter;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *  @author the cbr team 
 */

public class CBREngine {

	// set path to myCBR projects	
	private static String data_path = "E:\\Documents\\myCBRProjects\\GROW\\Clients\\"; 
	// name of the project file
	private static String projectName = "clients_men.prj";
	// name of the central concept 
	private static String conceptName = "Client_men"; 
	// name of the csv containing the instances
	private static String csv = "clients_men.csv"; 
	// name of the csv containing the instances
	private static String csv2 = "exercises_men.csv";
	// name of the csv containing the instances
	private static String columnseparator = ";";
	private static String multiplevalueseparator = ",";
	// name of the case base that should be used; the default name in myCBR is CB_csvImport
	private static String casebase = "CB_csvImport";
	
	private static ArrayList<String[]> workoutTable; 
	
	
	
	// Getter for the Project meta data
	public static String getCaseBase() {
		return casebase;
	}
	

	public static String getDataPath() {
		return data_path;
	}

	public static void setCasebase(String casebase) {
		CBREngine.casebase = casebase;
	}

	public static String getProjectName() {
		return projectName;
	}	

	public static void setProjectName(String projectName) {
		CBREngine.projectName = projectName;
	}

	public static String getConceptName() {
		return conceptName;
	}

	public static void setConceptName(String conceptName) {
		CBREngine.conceptName = conceptName;
	}
	

	public static String getCsv() {
		return csv;
	}

	public static void setCsv(String csv) {
		CBREngine.csv = csv;
	}	
	
	public static String getCsv2() {
		return csv2;
	}

	public static ArrayList<String[]> getWorkoutTable() {
		return workoutTable;
	}

	public static void setWorkoutTable(ArrayList<String[]> workoutTable) {
		CBREngine.workoutTable = workoutTable;
	}
	

	/**
	 * This methods creates a myCBR project and loads the project from a .prj file
	 */	
	public Project createProjectFromPRJ(){

		System.out.println("A carregar o projecto myCBR : "+data_path+ " "+projectName+" "+conceptName+" "); 

		Project project = null;

		try{

			project = new Project(data_path + projectName);		
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}		
			System.out.print("\n");	//console pretty print			
		}
		catch(Exception ex){

			System.out.println("Erro a carregar os ficheiros do Projecto");
		}		
		
		return project;		
	}	

	/**
	 * This methods creates a myCBR project and loads the cases in this project.
	 * The specification of the project's location and according file names has to be
	 * done at the beginning of this class.
	 * @return Project instance containing model, sims and cases (if available)
	 */
	public Project createCBRProject(){

		Project project = null;
		try {
			// load new project
			project = new Project(data_path+projectName);
			// create a concept and get the main concept of the project; 
			// the name has to be specified at the beginning of this class
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");	//console pretty print
			Concept concept = project.getConceptByID(conceptName);

			// Initialize CSV Import  
			CSVImporter csvImporter = new CSVImporter(data_path+csv, concept);
			
			/*
			 * Import data from clients_men.csv
			 * 
			 */
			// set the separators that are used in the csv file
			csvImporter.setSeparator(columnseparator); // column separator
			csvImporter.setSeparatorMultiple(multiplevalueseparator); //multiple value separator
			// prepare for import
			csvImporter.readData();	
			csvImporter.checkData();
			csvImporter.addMissingValues();
			csvImporter.addMissingDescriptions();
			// do the import of the instances 
			csvImporter.doImport();
			

			// wait until the import is done
			System.out.print("Importing "+csv);
			while (csvImporter.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}
			

			System.out.print("\n");	//console pretty print
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		return project;

	}

	/**
	 * This methods creates an EMPTY myCBR project.
	 * The specification of the project's location and according file names has to be
	 * done at the beginning of this class.
	 * @return Project instance containing model, sims and cases (if available)
	 */
	public Project createemptyCBRProject(){

		Project project = null;
		try {
			// load new project
			project = new Project(data_path+projectName);
			// create a concept and get the main concept of the project; 
			// the name has to be specified at the beginning of this class
			while (project.isImporting()){
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");	//console pretty print
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return project;
	}

}