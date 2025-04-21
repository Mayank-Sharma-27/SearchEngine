import java.util.*;
import java.util.stream.Collectors;

public class TrieBasedSearchImpl implements SearchService{
    private final DocumentService documentService = new DocumentService();
    List<Document> documents = documentService.getDocuments();
    private static TrieNode root = new TrieNode();
    public TrieBasedSearchImpl() {
        buildTrieIndex(documents);
    }
    @Override
    public Set<Integer> searchWord(String word) {
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
    public Set<Integer> searchPhrase(String phrase) {
        TrieNode node = root;
        String[] words = phrase.split("\\s+");
        Set<Integer> candidates = new HashSet<>(searchWord(words[0].toLowerCase()));
        for (int i = 1; i < words.length; i++) {
            candidates.retainAll(searchWord(words[i].toLowerCase()));
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

    @Override
    public List<String> autoCompleteSuggestions(String prefix) {
        if (prefix.isEmpty()) {
            return Collections.emptyList();
        }
        TrieNode node = searchPrefixNode(prefix);
        if (node == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();
        collectWords(node, prefix, suggestions);
        Collections.sort(suggestions);
        return suggestions;
    }

    private void collectWords(TrieNode node, String currentPrefix, List<String> suggestions) {
        if (currentPrefix.isEmpty()) {
            return;
        }
        if (!node.documentIds.isEmpty()) {
            suggestions.add(currentPrefix);
        }
        if (suggestions.size() >= 20) {
            return;
        }

        for (char ch : node.children.keySet().stream().sorted().toList()) {
            collectWords(node.children.get(ch), currentPrefix + ch, suggestions);
            if (suggestions.size() >= 20) {
                return;
            }
        }
    }

    private TrieNode searchPrefixNode(String string) {
        TrieNode node = root;
        for (char c : string.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return  null;
            }
        }
        return node;
    }

    private static class TrieNode {
        private Map<Character, TrieNode> children = new HashMap<>();
        Set<Integer> documentIds = new HashSet<>();
        boolean isWord = false;

        public TrieNode() {

        }
    }



    public static void buildTrieIndex(List<Document> documents) {
        for (Document document : documents) {
            List<String> words = document.getContent()
                    .stream()
                    .map(token -> token.getWord())
                    .collect(Collectors.toList());
            for (String word : words) {
                insertWord(word, document.getId());
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
