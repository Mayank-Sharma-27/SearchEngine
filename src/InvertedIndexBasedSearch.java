import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndexBasedSearch implements SearchService{

    private Map<String, Map<Integer, List<Integer>>> invertedIndex;

    public InvertedIndexBasedSearch() {
      this.invertedIndex = buildInvertedIndex();
    }

    @Override
    public Set<Integer> searchWord(String word) {
        return invertedIndex.get(word) != null ? new HashSet<>(invertedIndex.get(word).keySet()) : new HashSet<>();
    }

    @Override
    public Set<Integer> searchPhrase(String phrase) {
        Set<Integer> documents = new HashSet<>();
         String[] words = phrase.split("\\s+");
        Map<Integer, List<Integer>> firstWordDocuments = invertedIndex.get(words[0]);

        for (Integer id : firstWordDocuments.keySet()) {
            List<Integer> wordPositions = firstWordDocuments.get(id);

            for (int wordPosition : wordPositions) {
               boolean match = true;
                for (int i = 1; i < words.length; i++) {
                int expectedWordPosition = wordPosition + i;
                List<Integer> nextWordDocuments = invertedIndex
                        .getOrDefault(words[i], Collections.emptyMap())
                        .getOrDefault(id, Collections.emptyList());
                if (!nextWordDocuments.contains(expectedWordPosition)) {
                    match = false;
                    break;
                }
                }
                if (match) {
                    documents.add(id);
                }
            }
        }

        return documents;
    }

    @Override
    public Set<Integer> searchPrefix(String prefix) {
        return invertedIndex
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .flatMap(entry -> entry.getValue().keySet()
                        .stream())
                .collect(Collectors.toSet());
    }

    // Followup 2
    // ================================
    // Inverted Index Implementation
    // ================================
    private Map<String, Map<Integer, List<Integer>>> buildInvertedIndex() {
        DocumentService documentService = new DocumentService();
        List<Document> documents = documentService.getDocuments();
        Map<String, Map<Integer, List<Integer>>> index = new HashMap<>();
        for (Document document : documents) {
            for (Document.Token token : document.getContent()) {
                index
                        .computeIfAbsent(token.getWord(), k -> new HashMap<>())
                        .computeIfAbsent(document.getId(), k -> new ArrayList<>())
                        .add(token.getPosition());

            }
        }
        return index;
    }

}