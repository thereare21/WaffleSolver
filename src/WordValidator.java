import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class WordValidator {

  public static boolean isValidWord(List<Letter> letterList) {
    String word = letterList.stream().map(letter -> letter.getLetter()).map(String::valueOf)
            .collect(Collectors.joining());

    String filePath = "/Users/raymondtsai/Documents/JavaProjects/WaffleSolver/src/AllFiveLetterWords.txt";

    // Read and check each word from the file
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // Assuming each line in the file contains a valid word

        if (line.trim().equals(word)) {
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace(); // Handle the exception appropriately
    }
    return false;
  }
}
