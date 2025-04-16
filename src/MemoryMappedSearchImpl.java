import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

public class MemoryMappedSearchImpl implements SearchService{
    @Override
    public Set<Integer> searchWord(String word, List<Document> documents) {
        return searchUsingMemoryMap(word);
    }

    @Override
    public Set<Integer> searchPhrase(String phrase, List<Document> documents) {
        return searchUsingMemoryMap(phrase);
    }

    @Override
    public Set<Integer> searchPrefix(String phrase) {
        return searchUsingMemoryMap(phrase);
    }

    private Set<Integer> searchUsingMemoryMap(String word) {
        String filePath = "test";
        Set<Integer> potentialDocs = Set.of();
        try (FileChannel fileChannel = (FileChannel) Files.newByteChannel(Paths.get(filePath), StandardOpenOption.READ)) {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            StringBuilder sb = new StringBuilder();
            int docId =0;
            for (int i = 0; i < buffer.limit(); i++) {
                char c = (char) buffer.get(i);
                if (c == '\n') {
                    String content = sb.toString();
                    if (content.contains(word)) {
                        potentialDocs.add(docId);
                    }
                    sb.setLength(0);
                    docId++;
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                String content = sb.toString();
                if (content.contains(word)) {
                    potentialDocs.add(docId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return potentialDocs;
    }
}
