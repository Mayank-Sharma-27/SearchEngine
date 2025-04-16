import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndexBasedSearch implements SearchService{


    private static Map<String, Set<Integer>> invertedIndex = new HashMap<>();
    @Override
    public Set<Integer> searchWord(String word, List<Document> documents) {
        return invertedIndex.getOrDefault(word, Collections.emptySet());
    }

    @Override
    public Set<Integer> searchPhrase(String phrase, List<Document> documents) {
        String[] words = phrase.split("\\s+");
        Set<Integer> potentialDocs = new HashSet<>(searchWord(words[0],documents));
        for (int i = 1; i < words.length; i++) {
            potentialDocs.retainAll(searchWord(words[i], documents));
        }

        return potentialDocs;
    }

    @Override
    public Set<Integer> searchPrefix(String prefix, List<Document> documents) {
        return invertedIndex
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .flatMap(entry -> entry.getValue()
                        .stream())
                .collect(Collectors.toSet());
    }

    // O(n)
    // ==============================
    // Naive Implementation
    //

    // Followup 2
    // ================================
    // Inverted Index Implementation
    // ================================
    private void buildInvertedIndex(List<Document> documents) {
        documents.stream()
                .filter(doc -> !invertedIndex.containsKey(doc.getId()))
                .forEach(doc -> {
                    String[] words = doc.getContent().split("\\s+");
                    for (String word : words) {
                        invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(doc.getId());
                    }
                });
    }

}
