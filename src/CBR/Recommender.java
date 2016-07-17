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
import de.dfki.mycbr.core.model.BooleanDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.FloatDesc;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.io.CSVImporter;
import de.dfki.mycbr.util.Pair;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author the cbr team
 */
public class Recommender {

	public CBREngine engine;
	public Project rec;
	public DefaultCaseBase cb;
	public Concept myConcept;

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

	// Acrescente à lista de argumentos, os atributos que recebeu do utilizador
	// e vai usar no Retrieval
	public String solveOuery(Integer age, Integer gender, Integer height, Integer weight, Integer numberofcases)
			throws ParseException {
		String answer = "";
		String defaultSelection = "_unknown_"; // _unknown_ or _undefined_

		// create a new retrieval
		Retrieval ret = new Retrieval(myConcept, cb);

		// specify the retrieval method
		ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

		// create a query instance
		Instance query = ret.getQueryInstance();

		/*
		 * //Atributo String/Symbol COLOR SymbolDesc colorDesc = (SymbolDesc)
		 * myConcept.getAllAttributeDescs().get("Color"); if (color != null &&
		 * color != "") { query.addAttribute(colorDesc,
		 * colorDesc.getAttribute(color)); } else {
		 * query.addAttribute(colorDesc,
		 * colorDesc.getAttribute(defaultSelection)); }
		 */

		/**
		 * Value conversion to keep data set units consistent
		 */
		/*
		 * Integer heightMM = height * 1000; //converts from meter to millimeter
		 * Integer weightHG = weight * 10; //converts kilogram to hectogram
		 */

		// Atributo Integer Age
		IntegerDesc ageDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("AGE-ANSUR88");
		query.addAttribute(ageDesc, ageDesc.getIntegerAttribute(age));

		/*
		 * BooleanDesc genderDesc = (BooleanDesc)
		 * myConcept.getAllAttributeDescs().get("GENDER ");
		 * query.addAttribute(genderDesc,
		 * genderDesc.getBooleanAttribute(gender));
		 */

		IntegerDesc genderDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("GENDER");
		query.addAttribute(genderDesc, genderDesc.getIntegerAttribute(gender)); // 1
																				// =
																				// male,
																				// 0
																				// =
																				// female

		// Atributo Integer Height
		IntegerDesc heightDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("STATURE");
		query.addAttribute(heightDesc, heightDesc.getIntegerAttribute(height));

		// Atributo Integer Weight
		IntegerDesc weightDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("WEIGHT");
		query.addAttribute(weightDesc, weightDesc.getIntegerAttribute(weight));

		/**
		 * 
		 * not the best place for the following code, duplicating vars... does
		 * not work if put in cbrengine, why?
		 */

		// set path to myCBR projects
		String data_path = "E:\\Documents\\myCBRProjects\\GROW\\Clients\\"; // coloque
																			// o
																			// caminho
																			// do
																			// projecto
																			// cars.prj
		// name of the central concept
		String conceptName = "Client_men"; // altere se necessário

		String csv2 = "exercises_men.csv"; // altere se necessário
		// name of the csv containing the instances
		String columnseparator = ";";
		String multiplevalueseparator = ",";
		// Initialize CSV Import
		CSVImporter csvImporter = new CSVImporter(data_path + csv2, rec.getConceptByID(conceptName));

		/*
		 * Import data from clients_men.csv
		 * 
		 */
		// set the separators that are used in the csv file
		csvImporter.setSeparator(columnseparator); // column separator
		csvImporter.setSeparatorMultiple(multiplevalueseparator); // multiple
																	// value
																	// separator
		// prepare for import
		csvImporter.readData();
		csvImporter.checkData();
		csvImporter.addMissingValues();
		csvImporter.addMissingDescriptions();
		// do the import of the instances
		csvImporter.doImport();

		// wait until the import is done
		System.out.println("Importing " + csv2);
		while (csvImporter.isImporting()) {
			// Thread.sleep(1000);
			// System.out.print(".");
		}

		String[] headers = csvImporter.getHeader();

		ArrayList<String[]> workoutTable = csvImporter.getData();
		workoutTable.add(0, headers);

		/*
		 * for (String[] arr : workoutTable) {
		 * System.out.println(Arrays.toString(arr)); }
		 */

		/*
		 * //Atributo String/Symbol MANUFACTURER SymbolDesc ManufDesc =
		 * (SymbolDesc) myConcept.getAllAttributeDescs().get("Manufacturer"); if
		 * (manufacturer != null && manufacturer != "") {
		 * query.addAttribute(ManufDesc, ManufDesc.getAttribute(manufacturer));
		 * } else { query.addAttribute(ManufDesc,
		 * ManufDesc.getAttribute(defaultSelection)); }
		 */

		// FAZER O query.addAttribute para os restantes atributos que quiser

		// perform retrieval
		ret.start();
		ArrayList<Hashtable<String, String>> liste = null;

		// get the retrieval result
		List<Pair<Instance, Similarity>> result = ret.getResult();

		// ArrayList<String[]> workoutTable = engine.getWorkoutTable();

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
			answer = answer + "<br /><br />Os  " + numberofcases + " melhores casos são mostrados na tabela: "
					+ "<br /> <br /> <table border=\"1\">";
			/* ArrayList<Hashtable<String, String>> */ liste = new ArrayList<Hashtable<String, String>>();
			// if more case results are requested than we have in our case base
			// at all:
			if (numberofcases >= cb.getCases().size()) {
				numberofcases = cb.getCases().size();
			}

			for (int i = 0; i < numberofcases; i++) {
				liste.add(getAttributes(result.get(i), rec.getConceptByID(engine.getConceptName())));

				int index = liste.get(i).toString().indexOf("WORKOUT=");
				String ab = liste.get(i).toString().substring(index);
				String ac = ab.substring(ab.indexOf("=") + 1, ab.indexOf(","));

				System.out.println("workout: " + ac);

				for (String[] arr : workoutTable) {

					if (arr[0].toString().equals(ac)) {
						workoutPlans.add(arr);
						System.out.println(Arrays.toString(arr));
					}
				}

				System.out.println("liste " + liste.get(i).toString());
				answer = answer + "<tr><td>" + result.get(i).getFirst().getName() + "</td><td>"
						+ liste.get(i).toString() + "</td></tr>";
			}
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
}
