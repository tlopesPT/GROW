package CBR;

import java.text.ParseException;
import java.util.ArrayList;
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
    public Concept myConcept2;

    public void loadengine() {

        engine = new CBREngine();
        rec = engine.createProjectFromPRJ();
        // create case bases and assign the case bases that will be used for submitting a query 
        cb = (DefaultCaseBase) rec.getCaseBases().get(engine.getCaseBase());
        // create a concept and get the main concept of the project; 
        myConcept = rec.getConceptByID(engine.getConceptName());
        
        // create a concept and get the main concept of the project; 
        myConcept2 = rec.getConceptByID(engine.getConceptName2());
    }

    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    //Acrescente à lista de argumentos, os atributos que recebeu do utilizador e vai usar no Retrieval
    public String solveOuery(Integer age,Integer gender,Integer height, Integer weight,Integer numberofcases) throws ParseException {
        String answer = "";
        String defaultSelection = "_unknown_"; // _unknown_ or _undefined_

        // create a new retrieval
        Retrieval ret = new Retrieval(myConcept, cb);
        
        // create a new retrieval
        Retrieval ret2 = new Retrieval(myConcept2, cb);
        
        // specify the retrieval method
        ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
        
        // specify the retrieval method
        ret2.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);
        
	// create a query instance
        Instance query = ret.getQueryInstance();
        
    	// create a query instance
        Instance query2 = ret2.getQueryInstance();
     /*   
        //Atributo String/Symbol COLOR
	SymbolDesc colorDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Color");
        if (color != null && color != "") {
            query.addAttribute(colorDesc, colorDesc.getAttribute(color));
        } else {
            query.addAttribute(colorDesc, colorDesc.getAttribute(defaultSelection));
        }
        */
        
        /**
         * Value conversion to keep data set units consistent
         */
        /*
        Integer heightMM = height * 1000; //converts from meter to millimeter
        Integer weightHG = weight * 10; //converts kilogram to hectogram
        */
        
        //Atributo Integer Age
        IntegerDesc ageDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("AGE-ANSUR88");
        query.addAttribute(ageDesc, ageDesc.getIntegerAttribute(age));
        
        /*
        BooleanDesc genderDesc = (BooleanDesc) myConcept.getAllAttributeDescs().get("GENDER ");
        query.addAttribute(genderDesc, genderDesc.getBooleanAttribute(gender));
        */
        
        
        IntegerDesc genderDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("GENDER");
        query.addAttribute(genderDesc, genderDesc.getIntegerAttribute(gender)); //1 = male, 0 = female
        
        //Atributo Integer Height
        IntegerDesc heightDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("STATURE");
        query.addAttribute(heightDesc, heightDesc.getIntegerAttribute(height));

        //Atributo Integer Weight
        IntegerDesc weightDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("WEIGHT");
        query.addAttribute(weightDesc, weightDesc.getIntegerAttribute(weight));
  
        
        //Atributo String/Symbol MANUFACTURER
        SymbolDesc exercisDesc = (SymbolDesc) myConcept2.getAllAttributeDescs().get("EXERCISE_NAME");

         
        
        
        
        
        /*
        //Atributo String/Symbol MANUFACTURER
	SymbolDesc ManufDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Manufacturer");
        if (manufacturer != null && manufacturer != "") {
            query.addAttribute(ManufDesc, ManufDesc.getAttribute(manufacturer));
        } else {
            query.addAttribute(ManufDesc, ManufDesc.getAttribute(defaultSelection));
        }
         */
        
	//FAZER O query.addAttribute para os restantes atributos que quiser
   
        // perform retrieval
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
            
            answer = "Melhor Caso: " + casename + " com similaridade = " + sim +".";
            answer = answer + "<br /><br />Os  " + numberofcases + " melhores casos são mostrados na tabela: "
                    + "<br /> <br /> <table border=\"1\">";
            /*ArrayList<Hashtable<String, String>>*/ liste = new ArrayList<Hashtable<String, String>>();
            // if more case results are requested than we have in our case base at all:
            if (numberofcases >= cb.getCases().size()) {
                numberofcases = cb.getCases().size();
            }

            for (int i = 0; i < numberofcases; i++) {
                liste.add(getAttributes(result.get(i), rec.getConceptByID(engine.getConceptName())));
                String workout = liste.get(i).toString().substring(0,liste.get(i).toString().indexOf("WORKOUT="));
                System.out.println("liste " + liste.get(i).toString() +"workout: "+workout);
                answer = answer + "<tr><td>" + result.get(i).getFirst().getName() + "</td><td>" + liste.get(i).toString() + "</td></tr>";
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
     * @param r = An Instance.
     * @param concept = A Concept
     * @return List = List containing the Attributes of a case with their
     * values.
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
     * @param r = An Instance.
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

        listoffunctions = listoffunctions + (" <br /> <br /> Currently selected Amalgamationfunction: " + current.getName() + "\n");
        listoffunctions = listoffunctions + (" <br /> <br /> Please type the name of the Amalgamationfunction to use in the "
                + " Field \"Amalgamationfunction\" it will be automatically used during the next retrieval");
        System.out.println(listoffunctions);
        return listoffunctions;
    }
}
