package cs424.dataprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class LexicalProcessing {

	/**
	 * LexicalProcessing class 
	 * - Uses wordnet to identify all noun word forms of a list of specified keywords
	 * - find and replace all occurrences of these words with keywords
	 * - case insensitive
	 * - trim leading and trailing spaces
	 * - normalize	character accents with equivalent ASCII characters
	 * - exclude lines which do not have any keywords in them from the output csv file 
	 * 
	 */
	
	private String OUTPUT_FILE = "/home/pavan/software/data/Proj4/MC1/microblogs_wordnet.csv";
	private String INPUT_FILE = "/home/pavan/software/data/Proj4/MC1/microblogs_spellchecked.csv";
	private String WORDNET_DIR = "/home/pavan/software/data/Proj4/MC1/wordnet/wordnet-3.0";
	
	public static void main(String[] args) {

		LexicalProcessing ac = new LexicalProcessing();
		ac.synonymCheck();
	}
	
	/*utility to pre-process a string - trim leading and trailing spaces, normalize and
	characters accents with equivalent ASCII characters*/
	private static String stringConditioning(String string){
		
		string = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").trim();
		return string;
	}
	
	public void synonymCheck(){
		
		ArrayList<String> tempwords;
		ArrayList<ArrayList<String>> WordForms = new ArrayList<ArrayList<String>>();		
		ConcurrentHashMap<String,ArrayList<String>> words = new ConcurrentHashMap<String,ArrayList<String>>();
		
		FileWriter fstream;
		BufferedWriter out;
		String line_out = ""; 
		boolean includeLine = false;
		 
		try{
			fstream = new FileWriter(OUTPUT_FILE);		  	
			out = new BufferedWriter(fstream);		    	
		  	BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE));
			
			String wordNetDirectory = WORDNET_DIR;
			System.setProperty("wordnet.database.dir", wordNetDirectory);
			WordNetDatabase database = WordNetDatabase.getFileInstance();
						
			//list of keywords
			words.put("fever",new ArrayList<String>());
			words.put("chills", new ArrayList<String>());
			words.put("pain", new ArrayList<String>());
			words.put("flu", new ArrayList<String>());
			words.put("sweat", new ArrayList<String>());
			words.put("ache", new ArrayList<String>());
			words.put("fatigue", new ArrayList<String>());
			words.put("cough", new ArrayList<String>());
			words.put("breath", new ArrayList<String>());
			words.put("difficulty", new ArrayList<String>());
			words.put("chills", new ArrayList<String>());
			words.put("nausea", new ArrayList<String>());
			words.put("vomit", new ArrayList<String>());
			words.put("diarrhea", new ArrayList<String>());
			words.put("enlarged", new ArrayList<String>());
			words.put("lymph", new ArrayList<String>());
			words.put("sick", new ArrayList<String>());
			words.put("kill", new ArrayList<String>());
			words.put("headache", new ArrayList<String>());
			words.put("die", new ArrayList<String>());
			words.put("hurts", new ArrayList<String>());
			words.put("stomach", new ArrayList<String>());
			words.put("pneumonia", new ArrayList<String>());
			words.put("crisis", new ArrayList<String>());
			words.put("disaster", new ArrayList<String>());
			words.put("fire", new ArrayList<String>());
			words.put("hurricane", new ArrayList<String>());
			words.put("accident", new ArrayList<String>());
			words.put("Blood", new ArrayList<String>());
		
			Iterator it = words.entrySet().iterator();
			while (it.hasNext()) {
				 Map.Entry pairs = (Map.Entry)it.next();

				 it.remove(); // avoids a ConcurrentModificationException
				 tempwords = new ArrayList<String>();
				 
				 Synset[] synsets = database.getSynsets(stringConditioning(pairs.getKey().toString()), SynsetType.NOUN);
				 
				 System.out.println(pairs.getKey());
				 
				 tempwords = addWithoutDuplicates(tempwords, synsets);						
				 tempwords.add(pairs.getKey().toString());
				 words.put(pairs.getKey().toString(), tempwords);		        
			}
			
			while (true) {
				
		        String line_input = in.readLine();
		        int comma = 0;
		        int count = 0;		        
		        String pre = "";
		        String line = "";
		        
		        if (line_input == null || line_input.length() == -1)
		            break;
		        
		        for(int i =0;i<3;i++){
		        	
		        	comma = line_input.indexOf(",");		      	
		        	pre = pre + line_input.substring(0, comma + 1);
		        	line_input = line_input.substring(comma + 1);
		        	
		        	count++;
		        }
		        
		        line = line_input.toLowerCase();
		        line_out = line_input.toLowerCase();
	
		        if (line == null || line.length() == -1)
		          break;
	
		        includeLine = false;
		        
		        Iterator it1 = words.entrySet().iterator();
			    while (it1.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it1.next();

			        for(String wrd : (ArrayList<String>)pairs.getValue()){

			        	if(line.contains(wrd)){
			        		
			        		System.out.println("Replacing " +  wrd + " with " + pairs.getKey().toString()) ;
			        		
			        		includeLine = true;
			        		line_out = line.replaceAll(wrd, pairs.getKey().toString());
			        	}
			        	
			        }
			    }
		        	        
		        if(includeLine){
		        	
		        	System.out.println("Old : " + line);
			        System.out.println("new : " + line_out);
			        
		        	out.write(pre + line_out);
		        	out.newLine();
		        }
		        
		    }
		   
		    out.close();
		    System.out.println("Finally... complete");
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	//Utility to add strings to an ArrayList without duplicating strings
	private static ArrayList<String> addWithoutDuplicates(List<String> base, Synset[] synsets){
			
		for (int i = 0; i < synsets.length; i++) {
				
			String[] wordForms = synsets[i].getWordForms();	
				
			for (int index = 0; index < wordForms.length; index++){
				//System.out.println("----" + wordForms[index]);
				String localString = stringConditioning(wordForms[index]);
				
				if(!base.contains(localString)){
					base.add(localString);
				}				
			}	
		}
		return (ArrayList<String>)base;
	}
}
