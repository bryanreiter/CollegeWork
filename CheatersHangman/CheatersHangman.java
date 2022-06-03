import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;


//Worked with Tu Ha
public class CheatersHangman {

    public static void main(String[] args) {
        String answer="";

        //TODO scanner of input from user for word length
        do{
            Scanner wordLengthInput = new Scanner(System.in);
            System.out.println("How many letters would you like? ");
            int key = wordLengthInput.nextInt();

            Scanner attemptsInput = new Scanner(System.in);
            System.out.println("How many guesses would you like? ");
            int attempts = attemptsInput.nextInt();
            List<String> wordList = createWordLists().get(key);
            game(wordList, attempts);


            Scanner playAgain = new Scanner(System.in);
            System.out.println("Would you like to play again? (y/n)");
            answer = playAgain.nextLine();
        }while(answer.equalsIgnoreCase("y"));

    }
    public static void game(List<String> wordList, int guesses){
        Scanner input = new Scanner(System.in);
        Set<Character> usedLetters = new HashSet<>();
        String gameState = "";

        for (int i = 0; i < wordList.get(0).length(); i++) {
            gameState+="_";
        }

        while(guesses>0){
            System.out.println("Guesses remaining: " + guesses);
            System.out.println(gameState);
            System.out.println("Guessed letters:");
            System.out.println(usedLetters);
            System.out.println("Guess a letter: ");
            String a = input.next().toLowerCase();
            Character guess = a.charAt(0);
            while(usedLetters.contains(guess)){
                System.out.println("You've used this letter already, try again: ");
                a = input.next();
                guess = a.charAt(0);
            }
            usedLetters.add(guess);

            Map<String, List<String>> wordFamilies=createWordFamilies(wordList,usedLetters);
            String newGameState = getBestFamily(wordFamilies);
            System.out.println("You've guessed: " + usedLetters);

            gameState = newGameState;
            wordList = wordFamilies.get(newGameState);

            //display the correct answer if they don't win!
            if(!findWordFamily(newGameState, usedLetters).contains(String.valueOf(guess))){
                guesses--;
                if(guesses==0){
                    System.out.println("The word was: " +  wordFamilies.get(newGameState).get(1));
                }
            }

        }
    }


    //create word lists
    public static Map<Integer, List<String>> createWordLists() {
        Map<Integer, List<String>> wordLists = new HashMap<>();
        String fileName = "words.txt";
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                word = word.toLowerCase();
                word = word.strip();
                int key = word.length();
                //alternate method.
                /*
                if(wordLists.containsKey(key)) {
                   wordLists.get(key).add(word);
                } else {
                    List<String> list = new ArrayList<>();
                    list.add(word);
                    wordLists.put(key,list);
                }*/
                if (!wordLists.containsKey(key)) {
                    List<String> list = new ArrayList<>();
                    wordLists.put(key, list);
                }
                wordLists.get(key).add(word);
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return wordLists;
    }

    //create word families
    public static Map<String, List<String>> createWordFamilies(List<String> wordList, Set<Character> guessed){

        Map<String, List<String>> wordFamilies = new HashMap<>();
        for (String word : wordList) {
            String family = findWordFamily(word, guessed);
            if (!wordFamilies.containsKey(family)) {
                List<String> list = new ArrayList<>();
                wordFamilies.put(family, list);
            }
            wordFamilies.get(family).add(word);
        }

        return wordFamilies;
    }

    //figure out the word family for the string
    public static String findWordFamily(String word, Set<Character> guessed) {
        String family = "";
        for (char c : word.toCharArray()) {
            if (guessed.contains(c)) {
                family += c;
            } else {
                family += "_";
            }
        }

        return family;
    }

    //get best family
    public static String getBestFamily(Map<String, List<String>> wordFamilies) {
        //choose the largest family
        String bestKey = "";
        int bestKeySize = 0;

        for (String word : wordFamilies.keySet()) {
            int keySize = wordFamilies.get(word).size();
            if (keySize > bestKeySize) {
                bestKey = word;
                bestKeySize = keySize;
            }
        }
        return bestKey;
    }

}
