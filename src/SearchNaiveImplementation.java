
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchNaiveImplementation implements SearchService{

    @Override
    public Set<Integer> searchWord(String word, List<Document> documents) {
        return documents.stream()
                .filter(doc -> doc.getContent().contains(word))
                .map(doc -> doc.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> searchPhrase(String phrase, List<Document> documents) {
        return documents.stream()
                .filter(doc -> doc.getContent().contains(phrase))
                .map(doc -> doc.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> searchPrefix(String phrase, List<Document> documents) {
        return documents.stream()
                .filter(doc -> doc.getContent().contains(phrase))
                .map(doc -> doc.getId())
                .collect(Collectors.toSet());
    }

}
