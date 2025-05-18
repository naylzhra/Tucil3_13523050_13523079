import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;

public class Input { 
    public static Board readBoardFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        String[] dim = br.readLine().trim().split("\\s+");
        int row = Integer.parseInt(dim[0]);
        int col = Integer.parseInt(dim[1]);
        int nPieces = Integer.parseInt(br.readLine().trim());

        char[][] grid = new char[row][col];
        List<Piece> pieces = new ArrayList<>();
        Map<Character, Piece> pieceMap = new HashMap<>();

        int goalRow = -1, goalCol = -1;
        char exitDir = '?';

        for (int i = 0; i < row; i++) {
            String line = br.readLine();

            if (line.length() == col + 1 && line.charAt(0) == 'K') {
                exitDir = 'L';
                goalRow = i;
                goalCol = -1;
                line = line.substring(1);              // drop the K
            } else if (line.length() == col + 1 && line.charAt(col) == 'K') {
                exitDir = 'R';
                goalRow = i;
                goalCol = col;
                line = line.substring(0, col);        // keep only the board cells
            }

            for (int j = 0; j < col; j++) {
                grid[i][j] = line.charAt(j);
                if (grid[i][j] != '.' && grid[i][j] != 'K'){
                    Piece p = pieceMap.get(grid[i][j]);
                    if (p == null) {
                        boolean horizontal =
                            (j + 1 < col && line.charAt(j+1) == grid[i][j]) ||
                            (j - 1 >= 0   && line.charAt(j-1) == grid[i][j]);
                        boolean isPrimary = (grid[i][j] == 'P');
                        p = new Piece(grid[i][j], i, j, 1, horizontal, isPrimary);
                        pieceMap.put(grid[i][j], p);
                    } else {
                        p.addLength();
                    }
                }
            }
        }
        br.mark(2);
        String extra = br.readLine();
        if (extra != null && extra.indexOf('K') != -1) {
            int kPos = extra.indexOf('K');
            if (exitDir != '?')
                throw new IOException("Two exit gates found!");
            if (kPos >= 0 && kPos < col) {
                goalCol = kPos;
                if (extra.trim().equals(extra)) {
                    exitDir = 'D';  goalRow = row;
                } else {
                    exitDir = 'U';  goalRow = -1;
                }
            } else
                throw new IOException("Malformed K-line: "+extra);
        } else {
            br.reset();
        }
        br.close();

        pieces.addAll(pieceMap.values());
        return new Board(grid, pieces, goalRow, goalCol, exitDir);
    }
}
