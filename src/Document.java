import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Document {

        private int id;
        private String content;

        public Document(int id, String content) {
            this.id = id;
            this.content = content;
        }
    }

