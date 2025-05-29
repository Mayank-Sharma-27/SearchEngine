import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Document {

        private int id;
        private String stringContent;
        private List<Token> content;

        public Document(int id, String stringContent) {
            this.id = id;
            this.stringContent = stringContent;
            this.content = tokenize(stringContent);
        }

        public List<Token> tokenize(String text) {
            String[] words = text.split("\\s+");
            List<Token> tokens = new ArrayList<>();
            int index =0;
            for (String word: words) {
            Token token = new Token(word, index);
            tokens.add(token);
            index++;
            }
            return tokens;
        }

        @Data
        public class Token {
            private String word;
            private int index;

            public Token(String word, int index) {
                this.word = word;
                this.index = index;
            }
        }
    }

