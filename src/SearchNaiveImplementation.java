
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SearchNaiveImplementation implements SearchService{

    private final DocumentService documentService = new DocumentService();
    List<Document> documents = documentService.getDocuments();
    @Override
    public Set<Integer> searchWord(String word) {

        return documents.stream()
                .filter(doc -> doc.getContent().contains(word))
                .map(doc -> doc.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> searchPhrase(String phrase) {
        return documents.stream()
                .filter(doc -> doc.getContent().contains(phrase))
                .map(doc -> doc.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> searchPrefix(String phrase) {
        return documents.stream()
                .filter(doc -> doc.getContent().contains(phrase))
                .map(doc -> doc.getId())
                .collect(Collectors.toSet());
    }

}
