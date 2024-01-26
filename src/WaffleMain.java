/**
 * Main driver class for the Waffle
 */
public class WaffleMain {

  public static void main(String [] args) {

    String url = "";
    if (args.length == 0) {
      WaffleWebParser.getLettersDaily();

    } else if (args.length == 1) {
      url = "https://wafflegame.net/archive";
    } else {
      throw new IllegalArgumentException("Invalid number of args");
    }
  }

}
