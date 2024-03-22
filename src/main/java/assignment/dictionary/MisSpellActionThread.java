package assignment.dictionary;

import java.io.*;
import java.util.*;
import javafx.application.Platform;

/**
 * A Thread that contains the application we are going to animate
 *
 */

public class MisSpellActionThread implements Runnable {

    DictionaryController controller;
    private final String textFileName;
    private final String dictionaryFileName;

    private LinesToDisplay myLines;
    private DictionaryInterface<String, String> myDictionary;
    private boolean dictionaryLoaded;

    /**
     * Constructor for objects of class MisspellActionThread
     *
     * @param controller
     */
    public MisSpellActionThread(DictionaryController controller) {
        super();

        this.controller = controller;
        textFileName = "src/main/resources/assignment/dictionary/check.txt";
        dictionaryFileName = "src/main/resources/assignment/dictionary/sampleDictionary.txt";

        myDictionary = new HashedMapAdaptor<String, String>();
        myLines = new LinesToDisplay();
        dictionaryLoaded = false;

    }

    @Override
    public void run() {

        loadDictionary(dictionaryFileName, myDictionary);


        Platform.runLater(() -> {
            if (dictionaryLoaded) {
                controller.SetMsg("The Dictionary has been loaded");
            } else {
                controller.SetMsg("No Dictionary is loaded");
            }
        });

        checkWords(textFileName, myDictionary);

    }

    /**
     * Load the words into the dictionary.
     *
     * @param theFileName The name of the file holding the words to put in the
     * dictionary.
     * @param theDictionary The dictionary to load.
     */
    public void loadDictionary(String theFileName, DictionaryInterface<String, String> theDictionary) {
        Scanner input;
        try {
            File file = new File(theFileName);
            input = new Scanner(file);
            while(input.hasNextLine()){
                String line = input.nextLine();
                String[] segments = line.split("\\s+", 2);//split the line into two segments
                if(segments.length == 2){
                    String key = segments[0];//first segment becomes the key
                    String value = segments[1];//second segment becomes the value
                    theDictionary.add(key, value);//needs to use put to insert the key and value into the hash
                }
            }
            input.close();
            dictionaryLoaded = true;
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: " + theFileName);
            System.out.println(e.getMessage());
        }

    }

    /**
     * Get the words to check, check them, then put Wordlets into myLines. When
     * a single line has been read do an animation step to wait for the user.
     *
     */
    public void checkWords(String theFileName, DictionaryInterface<String, String> theDictionary) {
        Scanner input;
        try {
            File file = new File(theFileName); //sets a file to the file given
            input = new Scanner(file); //makes the input a scanner with a file
            while(input.hasNextLine()){ //while there are lines in the file
                String line = input.nextLine();
                String[] words = line.split("//s+");//split the string into wordlets
                for(String word : words){//for word in the string
                    boolean isCorrect = checkWord(word, theDictionary);
                    Wordlet wordlet = new Wordlet(word, isCorrect);
                    myLines.addWordlet(wordlet);
                }
                showLines(myLines);//showing the lines that were checked
                myLines.nextLine();//goes to the next line in the for loop
            }
            input.close();

        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: " + theFileName);
            System.out.println(e.getMessage());
        }

    }

    /**
     * Check the spelling of a single word.
     *
     */
    public boolean checkWord(String word, DictionaryInterface<String, String> theDictionary) {
        boolean result = false;

        result = theDictionary.contains(word);

        return result;

    }

    private void showLines(LinesToDisplay lines) {
        try {
            Thread.sleep(500);
            Platform.runLater(() -> {
                if (myLines != null) {
                    controller.UpdateView(lines);
                }
            });
        } catch (InterruptedException ex) {
        }
    }

} // end class MisspellActionThread

