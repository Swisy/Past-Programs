import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.MadlibsController;
import customMap.ListMap;
import model.MadlibsModel;

/**
 * 
 * @author Saul Weintraub
 *
 * This class is the view for the Madlibs game. This class will read in all the possible templates
 * found in templates.txt and will randomly select one to use. This class will read in all the words
 * and parts of speech from parts_of_speech.txt and will load them into a ListMap map. The user will
 * be asked to enter words of certain parts of speech that will be used to fill the template. After
 * all the words have been entered, the filled template will be printed out and the user will be asked
 * if they want to play again.
 * 
 */
public class Madlibs {

	public static void main(String[] args) throws FileNotFoundException {
		ListMap<String, String> words = readWords();
		
		Scanner scanner = new Scanner(System.in);
		String play = "yes";
		while(play.equals("yes")) {
			String template = chooseTemplate();
			ListMap<Integer, String[]> templateMap = mapTemplate(template);
			MadlibsModel model = new MadlibsModel(template, words, templateMap);
			MadlibsController controller = new MadlibsController(model);
			oneGame(scanner, controller);
			play = scanner.nextLine();
		}
		scanner.close();
	}
	
	/**
	 * This method will run a single game of Madlibs.
	 * 
	 * This method will ask the user to enter words of certain parts of speech until the user
	 * enters all the words that are blank in the template. If the user enters an invalid word
	 * they will be asked again to enter a valid word. After all the words have been entered, the
	 * controller will fill the template and this method will print it out.
	 * 
	 * @param scanner The scanner object that is used to get user inputs
	 * @param controller The controller that interacts with the view and model to execute the game
	 */
	private static void oneGame(Scanner scanner, MadlibsController controller) {
		ListMap<String, String> partsOfSpeech = createPartOfSpeechMap();
		String[] needed = controller.getNeededTypes();
		
		for(int i = 0; i < needed.length;) {
			System.out.print("Enter a(n) " + partsOfSpeech.get(needed[i]) + ": ");
			String input = scanner.nextLine();
			if(controller.isCorrectPartOfSpeech(needed[i], input)) {
				input = "(" + input.toUpperCase() + ")";
				controller.updateTemplateMapping(i, input);
				i++;
			}
			else {
				System.out.println("The entered word is not a valid option. Please try again.\n");
			}
		}
		System.out.println();
		
		String filledTemplate = controller.getFilledTemplate();
		System.out.println(filledTemplate);
		
		System.out.println();
		
		System.out.print("Puzzle complete! Would you like to play again?");
		
	}
	
	/**
	 * This method will assemble a ListMap of the words and parts of speeches found in
	 * parts_of_speech.txt.
	 * 
	 * This method will read each line of parts_of_speech.txt and add each word to the Map. If the
	 * word has more than one parts of speech, the method will update the word's entry in the map
	 * to contain all the word's parts of speech. 
	 * 
	 * @return A map of all the valid words and their parts of speech
	 * @throws FileNotFoundException If the parts_of_speech.txt file is not found
	 */
	private static ListMap<String, String> readWords() throws FileNotFoundException{
		File wordsFile = new File("parts_of_speech.txt");
		Scanner wordsScanner = new Scanner(wordsFile);
		ListMap<String, String> words = new ListMap<String, String>();
		
		// Read in every line of the parts of speech file
		while(wordsScanner.hasNextLine()) {
			String line = wordsScanner.nextLine();
			line = line.replace("\n", "");
			String[] splitLine = line.split("\\s+");
			String word = splitLine[0];
			String partOfSpeech = "(" + splitLine[1] + ")";
			
			// If the word doesn't already have a mapping, add it to the map
			if(!words.containsKey(word)) {
				words.put(word, partOfSpeech);
			}
			// If the word does have a mapping, update the value of the mapping
			else {
				partOfSpeech = words.get(word) + " " + partOfSpeech;
				words.put(word, partOfSpeech);
			}
		}
		wordsScanner.close();
		
		return words;
	}
	
	/**
	 * This method will randomly choose a template to use for the game.
	 * 
	 * This method will read in every template in templates.txt into an array and will randomly
	 * choose an index of the array to use as the template for the game.
	 * 
	 * @return The randomly chosen template
	 * @throws FileNotFoundException If templates.txt is not found
	 */
	private static String chooseTemplate() throws FileNotFoundException {
		// Read all the possible templates into an array list
		File templateFile = new File("templates.txt");
		Scanner templateScanner = new Scanner(templateFile);
		ArrayList<String> templateList = new ArrayList<String>();
		while(templateScanner.hasNextLine()) {
			String line = templateScanner.nextLine();
			line = line.replace("\n", "");
			templateList.add(line);
		}
		templateScanner.close();
		
		// Randomly select one of the templates in the array list
		int size = templateList.size();
		Random random = new Random();
		int index = random.nextInt(size);
		
		String template = templateList.get(index);
		
		// Return the randomly selected template
		return template;
	}
	
	/**
	 * This method will create a map that will be used to fill the blank spaces in the template.
	 * 
	 * This method will use a Pattern to find all the places in the template that need to be filled
	 * and will create a map of indexes to an array that contains the part of speech that the space
	 * needs to be filled with and the word that will fill that space. The words part of the arrays
	 * will be left blank for now.
	 * 
	 * @param template The current template for the game that needs to be filled.
	 * @return A map that will be used to fille the blank spaces in the template.
	 */
	private static ListMap<Integer, String[]> mapTemplate(String template){
		ListMap<Integer, String[]> templateMap = new ListMap<Integer, String[]>();
		
		Pattern p = Pattern.compile("\\([A-Z]+\\)");
		Matcher m = p.matcher(template);
		int i = 0;
		String out = "";
		while(m.find()) {
			String[] stringArray = {"", ""};
			stringArray[0] = m.group();
			templateMap.put(i, stringArray);
			out = m.replaceFirst("counted");
			i++;
			m = p.matcher(out);
		}
		
		return templateMap;
	}
	
	/**
	 * This method will create a Map of the abbreviations of the parts of speech to the whole parts
	 * of speech that will be printed out when the user is asked for words.
	 * 
	 * @return A map that contains all the valid parts of speeches
	 */
	private static ListMap<String, String> createPartOfSpeechMap(){
		ListMap<String, String> partsOfSpeech = new ListMap<String, String>();
		
		partsOfSpeech.put("(ADJ)", "Adjective");
		partsOfSpeech.put("(N)", "Noun");
		partsOfSpeech.put("(PLN)", "Plural Noun");
		partsOfSpeech.put("(GER)", "Verb Ending in 'ing'");
		partsOfSpeech.put("(VPT)", "Verb Past Tense");
		partsOfSpeech.put("(V)", "Verb");
		partsOfSpeech.put("(PN)", "Proper Noun");
		partsOfSpeech.put("(PPN)", "Plural Proper Noun");
		partsOfSpeech.put("(AA)", "Article Adjective");
		partsOfSpeech.put("(NUM)", "Number");
		
		return partsOfSpeech;
	}
}
