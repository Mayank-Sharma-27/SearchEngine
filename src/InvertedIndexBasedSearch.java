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

    private Set<Integer> searchBooleanQueue(String query) {
        Stack<Set<Integer>> operands = new Stack<>();
        Stack<String> operators = new Stack<>();
        List<String> tokens = tokenize(query);
        for (String token : tokens) {
            if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.empty() && operators.peek().equals("(")) {
                    applyOperator(operands, operators.pop());
                }
                operators.pop();
            } else if (token.equals("AND") || token.equals("OR")) {
                while (!operators.empty() && precedence(operators.peek()) >= precedence(token)) {
                    applyOperator(operands, operators.pop());
                }
                operators.push(token);
            } else {
                operands.push(searchWord(token));
            }

        }

        while (!operators.isEmpty()) {
            applyOperator(operands, operators.pop());
        }

        return operands.isEmpty() ? new HashSet<>() : operands.pop();
    }

    private int precedence(String op) {
        if (op.equals("AND")) return 2;
        if (op.equals("OR")) return 1;
        return 0;
    }

    private void applyOperator(Stack<Set<Integer>> operands, String op) {
        Set<Integer> right = operands.pop();
        Set<Integer> left = operands.pop();
        if (op.equals("AND")) {
            left.retainAll(right); // intersection
            operands.push(left);
        } else if (op.equals("OR")) {
            left.addAll(right); // union
            operands.push(left);
        }
    }

    private List<String> tokenize(String word) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (c == '(' || c == ')') {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                tokens.add(String.valueOf(c));
            } else if (Character.isWhitespace(c)) {
                if (sb.length() > 0) {
                    tokens.add(sb.toString().trim());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            tokens.add(sb.toString().trim());
        }
        return tokens;
    }

    // Followup 2
    // ================================
    // Inverted Index Implementation
    // ================================
    // Let N be the number of documents.
    // Let W be the total number of words across all documents.
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