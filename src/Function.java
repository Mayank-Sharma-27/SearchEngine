import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Function {
    String name;
    List<String> argumentTypes;
    boolean isVariadic;

    Function(String name, List<String> argumentTypes, boolean isVariadic) {
        this.name = name;
        this.argumentTypes = argumentTypes;
        this.isVariadic = isVariadic;
    }

    class FunctionLibraryBruteForce {

        List<Function> functions;

        public void registerFunction(Function function) {
            functions.add(function);
        }

        public List<Function> findMatches(List<String> args) {
            List<Function> matches = new ArrayList<>();
            for (Function function : functions) {
                if (matches(function, args)) {
                    matches.add(function);
                }
            }
            return matches;
        }

        private boolean matches(Function function, List<String> args) {
           List<String> functionArgumentTypes = function.argumentTypes;
           if (!function.isVariadic) {
               return functionArgumentTypes.equals(args);
           }

           if (functionArgumentTypes.size() > argumentTypes.size()) {
               return false;
           }

           int size = functionArgumentTypes.size() -1;
           for (int i =0; i < args.size(); i++) {
               if (!functionArgumentTypes.get(i).equals(args.get(i))) {
                   return false;
               }
           }

           String arg = functionArgumentTypes.get(size);

           for (int i = size ; i < args.size(); i++) {
               if (!args.get(i).equals(arg)) {
                   return false;
               }
           }
           return true;
        }
    }

    class FunctionTrieBasedApproach {
        class TrieNode {
            Map<String, TrieNode> children;
            List<Function> isVardicTrue = new ArrayList<>();
            List<Function> isVardicFalse = new ArrayList<>();
        }

        private TrieNode root = new TrieNode();

        public void registerFunction(Function function) {
            insert(function);
        }

        private void insert(Function function) {
            TrieNode node = root;
            for (String arg : function.argumentTypes) {
                node = node.children.computeIfAbsent(arg, k -> new TrieNode());
            }
            if (function.isVariadic) {
                node.isVardicTrue.add(function);
            } else {
                node.isVardicFalse.add(function);
            }
        }

        private List<Function> findMatches(List<String> args) {
            List<Function> result = new ArrayList<>();
            TrieNode node = root;
            for (int i = 0; i < args.size(); i++) {
                String arg = args.get(i);
                if (!node.children.containsKey(arg)) {
                    return result;
                }
                TrieNode next = node.children.get(arg);
                if (i == args.size() - 1) {
                    result.addAll(next.isVardicFalse);
                    result.addAll(next.isVardicTrue);
                } else {
                    for (Function function : next.isVardicTrue) {
                        if (allSame(args.subList(i, args.size()), arg)) {
                            result.add(function);
                        }
                    }
                }
            }
            return result;
        }

        private boolean allSame(List<String> args, String value) {
            for (String arg : args) {
                if (!arg.equals(value)) {
                    return false;
                }
            }
            return true;
        }

    }
}
