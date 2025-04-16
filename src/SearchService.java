import java.util.List;
import java.util.Set;

public interface SearchService {

    Set<Integer> searchWord(String word, List<Document> documents);

    Set<Integer> searchPhrase(String phrase,  List<Document> documents);

    Set<Integer> searchPrefix(String phrase);
}
