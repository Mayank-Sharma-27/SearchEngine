
---

### 📘 `README.md`: Java Search Engine – Naive vs. Inverted Index vs. Trie vs. Memory-Mapped

---

## 🔍 Overview

This Java-based search engine supports **four** core search implementations:
- **Naive Linear Scan**
- **Inverted Index**
- **Trie-Based Indexing**
- **Memory-Mapped File Search**

Each approach is implemented to support:
- Word search
- Phrase search
- Prefix search

The goal is to understand the trade-offs between these techniques, their performance characteristics, and which to use depending on your application's needs.

---

## 📦 Features

- ✅ Full-text word search
- ✅ Phrase search
- ✅ Prefix search (autocomplete-style)
- ✅ Multiple indexing strategies
- ✅ Memory-efficient file search using MappedByteBuffer
- ✅ Easy benchmarking and extensibility

---

## 🧠 Implementations

### 1. Naive Search (O(n))
#### ✅ Description:
Iterates through each document and checks if a word or phrase exists using `String.contains()`.

#### ✅ Pros:
- Simple and easy to implement.
- No preprocessing or additional memory overhead.

#### ❌ Cons:
- Inefficient for large datasets.
- Repeated scanning for every query.

#### ✅ Use Cases:
- Small-scale applications or prototyping.
- Situations where indexing is not feasible.

---

### 2. Inverted Index

#### ✅ Description:
Maps each word to the set of document IDs where it appears.

```java
Map<String, Set<Integer>> invertedIndex;
```

#### ✅ Pros:
- Fast lookups (`O(1)` per word).
- Easy to store additional metadata (e.g., frequency, position).
- Scales well with large document sets.
- Suitable for disk-based storage and compression.

#### ❌ Cons:
- Phrase search is approximate unless you store word positions.
- Slower for prefix matching unless iterating through all keys.

#### ✅ Use Cases:
- Real-world search engines (Elasticsearch, Lucene).
- Scalable systems with billions of documents.

---

### 3. Trie-Based Search

#### ✅ Description:
Stores words character-by-character in a prefix tree (Trie). Each node stores document IDs that share that prefix.

```java
class TrieNode {
    Map<Character, TrieNode> children;
    Set<Integer> documentIds;
    boolean isEndOfWord;
}
```

#### ✅ Pros:
- Extremely fast prefix search.
- In-memory autocomplete with low latency.
- Can be extended for fuzzy search or spell check.

#### ❌ Cons:
- High memory consumption due to character-level granularity.
- Redundant document ID storage across nodes.
- Not disk-friendly or compressible like inverted index.

#### ✅ Use Cases:
- Autocomplete suggestions.
- Spell-check engines.
- In-memory search on small-to-medium datasets.

---

### 4. Memory-Mapped File Search

#### ✅ Description:
Uses `MappedByteBuffer` to read large files efficiently and search content without loading everything into memory.

```java
MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
```

#### ✅ Pros:
- Memory efficient for large files.
- OS-level paging avoids full file loading.
- Great for log processing and tail-like functionality.

#### ❌ Cons:
- Slower than indexes for repeated queries.
- Phrase and prefix matching require string scans.

#### ✅ Use Cases:
- Large log files or read-only archives.
- Search-once workflows where indexing isn't worthwhile.

---

## 🆚 Comparison Table

| Feature               | Naive           | Inverted Index           | Trie                      | Memory-Mapped             |
|-----------------------|------------------|----------------------------|---------------------------|---------------------------|
| Search Speed          | ❌ Slow           | ✅ Fast                    | ✅ Fast (prefix only)      | ⚠️ Medium (depends on disk)|
| Memory Efficiency     | ✅ High           | ✅ Moderate (scalable)     | ❌ Low (many nodes)        | ✅ Very High               |
| Phrase Matching       | ✅ Exact match    | ✅ Approximate (needs post-check) | ✅ With post-check    | ✅ Full String Scan        |
| Prefix Matching       | ❌ Inefficient    | ❌ Inefficient             | ✅ Efficient               | ❌ Inefficient             |
| Scalability           | ❌ Poor           | ✅ Excellent               | ❌ Poor                    | ✅ Excellent (no RAM needed) |
| Implementation Effort | ✅ Low            | ✅ Medium                  | ❌ High                    | ✅ Medium                  |

---

## 🧪 How to Run

```java
List<Document> docs = List.of(
    new Document(1, "apple pie and banana smoothie"),
    new Document(2, "banana apple fruit salad"),
    new Document(3, "chocolate pie and lemon tart")
);

// Inverted Index
SearchEngine.buildInvertedIndex(docs);
SearchEngine.invertedIndexSearchPhrase("banana apple", docs);

// Trie
SearchEngine.buildTrieIndex(docs);
SearchEngine.searchPrefix("ban");

// Naive
SearchEngine.naiveSearch(docs, "banana");

// Memory Mapped
SearchEngine.MemoryMappedSearchServiceImpl service = new SearchEngine.MemoryMappedSearchServiceImpl("data.txt");
service.searchWord("banana", docs);
```

---

## 🧵 Design Trade-offs

| Decision | Rationale |
|---------|-----------|
| `Trie` nodes store document IDs per character | Enables fast prefix queries, but duplicates storage |
| `Inverted Index` only stores complete words | Memory efficient, good for keyword-based search |
| Phrase matching uses post-check in both `Inverted Index` and `Trie` | Avoids tracking word positions for simplicity |
| `Memory-Mapped IO` used instead of loading entire file | Keeps RAM usage low, efficient file scan |
| All 4 implementations included | Helps benchmark and understand trade-offs practically |

---

## 🧑‍🏫 Learning Outcomes

- Understand foundational search strategies in information retrieval.
- Get hands-on with trie and hashmap-based indexing.
- Analyze trade-offs in speed vs. memory vs. complexity.
- Learn how to use memory-mapped files for low-RAM workloads.
- Prepare for system design/LLD interviews involving search.

---

## 🧩 Possible Extensions

- Store word positions for exact phrase search using inverted index.
- Compress trie with DAWG (Directed Acyclic Word Graph).
- Add ranking (TF-IDF, BM25).
- Index and search large files using memory-mapped IO.
- Add fuzzy search support using Levenshtein distance.

---

Here's an updated version of your `README.md` that includes an in-depth comparison of **file reading strategies**—particularly focusing on `FileChannel`, `MappedByteBuffer`, and `BufferedReader`. This version is tailored like a senior engineer’s internal knowledge doc, with clear pros, cons, and architectural rationale.

---

## 📘 README.md: Java Search Engine – Naive vs. Inverted Index vs. Trie + File I/O Trade-offs

---

## 🔍 Overview

This Java-based search engine project explores how full-text and prefix search can be implemented using:
- Naive Scanning
- Inverted Indexing
- Trie Indexing
- Memory-mapped I/O for large file search

The project supports:
- Word search
- Phrase search
- Prefix-based autocomplete

---

## 📦 Features

- ✅ Fast in-memory search
- ✅ Memory-efficient prefix trie
- ✅ Benchmarkable search strategies
- ✅ Memory-mapped file support
- ✅ Modular service interface (`SearchService`)

---

## 🧠 Implementations Summary

| Technique         | Word Search | Phrase Search | Prefix Search | Indexing Needed | File Scalable |
|------------------|-------------|---------------|---------------|------------------|---------------|
| Naive            | ✅           | ✅             | ❌             | ❌                | ✅ (small)     |
| Inverted Index   | ✅           | ✅ (post-check) | ❌             | ✅                | ✅             |
| Trie             | ✅           | ✅ (post-check) | ✅             | ✅                | ❌ (in-memory) |
| Memory-Mapped I/O| ✅           | ✅             | ✅             | ❌ (no pre-index) | ✅ (large)     |

---

## 🧩 Memory-Mapped File Search (`MappedByteBuffer`)

### ✅ Description

Instead of loading the entire file into memory or reading it line-by-line, we **map the file content into memory** using `MappedByteBuffer`, and scan it byte-by-byte. This approach mimics how the `tail` command works at OS level.

### 💡 Why We Chose This

For large datasets where in-memory structures like `Trie` or `Map` are impractical, memory-mapped IO:
- Offers a **zero-copy** approach (data stays in kernel space until accessed).
- Allows **random access** to large files.
- Minimizes JVM heap pressure and GC pauses.

```java
FileChannel fileChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
```

---

## 📚 File Reading Techniques Compared

### 1. `BufferedReader` (Line-by-Line Read)

#### ✅ Pros
- Easy to use.
- Low memory footprint.
- Good for medium-sized files.

#### ❌ Cons
- Still reads via syscall on every line.
- Slower for high-throughput search (line-by-line).

---

### 2. `FileInputStream` with `Scanner` or Manual Split

#### ✅ Pros
- Control over buffer size and format.
- Works with binary files too.

#### ❌ Cons
- Still incurs user-space memory copying.
- Tedious parsing logic for phrases.

---

### 3. `FileChannel` + `MappedByteBuffer` (Memory-Mapped)

#### ✅ Pros
- Zero-copy mechanism (OS handles paging).
- Fast random access.
- Low JVM memory usage.
- Ideal for **read-only** large files.

#### ❌ Cons
- Not suited for write-heavy operations.
- Entire file segment gets mapped; too large = `OutOfMemoryError`.
- Debugging harder if file corruption occurs.
- Platform-dependent behavior at extreme sizes.

---

## 📈 Trade-Off Matrix (Senior Engineer Perspective)

| Criteria                  | BufferedReader       | FileInputStream          | MappedByteBuffer            |
|---------------------------|----------------------|---------------------------|------------------------------|
| Memory Usage              | ✅ Low               | ✅ Medium                 | ✅ Very Low (off-heap)       |
| Search Performance        | ❌ Slower            | ❌ Medium                 | ✅ Fast (OS-level paging)    |
| Parallel Access           | ❌ Hard              | ❌ Hard                   | ✅ Easier with offset chunks |
| Suitability for Large Files| ❌ Not great         | ❌ Limited                | ✅ Ideal                     |
| Write Operations          | ✅ Safe               | ✅ Safe                   | ❌ Not recommended           |
| Complexity                | ✅ Low                | ✅ Medium                 | ❌ High                      |

---

## 🛠 Sample Usage

```java
SearchService mmapSearch = new MemoryMappedSearchServiceImpl("docs.txt");
Set<Integer> matches = mmapSearch.searchPhrase("banana apple", List.of());
```

---

## 🧠 Engineering Learnings

- Use `BufferedReader` for line-wise reads where memory isn't a concern.
- Use `Inverted Index` for large-scale keyword lookups and scalable systems.
- Use `Trie` only for autocomplete or small in-memory datasets.
- Use `MappedByteBuffer` when:
    - Working with **very large files**.
    - Needing **low-latency search**.
    - Search is read-only and sequential or chunked.

---

## 🧪 Future Improvements

- 🧠 Add parallel chunked scanning using `MappedByteBuffer` and `ForkJoinPool`.
- 🧠 Benchmark `MappedByteBuffer` vs. `BufferedReader` for files >1GB.
- 🧠 Add word-position tracking in inverted index for exact phrase match.

---

