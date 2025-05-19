package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import utils.Input;

import java.io.File;

public class RushHourApp extends Application {

    private static final int CELL = 60;          // px
    private Canvas boardCanvas;
    private Board currentBoard;
    private ComboBox<String> algoChoice;
    private ListView<String> moveList;

    @Override
    public void start(Stage stage) {
        /* ---------- Controls panel ---------- */
        Button loadBtn  = new Button("Load Board");
        Button solveBtn = new Button("Solve");
        algoChoice      = new ComboBox<>();
        algoChoice.getItems().addAll("GBFS (Greedy)", "A*" /*"UCS", "A*"*/);
        algoChoice.getSelectionModel().selectFirst();

        HBox controlBar = new HBox(10, loadBtn, algoChoice, solveBtn);
        controlBar.setAlignment(Pos.CENTER);

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
        Task<object.Node> task = new Task<>() {
            @Override protected object.Node call() {
                String algo = algoChoice.getValue();
                if (algo.equals("GBFS (Greedy)")) {
                    return GBFS.solve(currentBoard);
                } else if (algo.equals("A*")) {
                    return AStar.solve(currentBoard);
                } // else if (algo.equals("UCS")) {
                //     return UCS.solve(currentBoard);
                // }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            object.Node goal = task.getValue();
            if (goal == null) {
                moveList.getItems().setAll("❌ No solution");
                return;
            }
            List<object.Node> path = goal.getPath();
            List<Board> boards = path.stream().map(n -> n.board).toList();
            List<String> moves  = path.stream().map(n -> n.move == null ? "Start" : n.move).toList();
            moveList.getItems().setAll(moves);
            for (int i = 0; i < path.size(); i++) {
                moveList.getItems().add(i + " : " + path.get(i).move);
            }
            animate(boards);
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
        for (int r = 0; r <= b.height; r++)
            g.strokeLine(0, r*CELL, w, r*CELL);
        for (int c = 0; c <= b.width; c++)
            g.strokeLine(c*CELL, 0, c*CELL, h);

        for (Piece p : b.pieces) {
            Color col = p.isPrimary ? Color.CRIMSON : Color.CADETBLUE;
            g.setFill(col);
            double x = p.col * CELL + 2;
            double y = p.row * CELL + 2;
            double pw = (p.isHorizontal ? p.length : 1) * CELL - 4;
            double ph = (p.isHorizontal ? 1 : p.length) * CELL - 4;
            g.fillRoundRect(x, y, pw, ph, 8, 8);
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
