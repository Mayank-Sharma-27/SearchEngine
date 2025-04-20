import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Document {

        private int id;
        private String textContent;
        private List<Token> content;

        public Document(int id, String textContent) {
            this.id = id;
            this.textContent = textContent;
            this.content = tokenize(textContent);
        }

        private static List<Token> tokenize(String content) {
            String[] words = content.split(" ");

            List<Token> tokens = new ArrayList<>();
            int position = 0;
            for (String word : words) {
                tokens.add(new Token(position, word));
                position++;
            }
            return  tokens;
        }

        @Data
        public static class Token {
            private int position;
            private String word;

            public Token(int position, String word) {
                this.position = position;
                this.word = word;
            }
        }
    }

