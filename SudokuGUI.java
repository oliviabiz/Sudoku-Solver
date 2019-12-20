import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.*;

public class SudokuGUI extends Application{

	Stage s;
	GridPane grid;
	List<TextArea> slots = new ArrayList<TextArea>();
	static HashMap<List<Integer>,TextArea> hm = new HashMap<List<Integer>, TextArea>();
	static Set<TextArea> badNodes = new HashSet<TextArea>();

	@Override
	public void start(Stage stage) throws Exception {
		s = stage;
		s.setResizable(false);

		grid = new GridPane();
		grid.setPadding(new Insets(5,5,0,5));

		for(int i=0; i<9; i++) {
			ColumnConstraints col = new ColumnConstraints();
			col.setPercentWidth(11);
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(11);
			grid.getColumnConstraints().add(col);
			grid.getRowConstraints().add(row);
		}

		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				TextArea ta = new TextArea();
				ta.setPromptText("0");
				ta.setWrapText(true);

				GridPane.setConstraints(ta, i, j);
				grid.getChildren().add(ta);
				slots.add(ta);
				hm.put(Arrays.asList(i,j), ta);

				ta.setOnKeyTyped(e -> validEntry(ta,ta.getText()));;
			}
		}

		HBox bottom = new HBox();
		Button b = new Button("SOLVE ME");
		b.setOnAction(e -> readGrid());
		Button clr = new Button("CLEAR");
		clr.setOnAction(e -> clearGrid());

		bottom.getChildren().addAll(b,clr);
		HBox.setHgrow(b, Priority.ALWAYS);
		bottom.setPadding(new Insets(0,5,5,5));
		b.setMaxWidth(Double.MAX_VALUE);
		bottom.setSpacing(5);

		VBox parent = new VBox(grid,bottom);
		parent.setAlignment(Pos.CENTER);
		VBox.setMargin(b, new Insets(5,5,5,5));
		Scene scene = new Scene(parent,400,400);
		s.setScene(scene);
		s.setTitle("SUDOKU SOLVER");

		s.show();
	}

	public static void displayGrid(Sudoku s) {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				hm.get(Arrays.asList(j,i)).setText(String.valueOf(s.grid[i][j]));
			}
		}
	}

	public void readGrid() {
		if(badNodes.size()>0) {
			badNodes.forEach(ta ->{;
				ta.setText("");
				ta.setStyle("-fx-control-inner-background: white");
			});
			badNodes.clear();
			return;
		}

		int[][] sudogrid = new int[9][9];
		for(TextArea t:slots) {
			if(t.getText()==null || t.getText().length()==0) {
				sudogrid[GridPane.getRowIndex(t)][GridPane.getColumnIndex(t)] = 0;	
			}
			else{
				sudogrid[GridPane.getRowIndex(t)][GridPane.getColumnIndex(t)] = Integer.parseInt(t.getText());
			}
		}

		Sudoku s = new Sudoku(sudogrid);
		s.solve();
		displayGrid(s);
	}

	public static boolean validEntry(TextArea ta, String msg) {
		if(msg.length()!=1) {
			ta.setStyle("-fx-control-inner-background: #ffebe6");
			badNodes.add(ta);
			return false;
		}

		try {
			Integer.parseInt(msg);
			ta.setStyle("-fx-control-inner-background: #f2ffe6");
			badNodes.remove(ta);
			return true;
		}
		catch(NumberFormatException e) {
			ta.setStyle("-fx-control-inner-background: #ffebe6");
			badNodes.add(ta);
			return false;
		}
	}

	public void clearGrid() {
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				hm.get(Arrays.asList(j,i)).setText((""));
				hm.get(Arrays.asList(j,i)).setStyle("-fx-control-inner-background: white");
			}
		}
		badNodes.clear();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
