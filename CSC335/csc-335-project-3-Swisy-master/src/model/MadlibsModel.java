package model;

import customMap.ListMap;

/**
 * 
 * @author Saul Weintraub
 *
 * This class is the model of the Madlibs game. This class will store the template, map of words,
 * and the map of empty spaces in the template to the words that will fill those spaces. This class
 * contains many methods that will be used to get and update the information stored.
 */
public class MadlibsModel {
	// String to store the template
	private String template;
	
	// Map to store the words and parts of speech
	private ListMap<String, String> words;
	
	// Map to store empty spaces to words
	private ListMap<Integer, String[]> templateMap;
	

	/**
	 * This is the constructor for a MadlibsModel object. The constructor requires 3 parameters.
	 * 
	 * @param template The blank template that needs to be filled
	 * @param words The map of words and parts of speech
	 * @param templateMap The map of blank spaces in the template that needs to be filled
	 */
	public MadlibsModel(String template, ListMap<String, String> words, ListMap<Integer, String[]> templateMap) {
		this.template = template;
		this.words = words;
		this.templateMap = templateMap;
	}

	/**
	 * This method will return the part of speech of the parameterized word.
	 * 
	 * This method will access the map of words to parts of speech, using the word as the key, and
	 * will return the value in the map.
	 * 
	 * @param word The word that you want to know the part of speech for
	 * @return Will return "Not Found" if the word is not in the Map, or will return the parts of
	 * 		   speech of the word.
	 */
	public String getPartOfSpeech(String word) {
		if(!words.containsKey(word)) {
			return "Not Found";
		}
		else {
			return words.get(word);
		}
	}
	
	/**
	 * This method will return the stored template.
	 * 
	 * @return The stored template
	 */
	public String getTemplate() {
		return template;
	}
	
	
	/**
	 * This method will update the mapping of the blank spaces in the template that need to be
	 * filled.
	 * 
	 * This method will update the word that will be filled into the template.
	 * 
	 * @param index Which blank space the word will fill.
	 * @param word The word that will be filled into the blank space.
	 */
	public void updateTemplateMapping(int index, String word) {
		String[] oldMapping = templateMap.get(index);
		oldMapping[1] = word;
		templateMap.put(index, oldMapping);
	}
	
	/**
	 * This method will return the amount of words that will be filled into the template.
	 * 
	 * This method will return the size of the templateMap map.
	 * 
	 * @return The amount of words that will be filled into the template.
	 */
	public int getTemplateMappingSize() {
		return templateMap.size();
	}
	
	/**
	 * This method will return the value of the templateMap at the specified index.
	 * 
	 * This method will return the String array that contains the part of speech that needs to be
	 * filled, and the current word that will be used to fill the template at the index.
	 * 
	 * @param index The index of the blank space on the template
	 * @return The string array containing the part of speech, and word that will fill the template
	 */
	public String[] getSingleTemplateMap(int index) {
		return templateMap.get(index);
	}
}
