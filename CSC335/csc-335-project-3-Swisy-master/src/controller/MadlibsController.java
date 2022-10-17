package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.MadlibsModel;

/**
 * 
 * @author Saul Weintraub
 *
 * This class is the controller of the Madlibs game. This class contains methods that will be used
 * by the view to execute the game.
 */
public class MadlibsController {
	private MadlibsModel model;
	
	/**
	 * This is the constructor for the MadlibsController.
	 * 
	 * @param model The model of the game that contains the information needed
	 */
	public MadlibsController(MadlibsModel model) {
		this.model = model;
	}

	/**
	 * This method will return whether or not the word is the correct part of speech.
	 * 
	 * This method will access the model in order to get the part of speech of the word and will
	 * then compare that part of speech with the passed one.
	 * 
	 * @param partOfSpeech The part of speech. Must be in the same format as shown in parts_of_speech.txt
	 * @param word The word that is being evaluated for it's parts of speech.
	 * @return True if the word is the correct part of speech
	 */
	public boolean isCorrectPartOfSpeech(String partOfSpeech, String word) {
		if(model.getPartOfSpeech(word).contains(partOfSpeech)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * This method will update the mapping of the blank spaces in the template that need to be
	 * filled.
	 * 
	 * This method will update the templateMap Map located in the MadlibsModel.
	 * 
	 * @param index Which blank space the word will fill.
	 * @param word The word that will be filled into the blank space.
	 */
	public void updateTemplateMapping(int index, String word) {
		model.updateTemplateMapping(index, word);
	}
	
	/**
	 * This method will return an array that contains the parts of speech that need to fill the
	 * template.
	 * 
	 * This method will access the templateMap Map located in the MadlibsModel in order to 
	 * construct the String array of needed parts of speech.
	 * 
	 * @return A string array containing the parts of speech needed to fill the template in order
	 */
	public String[] getNeededTypes() {
		int size = model.getTemplateMappingSize();
		String[] needed = new String[size];
		
		for(int i = 0; i < size; i++) {
			needed[i] = model.getSingleTemplateMap(i)[0];
		}
		
		return needed;
	}
	
	/**
	 * This method will replace all the blank spaces in the template with the words chosen by the
	 * user.
	 * 
	 * This method will first replace all the parts of speech in the template with three hashtags
	 * and will then replace every instance of three hashtags with the words that the user chose
	 * to fill the template stored in the templateMap Map located in the MadlibsModel. Then, the
	 * method will return the filled template so it can be printed by the view.
	 * 
	 * @return The filled template 
	 */
	public String getFilledTemplate() {
		String template = model.getTemplate();
		
		// Replace all the parts of speech with "###"
		Pattern p = Pattern.compile("\\([A-Z]+\\)");
		Matcher m = p.matcher(template);
		m.find();
		String replacement = "###";
		template = m.replaceAll(replacement);
		
		// Replace all "###" with the words entered by the user
		p = Pattern.compile("###");
		m = p.matcher(template);
		String filledTemplate = null;
		int i = 0;
		
		while(m.find()) {
			replacement = model.getSingleTemplateMap(i)[1];
			filledTemplate = m.replaceFirst(replacement);
			m = p.matcher(filledTemplate);
			i++;
		}
		
		return filledTemplate;
	}
}
