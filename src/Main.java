import java.util.List;

public class Main {


    public static void main(String[] args) {

        SearchEngine searchEngine = new SearchEngine();
        List<SearchEngine.Document> docs = List.of(
                new SearchEngine.Document(1, "apple pie and banana smoothie"),
                new SearchEngine.Document(2, "banana apple fruit salad"),
                new SearchEngine.Document(3, "chocolate pie and lemon tart")
        );

        SearchEngine.buildInvertedIndex(docs);
        SearchEngine.buildTrieIndex(docs);

// Example queries
        searchEngine.naiveSearch(docs, "banana");
        searchEngine.invertedIndexSearchPhrase("banana apple", docs);
        searchEngine.searchPrefix("ban");
    }
}