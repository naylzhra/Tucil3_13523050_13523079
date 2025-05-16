import java.io.*;
import java.util.*;

public class Input { // BELUM DITESSSS
    public static Board readBoardFromFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] dim = br.readLine().split(" ");
        int row = Integer.parseInt(dim[0]);
        int col = Integer.parseInt(dim[1]);
        int nPieces = Integer.parseInt(br.readLine()); 
        char[][] grid = new char[row][col];
        List<Piece> pieces = new ArrayList<>();
        Map<Character, Piece> pieceMap = new HashMap<>();
        for (int i = 0; i < row; i++) {
            String line = br.readLine();
            for (int j = 0; j < col; j++) {
                grid[i][j] = line.charAt(j);
                char c = grid[i][j];
                if (c != '.') {
                    if (!pieceMap.containsKey(c)) {
                        boolean isHorizontal = false;
                        if (j+1 < col && line.charAt(j+1) == c) {
                            isHorizontal = true;
                        }
                        boolean isPrimary = (c == 'P'); 
                        Piece p = new Piece(c, i, j, 1, isHorizontal, isPrimary);
                        pieceMap.put(c, p);
                    } else {
                        pieceMap.get(c).addLength();
                    }
                }
            }
        }

        pieces.addAll(pieceMap.values());

        int goalRow = -1, goalCol = -1;
        for (Piece p : pieces) {
            if (p.isPrimary) {
                if (p.isHorizontal) goalRow = p.row;
                else goalCol = p.col;
                break;
            }
        }

        br.close();
        return new Board(grid, pieces, goalRow, goalCol);
    }
}
