public class Sudukou {

    public boolean isValidSudoku(char[][] board) {

        boolean[][] rows = new boolean[9][9];
        boolean[][] cols = new boolean[9][9];
        boolean[][] box = new boolean[9][9];

        for (int i =0; i < 9; i++) {
            for (int j =0; j < 9; j++) {
                if (board[i][j] == '.') {
                    continue;
                }
                int value = board[i][j] - '1';
                int boardKey = ((i /3) * 3) + (j/3);

                if (rows[i][value] || cols[j][value] || box[boardKey][value]) {
                    return false;
                }
                rows[i][value] = true;
                cols[j][value] = true;
                box[boardKey][value] = true;

            }
        }
        return true;
    }

    public void solveSudoku(char[][] board) {
        traverse(board);
    }

    private boolean traverse(char[][] board) {
        for (int i =0; i < 9; i++) {
            for (int j =0; j < 9; j++) {
                if (board[i][j] != '.') {
                    continue;
                }
                for (char c ='1'; c <='9'; c++){
                    if (isValid(c, i, j, board)) {
                        board[i][j] = c;
                        if (traverse(board)) {
                            return true;
                        }
                        board[i][j] = '.';
                    }
                }
                return false;

            }
        }
        return true;
    }

    private boolean isValid(char value, int row, int col, char[][] board) {
        for (int i =0; i < 9; i++) {
            if (board[row][i] == value) {
                return false;
            }
            if (board[i][col] == value ) {
                return false;
            }

            int boardX = ((row/3) * 3) + (i /3);
            int boardY = ((col/3) *3) + (i%3);

            if (board[boardX][boardY] == value) {
                return false;
            }
        }
        return true;
    }
}
