package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.List;
import object.Board;
import object.Piece;
import algo.AStar;
import algo.GBFS;
import algo.UCS;
import algo.SolveResult;
import utils.Input;

import java.io.File;

public class RushHourApp extends Application {
    
    private static final int CELL = 60;          // px
    private Canvas boardCanvas;
    private Board currentBoard;
    private ComboBox<String> algoChoice;
    private ListView<String> moveList;
    private Label visitedLabel;   // new
    private Label timeLabel;

    @Override
    public void start(Stage stage) {
        /* ---------- Controls panel ---------- */
        Button loadBtn  = new Button("Load Board");
        Button solveBtn = new Button("Solve");
        algoChoice      = new ComboBox<>();
        algoChoice.getItems().addAll("GBFS (Greedy)", "A*", "UCS");
        algoChoice.getSelectionModel().selectFirst();

        HBox controlBar = new HBox(10, loadBtn, algoChoice, solveBtn);
        controlBar.setAlignment(Pos.TOP_CENTER);
        controlBar.setPrefWidth(140); 

        visitedLabel = new Label("Visited: 0");
        timeLabel    = new Label("Time: 0 ms");
        HBox statusBar = new HBox(20, visitedLabel, timeLabel);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(5));

        /* ---------- Drawing surface ---------- */
        boardCanvas = new Canvas();
        StackPane boardPane = new StackPane(boardCanvas);
        boardPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        /* ---------- Move list ---------- */
        moveList = new ListView<>();

        BorderPane root = new BorderPane();
        root.setTop(controlBar);
        root.setCenter(boardPane);
        root.setRight(moveList);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Rush Hour Solver");
        stage.setScene(scene);
        stage.show();

        loadBtn.setOnAction(e -> loadBoard(stage));
        solveBtn.setOnAction(e -> runSolver());
    }

    private void loadBoard(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text board", "*.txt"));
        File f = fc.showOpenDialog(stage);
        if (f == null) return;
        try {
            currentBoard = Input.readBoardFromFile(f.getAbsolutePath());
            drawBoard(currentBoard);
            moveList.getItems().clear();
        } catch (Exception ex) {
            showErr("Failed to load board:\n" + ex.getMessage());
        }
    }

    private void runSolver() {
        if (currentBoard == null) {
            showErr("Load a board first.");
            return;
        }
        moveList.getItems().setAll("⏳ solving …");
        visitedLabel.setText("Visited: 0");
        timeLabel.setText("Time: 0 ms");
        Task<SolveResult> task = new Task<>() {
            @Override protected SolveResult call() {
                String algo = algoChoice.getValue();
                long start = System.currentTimeMillis();
                SolveResult result = null;
                if (algo.equals("GBFS (Greedy)")) {
                    result = GBFS.solve(currentBoard);
                } else if (algo.equals("A*")) {
                    result = AStar.solve(currentBoard);
                } else if (algo.equals("UCS")) {
                    result = UCS.solve(currentBoard);
                }
                long end = System.currentTimeMillis();
                return new SolveResult(result.solution, result.nodesVisited, end - start);
            }
        };
        task.setOnSucceeded(e -> {
            SolveResult result = task.getValue();
            if (result.solution == null) {
                moveList.getItems().setAll("❌ No solution");
                return;
            }
            List<object.Node> path = result.solution.getPath();
            List<Board> boards = path.stream().map(n -> n.board).toList();
            List<String> moves  = path.stream().map(n -> n.move == null ? "Start" : n.move).toList();
            moveList.getItems().setAll(moves);
            for (int i = 0; i < path.size(); i++) {
                moveList.getItems().add(i + " : " + path.get(i).move);
            }
            animate(boards);
            visitedLabel.setText("Visited: " + result.nodesVisited);
            timeLabel.setText("Time: " + result.timeMs + " ms");
        });
        task.setOnFailed(e -> showErr("Solver crashed: " + task.getException()));
        new Thread(task).start();
    }

    // render
    private void drawBoard(Board b) {
        double w = b.width * CELL;
        double h = b.height * CELL;
        boardCanvas.setWidth(w);
        boardCanvas.setHeight(h);

        GraphicsContext g = boardCanvas.getGraphicsContext2D();
        g.clearRect(0,0,w,h);

        g.setStroke(Color.LIGHTGRAY);
        for (int r = 0; r <= b.height; r++) g.strokeLine(0, r*CELL, w, r*CELL);
        for (int c = 0; c <= b.width; c++) g.strokeLine(c*CELL, 0, c*CELL, h);

        for (Piece p : b.pieces) {
            Color col = p.isPrimary ? Color.CRIMSON : Color.CADETBLUE;
            double x = p.col * CELL + 2;
            double y = p.row * CELL + 2;
            double pw = (p.isHorizontal ? p.length : 1) * CELL - 4;
            double ph = (p.isHorizontal ? 1 : p.length) * CELL - 4;
            g.setFill(col);
            g.fillRoundRect(x, y, pw, ph, 8, 8);
            
            g.setFill(Color.WHITE);
            g.setFont(javafx.scene.text.Font.font(18));
            g.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
            g.setTextBaseline(javafx.geometry.VPos.CENTER);
            g.fillText(String.valueOf(p.id), x + pw / 2, y + ph / 2);
        }

    }

    // animasi steps
    private void animate(java.util.List<object.Board> path) {
        new Thread(() -> {
            for (object.Board b : path) {
                Platform.runLater(() -> drawBoard(b));
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void showErr(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    public static void main(String[] args) { launch(); }
}
