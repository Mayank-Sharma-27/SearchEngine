import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {

    static class Document {
        private int id;
        private String content;

        public Document(int id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    private static Map<String, Set<Integer>> invertedIndex = new HashMap<>();

    // O(n)
    public static List<Integer> search(List<Document> documents, String word) {
        return documents.stream()
                .filter(doc -> doc.content.contains(word))
                .map(doc -> doc.id)
                .collect(Collectors.toList());
    }

    // Search for a phrase (sequence of words) // O(n)
    public static List<Integer> searchPhrase(List<Document> documents, String phrase) {
        return documents.stream()
                .filter(doc -> doc.content.contains(phrase))
                .map(doc -> doc.id)
                .collect(Collectors.toList());
    }

    // Followup 2
    public static void buildInvertedIndex(List<Document> documents) {
        documents.stream()
                .filter(doc -> !invertedIndex.containsKey(doc.id))
                .forEach(doc -> {
                    String[] words = doc.content.split("\\s+");
                    for (String word : words) {
                        invertedIndex.computeIfAbsent(word, k -> new HashSet<>()).add(doc.id);
                    }
                });
    }

    public static Set<Integer> searchWord(String word) {
        return invertedIndex.getOrDefault(word, Collections.emptySet());
    }

    public static Set<Integer> searchPhrase(String phrase) {
        String[] words = phrase.split("\\s+");
        Set<Integer> potentialDocs = new HashSet<>(searchWord(words[0]));
        for (int i = 1; i < words.length; i++) {
            potentialDocs.retainAll(searchWord(words[i]));
        }

        return potentialDocs;
    }

    public static Set<Integer> searchExactPharse(String phrase, List<Document> documents) {
        Set<Integer> docs = searchPhrase(phrase);

        Set<Integer> result = new HashSet();
        return documents.stream()
                .filter(document -> docs.contains(document.id))
                .filter(document -> document.content.contains(phrase))
                .map(document -> document.id)
                .collect(Collectors.toSet());

    }

    // This implementation will search all the keys in the invertedIndex;
    public static Set<Integer> searchPrefix(String prefix) {
        return invertedIndex
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(prefix))
                .flatMap(entry -> entry.getValue()
                        .stream())
                .collect(Collectors.toSet());
    }

}
