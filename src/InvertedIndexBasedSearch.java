import java.util.*;
import java.util.stream.Collectors;

public class InvertedIndexBasedSearch implements SearchService {

    private Map<String, Map<Integer, List<Integer>>> invertedIndex;

    public InvertedIndexBasedSearch() {
        this.invertedIndex = buildInvertedIndex();
    }

    @Override
    public Set<Integer> searchWord(String word) {
        return invertedIndex.get(word) == null ? Collections.emptySet() : invertedIndex.get(word).keySet();
    }

    public Set<Integer> phraseSearch(String phrase) {
        Set<Integer> result = new HashSet();
        String[] words = phrase.split("\\s+");
        Map<Integer, List<Integer>> documentsWithWords = invertedIndex.get(words[0]);

        for (int id : documentsWithWords.keySet()) {


            for (int index : documentsWithWords.get(id)) {
                boolean found = true;
                for (int i = 1; i < words.length; i++) {
                    int requiredIndex =index + i;
                            List <Integer> possibleIndexes = invertedIndex
                                    .getOrDefault(words[i], new HashMap<>())
                                    .getOrDefault(id, new ArrayList<>());
                            if (!possibleIndexes.contains(requiredIndex)) {
                                found = false;
                            }
                }
                if (found) {
                    result.add(id);
                }
            }
        }
        return result;
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
        // Stack to hold intermediate document sets for operands (results of searchWord)
        Stack<Set<Integer>> intermediateStack = new Stack<>();

        // Stack to hold logical operators (AND, OR, parentheses)
        Stack<String> operators = new Stack<>();

        // Tokenize the input query string into words and operators
        List<String> tokens = tokenize(query);

        for (String token : tokens) {
            if (token.equals("(")) {
                // Push opening parenthesis to the operator stack
                operators.push(token);
            } else if (token.equals(")")) {
                // When a closing parenthesis is found, process the expression inside
                // Keep applying operators until matching "(" is found
                while (!operators.empty() && !operators.peek().equals("(")) {
                    applyOperator(intermediateStack, operators.pop());
                }
                // Discard the matching "("
                operators.pop();
            } else if (token.equals("AND") || token.equals("OR")) {
                // While there are operators with higher or equal precedence, apply them
                while (!operators.empty() && precedence(operators.peek()) >= precedence(token)) {
                    applyOperator(intermediateStack, operators.pop());
                }
                // Push the current operator to the stack
                operators.push(token);
            } else {
                // It's a search term, perform word search and push result set to operand stack
                intermediateStack.push(searchWord(token));
            }
        }

        // Apply any remaining operators
        while (!operators.isEmpty()) {
            applyOperator(intermediateStack, operators.pop());
        }

        // Final result is on top of operand stack
        return intermediateStack.isEmpty() ? new HashSet<>() : intermediateStack.pop();
    }

    // Assign precedence to operators (AND > OR)
    private int precedence(String op) {
        if (op.equals("AND")) return 2;
        if (op.equals("OR")) return 1;
        return 0;
    }

    // Apply the logical operator to top two sets on the operand stack
    private void applyOperator(Stack<Set<Integer>> intermediateStack, String op) {
        Set<Integer> right = intermediateStack.pop(); // right operand
        Set<Integer> left = intermediateStack.pop();  // left operand
        if (op.equals("AND")) {
            // Intersection: documents that appear in both sets
            left.retainAll(right);
            intermediateStack.push(left);
        } else if (op.equals("OR")) {
            // Union: documents that appear in either set
            left.addAll(right);
            intermediateStack.push(left);
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

    private List<String> tokenizeV2(String phrase) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char c: phrase.toCharArray()) {
            if (c == '(' || c == ')') {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
                    sb.setLength(0);
                }
                sb.append(c);
            } else if (Character.isWhitespace(c)) {
                if (sb.length() > 0) {
                    tokens.add(sb.toString());
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
        DocumentService docService = new DocumentService();
        List<Document> documents = docService.getDocuments();
        Map<String, Map<Integer, List<Integer>>> invertedIndex = new HashMap<>();
        for (Document document : documents) {
            for (Document.Token word : document.getContent()) {
                invertedIndex.computeIfAbsent(word.getWord(), k -> new HashMap<>())
                        .computeIfAbsent(document.getId(), k -> new ArrayList<>())
                        .add(word.getIndex());
            }
        }
        return invertedIndex;
    }

}