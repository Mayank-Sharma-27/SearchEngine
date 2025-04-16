import java.util.*;
import java.util.stream.Collectors;

public class TrieBasedSearchImpl implements SearchService{
    @Override
    public Set<Integer> searchWord(String word, List<Document> documents) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return Collections.emptySet();
            }
            node = node.children.get(c);
        }

        return node.isWord ? node.documentIds : Collections.emptySet();
    }

    @Override
    public Set<Integer> searchPhrase(String phrase, List<Document> documents) {
        TrieNode node = root;
        String[] words = phrase.split("\\s+");
        Set<Integer> candidates = new HashSet<>(searchWord(words[0].toLowerCase(), documents));
        for (int i = 1; i < words.length; i++) {
            candidates.retainAll(searchWord(words[i].toLowerCase(), documents));
        }
        return documents.stream()
                .filter(document -> candidates.contains(document.getId()))
                .filter(document -> document.getContent().contains(phrase))
                .map(document -> document.getId())
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> searchPrefix(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return Collections.emptySet();
            }
            current = current.children.get(c);
        }
        return current.documentIds;
    }

    private static class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        Set<Integer> documentIds = new HashSet<>();
        boolean isWord = false;

        public TrieNode() {}
    }

    private static TrieNode root = new TrieNode();

    public static void buildTrieIndex(List<Document> documents) {
        for (Document document : documents) {
            String[] words = document.getContent().split("\\s+");
            for (String word : words) {

            }
        }
    }

    // In this each node holds the information of where the word is in the document
    public static void insertWord(String word, int docId) {
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
            node.documentIds.add(docId);
        }
        node.isWord = true;
    }

    // In this each node holds the information of where the word is in the document
    public static void insertWordWithDocIdInWord(String word, int docId) {
        TrieNode node = root;

        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isWord = true;
        node.documentIds.add(docId);
    }

}
