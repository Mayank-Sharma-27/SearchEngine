import lombok.AllArgsConstructor;

import javax.print.Doc;
import java.util.*;

public class Solution {

    public class Document {
        private int id;
        private String content;
        private List<Token> tokens;

       public Document(int id, String content) {
           this.id = id;
           this.content = content;
           this.tokens = tokenise(content);
       }

       private List<Token> tokenise(String content) {
           List<Token> result = new ArrayList<>();
           String[] words = content.split("\\s+");
           int position =0;
           for (String word : words) {
               result.add(new Token(word, position));
           }
           return result;
       }



    }
    @AllArgsConstructor
    private class Token {
        private String word;
        private int position;
    }

    private static Map<String, Map<Integer, List<Integer>>> invertedIndex;

    private void buildInvertedIndex() {
       List<Document> documents = new ArrayList<>();
       for (Document document: documents) {
           for (Token token: document.tokens) {
               invertedIndex.computeIfAbsent(token.word, k -> new HashMap<>())
                       .computeIfAbsent(document.id, k -> new ArrayList<>()).add(token.position);
           }
       }

    }

    public Solution() {
        buildInvertedIndex();
    }

    private Set<Integer> searchWord(String word) {
       return invertedIndex.get(word) == null ? Collections.emptySet() : invertedIndex.get(word).keySet();
    }

    private Set<Integer> searchPhrase(String phrase) {
        String[] words = phrase.split("\\s+");
        Set<Integer> result = new HashSet<>();

        Set<Integer> documents = invertedIndex.get(words[0]).keySet();

        for (int documentId: documents) {
            List<Integer> wordPositions = invertedIndex.get(words[0]).get(documentId);
            for (int wordPosition: wordPositions) {
                boolean found = true;
                for (int i =1; i < words.length; i++) {
                    int expectedPosition = wordPosition + i;
                    List<Integer> possiblePositions = invertedIndex.get(words[i]).get(documentId);

                    if (!possiblePositions.contains(expectedPosition)) {
                        found = false;
                    }
                }
                if (found) {
                    result.add(documentId);
                }
            }
        }
        return result;
    }


}
