import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DocumentService {

    private static Map<String, Document> documents = new HashMap<>();

    public List<Document> getDocuments() {
        return documents.entrySet()
                .stream().map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
