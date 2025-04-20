import java.util.List;
import java.util.Set;

public interface SearchService {

    default Set<Integer> searchWord(String word) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default Set<Integer> searchPhrase(String phrase) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default Set<Integer> searchPrefix(String phrase) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

     default List<String> autoCompleteSuggestions(String prefix) {
         throw new UnsupportedOperationException("Not supported yet.");
     }
}
