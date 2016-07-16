package CBR;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.io.CSVImporter;
import de.dfki.mycbr.core.*;
import de.dfki.mycbr.util.Pair;
import de.dfki.mycbr.io.CSVImporter;

/**
 *  @author the cbr team 
 */

public class CBREngine {

	// set path to myCBR projects	
	private static String data_path = "E:\\Documents\\myCBRProjects\\GROW\\Clients\\"; //coloque o caminho do projecto cars.prj
	// name of the project file
	private static String projectName = "clients_men.prj"; //altere se necessário
	// name of the central concept 
	private static String conceptName = "Client_men"; //altere se necessário
	// name of the central concept 
	private static String conceptName2 = "Exercise_men"; //altere se necessário
	// name of the csv containing the instances
	private static String csv = "clients_men.csv"; //altere se necessário
	// name of the csv containing the instances
	private static String csv2 = "exercise_men.csv"; //altere se necessário
	// set the separators that are used in the csv file
	private static String columnseparator = ";";
	private static String multiplevalueseparator = ",";
	// name of the case base that should be used; the default name in myCBR is CB_csvImport
	private static String casebase = "CB_csvImport"; //altere se necessário
	// Getter for the Project meta data
	public static String getCaseBase() {
		return casebase;
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
	
	public static String getConceptName2() {
		return conceptName2;
	}

	public static void setConceptName2(String conceptName2) {
		CBREngine.conceptName2 = conceptName2;
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

	public static void setCsv2(String csv2) {
		CBREngine.csv2 = csv2;
	}

	/**
	 * This methods creates a myCBR project and loads the project from a .prj file
	 */	
	public Project createProjectFromPRJ(){

		System.out.println("A carregar o projecto myCBR : "+data_path+ " "+projectName+" "+conceptName+" "+conceptName+" "); 

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
			Concept concept2 = project.getConceptByID(conceptName2);
			// Initialize CSV Import  
			CSVImporter csvImporter = new CSVImporter(data_path+csv, concept);
			
			// Initialize CSV Import  
			CSVImporter csvImporter2 = new CSVImporter(data_path+csv, concept2);
			
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
			/*
			 * Import data from exercises_men.csv
			 * 
			 */
			// set the separators that are used in the csv file
			csvImporter2.setSeparator(columnseparator); // column separator
			csvImporter2.setSeparatorMultiple(multiplevalueseparator); //multiple value separator
			// prepare for import
			csvImporter2.readData();	
			csvImporter2.checkData();
			csvImporter2.addMissingValues();
			csvImporter2.addMissingDescriptions();
			// do the import of the instances 
			csvImporter2.doImport();
			// wait until the import is done
			System.out.print("Importing "+csv2);
			while (csvImporter2.isImporting()){
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