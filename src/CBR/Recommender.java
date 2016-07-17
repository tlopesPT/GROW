package CBR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.io.CSVImporter;
import de.dfki.mycbr.util.Pair;

/**
 * @author the cbr team
 */
public class Recommender {

	public CBREngine engine;
	public Project rec;
	public DefaultCaseBase cb;
	public Concept myConcept;

	/**
	 * Used to store the information about the similar cases' workout plans
	 */
	private ArrayList<String[]> workoutPlans;

	public void loadengine() {

		engine = new CBREngine();
		rec = engine.createProjectFromPRJ();
		

		// create case bases and assign the case bases that will be used for
		// submitting a query
		cb = (DefaultCaseBase) rec.getCaseBases().get(engine.getCaseBase());
		// create a concept and get the main concept of the project;
		myConcept = rec.getConceptByID(engine.getConceptName());
		
		workoutPlans = new ArrayList<String[]>();
		
	}

	public boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {
		}
		return false;
	}


	/**
	 * Query to obtain the similar cases (Retrieval)
	 * 
	 * @param age
	 * @param gender
	 * @param height
	 * @param weight
	 * @param shoulders
	 * @param chest
	 * @param waist
	 * @param arms
	 * @param legs
	 * @param numberofcases
	 * @return String with html creating a table with the similar cases
	 * @throws ParseException
	 */
	public String solveOuery(Integer age, Integer gender, Integer height, Integer weight,
			Integer shoulders, Integer chest, Integer waist, Integer arms, Integer legs, Integer numberofcases)
			throws ParseException {
		String answer = "";
		String defaultSelection = "_unknown_"; // _unknown_ or _undefined_

		// create a new retrieval
		Retrieval ret = new Retrieval(myConcept, cb);

		// specify the retrieval method
		ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

		// create a query instance
		Instance query = ret.getQueryInstance();

		IntegerDesc ageDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("AGE-ANSUR88");
		query.addAttribute(ageDesc, ageDesc.getIntegerAttribute(age));

		IntegerDesc genderDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("GENDER");
		query.addAttribute(genderDesc, genderDesc.getIntegerAttribute(gender));

		IntegerDesc heightDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("STATURE");
		query.addAttribute(heightDesc, heightDesc.getIntegerAttribute(height));

		IntegerDesc weightDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("WEIGHT");
		query.addAttribute(weightDesc, weightDesc.getIntegerAttribute(weight));
		
		IntegerDesc shouldersDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("SHOULDER_CIRC");
		query.addAttribute(shouldersDesc, shouldersDesc.getIntegerAttribute(shoulders));
		
		IntegerDesc chestDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("CHEST_CIRC");
		query.addAttribute(chestDesc, chestDesc.getIntegerAttribute(chest));
		
		IntegerDesc waistDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("WAIST_CIRC_NATURAL");
		query.addAttribute(waistDesc, waistDesc.getIntegerAttribute(waist));
	
		IntegerDesc armsDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("ARMCIRCBCPS_FLEX");
		query.addAttribute(armsDesc, armsDesc.getIntegerAttribute(arms));
		
		IntegerDesc legsDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("THIGH_CIRC-PROXIMAL");
		query.addAttribute(legsDesc, legsDesc.getIntegerAttribute(legs));
		

		String columnseparator = ";";
		String multiplevalueseparator = ",";
		// Initialize CSV Import
		CSVImporter csvImporter = new CSVImporter(engine.getDataPath() + engine.getCsv2(), rec.getConceptByID(engine.getConceptName()));

		/*
		 * Import data from clients_men.csv
		 * 
		 */
		csvImporter.setSeparator(columnseparator);
		csvImporter.setSeparatorMultiple(multiplevalueseparator); 
		// prepare for import
		csvImporter.readData();
		csvImporter.checkData();
		csvImporter.addMissingValues();
		csvImporter.addMissingDescriptions();
		// do the import of the instances
		csvImporter.doImport();

		// wait until the import is done
		while (csvImporter.isImporting()) {
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 System.out.print(".");
		}

		/**
		 * Gets table headers (exercise names)
		 */
		String[] headers = csvImporter.getHeader();

		/**
		 * Gets table data (workout booleans)
		 */
		ArrayList<String[]> workoutTable = csvImporter.getData();
		workoutTable.add(0, headers);


		/**
		 * Perform retrieval
		 */
		ret.start();
		ArrayList<Hashtable<String, String>> liste = null;

		// get the retrieval result
		List<Pair<Instance, Similarity>> result = ret.getResult();


		// get the case name
		if (!result.isEmpty()) {
			// get the best case's name
			String casename = result.get(0).getFirst().getName();
			// get the similarity value
			Double sim = result.get(0).getSecond().getValue();

			/**
			 * Gets the data in the csv exercises_men
			 */

			answer = "Melhor Caso: " + casename + " com similaridade = " + sim + ".";
			answer = answer + "<br /><br />Os  " + numberofcases + " melhores casos sao mostrados na tabela: "
					+ "<br /> <br /> <table border=\"1\">";
			liste = new ArrayList<Hashtable<String, String>>();
			
			// if more case results are requested than we have in our case base
			// at all:
			if (numberofcases >= cb.getCases().size()) {
				numberofcases = cb.getCases().size();
			}

			/**
			 * Saves the names of the workouts from similar cases
			 */
			ArrayList<String> workoutMatches = new ArrayList<String>();

			/*
			 * Get the number of rows in exercises_men.csv
			 */
			int rows = 0;
			for (String[] arr : workoutTable) {
				rows++;
			}

			for (int i = 0; i < numberofcases; i++) {
				liste.add(getAttributes(result.get(i), rec.getConceptByID(engine.getConceptName())));

				/**
				 * Gets the workout ID out of the string.
				 */
				int index = liste.get(i).toString().indexOf("WORKOUT=");
				String ab = liste.get(i).toString().substring(index);
				String ac = ab.substring(ab.indexOf("=") + 1, ab.indexOf(","));


				/*
				 * Tries to find similar cases with different workout plans
				 * Not interested in having duplicated workouts
				 */
				if (!workoutMatches.contains(ac)) {
					workoutMatches.add(ac);

					// System.out.println("liste " + liste.get(i).toString());
					answer = answer + "<tr><td>" + result.get(i).getFirst().getName() + "</td><td>"
							+ liste.get(i).toString() + "</td></tr>";
				}
			}

			workoutPlans.add(headers);
			
			for(String s : workoutMatches){
				for(String[] arr : workoutTable){
					if(s.equals(arr[0])){
						workoutPlans.add(arr);
					}					
				}		
			}
			
/*
 * 			
 * 		DEBUG PRINTS
 * 
 * 
 * 
			  System.out.println("___________________ workouttable_______________"); 
			  
			  for (String[] arr : workoutTable) {
			  System.out.println(Arrays.toString(arr)); 
			  } 
			  System.out.println("___________________ workout matches_______________"); 
			  
			  for (String arr : workoutMatches) {
			  System.out.println(arr); 
			  } 
			  System.out.println("___________________ workoutPlans_______________"); 
			  
			  for (String[] arr : workoutPlans) {
			  System.out.println(Arrays.toString(arr)); 
			  } 
			*/

			
			// int exerciseName = 1;
			/*
			 * Read the csv vertically
			 */
			/*
			 * for (String[] arr : workoutTable) {
			 * System.out.println(Arrays.toString(arr)); }
			 */
			/*
			 * int j = 0;
			 * 
			 * Get the headers
			 * 
			 * for (String s : workoutTable.get(0)) { for (String s1 :
			 * workoutMatches) { int col1 = 0;
			 * 
			 * 
			 * Workout name matches
			 * 
			 * 
			 * if (s1.equals(s)) { String[] tmp1 = new
			 * String[workoutMatches.size() + 1]; String[] tmpArr1 =
			 * workoutTable.get(exerciseName); tmp1[0] = tmpArr1[0];
			 * 
			 * String[] tmp = new String[rows]; int col = 0;
			 * 
			 * for (String[] arr : workoutTable) { if (col < 1) { String[]
			 * tmpArr = workoutTable.get(exerciseName); System.out.println(
			 * "Exercise Index: " + exerciseName); tmp[col] = tmpArr[0]; // name
			 * of the // exercise exerciseName++; } else { tmp[col] = arr[j]; }
			 * col++; } workoutPlans.add(tmp); System.out.println(
			 * "PRINTING WORKOUT ADDED:");
			 * System.out.println(Arrays.toString(tmp)); break; } j++; }
			 * 
			 * }
			 * 
			 * System.out.println("PRINTING ACTUAL WORKOUT PLANS FOUND:"); for
			 * (String[] arr : workoutPlans) {
			 * System.out.println(Arrays.toString(arr)); }
			 * 
			 * 
			 * 
			 * for (String[] arr : workoutTable) {
			 * 
			 * if (arr[0].toString().equals(ac)) { workoutPlans.add(arr);
			 * System.out.println(Arrays.toString(arr)); } }
			 * 
			 * 
			 * 
			 */
		}

		answer = answer + "</table>";

		return answer;

	}

	/**
	 * This method delivers a Hashtable which contains the Attributs names
	 * (Attributes of the case) combined with their respective values.
	 *
	 * @author weber,koehler,namuth
	 * @param r
	 *            = An Instance.
	 * @param concept
	 *            = A Concept
	 * @return List = List containing the Attributes of a case with their
	 *         values.
	 */
	public static Hashtable<String, String> getAttributes(Pair<Instance, Similarity> r, Concept concept) {

		Hashtable<String, String> table = new Hashtable<String, String>();
		ArrayList<String> cats = getCategories(r);
		// Add the similarity of the case
		table.put("Sim", String.valueOf(r.getSecond().getValue()));
		for (String cat : cats) {
			// Add the Attribute name and its value into the Hashtable
			table.put(cat, r.getFirst().getAttForDesc(concept.getAllAttributeDescs().get(cat)).getValueAsString());
		}
		return table;
	}

	/**
	 * This Method generates an ArrayList, which contains all Categories of aa
	 * Concept.
	 *
	 * @author weber,koehler,namuth
	 * @param r
	 *            = An Instance.
	 * @return List = List containing the Attributes names.
	 */
	public static ArrayList<String> getCategories(Pair<Instance, Similarity> r) {

		ArrayList<String> cats = new ArrayList<String>();

		// Read all Attributes of a Concept
		Set<AttributeDesc> catlist = r.getFirst().getAttributes().keySet();

		for (AttributeDesc cat : catlist) {
			if (cat != null) {
				// Add the String literals for each Attribute into the ArrayList
				cats.add(cat.getName());
			}
		}
		return cats;
	}

	public String displayAmalgamationFunctions() {

		ArrayList<String> amalgam = new ArrayList<String>();
		String listoffunctions = "Currently available Amalgamationfunctions: <br /> <br />";
		AmalgamationFct current = myConcept.getActiveAmalgamFct();
		System.out.println("Amalgamation Function is used = " + current.getName());
		List<AmalgamationFct> liste = myConcept.getAvailableAmalgamFcts();

		for (int i = 0; i < liste.size(); i++) {
			System.out.println(liste.get(i).getName());
			listoffunctions = listoffunctions + liste.get(i).getName() + "<br />";
		}

		listoffunctions = listoffunctions
				+ (" <br /> <br /> Currently selected Amalgamationfunction: " + current.getName() + "\n");
		listoffunctions = listoffunctions
				+ (" <br /> <br /> Please type the name of the Amalgamationfunction to use in the "
						+ " Field \"Amalgamationfunction\" it will be automatically used during the next retrieval");
		System.out.println(listoffunctions);
		return listoffunctions;
	}

	public ArrayList<String[]> getWorkoutPlans() {
		return workoutPlans;
	}
	
	public void cleanUp(){
		workoutPlans.clear();
		
	}
}
