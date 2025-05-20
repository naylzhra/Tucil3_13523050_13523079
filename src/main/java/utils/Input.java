package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import object.Board;
import object.Piece;

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
        
        int i = 0;
        while (i < row) {
            String rawLine = br.readLine();
            if (rawLine == null) throw new IOException("Unexpected end of board data on row " + i);
            if (i == 0 && rawLine.trim().equals("K")) {
                exitDir = 'U';
                goalRow = -1;
                continue;
            }
            String line = rawLine.stripLeading();
            if (line.length() == col + 1 && line.charAt(0) == 'K') {
                exitDir = 'L';
                goalCol = -1;
                line = line.substring(1);
            } else if (line.length() == col + 1 && line.charAt(col) == 'K') {
                exitDir = 'R';
                goalCol = col;
                line = line.substring(0, col);
            }

            for (int j = 0; j < col; j++) {
                grid[i][j] = line.charAt(j);
                if (grid[i][j] != '.' && grid[i][j] != 'K') {
                    Piece p = pieceMap.get(grid[i][j]);
                    if (p == null) {
                        boolean horizontal = (j + 1 < col && line.charAt(j + 1) == grid[i][j]) ||
                                (j - 1 >= 0 && line.charAt(j - 1) == grid[i][j]);
                        boolean isPrimary = (grid[i][j] == 'P');
                        p = new Piece(grid[i][j], i, j, 1, horizontal, isPrimary);
                        if (isPrimary) {
                            if (horizontal){
                                goalRow = i;
                            } else{
                                goalCol = j;
                            }
                        }
                        pieceMap.put(grid[i][j], p);
                    } else {
                        p.addLength();
                    }
                }
            }
            i++;
        }
        br.mark(1);
        String extra = br.readLine();
        if (extra != null && extra.indexOf('K') != -1) {
            int kPos = extra.indexOf('K');
            if (exitDir != '?')
                throw new IOException("Two exit gates found!");
            if (kPos >= 0 && kPos < col) {
                goalCol = kPos;
                goalRow = row;
                exitDir = 'D';
            } else
                throw new IOException("Malformed Line: " + extra);
        } else {
            br.reset();
        }
        br.close();
        
        pieces.addAll(pieceMap.values());
        for (Piece p : pieces) {
            char id = p.id;
            int len = p.length;
            if (len < 2) {
                throw new IOException("Piece '" + id + "' too short: length=" + len + " (min 2)");
            }
            List<int[]> coords = new ArrayList<>();
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    if (grid[r][c] == id) {
                        coords.add(new int[]{r,c});
                    }
                }
            }
            if (coords.size() != len) {
                throw new IOException("Piece '" + id + "' inconsistency: counted=" 
                                      + coords.size() + " vs length=" + len);
            }
            if (p.isHorizontal) {
                int r0 = coords.get(0)[0];
                int minC = coords.stream().mapToInt(c->c[1]).min().getAsInt();
                int maxC = coords.stream().mapToInt(c->c[1]).max().getAsInt();
                if (coords.stream().anyMatch(c->c[0]!=r0) || maxC-minC+1 != len) {
                    throw new IOException("Piece '" + id + "' is not straight horizontal");
                }
            } else {
                int c0 = coords.get(0)[1];
                int minR = coords.stream().mapToInt(c->c[0]).min().getAsInt();
                int maxR = coords.stream().mapToInt(c->c[0]).max().getAsInt();
                if (coords.stream().anyMatch(c->c[1]!=c0) || maxR-minR+1 != len) {
                    throw new IOException("Piece '" + id + "' is not straight vertical");
                }
            }
        }
        return new Board(grid, pieces, goalRow, goalCol, exitDir);
    }
}
