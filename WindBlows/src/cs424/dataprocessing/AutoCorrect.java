package cs424.dataprocessing;

/*
Jazzy - a Java library for Spell Checking
Copyright (C) 2001 Mindaugas Idzelis
Full text of license can be found in LICENSE.txt

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import sun.security.jgss.TokenTracker;

public class AutoCorrect implements SpellCheckListener {

	//private static String PHONETICS_FILE = "../dict/phonet.en";
	private static String DICT_FILE = "../dict/eng_com.dic";
	private static String OUTPUT_FILE = "/home/pavan/software/data/Proj4/MC1/microblogs_spellchecked.csv";
	private static String INPUT_FILE = "/home/pavan/software/data/Proj4/MC1/microblogs.csv";
	
	FileWriter fstream;
	BufferedWriter out;
	String line_out = ""; 		
	
	private SpellChecker spellCheck = null;
	
	/**
	 * AutoCorrect class
	 * Uses Jazzy-0.5.2 library to perform a spell-check on text and auto-correct
	 * in case of an spelling error/typo by replacing the typo with the first suggestion, if there exists one.
	 */
	public static void main(String[] args) {
		
		 System.out.println("Running spell check against DoubleMeta");
		 new AutoCorrect(null);

		 //System.out.println("\n\nRunning spell check against GenericTransformator");
		 //new SpellCheckExample2("PHONETICS_FILE");

	}
	
	public AutoCorrect(String phoneticFileName) {
	    try {
	    	
	    	fstream = new FileWriter(OUTPUT_FILE);
	  	  	out = new BufferedWriter(fstream);
	    		        
	  	    BufferedReader in = new BufferedReader(new FileReader(INPUT_FILE));
	  	    File phonetic = null;
	  	    
	  	    if (phoneticFileName != null)
	  	    	phonetic = new File(phoneticFileName);

	  	    SpellDictionary dictionary = new SpellDictionaryHashMap(new File(DICT_FILE), phonetic);
	  	    spellCheck = new SpellChecker(dictionary);
	  	    spellCheck.addSpellCheckListener(this);

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
		        
		        line = line_input;
		        line_out = line_input;
	
		        if (line == null || line.length() == -1)
		          break;
	
		        spellCheck.checkSpelling(new StringWordTokenizer(line));
		        
		        System.out.println("Old : " + line);
		        System.out.println("New : " + line_out);
		        
		        out.write(pre + line_out + ",");
		        out.newLine();
	  	    }
	  	    
	      out.close();
	      System.out.println("Finally.. complete");

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	//Event handler - spelling error
	public void spellingError(SpellCheckEvent event) {
		
	    List suggestions = event.getSuggestions();
	    
	    if (suggestions.size() > 0) {
	      
	    	String suggest = "event.getInvalidWord()";
	      
	    	//most commonly used T9 dict typos
	    	if(event.getInvalidWord().toString() == "y" || event.getInvalidWord().toString() == "Y")
	    		suggest = "why";
	    	else if(event.getInvalidWord().toString() == "d" || event.getInvalidWord().toString() == "D")
	    		suggest = "the";
	    	else
	    		suggest = suggestions.get(0).toString();
	      
	    	line_out = line_out.replaceFirst(event.getInvalidWord().toString(), suggest);
	    	
	    	System.out.println("Replacing " + event.getInvalidWord() + " with " + suggest) ;	      
	    }
	}
}
