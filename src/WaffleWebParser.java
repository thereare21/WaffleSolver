
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Parses the Waffle Website for the Waffle information and sets the Waffle up accordingly.
 */
public class WaffleWebParser {

  //public static Map<Posn, Letter> parsedLetterMap;


  public static Map<Posn, Letter> getLettersDaily() {
     String url = "https://wafflegame.net/daily";

     try {
       Document dailySite = Jsoup.connect(url).get();

       Element inner = dailySite.body()
               .selectFirst("div.centre")
               .selectFirst("div.app")
               .selectFirst("main.game")
               .selectFirst("div.top")
               .selectFirst("div.board");

       Elements allInnerElements = inner.getAllElements();
       for (Element e : allInnerElements) {
         System.out.println(e);
       }

       /*
       Elements boardPieces = inner.select("div.tile");
       for (Element piece : boardPieces) {
         String pieceClassAttributes = piece.attr("class");
         System.out.println(pieceClassAttributes);
       }*/
     } catch(IOException e) {
       System.out.println("could not find website");
     }

     return new HashMap<>();


  }

  public static Map<Posn, Letter> getLettersArchive() {
    return new HashMap<>();
  }
}
