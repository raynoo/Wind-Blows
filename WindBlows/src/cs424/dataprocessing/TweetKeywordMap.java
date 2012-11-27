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

import processing.core.PApplet;

import cs424.windblows.application.Utils;
import cs424.windblows.data.DBFacade;
import cs424.windblows.data.Tweet;
import de.bezier.data.sql.SQLite;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class TweetKeywordMap {

	/**
	 * Generate tweet keyword mappings 
	 * 
	 * 
	 */
	
	/*private String OUTPUT_FILE = "/home/pavan/software/data/Proj4/MC1/microblogs_wordnet.csv";
	private String INPUT_FILE = "/home/pavan/software/data/Proj4/MC1/microblogs_spellchecked.csv";
	private String WORDNET_DIR = "/home/pavan/software/data/Proj4/MC1/wordnet/wordnet-3.0";*/
	
	private String OUTPUT_FILE = "/home/pavan/software/data/Proj4/MC1/output2.csv";
	private String INPUT_FILE = "/home/pavan/software/data/Proj4/MC1/example2.csv";
	private String WORDNET_DIR = "/home/pavan/software/data/Proj4/MC1/wordnet/wordnet-3.0";
	
	private HashMap<Integer, String> keywords;
	
	/*public static void main (String[] args) {

		TweetKeywordMap tm = new TweetKeywordMap();		
		
		tm.Map();
	}*/
	
	/*utility to pre-process a string - trim leading and trailing spaces, normalize and
	characters accents with equivalent ASCII characters*/
	private static String stringConditioning(String string){
		
		string = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").trim();
		return string;
	}
	
	public void Map(){
		
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
					
			keywords = DBFacade.getInstance().getKeywords();
			
			Iterator it = keywords.entrySet().iterator();
			while (it.hasNext()) {
				
				Map.Entry pairs = (Map.Entry)it.next();

				 //it.remove(); // avoids a ConcurrentModificationException
				 
				 words.put((String) pairs.getValue(),new ArrayList<String>());
			}
			
			it = words.entrySet().iterator();
			while (it.hasNext()) {
				 Map.Entry pairs = (Map.Entry)it.next();

				 //it.remove(); // avoids a ConcurrentModificationException
				 tempwords = new ArrayList<String>();
				 
				 Synset[] synsets = database.getSynsets(stringConditioning(pairs.getKey().toString()), SynsetType.NOUN);
				 
				 System.out.println(pairs.getKey());
				 
				 tempwords = addWithoutDuplicates(tempwords, synsets);						
				 tempwords.add(pairs.getKey().toString());
				 words.put(pairs.getKey().toString(), tempwords);		        
			}
			
			int id = 1;
			String keyWord = "";
			
			HashMap<Integer,String> data = DBFacade.getInstance().getAllTweets();
			
			it = data.entrySet().iterator();
			
			String line;
			Integer tweet_id;
			
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();

				//it.remove(); // avoids a ConcurrentModificationException
				 
				line = pairs.getValue().toString().toLowerCase();
				tweet_id = (Integer) pairs.getKey();
				 
				Iterator it1 = words.entrySet().iterator();
				 
				while (it1.hasNext()) {
				    Map.Entry pairs1 = (Map.Entry)it1.next();

				    for(String wrd : (ArrayList<String>)pairs1.getValue()){

				        if(line.contains(wrd)){
				        		
				        	keyWord = pairs1.getKey().toString();
				        	int key = getId(keyWord);
				        	System.out.println("Mapping " +  keyWord + "( " + key + ") + with tweet - " + line) ;
				        		
				        	out.write("insert into TweetCategory values (" + key + "," + tweet_id + ");");						
					       	out.newLine();
				        }
				        	
				    }
			    }
			
			}
		   
		    out.close();
		    System.out.println("Finally... complete");
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private Integer getId(String keyword){
		
		Integer keyId = 0;
		
		Iterator it = keywords.entrySet().iterator();
		while (it.hasNext()) {
			
			Map.Entry pairs = (Map.Entry)it.next();
			 
			 if(pairs.getValue().toString() == keyword){
				 keyId = (Integer) pairs.getKey();
				 break;
			 }				 
		}
		
		return keyId;		
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
