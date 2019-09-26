import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MoirePatterns extends Application {
	private int sceneWidth = 1100;
	private int sceneHeight = 600;
	private Image iconImage = new Image(getClass().getResource("/icon.png").toString());
	
	Pane backgroundPatternPane = new Pane();
	Pane foregroundPatternPane = new Pane();
	ScrollPane patternScrollPane = new ScrollPane();
	
	//Value sliders
	Slider rotateSlider = new Slider(0, 360, 0);
	Slider scaleSlider = new Slider(-5, 5, 0);
	Slider xSlider = new Slider(-500, 500, 0);
	Slider ySlider = new Slider(-500, 500, 0);
	
	//Transforms
	Scale foregroundScale = new Scale();
	Rotate foregroundRotate = new Rotate();
	Scale zoomScale = new Scale();
	
	//Create pattern options
	boolean isColor = false;
	int rectCount = 100;
	int circleCount = 60;
	int gridCount = 100;
	int circTriangleCount = 100;
	int squareCount = 60;
	int triangleCount = 800;
	int hexagonCount = 800;
	String shape = "";
	CheckBox lockRotateCheckBox = new CheckBox("Lock rotation, x, and y to center");
	CheckBox colorCheckBox = new CheckBox("Toggle color");
	
	//Animation timer class variables
	AnimationTimer colorAnimation;
	AnimationTimer rotateAnimation;
	AnimationTimer scaleAnimation;
	AnimationTimer xAnimation;
	AnimationTimer yAnimation;
	
	//Default animation class variables
	final double defaultColorAngle = 90;
    
	final double defaultRotateAnimationAngle = 0;
    
	final double defaultScaleAnimationValue = 0;
	final double defaultScaleAnimationAngle = 0;
	final double defaultScaleAnimationLowerBound = -5;
	final double defaultScaleAnimationUpperBound = 5;
	final double defaultScaleAnimationAdjustedLowerBound = 0.1;
	final double defaultScaleAnimationAdjustedUpperBound = 6;
    
	final double defaultXAnimationValue = 0;
	final double defaultXAnimationAngle = 0;
	final double defaultXAnimationLowerBound = -500;
	final double defaultXAnimationUpperBound = 500;
    
	final double defaultYAnimationValue = 0;
	final double defaultYAnimationAngle = 0;
	final double defaultYAnimationLowerBound = -500;
	final double defaultYAnimationUpperBound = 500;
	
	//Animation class variables
	//These strings can't be booleans or ints because they are used to auto select the comboboxes in the animation dialog
	String colorAnimationType = "None";
	double colorAnimationSpeed = 1;
	double colorAngle = defaultColorAngle;
		
	String rotateAnimationType = "None";
	double rotateAnimationSpeed = 1;
	double rotateAnimationAngle = defaultRotateAnimationAngle;
	String rotateAnimationDirection = "Forward";
	String rotateAnimationReverse = "Reset";
		
	String scaleAnimationType = "None";
	double scaleAnimationSpeed = 1;
	double scaleAnimationValue = defaultScaleAnimationValue;
	double scaleAnimationAngle = defaultScaleAnimationAngle;
	String scaleAnimationDirection = "Forward";
	String scaleAnimationReverse = "Reset";
	double scaleAnimationLowerBound = defaultScaleAnimationLowerBound;
	double scaleAnimationUpperBound = defaultScaleAnimationUpperBound;
	double scaleAnimationAdjustedLowerBound = defaultScaleAnimationAdjustedLowerBound;
	double scaleAnimationAdjustedUpperBound = defaultScaleAnimationAdjustedUpperBound;
		
	String xAnimationType = "None";
	double xAnimationSpeed = 1;
	double xAnimationValue = defaultXAnimationValue;
	double xAnimationAngle = defaultXAnimationAngle;
	String xAnimationDirection = "Forward";
	String xAnimationReverse = "Reset";
	double xAnimationLowerBound = defaultXAnimationLowerBound;
	double xAnimationUpperBound = defaultXAnimationUpperBound;
		
	String yAnimationType = "None";
	double yAnimationSpeed = 1;
	double yAnimationValue = defaultYAnimationValue;
	double yAnimationAngle = defaultYAnimationAngle;
	String yAnimationDirection = "Forward";
	String yAnimationReverse = "Reset";
	double yAnimationLowerBound = defaultYAnimationLowerBound;
	double yAnimationUpperBound = defaultYAnimationUpperBound;
	
	//Shape parameters
	int backgroundStripeWidth = 4;
	int backgroundStripeGap = 4;
	int foregroundStripeWidth = 4;
	int foregroundStripeGap = 4;
	
	int backgroundCircleRadius = 4;
	int backgroundCircleGap = 4;
	int foregroundCircleRadius = 4;
	int foregroundCircleGap = 4;
	
	int backgroundGridWidth = 4;
	int backgroundGridGap = 6;
	int foregroundGridWidth = 4;
	int foregroundGridGap = 6;
	
	int backgroundCircTriangleRadius = 500;
	int backgroundCircTriangleWidth = 2;
	int foregroundCircTriangleRadius = 500;
	int foregroundCircTriangleWidth = 2;
	
	int backgroundSquareWidth = 4;
	int backgroundSquareGap = 4;
	int foregroundSquareWidth = 4;
	int foregroundSquareGap = 4;
	
	int backgroundTriangleGap = 15;
	int backgroundTriangleWidth = 2;
	int foregroundTriangleGap = 15;
	int foregroundTriangleWidth = 2;
	
	int backgroundHexagonGap = 15;
	int backgroundHexagonWidth = 2;
	int foregroundHexagonGap = 15;
	int foregroundHexagonWidth = 2;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		//To add a new shape:
		//Add the shape menuItem to the shape menu
		//Create the shape in createPattern() method
		//Add shape to correctColor() method
		//Add shape to animations
		//Add shape parameters to parameters menu
		//Add shape to export menuItem
		//Add shape to importConfiguration() method
		//Add shape to reset() method
		
		backgroundPatternPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		
		foregroundPatternPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		foregroundPatternPane.getTransforms().add(foregroundScale);
		foregroundPatternPane.getTransforms().add(foregroundRotate);
		
		colorAnimation = new AnimationTimer() {
			public void handle(long now) {
				switch(colorAnimationType) {
					case "Linear":
						switch(shape) {
							case "stripes":
							case "grid":
							case "circTriangles":
								for(int i = 0; i < foregroundPatternPane.getChildren().size(); i++) {
									((Shape) foregroundPatternPane.getChildren().get(i)).setFill(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getFill()).deriveColor(colorAnimationSpeed, 1, 1, 1));
									((Shape) backgroundPatternPane.getChildren().get(i)).setFill(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getFill()).deriveColor(colorAnimationSpeed, 1, 1, 1));
								}
								
								break;
								
							case "circles":
							case "squares":
								for(int i = 0; i < foregroundPatternPane.getChildren().size(); i++) {
									((Shape) foregroundPatternPane.getChildren().get(i)).setStroke(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getStroke()).deriveColor(colorAnimationSpeed, 1, 1, 1));
									((Shape) backgroundPatternPane.getChildren().get(i)).setStroke(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getStroke()).deriveColor(colorAnimationSpeed, 1, 1, 1));
								}
								
								break;
						}
						
						break;
					
					case "Trigononometric":
						switch(shape) {
							case "stripes":
							case "grid":
							case "circTriangles":
								for(int i = 0; i < foregroundPatternPane.getChildren().size(); i++) {
									((Shape) foregroundPatternPane.getChildren().get(i)).setFill(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getFill()).deriveColor((Math.sin(colorAngle)) * (2 * colorAnimationSpeed), 1, 1, 1));
									((Shape) backgroundPatternPane.getChildren().get(i)).setFill(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getFill()).deriveColor((Math.sin(colorAngle)) * (2 * colorAnimationSpeed), 1, 1, 1));
								}
								
								break;
								
							case "circles":
							case "squares":
								for(int i = 0; i < foregroundPatternPane.getChildren().size(); i++) {
									((Shape) foregroundPatternPane.getChildren().get(i)).setStroke(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getStroke()).deriveColor((Math.sin(colorAngle)) * (2 * colorAnimationSpeed), 1, 1, 1));
									((Shape) backgroundPatternPane.getChildren().get(i)).setStroke(((Color) ((Shape) foregroundPatternPane.getChildren().get(i)).getStroke()).deriveColor((Math.sin(colorAngle)) * (2 * colorAnimationSpeed), 1, 1, 1));
								}
								
								break;
						}
						
						colorAngle += (0.01 * colorAnimationSpeed);
						
						break;
				}
			}
		};
		
		rotateAnimation = new AnimationTimer() {
			public void handle(long now) {
				switch(rotateAnimationType) {
					case "Linear":
						rotateSlider.setValue(rotateAnimationAngle);
						
						switch(shape) {
							case "stripes":
							case "grid":
							case "circTriangles":
								if(rotateAnimationDirection.equals("Forward")) {
									rotateAnimationAngle += (0.03 * rotateAnimationSpeed);
								} else {
									rotateAnimationAngle -= (0.03 * rotateAnimationSpeed);
								}
								
								break;
								
							case "circles":
							case "squares":
								if(rotateAnimationDirection.equals("Forward")) {
									rotateAnimationAngle += (0.09 * rotateAnimationSpeed);
								} else {
									rotateAnimationAngle -= (0.09 * rotateAnimationSpeed);
								}
								
								break;
						}
						
						if(rotateAnimationAngle > 360) {
							if(rotateAnimationReverse.equals("Reset")) {
								rotateAnimationAngle -= 360;
							} else {
								rotateAnimationDirection = "Backward";
							}
						} else if(rotateAnimationAngle < 0) {
							if(rotateAnimationReverse.equals("Reset")) {
								rotateAnimationAngle += 360;
							} else {
								rotateAnimationDirection = "Forward";
							}
						}
						
						break;
					case "Trigononometric":
						rotateSlider.setValue(((Math.sin(Math.toRadians(rotateAnimationAngle - 90)) + 1) * 180));
						
						switch(shape) {
							case "stripes":
							case "grid":
							case "circTriangles":
								if(rotateAnimationDirection.equals("Forward")) {
									rotateAnimationAngle += (0.02 * rotateAnimationSpeed);
								} else {
									rotateAnimationAngle -= (0.02 * rotateAnimationSpeed);
								}
								
								break;
								
							case "circles":
							case "squares":
								if(rotateAnimationDirection.equals("Forward")) {
									rotateAnimationAngle += (0.06 * rotateAnimationSpeed);
								} else {
									rotateAnimationAngle -= (0.06 * rotateAnimationSpeed);
								}
								
								break;
						}
						
						if(rotateAnimationAngle > 180) {
							if(rotateAnimationReverse.equals("Reset")) {
								rotateAnimationAngle -= 180;
							} else {
								rotateAnimationDirection = "Backward";
							}
						} else if(rotateAnimationAngle < 0) {
							if(rotateAnimationReverse.equals("Reset")) {
								rotateAnimationAngle += 180;
							} else {
								rotateAnimationDirection = "Forward";
							}
						}
						
						break;
				}
			}
		};
		
		scaleAnimation = new AnimationTimer() {
			public void handle(long now) {
				switch(scaleAnimationType) {
					case "Linear":
						if(scaleAnimationDirection.equals("Forward")) {
							scaleAnimationValue = scaleSlider.getValue() + (0.002 * scaleAnimationSpeed);
						} else {
							scaleAnimationValue = scaleSlider.getValue() - (0.002 * scaleAnimationSpeed);
						}
						
						if(scaleAnimationValue > scaleAnimationUpperBound) {
							if(scaleAnimationReverse.equals("Reset")) {
								scaleSlider.setValue(scaleAnimationLowerBound);
							} else {
								scaleAnimationDirection = "Backward";
								scaleSlider.setValue(scaleAnimationValue);
							}
						} else if(scaleAnimationValue < scaleAnimationLowerBound) {
							if(scaleAnimationReverse.equals("Reset")) {
								scaleSlider.setValue(scaleAnimationUpperBound);
							} else {
								scaleAnimationDirection = "Forward";
								scaleSlider.setValue(scaleAnimationValue);
							}
						} else {
							scaleSlider.setValue(scaleAnimationValue);
						}
						
						break;
						
					case "Trigononometric":
						//scaleAnimationAngle = (0, 180)
						//scaleAnimationAngle - 90 = (-90, 90)
						//Math.toRadians(scaleAnimationAngle - 90) = (3 PI / 2, PI / 2)
						//Math.sin(Math.toRadians(scaleAnimationAngle - 90)) = (-1, 0, 1)
						//Math.sin(Math.toRadians(scaleAnimationAngle - 90)) + 1 = (0, 1, 2)
						//(Math.sin(Math.toRadians(scaleAnimationAngle - 90)) + 1) / 2 = (0, 0.5, 1)
						//((Math.sin(Math.toRadians(scaleAnimationAngle - 90)) + 1) / 2) * Math.abs(scaleAnimationUpperBound - scaleAnimationLowerBound) = (0, sliderBoundWidth / 2, sliderBoundWidth)
						//(((Math.sin(Math.toRadians(scaleAnimationAngle - 90)) + 1) / 2) * Math.abs(scaleAnimationUpperBound - scaleAnimationLowerBound)) + scaleAnimationLowerBound = (lowerBound, lowerBound + (sliderBoundWidth / 2), lowerBound + sliderBoundWidth)
						scaleSlider.setValue((((Math.sin(Math.toRadians(scaleAnimationAngle - 90)) + 1) / 2) * Math.abs(scaleAnimationUpperBound - scaleAnimationLowerBound)) + scaleAnimationLowerBound);
						
						if(scaleAnimationDirection.equals("Forward")) {
							//Scale speed up when sin wave period width is small (and slow):
							//(1 / (Math.abs(scaleAnimationUpperBound - scaleAnimationLowerBound) / (scaleSlider.getMax() - scaleSlider.getMin())))
							scaleAnimationAngle += 0.02 * scaleAnimationSpeed * (1 / (Math.abs(scaleAnimationUpperBound - scaleAnimationLowerBound) / (scaleSlider.getMax() - scaleSlider.getMin())));
						} else {
							scaleAnimationAngle -= 0.02 * scaleAnimationSpeed * (1 / (Math.abs(scaleAnimationUpperBound - scaleAnimationLowerBound) / (scaleSlider.getMax() - scaleSlider.getMin())));
						}
						
						if(scaleAnimationAngle > 180) {
							if(scaleAnimationReverse.equals("Reset")) {
								scaleAnimationAngle -= 180;
							} else {
								scaleAnimationDirection = "Backward";
							}
						} else if(scaleAnimationAngle < 0) {
							if(scaleAnimationReverse.equals("Reset")) {
								scaleAnimationAngle += 180;
							} else {
								scaleAnimationDirection = "Forward";
							}
						}
						
						break;
				}
			}
		};
		
		xAnimation = new AnimationTimer() {
			public void handle(long now) {
				switch(xAnimationType) {
					case "Linear":
						if(xAnimationDirection.equals("Forward")) {
							xAnimationValue = xSlider.getValue() + (0.1 * xAnimationSpeed);
						} else {
							xAnimationValue = xSlider.getValue() - (0.1 * xAnimationSpeed);
						}
						
						if(xAnimationValue > xAnimationUpperBound) {
							if(xAnimationReverse.equals("Reset")) {
								xSlider.setValue(xAnimationLowerBound);
							} else {
								xAnimationDirection = "Backward";
								xSlider.setValue(xAnimationValue);
							}
						} else if(xAnimationValue < xAnimationLowerBound) {
							if(xAnimationReverse.equals("Reset")) {
								xSlider.setValue(xAnimationUpperBound);
							} else {
								xAnimationDirection = "Forward";
								xSlider.setValue(xAnimationValue);
							}
						} else {
							xSlider.setValue(xAnimationValue);
						}
						
						break;
						
					case "Trigononometric":
						//xAnimationAngle = (0, 180)
						//xAnimationAngle - 90 = (-90, 90)
						//Math.toRadians(xAnimationAngle - 90) = (3 PI / 2, PI / 2)
						//Math.sin(Math.toRadians(xAnimationAngle - 90)) = (-1, 0, 1)
						//Math.sin(Math.toRadians(xAnimationAngle - 90)) + 1 = (0, 1, 2)
						//(Math.sin(Math.toRadians(xAnimationAngle - 90)) + 1) / 2 = (0, 0.5, 1)
						//((Math.sin(Math.toRadians(xAnimationAngle - 90)) + 1) / 2) * Math.abs(xAnimationUpperBound - xAnimationLowerBound) = (0, sliderBoundWidth / 2, sliderBoundWidth)
						//(((Math.sin(Math.toRadians(xAnimationAngle - 90)) + 1) / 2) * Math.abs(xAnimationUpperBound - xAnimationLowerBound)) + xAnimationLowerBound = (lowerBound, lowerBound + (sliderBoundWidth / 2), lowerBound + sliderBoundWidth)
						xSlider.setValue((((Math.sin(Math.toRadians(xAnimationAngle - 90)) + 1) / 2) * Math.abs(xAnimationUpperBound - xAnimationLowerBound)) + xAnimationLowerBound);
						
						if(xAnimationDirection.equals("Forward")) {
							//Scale speed up when sin wave period width is small (and slow):
							//(1 / (Math.abs(xAnimationUpperBound - xAnimationLowerBound) / 10))
							xAnimationAngle += 0.03 * xAnimationSpeed * (1 / (Math.abs(xAnimationUpperBound - xAnimationLowerBound) / (xSlider.getMax() - xSlider.getMin())));
						} else {
							xAnimationAngle -= 0.03 * xAnimationSpeed * (1 / (Math.abs(xAnimationUpperBound - xAnimationLowerBound) / (xSlider.getMax() - xSlider.getMin())));
						}
						
						if(xAnimationAngle > 180) {
							if(xAnimationReverse.equals("Reset")) {
								xAnimationAngle -= 180;
							} else {
								xAnimationDirection = "Backward";
							}
						} else if(xAnimationAngle < 0) {
							if(xAnimationReverse.equals("Reset")) {
								xAnimationAngle += 180;
							} else {
								xAnimationDirection = "Forward";
							}
						}
						
						break;
				}
			}
		};
		
		yAnimation = new AnimationTimer() {
			public void handle(long now) {
				switch(yAnimationType) {
					case "Linear":
						if(yAnimationDirection.equals("Forward")) {
							yAnimationValue = ySlider.getValue() + (0.1 * yAnimationSpeed);
						} else {
							yAnimationValue = ySlider.getValue() - (0.1 * yAnimationSpeed);
						}
						
						if(yAnimationValue > yAnimationUpperBound) {
							if(yAnimationReverse.equals("Reset")) {
								ySlider.setValue(yAnimationLowerBound);
							} else {
								yAnimationDirection = "Backward";
								ySlider.setValue(yAnimationValue);
							}
						} else if(yAnimationValue < yAnimationLowerBound) {
							if(yAnimationReverse.equals("Reset")) {
								ySlider.setValue(yAnimationUpperBound);
							} else {
								yAnimationDirection = "Forward";
								ySlider.setValue(yAnimationValue);
							}
						} else {
							ySlider.setValue(yAnimationValue);
						}
						
						break;
						
					case "Trigononometric":
						//yAnimationAngle = (0, 180)
						//yAnimationAngle - 90 = (-90, 90)
						//Math.toRadians(yAnimationAngle - 90) = (3 PI / 2, PI / 2)
						//Math.sin(Math.toRadians(yAnimationAngle - 90)) = (-1, 0, 1)
						//Math.sin(Math.toRadians(yAnimationAngle - 90)) + 1 = (0, 1, 2)
						//(Math.sin(Math.toRadians(yAnimationAngle - 90)) + 1) / 2 = (0, 0.5, 1)
						//((Math.sin(Math.toRadians(yAnimationAngle - 90)) + 1) / 2) * Math.abs(yAnimationUpperBound - yAnimationLowerBound) = (0, sliderBoundWidth / 2, sliderBoundWidth)
						//(((Math.sin(Math.toRadians(yAnimationAngle - 90)) + 1) / 2) * Math.abs(yAnimationUpperBound - yAnimationLowerBound)) + yAnimationLowerBound = (lowerBound, lowerBound + (sliderBoundWidth / 2), lowerBound + sliderBoundWidth)
						ySlider.setValue((((Math.sin(Math.toRadians(yAnimationAngle - 90)) + 1) / 2) * Math.abs(yAnimationUpperBound - yAnimationLowerBound)) + yAnimationLowerBound);
						
						if(yAnimationDirection.equals("Forward")) {
							//Scale speed up when sin wave period width is small (and slow):
							//(1 / (Math.abs(yAnimationUpperBound - yAnimationLowerBound) / 10))
							yAnimationAngle += 0.03 * yAnimationSpeed * (1 / (Math.abs(yAnimationUpperBound - yAnimationLowerBound) / (ySlider.getMax() - ySlider.getMin())));
						} else {
							yAnimationAngle -= 0.03 * yAnimationSpeed * (1 / (Math.abs(yAnimationUpperBound - yAnimationLowerBound) / (ySlider.getMax() - ySlider.getMin())));
						}
						
						if(yAnimationAngle > 180) {
							if(yAnimationReverse.equals("Reset")) {
								yAnimationAngle -= 180;
							} else {
								yAnimationDirection = "Backward";
							}
						} else if(yAnimationAngle < 0) {
							if(yAnimationReverse.equals("Reset")) {
								yAnimationAngle += 180;
							} else {
								yAnimationDirection = "Forward";
							}
						}
						
						break;
				}
			}
		};
		
		Pane patternPane = new Pane(backgroundPatternPane, foregroundPatternPane);
		patternPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		patternPane.getTransforms().add(zoomScale);
		
		MenuItem stripesMenuItem = new MenuItem("Stripes");
		
		stripesMenuItem.setOnAction(e -> {
			shape = "stripes";
			createPattern();
		});
		
		MenuItem circlesMenuItem = new MenuItem("Circles");
		
		circlesMenuItem.setOnAction(e -> {
			shape = "circles";
			createPattern();
		});
		
		MenuItem gridMenuItem = new MenuItem("Grid");
		
		gridMenuItem.setOnAction(e -> {
			shape = "grid";
			createPattern();
		});
		
		MenuItem circTrianglesMenuItem = new MenuItem("Circular triangles");
		
		circTrianglesMenuItem.setOnAction(e -> {
			shape = "circTriangles";
			createPattern();
		});
		
		MenuItem squaresMenuItem = new MenuItem("Squares");
		
		squaresMenuItem.setOnAction(e -> {
			shape = "squares";
			createPattern();
		});
		
		MenuItem trianglesMenuItem = new MenuItem("Triangles");
		
		trianglesMenuItem.setOnAction(e -> {
			shape = "triangles";
			createPattern();
		});
		
		MenuItem hexagonsMenuItem = new MenuItem("Hexagons");
		
		hexagonsMenuItem.setOnAction(e -> {
			shape = "hexagons";
			createPattern();
		});
		
		Menu shapesMenu = new Menu("Shapes");
		shapesMenu.getItems().addAll(stripesMenuItem, circlesMenuItem, gridMenuItem, circTrianglesMenuItem, squaresMenuItem, trianglesMenuItem, hexagonsMenuItem);
		
		MenuItem animateItemHistory = new MenuItem();
		
		Menu animateMenu = new Menu("Animate");
		animateMenu.getItems().addAll(animateItemHistory);
		animateMenu.addEventHandler(Menu.ON_SHOWN, event -> animateMenu.hide());
		animateMenu.addEventHandler(Menu.ON_SHOWING, event -> animateMenu.fire());
		
		animateMenu.setOnAction(e -> {
			//COLOR ANIMATION
			
			
			Label colorAnimationTypeLabel = new Label("Animation type:");
			
			ComboBox<String> colorAnimationTypeComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"None", "Linear", "Trigononometric"}));
			colorAnimationTypeComboBox.getSelectionModel().select(colorAnimationType);
			colorAnimationTypeComboBox.setOnAction(e2 -> {
				colorAnimationType = colorAnimationTypeComboBox.getValue();
				
				switch(colorAnimationTypeComboBox.getValue()) {
					case "None":
						colorAnimation.stop();
						
						break;
					case "Linear":
						colorAnimation.start();
						
						break;
					case "Trigononometric":
						colorAnimation.start();
						
						break;
				}
			});
			
			Label colorAnimationSpeedLabel = new Label("Animation speed:");
			
			ComboBox<String> colorAnimationSpeedComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"1.0x", "1.5x", "2.0x", "3.0x"}));
			colorAnimationSpeedComboBox.getSelectionModel().select(colorAnimationSpeed + "x");
			colorAnimationSpeedComboBox.setOnAction(e2 -> {
				switch(colorAnimationSpeedComboBox.getValue()) {
					case "1.0x":
						colorAnimationSpeed = 1;
						
						break;
					case "1.5x":
						colorAnimationSpeed = 1.5;
						
						break;
					case "2.0x":
						colorAnimationSpeed = 2;
						
						break;
					case "3.0x":
						colorAnimationSpeed = 3;
						
						break;
				}
			});
			
			GridPane colorAnimationGridPane = new GridPane();
			colorAnimationGridPane.setPadding(new Insets(5, 5, 5, 5));
			colorAnimationGridPane.setHgap(10);
			colorAnimationGridPane.setVgap(10);
			colorAnimationGridPane.add(colorAnimationTypeLabel, 0, 0);
			colorAnimationGridPane.add(colorAnimationTypeComboBox, 1, 0);
			colorAnimationGridPane.add(colorAnimationSpeedLabel, 0, 1);
			colorAnimationGridPane.add(colorAnimationSpeedComboBox, 1, 1);
			
			Tab colorTab = new Tab("Color", colorAnimationGridPane);
			colorTab.setClosable(false);
			
			
			//ROATION ANIMATION
			
			
			Label rotateAnimationTypeLabel = new Label("Animation type:");
			
			ComboBox<String> rotateAnimationTypeComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"None", "Linear", "Trigononometric"}));
			rotateAnimationTypeComboBox.getSelectionModel().select(rotateAnimationType);
			rotateAnimationTypeComboBox.setOnAction(e2 -> {
				rotateAnimationType = rotateAnimationTypeComboBox.getValue();
				
				switch(rotateAnimationTypeComboBox.getValue()) {
					case "None":
						rotateAnimation.stop();
						
						break;
					case "Linear":
						rotateAnimation.start();
						
						break;
					case "Trigononometric":
						rotateAnimation.start();
						
						break;
				}
			});
			
			Label rotateAnimationSpeedLabel = new Label("Animation speed:");
			
			ComboBox<String> rotateAnimationSpeedComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"1.0x", "1.5x", "2.0x", "3.0x", "5.0x", "10.0x", "20.0x", "50.0x"}));
			rotateAnimationSpeedComboBox.getSelectionModel().select(rotateAnimationSpeed + "x");
			rotateAnimationSpeedComboBox.setOnAction(e2 -> {
				switch(rotateAnimationSpeedComboBox.getValue()) {
					case "1.0x":
						rotateAnimationSpeed = 1;
						
						break;
					case "1.5x":
						rotateAnimationSpeed = 1.5;
						
						break;
					case "2.0x":
						rotateAnimationSpeed = 2;
						
						break;
					case "3.0x":
						rotateAnimationSpeed = 3;
						
						break;
					case "5.0x":
						rotateAnimationSpeed = 5;
						
						break;
					case "10.0x":
						rotateAnimationSpeed = 10;
						
						break;
					case "20.0x":
						rotateAnimationSpeed = 20;
						
						break;
					case "50.0x":
						rotateAnimationSpeed = 50;
						
						break;
				}
			});
			
			Label rotateAnimationDirectionLabel = new Label("Animation direction:");
			
			ComboBox<String> rotateAnimationDirectionComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Forward", "Backward"}));
			rotateAnimationDirectionComboBox.getSelectionModel().select(rotateAnimationDirection);
			rotateAnimationDirectionComboBox.setOnAction(e2 -> {
				rotateAnimationDirection = rotateAnimationDirectionComboBox.getValue();
			});
			
			Label rotateAnimationReverseLabel = new Label("Auto-reverse animation:");
			
			ComboBox<String> rotateAnimationReverseComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Reset", "Auto-reverse"}));
			rotateAnimationReverseComboBox.getSelectionModel().select(rotateAnimationReverse);
			rotateAnimationReverseComboBox.setOnAction(e2 -> {
				rotateAnimationReverse = rotateAnimationReverseComboBox.getValue();
			});
			
			GridPane rotateAnimationGridPane = new GridPane();
			rotateAnimationGridPane.setPadding(new Insets(5, 5, 5, 5));
			rotateAnimationGridPane.setHgap(10);
			rotateAnimationGridPane.setVgap(10);
			rotateAnimationGridPane.add(rotateAnimationTypeLabel, 0, 0);
			rotateAnimationGridPane.add(rotateAnimationTypeComboBox, 1, 0);
			rotateAnimationGridPane.add(rotateAnimationSpeedLabel, 0, 1);
			rotateAnimationGridPane.add(rotateAnimationSpeedComboBox, 1, 1);
			rotateAnimationGridPane.add(rotateAnimationDirectionLabel, 0, 2);
			rotateAnimationGridPane.add(rotateAnimationDirectionComboBox, 1, 2);
			rotateAnimationGridPane.add(rotateAnimationReverseLabel, 0, 3);
			rotateAnimationGridPane.add(rotateAnimationReverseComboBox, 1, 3);
			
			Tab rotationTab = new Tab("Rotation", rotateAnimationGridPane);
			rotationTab.setClosable(false);
			
			
			//SCALE ANIMATION
			
			
			Label scaleAnimationTypeLabel = new Label("Animation type:");
			
			ComboBox<String> scaleAnimationTypeComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"None", "Linear", "Trigononometric"}));
			scaleAnimationTypeComboBox.getSelectionModel().select(scaleAnimationType);
			scaleAnimationTypeComboBox.setOnAction(e2 -> {
				scaleAnimationType = scaleAnimationTypeComboBox.getValue();
				
				switch(scaleAnimationTypeComboBox.getValue()) {
					case "None":
						scaleAnimation.stop();
						
						break;
					case "Linear":
						scaleAnimation.start();
						
						break;
					case "Trigononometric":
						scaleAnimation.start();
						
						break;
				}
			});
			
			Label scaleAnimationSpeedLabel = new Label("Animation speed:");
			
			ComboBox<String> scaleAnimationSpeedComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"1.0x", "1.5x", "2.0x", "3.0x", "5.0x", "10.0x", "20.0x", "50.0x"}));
			scaleAnimationSpeedComboBox.getSelectionModel().select(scaleAnimationSpeed + "x");
			scaleAnimationSpeedComboBox.setOnAction(e2 -> {
				switch(scaleAnimationSpeedComboBox.getValue()) {
					case "1.0x":
						scaleAnimationSpeed = 1;
						
						break;
					case "1.5x":
						scaleAnimationSpeed = 1.5;
						
						break;
					case "2.0x":
						scaleAnimationSpeed = 2;
						
						break;
					case "3.0x":
						scaleAnimationSpeed = 3;
						
						break;
					case "5.0x":
						scaleAnimationSpeed = 5;
						
						break;
					case "10.0x":
						scaleAnimationSpeed = 10;
						
						break;
					case "20.0x":
						scaleAnimationSpeed = 20;
						
						break;
					case "50.0x":
						scaleAnimationSpeed = 50;
						
						break;
				}
			});
			
			Label scaleAnimationDirectionLabel = new Label("Animation direction:");
			
			ComboBox<String> scaleAnimationDirectionComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Forward", "Backward"}));
			scaleAnimationDirectionComboBox.getSelectionModel().select(scaleAnimationDirection);
			scaleAnimationDirectionComboBox.setOnAction(e2 -> {
				scaleAnimationDirection = scaleAnimationDirectionComboBox.getValue();
			});
			
			Label scaleAnimationReverseLabel = new Label("Auto-reverse animation:");
			
			ComboBox<String> scaleAnimationReverseComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Reset", "Auto-reverse"}));
			scaleAnimationReverseComboBox.getSelectionModel().select(scaleAnimationReverse);
			scaleAnimationReverseComboBox.setOnAction(e2 -> {
				scaleAnimationReverse = scaleAnimationReverseComboBox.getValue();
			});
			
			Label scaleAnimationLowerBoundLabel = new Label("Lower bound (0.1-6.0):");
			
			TextField scaleAnimationLowerBoundTextField = new TextField(scaleAnimationAdjustedLowerBound + "");
			scaleAnimationLowerBoundTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("^\\d*\\.?\\d*$")) {
						scaleAnimationLowerBoundTextField.setText(oldValue);
						return;
					}
					
					try {
						double doubleValue = Double.parseDouble(scaleAnimationLowerBoundTextField.getText());
						
						if(doubleValue < 0.1) {
							scaleAnimationAdjustedLowerBound = 0.1;
							scaleAnimationLowerBound = -5;
						} else if(doubleValue > 6) {
							scaleAnimationAdjustedLowerBound = 6;
							scaleAnimationLowerBound = 5;
						} else {
							scaleAnimationAdjustedLowerBound = doubleValue;
							
							if(doubleValue >= 1) {
								scaleAnimationLowerBound = (doubleValue - 1);
							} else {
								scaleAnimationLowerBound = 10 * (((5.0 / 9.0) * doubleValue) - (5.0 / 9.0));
							}
						}
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label scaleAnimationUpperBoundLabel = new Label("Upper bound (0.1-6.0):");
			
			TextField scaleAnimationUpperBoundTextField = new TextField(scaleAnimationAdjustedUpperBound + "");
			scaleAnimationUpperBoundTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("^\\d*\\.?\\d*$")) {
						scaleAnimationUpperBoundTextField.setText(oldValue);
						return;
					}
					
					try {
						double doubleValue = Double.parseDouble(scaleAnimationUpperBoundTextField.getText());
						
						if(doubleValue < 0.1) {
							scaleAnimationAdjustedUpperBound = 0.1;
							scaleAnimationUpperBound = -5;
						} else if(doubleValue > 6) {
							scaleAnimationAdjustedUpperBound = 6;
							scaleAnimationUpperBound = 5;
						} else {
							scaleAnimationAdjustedUpperBound = doubleValue;
							
							if(doubleValue >= 1) {
								scaleAnimationUpperBound = (doubleValue - 1);
							} else {
								scaleAnimationUpperBound = 10 * (((5.0 / 9.0) * doubleValue) - (5.0 / 9.0));
							}
						}
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane scaleAnimationGridPane = new GridPane();
			scaleAnimationGridPane.setPadding(new Insets(5, 5, 5, 5));
			scaleAnimationGridPane.setHgap(10);
			scaleAnimationGridPane.setVgap(10);
			scaleAnimationGridPane.add(scaleAnimationTypeLabel, 0, 0);
			scaleAnimationGridPane.add(scaleAnimationTypeComboBox, 1, 0);
			scaleAnimationGridPane.add(scaleAnimationSpeedLabel, 0, 1);
			scaleAnimationGridPane.add(scaleAnimationSpeedComboBox, 1, 1);
			scaleAnimationGridPane.add(scaleAnimationDirectionLabel, 0, 2);
			scaleAnimationGridPane.add(scaleAnimationDirectionComboBox, 1, 2);
			scaleAnimationGridPane.add(scaleAnimationReverseLabel, 0, 3);
			scaleAnimationGridPane.add(scaleAnimationReverseComboBox, 1, 3);
			scaleAnimationGridPane.add(scaleAnimationLowerBoundLabel, 0, 4);
			scaleAnimationGridPane.add(scaleAnimationLowerBoundTextField, 1, 4);
			scaleAnimationGridPane.add(scaleAnimationUpperBoundLabel, 0, 5);
			scaleAnimationGridPane.add(scaleAnimationUpperBoundTextField, 1, 5);
			
			Tab scaleTab = new Tab("Scale", scaleAnimationGridPane);
			scaleTab.setClosable(false);
			
			
			//X ANIMATION
			
			
			Label xAnimationTypeLabel = new Label("Animation type:");
			
			ComboBox<String> xAnimationTypeComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"None", "Linear", "Trigononometric"}));
			xAnimationTypeComboBox.getSelectionModel().select(xAnimationType);
			xAnimationTypeComboBox.setOnAction(e2 -> {
				xAnimationType = xAnimationTypeComboBox.getValue();
				
				switch(xAnimationTypeComboBox.getValue()) {
					case "None":
						xAnimation.stop();
						
						break;
					case "Linear":
						xAnimation.start();
						
						break;
					case "Trigononometric":
						xAnimation.start();
						
						break;
				}
			});
			
			Label xAnimationSpeedLabel = new Label("Animation speed:");
			
			ComboBox<String> xAnimationSpeedComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"1.0x", "1.5x", "2.0x", "3.0x", "5.0x", "10.0x", "20.0x", "50.0x"}));
			xAnimationSpeedComboBox.getSelectionModel().select(xAnimationSpeed + "x");
			xAnimationSpeedComboBox.setOnAction(e2 -> {
				switch(xAnimationSpeedComboBox.getValue()) {
					case "1.0x":
						xAnimationSpeed = 1;
						
						break;
					case "1.5x":
						xAnimationSpeed = 1.5;
						
						break;
					case "2.0x":
						xAnimationSpeed = 2;
						
						break;
					case "3.0x":
						xAnimationSpeed = 3;
						
						break;
					case "5.0x":
						xAnimationSpeed = 5;
						
						break;
					case "10.0x":
						xAnimationSpeed = 10;
						
						break;
					case "20.0x":
						xAnimationSpeed = 20;
						
						break;
					case "50.0x":
						xAnimationSpeed = 50;
						
						break;
				}
			});
			
			Label xAnimationDirectionLabel = new Label("Animation direction:");
			
			ComboBox<String> xAnimationDirectionComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Forward", "Backward"}));
			xAnimationDirectionComboBox.getSelectionModel().select(xAnimationDirection);
			xAnimationDirectionComboBox.setOnAction(e2 -> {
				xAnimationDirection = xAnimationDirectionComboBox.getValue();
			});
			
			Label xAnimationReverseLabel = new Label("Auto-reverse animation:");
			
			ComboBox<String> xAnimationReverseComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Reset", "Auto-reverse"}));
			xAnimationReverseComboBox.getSelectionModel().select(xAnimationReverse);
			xAnimationReverseComboBox.setOnAction(e2 -> {
				xAnimationReverse = xAnimationReverseComboBox.getValue();
			});
			
			Label xAnimationLowerBoundLabel = new Label("Lower bound (0.1-6.0):");
			
			TextField xAnimationLowerBoundTextField = new TextField(xAnimationLowerBound + "");
			xAnimationLowerBoundTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("^-?\\d*\\.?\\d*$")) {
						xAnimationLowerBoundTextField.setText(oldValue);
						return;
					}
					
					try {
						double doubleValue = Double.parseDouble(xAnimationLowerBoundTextField.getText());
						
						if(doubleValue < -500) {
							xAnimationLowerBound = -500;
						} else if(doubleValue > 500) {
							xAnimationLowerBound = 500;
						} else {
							xAnimationLowerBound = doubleValue;
						}
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label xAnimationUpperBoundLabel = new Label("Upper bound (0.1-6.0):");
			
			TextField xAnimationUpperBoundTextField = new TextField(xAnimationUpperBound + "");
			xAnimationUpperBoundTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("^\\d*\\.?\\d*$")) {
						xAnimationUpperBoundTextField.setText(oldValue);
						return;
					}
					
					try {
						double doubleValue = Double.parseDouble(xAnimationUpperBoundTextField.getText());
						
						if(doubleValue < -500) {
							xAnimationUpperBound = -500;
						} else if(doubleValue > 500) {
							xAnimationUpperBound = 500;
						} else {
							xAnimationUpperBound = doubleValue;
						}
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane xAnimationGridPane = new GridPane();
			xAnimationGridPane.setPadding(new Insets(5, 5, 5, 5));
			xAnimationGridPane.setHgap(10);
			xAnimationGridPane.setVgap(10);
			xAnimationGridPane.add(xAnimationTypeLabel, 0, 0);
			xAnimationGridPane.add(xAnimationTypeComboBox, 1, 0);
			xAnimationGridPane.add(xAnimationSpeedLabel, 0, 1);
			xAnimationGridPane.add(xAnimationSpeedComboBox, 1, 1);
			xAnimationGridPane.add(xAnimationDirectionLabel, 0, 2);
			xAnimationGridPane.add(xAnimationDirectionComboBox, 1, 2);
			xAnimationGridPane.add(xAnimationReverseLabel, 0, 3);
			xAnimationGridPane.add(xAnimationReverseComboBox, 1, 3);
			xAnimationGridPane.add(xAnimationLowerBoundLabel, 0, 4);
			xAnimationGridPane.add(xAnimationLowerBoundTextField, 1, 4);
			xAnimationGridPane.add(xAnimationUpperBoundLabel, 0, 5);
			xAnimationGridPane.add(xAnimationUpperBoundTextField, 1, 5);
			
			Tab xTab = new Tab("X-Postion", xAnimationGridPane);
			xTab.setClosable(false);
			
			
			//Y ANIMATION
			
			
			Label yAnimationTypeLabel = new Label("Animation type:");
			
			ComboBox<String> yAnimationTypeComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"None", "Linear", "Trigononometric"}));
			yAnimationTypeComboBox.getSelectionModel().select(yAnimationType);
			yAnimationTypeComboBox.setOnAction(e2 -> {
				yAnimationType = yAnimationTypeComboBox.getValue();
				
				switch(yAnimationTypeComboBox.getValue()) {
					case "None":
						yAnimation.stop();
						
						break;
					case "Linear":
						yAnimation.start();
						
						break;
					case "Trigononometric":
						yAnimation.start();
						
						break;
				}
			});
			
			Label yAnimationSpeedLabel = new Label("Animation speed:");
			
			ComboBox<String> yAnimationSpeedComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"1.0x", "1.5x", "2.0x", "3.0x", "5.0x", "10.0x", "20.0x", "50.0x"}));
			yAnimationSpeedComboBox.getSelectionModel().select(yAnimationSpeed + "x");
			yAnimationSpeedComboBox.setOnAction(e2 -> {
				switch(yAnimationSpeedComboBox.getValue()) {
					case "1.0x":
						yAnimationSpeed = 1;
						
						break;
					case "1.5x":
						yAnimationSpeed = 1.5;
						
						break;
					case "2.0x":
						yAnimationSpeed = 2;
						
						break;
					case "3.0x":
						yAnimationSpeed = 3;
						
						break;
					case "5.0x":
						yAnimationSpeed = 5;
						
						break;
					case "10.0x":
						yAnimationSpeed = 10;
						
						break;
					case "20.0x":
						yAnimationSpeed = 20;
						
						break;
					case "50.0x":
						yAnimationSpeed = 50;
						
						break;
				}
			});
			
			Label yAnimationDirectionLabel = new Label("Animation direction:");
			
			ComboBox<String> yAnimationDirectionComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Forward", "Backward"}));
			yAnimationDirectionComboBox.getSelectionModel().select(yAnimationDirection);
			yAnimationDirectionComboBox.setOnAction(e2 -> {
				yAnimationDirection = yAnimationDirectionComboBox.getValue();
			});
			
			Label yAnimationReverseLabel = new Label("Auto-reverse animation:");
			
			ComboBox<String> yAnimationReverseComboBox = new ComboBox<String>(FXCollections.observableArrayList(new String[] {"Reset", "Auto-reverse"}));
			yAnimationReverseComboBox.getSelectionModel().select(yAnimationReverse);
			yAnimationReverseComboBox.setOnAction(e2 -> {
				yAnimationReverse = yAnimationReverseComboBox.getValue();
			});
			
			Label yAnimationLowerBoundLabel = new Label("Lower bound (0.1-6.0):");
			
			TextField yAnimationLowerBoundTextField = new TextField(yAnimationLowerBound + "");
			yAnimationLowerBoundTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("^-?\\d*\\.?\\d*$")) {
						yAnimationLowerBoundTextField.setText(oldValue);
						return;
					}
					
					try {
						double doubleValue = Double.parseDouble(yAnimationLowerBoundTextField.getText());
						
						if(doubleValue < -500) {
							yAnimationLowerBound = -500;
						} else if(doubleValue > 500) {
							yAnimationLowerBound = 500;
						} else {
							yAnimationLowerBound = doubleValue;
						}
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label yAnimationUpperBoundLabel = new Label("Upper bound (0.1-6.0):");
			
			TextField yAnimationUpperBoundTextField = new TextField(yAnimationUpperBound + "");
			yAnimationUpperBoundTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("^\\d*\\.?\\d*$")) {
						yAnimationUpperBoundTextField.setText(oldValue);
						return;
					}
					
					try {
						double doubleValue = Double.parseDouble(yAnimationUpperBoundTextField.getText());
						
						if(doubleValue < -500) {
							yAnimationUpperBound = -500;
						} else if(doubleValue > 500) {
							yAnimationUpperBound = 500;
						} else {
							yAnimationUpperBound = doubleValue;
						}
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane yAnimationGridPane = new GridPane();
			yAnimationGridPane.setPadding(new Insets(5, 5, 5, 5));
			yAnimationGridPane.setHgap(10);
			yAnimationGridPane.setVgap(10);
			yAnimationGridPane.add(yAnimationTypeLabel, 0, 0);
			yAnimationGridPane.add(yAnimationTypeComboBox, 1, 0);
			yAnimationGridPane.add(yAnimationSpeedLabel, 0, 1);
			yAnimationGridPane.add(yAnimationSpeedComboBox, 1, 1);
			yAnimationGridPane.add(yAnimationDirectionLabel, 0, 2);
			yAnimationGridPane.add(yAnimationDirectionComboBox, 1, 2);
			yAnimationGridPane.add(yAnimationReverseLabel, 0, 3);
			yAnimationGridPane.add(yAnimationReverseComboBox, 1, 3);
			yAnimationGridPane.add(yAnimationLowerBoundLabel, 0, 4);
			yAnimationGridPane.add(yAnimationLowerBoundTextField, 1, 4);
			yAnimationGridPane.add(yAnimationUpperBoundLabel, 0, 5);
			yAnimationGridPane.add(yAnimationUpperBoundTextField, 1, 5);
			
			Tab yTab = new Tab("Y-Position", yAnimationGridPane);
			yTab.setClosable(false);
			
			TabPane tabPane = new TabPane(colorTab, rotationTab, scaleTab, xTab, yTab);
			
			Dialog<Void> animationDialog = new Dialog<>();
			animationDialog.setTitle("Animate");
			animationDialog.getDialogPane().getButtonTypes().clear();
			animationDialog.getDialogPane().setContent(tabPane);
			animationDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
			((Stage) animationDialog.getDialogPane().getScene().getWindow()).getIcons().add(iconImage);
			
			Node closeButton = animationDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
			closeButton.managedProperty().bind(closeButton.visibleProperty());
			closeButton.setVisible(false);
			
			animationDialog.showAndWait();
		});
		
		MenuItem resetMenuItem = new MenuItem();

		Menu resetMenu = new Menu("Reset");
		resetMenu.getItems().addAll(resetMenuItem);
		resetMenu.addEventHandler(Menu.ON_SHOWN, event -> resetMenu.hide());
		resetMenu.addEventHandler(Menu.ON_SHOWING, event -> resetMenu.fire());
		
		resetMenu.setOnAction(e -> {
			shape = "";
			isColor = false;
			
			colorCheckBox.setSelected(false);
			lockRotateCheckBox.setSelected(false);
			
			colorAnimation.stop();
			rotateAnimation.stop();
			scaleAnimation.stop();
			xAnimation.stop();
			yAnimation.stop();
			
			rotateSlider.setValue(0);
			scaleSlider.setValue(0);
			xSlider.setValue(0);
			ySlider.setValue(0);
			
			reset();
			
			createPattern();
		});
		
		MenuItem importMenuItem = new MenuItem("Import configuration");
		
		importMenuItem.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Import configuration");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
			
			File openFile = fileChooser.showOpenDialog(primaryStage);
			if(openFile == null) {
				return;
			}
			
			importConfiguration(openFile);
		});

		MenuItem exportMenuItem = new MenuItem("Export configuration");
		
		exportMenuItem.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Export configuration");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
			
			File saveFile = fileChooser.showSaveDialog(primaryStage);
			if(saveFile == null) {
				return;
			}
			
			try {
				JSONObject jsonObject = new JSONObject();
				
				Map<String, Number> defaultMap = new LinkedHashMap<String, Number>(6);
				defaultMap.put("rotate", rotateSlider.getValue());
				defaultMap.put("scale", scaleSlider.getValue());
				defaultMap.put("x", xSlider.getValue());
				defaultMap.put("y", ySlider.getValue());
				
				Map<String, String> colorAnimateMap = new LinkedHashMap<String, String>(2);
				colorAnimateMap.put("type", colorAnimationType);
				colorAnimateMap.put("speed", colorAnimationSpeed + "");
				
				Map<String, String> rotateAnimateMap = new LinkedHashMap<String, String>(4);
				rotateAnimateMap.put("type", rotateAnimationType);
				rotateAnimateMap.put("speed", rotateAnimationSpeed + "");
				rotateAnimateMap.put("direction", rotateAnimationDirection);
				rotateAnimateMap.put("reverse", rotateAnimationReverse);
				
				Map<String, String> scaleAnimateMap = new LinkedHashMap<String, String>(6);
				scaleAnimateMap.put("type", scaleAnimationType);
				scaleAnimateMap.put("speed", scaleAnimationSpeed + "");
				scaleAnimateMap.put("direction", scaleAnimationDirection);
				scaleAnimateMap.put("reverse", scaleAnimationReverse);
				scaleAnimateMap.put("lowerBound", scaleAnimationLowerBound + "");
				scaleAnimateMap.put("upperBound", scaleAnimationUpperBound + "");
				
				Map<String, String> xAnimateMap = new LinkedHashMap<String, String>(6);
				xAnimateMap.put("type", xAnimationType);
				xAnimateMap.put("speed", xAnimationSpeed + "");
				xAnimateMap.put("direction", xAnimationDirection);
				xAnimateMap.put("reverse", xAnimationReverse);
				xAnimateMap.put("lowerBound", xAnimationLowerBound + "");
				xAnimateMap.put("upperBound", xAnimationUpperBound + "");
				
				Map<String, String> yAnimateMap = new LinkedHashMap<String, String>(6);
				yAnimateMap.put("type", yAnimationType);
				yAnimateMap.put("speed", yAnimationSpeed + "");
				yAnimateMap.put("direction", yAnimationDirection);
				yAnimateMap.put("reverse", yAnimationReverse);
				yAnimateMap.put("lowerBound", yAnimationLowerBound + "");
				yAnimateMap.put("upperBound", yAnimationUpperBound + "");
				
				Map<String, Map<String, String>> animateMap = new LinkedHashMap<String, Map<String, String>>(5);
				animateMap.put("color", colorAnimateMap);
				animateMap.put("rotate", rotateAnimateMap);
				animateMap.put("scale", scaleAnimateMap);
				animateMap.put("x", xAnimateMap);
				animateMap.put("y", yAnimateMap);
				
				Map<String, Number> parametersMap = new LinkedHashMap<String, Number>(4);
				
				switch(shape) {
					case "stripes":
						parametersMap.put("backgroundStripeWidth", backgroundStripeWidth);
						parametersMap.put("backgroundStripeGap", backgroundStripeGap);
						parametersMap.put("foregroundStripeWidth", foregroundStripeWidth);
						parametersMap.put("foregroundStripeGap", foregroundStripeGap);
						
						break;
						
					case "circles":
						parametersMap.put("backgroundCircleRadius", backgroundCircleRadius);
						parametersMap.put("backgroundCircleGap", backgroundCircleGap);
						parametersMap.put("foregroundCircleRadius", foregroundCircleRadius);
						parametersMap.put("foregroundCircleGap", foregroundCircleGap);
						
						break;
						
					case "grid":
						parametersMap.put("backgroundGridWidth", backgroundGridWidth);
						parametersMap.put("backgroundGridGap", backgroundGridGap);
						parametersMap.put("foregroundGridWidth", foregroundGridWidth);
						parametersMap.put("foregroundGridGap", foregroundGridGap);
						
						break;
						
					case "circTriangles":
						parametersMap.put("backgroundCircTriangleRadius", backgroundCircTriangleRadius);
						parametersMap.put("backgroundCircTriangleWidth", backgroundCircTriangleWidth);
						parametersMap.put("foregroundCircTriangleRadius", foregroundCircTriangleRadius);
						parametersMap.put("foregroundCircTriangleWidth", foregroundCircTriangleWidth);
						
						break;
						
					case "squares":
						parametersMap.put("backgroundSquareWidth", backgroundSquareWidth);
						parametersMap.put("backgroundSquareGap", backgroundSquareGap);
						parametersMap.put("foregroundSquareWidth", foregroundSquareWidth);
						parametersMap.put("foregroundSquareGap", foregroundSquareGap);
						
						break;
				}
				
				jsonObject.put("shape", shape);
				jsonObject.put("isColor", isColor);
				jsonObject.put("lockRotate", lockRotateCheckBox.isSelected());
				jsonObject.put("default", defaultMap);
				jsonObject.put("animate", animateMap);
				jsonObject.put("parameters", parametersMap);
				
				PrintWriter printWriter = new PrintWriter(saveFile);
				printWriter.write(jsonObject.toJSONString());
				printWriter.flush();
				printWriter.close();
			} catch(IOException ioe) {
				DebugInfoFX.show(ioe);
			}
		});
		
		Menu importMenu = new Menu("Import/Export");
		importMenu.getItems().addAll(importMenuItem, exportMenuItem);
		
		Menu presetsMenu = new Menu("Presets");
		
		File[] possiblePresetFiles = new File(getClass().getResource("").toURI()).listFiles();
		
		Arrays.sort(possiblePresetFiles);
		
		for(int i = 0; i < possiblePresetFiles.length; i++) {
			if(possiblePresetFiles[i].getName().substring(possiblePresetFiles[i].getName().length() - 5).equals(".json")) {
				File presetFile = possiblePresetFiles[i];
				
				MenuItem presetMenuItem = new MenuItem(presetFile.getName().substring(0, presetFile.getName().length() - 5));
				
				presetMenuItem.setOnAction(e -> {
					importConfiguration(presetFile);
				});
				
				presetsMenu.getItems().add(presetMenuItem);
			}
		}
		
		MenuItem parametersMenuItem = new MenuItem();
		
		Menu parametersMenu = new Menu("Parameters");
		parametersMenu.getItems().addAll(parametersMenuItem);
		parametersMenu.addEventHandler(Menu.ON_SHOWN, event -> parametersMenu.hide());
		parametersMenu.addEventHandler(Menu.ON_SHOWING, event -> parametersMenu.fire());
		
		parametersMenu.setOnAction(e -> {
			Label backgrounStripeWidthLabel = new Label("Background stripe width:");
			
			TextField backgroundStripeWidthTextField = new TextField(backgroundStripeWidth + "");
			backgroundStripeWidthTextField.setMaxWidth(50);
			backgroundStripeWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundStripeWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundStripeWidth = Integer.parseInt(backgroundStripeWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label backgrounStripeGapLabel = new Label("Background stripe gap:");
			
			TextField backgroundStripeGapTextField = new TextField(backgroundStripeGap + "");
			backgroundStripeGapTextField.setMaxWidth(50);
			backgroundStripeGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundStripeGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundStripeGap = Integer.parseInt(backgroundStripeGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounStripeWidthLabel = new Label("Foreground stripe width:");
			
			TextField foregroundStripeWidthTextField = new TextField(foregroundStripeWidth + "");
			foregroundStripeWidthTextField.setMaxWidth(50);
			foregroundStripeWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundStripeWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundStripeWidth = Integer.parseInt(foregroundStripeWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounStripeGapLabel = new Label("Foreground stripe gap:");
			
			TextField foregroundStripeGapTextField = new TextField(foregroundStripeGap + "");
			foregroundStripeGapTextField.setMaxWidth(50);
			foregroundStripeGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundStripeGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundStripeGap = Integer.parseInt(foregroundStripeGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane stripesGridPane = new GridPane();
			stripesGridPane.setPadding(new Insets(5, 5, 5, 5));
			stripesGridPane.setHgap(10);
			stripesGridPane.setVgap(10);
			stripesGridPane.add(backgrounStripeWidthLabel, 0, 0);
			stripesGridPane.add(backgroundStripeWidthTextField, 1, 0);
			//This is only needed for one node of the column
			GridPane.setHgrow(backgroundStripeWidthTextField, Priority.ALWAYS);
			GridPane.setHalignment(backgroundStripeWidthTextField, HPos.RIGHT);
			stripesGridPane.add(backgrounStripeGapLabel, 0, 1);
			stripesGridPane.add(backgroundStripeGapTextField, 1, 1);
			GridPane.setHalignment(backgroundStripeGapTextField, HPos.RIGHT);
			stripesGridPane.add(foregrounStripeWidthLabel, 0, 2);
			stripesGridPane.add(foregroundStripeWidthTextField, 1, 2);
			GridPane.setHalignment(foregroundStripeWidthTextField, HPos.RIGHT);
			stripesGridPane.add(foregrounStripeGapLabel, 0, 3);
			stripesGridPane.add(foregroundStripeGapTextField, 1, 3);
			GridPane.setHalignment(foregroundStripeGapTextField, HPos.RIGHT);
			
			Tab stripesTab = new Tab("Stripes", stripesGridPane);
			stripesTab.setClosable(false);
			
			Label backgrounCircleWidthLabel = new Label("Background circle width:");
			
			TextField backgroundCircleWidthTextField = new TextField(backgroundCircleRadius + "");
			backgroundCircleWidthTextField.setMaxWidth(50);
			backgroundCircleWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundCircleWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundCircleRadius = Integer.parseInt(backgroundCircleWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label backgroundCircleGapLabel = new Label("Background circle gap:");
			
			TextField backgroundCircleGapTextField = new TextField(backgroundCircleGap + "");
			backgroundCircleGapTextField.setMaxWidth(50);
			backgroundCircleGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundCircleGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundCircleGap = Integer.parseInt(backgroundCircleGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounCircleWidthLabel = new Label("Foreground circle width:");
			
			TextField foregroundCircleWidthTextField = new TextField(foregroundCircleRadius + "");
			foregroundCircleWidthTextField.setMaxWidth(50);
			foregroundCircleWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundCircleWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundCircleRadius = Integer.parseInt(foregroundCircleWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounCircleGapLabel = new Label("Foreground stripe gap:");
			
			TextField foregroundCircleGapTextField = new TextField(foregroundCircleGap + "");
			foregroundCircleGapTextField.setMaxWidth(50);
			foregroundCircleGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundCircleGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundCircleGap = Integer.parseInt(foregroundCircleGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane circlesGridPane = new GridPane();
			circlesGridPane.setPadding(new Insets(5, 5, 5, 5));
			circlesGridPane.setHgap(10);
			circlesGridPane.setVgap(10);
			circlesGridPane.add(backgrounCircleWidthLabel, 0, 0);
			circlesGridPane.add(backgroundCircleWidthTextField, 1, 0);
			//This is only needed for one node of the column
			GridPane.setHgrow(backgroundCircleWidthTextField, Priority.ALWAYS);
			GridPane.setHalignment(backgroundCircleWidthTextField, HPos.RIGHT);
			circlesGridPane.add(backgroundCircleGapLabel, 0, 1);
			circlesGridPane.add(backgroundCircleGapTextField, 1, 1);
			GridPane.setHalignment(backgroundCircleGapTextField, HPos.RIGHT);
			circlesGridPane.add(foregrounCircleWidthLabel, 0, 2);
			circlesGridPane.add(foregroundCircleWidthTextField, 1, 2);
			GridPane.setHalignment(foregroundCircleWidthTextField, HPos.RIGHT);
			circlesGridPane.add(foregrounCircleGapLabel, 0, 3);
			circlesGridPane.add(foregroundCircleGapTextField, 1, 3);
			GridPane.setHalignment(foregroundCircleGapTextField, HPos.RIGHT);
			
			Tab circlesTab = new Tab("Circles", circlesGridPane);
			circlesTab.setClosable(false);
			
			Label backgrounGridWidthLabel = new Label("Background grid width:");
			
			TextField backgroundGridWidthTextField = new TextField(backgroundGridWidth + "");
			backgroundGridWidthTextField.setMaxWidth(50);
			backgroundGridWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundGridWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundGridWidth = Integer.parseInt(backgroundGridWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label backgrounGridGapLabel = new Label("Background grid gap:");
			
			TextField backgroundGridGapTextField = new TextField(backgroundGridGap + "");
			backgroundGridGapTextField.setMaxWidth(50);
			backgroundGridGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundGridGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundGridGap = Integer.parseInt(backgroundGridGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounGridWidthLabel = new Label("Foreground grid width:");
			
			TextField foregroundGridWidthTextField = new TextField(foregroundGridWidth + "");
			foregroundGridWidthTextField.setMaxWidth(50);
			foregroundGridWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundGridWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundGridWidth = Integer.parseInt(foregroundGridWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounGridGapLabel = new Label("Foreground grid gap:");
			
			TextField foregroundGridGapTextField = new TextField(foregroundGridGap + "");
			foregroundGridGapTextField.setMaxWidth(50);
			foregroundGridGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundGridGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundGridGap = Integer.parseInt(foregroundGridGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane gridGridPane = new GridPane();
			gridGridPane.setPadding(new Insets(5, 5, 5, 5));
			gridGridPane.setHgap(10);
			gridGridPane.setVgap(10);
			gridGridPane.add(backgrounGridWidthLabel, 0, 0);
			gridGridPane.add(backgroundGridWidthTextField, 1, 0);
			//This is only needed for one node of the column
			GridPane.setHgrow(backgroundGridWidthTextField, Priority.ALWAYS);
			GridPane.setHalignment(backgroundGridWidthTextField, HPos.RIGHT);
			gridGridPane.add(backgrounGridGapLabel, 0, 1);
			gridGridPane.add(backgroundGridGapTextField, 1, 1);
			GridPane.setHalignment(backgroundGridGapTextField, HPos.RIGHT);
			gridGridPane.add(foregrounGridWidthLabel, 0, 2);
			gridGridPane.add(foregroundGridWidthTextField, 1, 2);
			GridPane.setHalignment(foregroundGridWidthTextField, HPos.RIGHT);
			gridGridPane.add(foregrounGridGapLabel, 0, 3);
			gridGridPane.add(foregroundGridGapTextField, 1, 3);
			GridPane.setHalignment(foregroundGridGapTextField, HPos.RIGHT);
			
			Tab gridTab = new Tab("Grid", gridGridPane);
			gridTab.setClosable(false);
			
			Label backgrounCircTriangleRadiusLabel = new Label("Background circular triangle radius:");
			
			TextField backgroundCircTriangleRadiusTextField = new TextField(backgroundCircTriangleRadius + "");
			backgroundCircTriangleRadiusTextField.setMaxWidth(50);
			backgroundCircTriangleRadiusTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundCircTriangleRadiusTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundCircTriangleRadius = Integer.parseInt(backgroundCircTriangleRadiusTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label backgrounCircTriangleWidthLabel = new Label("Background circular triangle width:");
			
			TextField backgroundCircTriangleWidthTextField = new TextField(backgroundCircTriangleWidth + "");
			backgroundCircTriangleWidthTextField.setMaxWidth(50);
			backgroundCircTriangleWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundCircTriangleWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundCircTriangleWidth = Integer.parseInt(backgroundCircTriangleWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounCircTriangleRadiusLabel = new Label("Foreground circular triangle radius:");
			
			TextField foregroundCircTriangleRadiusTextField = new TextField(foregroundCircTriangleRadius + "");
			foregroundCircTriangleRadiusTextField.setMaxWidth(50);
			foregroundCircTriangleRadiusTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundCircTriangleRadiusTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundCircTriangleRadius = Integer.parseInt(foregroundCircTriangleRadiusTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounCircTriangleWidthLabel = new Label("Foreground circular triangle width:");
			
			TextField foregroundCircTriangleWidthTextField = new TextField(foregroundCircTriangleWidth + "");
			foregroundCircTriangleWidthTextField.setMaxWidth(50);
			foregroundCircTriangleWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundCircTriangleWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundCircTriangleWidth = Integer.parseInt(foregroundCircTriangleWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane circTrianglesGridPane = new GridPane();
			circTrianglesGridPane.setPadding(new Insets(5, 5, 5, 5));
			circTrianglesGridPane.setHgap(10);
			circTrianglesGridPane.setVgap(10);
			circTrianglesGridPane.add(backgrounCircTriangleRadiusLabel, 0, 0);
			circTrianglesGridPane.add(backgroundCircTriangleRadiusTextField, 1, 0);
			//This is only needed for one node of the column
			GridPane.setHgrow(backgroundCircTriangleRadiusTextField, Priority.ALWAYS);
			GridPane.setHalignment(backgroundCircTriangleRadiusTextField, HPos.RIGHT);
			circTrianglesGridPane.add(backgrounCircTriangleWidthLabel, 0, 1);
			circTrianglesGridPane.add(backgroundCircTriangleWidthTextField, 1, 1);
			GridPane.setHalignment(backgroundCircTriangleWidthTextField, HPos.RIGHT);
			circTrianglesGridPane.add(foregrounCircTriangleRadiusLabel, 0, 2);
			circTrianglesGridPane.add(foregroundCircTriangleRadiusTextField, 1, 2);
			GridPane.setHalignment(foregroundCircTriangleRadiusTextField, HPos.RIGHT);
			circTrianglesGridPane.add(foregrounCircTriangleWidthLabel, 0, 3);
			circTrianglesGridPane.add(foregroundCircTriangleWidthTextField, 1, 3);
			GridPane.setHalignment(foregroundCircTriangleWidthTextField, HPos.RIGHT);
			
			Tab circTrianglesTab = new Tab("Circular triangles", circTrianglesGridPane);
			circTrianglesTab.setClosable(false);
			
			Label backgrounSquareWidthLabel = new Label("Background square width:");
			
			TextField backgroundSquareWidthTextField = new TextField(backgroundSquareWidth + "");
			backgroundSquareWidthTextField.setMaxWidth(50);
			backgroundSquareWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundSquareWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundSquareWidth = Integer.parseInt(backgroundSquareWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			Label backgrounSquareGapLabel = new Label("Background square gap:");
			
			TextField backgroundSquareGapTextField = new TextField(backgroundSquareGap + "");
			backgroundSquareGapTextField.setMaxWidth(50);
			backgroundSquareGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						backgroundSquareGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					
					try {
						backgroundSquareGap = Integer.parseInt(backgroundSquareGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounSquareWidthLabel = new Label("Foreground square width:");
			
			TextField foregroundSquareWidthTextField = new TextField(foregroundSquareWidth + "");
			foregroundSquareWidthTextField.setMaxWidth(50);
			foregroundSquareWidthTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundSquareWidthTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundSquareWidth = Integer.parseInt(foregroundSquareWidthTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});

			Label foregrounSquareGapLabel = new Label("Foreground square gap:");
			
			TextField foregroundSquareGapTextField = new TextField(foregroundSquareGap + "");
			foregroundSquareGapTextField.setMaxWidth(50);
			foregroundSquareGapTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!newValue.matches("\\d*")) {
						foregroundSquareGapTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						foregroundSquareGap = Integer.parseInt(foregroundSquareGapTextField.getText());
					} catch(NumberFormatException nfe) {}
				}
			});
			
			GridPane squaresGridPane = new GridPane();
			squaresGridPane.setPadding(new Insets(5, 5, 5, 5));
			squaresGridPane.setHgap(10);
			squaresGridPane.setVgap(10);
			squaresGridPane.add(backgrounSquareWidthLabel, 0, 0);
			squaresGridPane.add(backgroundSquareWidthTextField, 1, 0);
			//This is only needed for one node of the column
			GridPane.setHgrow(backgroundSquareWidthTextField, Priority.ALWAYS);
			GridPane.setHalignment(backgroundSquareWidthTextField, HPos.RIGHT);
			squaresGridPane.add(backgrounSquareGapLabel, 0, 1);
			squaresGridPane.add(backgroundSquareGapTextField, 1, 1);
			GridPane.setHalignment(backgroundSquareGapTextField, HPos.RIGHT);
			squaresGridPane.add(foregrounSquareWidthLabel, 0, 2);
			squaresGridPane.add(foregroundSquareWidthTextField, 1, 2);
			GridPane.setHalignment(foregroundSquareWidthTextField, HPos.RIGHT);
			squaresGridPane.add(foregrounSquareGapLabel, 0, 3);
			squaresGridPane.add(foregroundSquareGapTextField, 1, 3);
			GridPane.setHalignment(foregroundSquareGapTextField, HPos.RIGHT);
			
			Tab squaresTab = new Tab("Squares", squaresGridPane);
			squaresTab.setClosable(false);
			
			TabPane tabPane = new TabPane(stripesTab, circlesTab, gridTab, circTrianglesTab, squaresTab);
			
			Dialog<Void> parametersDialog = new Dialog<>();
			parametersDialog.setTitle("Parameters");
			parametersDialog.getDialogPane().setContent(tabPane);
			parametersDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
			((Stage) parametersDialog.getDialogPane().getScene().getWindow()).getIcons().add(iconImage);
			
			Node closeButton = parametersDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
			closeButton.managedProperty().bind(closeButton.visibleProperty());
			
			closeButton.setVisible(false);
			
			parametersDialog.showAndWait();
			
			createPattern();
		});
		
		MenuBar menuBar = new MenuBar(shapesMenu, animateMenu, resetMenu, importMenu, presetsMenu, parametersMenu);
		
		patternScrollPane = new ScrollPane(patternPane);
		patternScrollPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
		patternScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		patternScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		patternScrollPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		
		patternScrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				event.consume();
			}
		});
		
		rotateSlider.setMajorTickUnit(1);
		rotateSlider.setMinorTickCount(100);
		rotateSlider.setOrientation(Orientation.VERTICAL);
		rotateSlider.setRotate(180);

		Label rotateLabel = new Label("R: 0.0°");
		rotateLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT, new Insets(2, 2, 2, 2))));
		rotateLabel.setPadding(new Insets(2, 2, 2, 2));
		rotateLabel.setCursor(Cursor.HAND);
		
		rotateLabel.setOnMouseClicked(e -> {
			rotateSlider.setValue(0);
		});
		
		rotateSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				foregroundRotate.setAngle(newValue.doubleValue());
				
				rotateLabel.setText("R: " + (((int) (newValue.doubleValue() * 10)) / 10.0) + "°");
			}
		});
		
		GridPane rotateGridPane = new GridPane();
		rotateGridPane.add(rotateSlider, 0, 0);
		rotateGridPane.add(rotateLabel, 0, 1);
		rotateGridPane.setPrefWidth(75);
		rotateGridPane.setAlignment(Pos.CENTER);
		GridPane.setVgrow(rotateSlider, Priority.ALWAYS);
		GridPane.setHalignment(rotateSlider, HPos.CENTER);
		GridPane.setHalignment(rotateLabel, HPos.CENTER);
		
		scaleSlider.setMajorTickUnit(1);
		scaleSlider.setMinorTickCount(100);
		scaleSlider.setOrientation(Orientation.VERTICAL);
		scaleSlider.setRotate(180);
		
		Label scaleLabel = new Label("S: 1.0x");
		scaleLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT, new Insets(2, 2, 2, 2))));
		scaleLabel.setPadding(new Insets(2, 2, 2, 2));
		scaleLabel.setCursor(Cursor.HAND);
		
		scaleLabel.setOnMouseClicked(e -> {
			scaleSlider.setValue(0);
		});
		
		scaleSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				//Similar code is used in scale animation
				if(newValue.doubleValue() >= 0) {
					foregroundScale.setX(newValue.doubleValue() + 1);
					foregroundScale.setY(newValue.doubleValue() + 1);
					
					scaleLabel.setText("S: " + (((int) ((newValue.doubleValue() + 1) * 10)) / 10.0) + "x");
				} else {
					foregroundScale.setX(1 / Math.abs(newValue.doubleValue() - 1));
					foregroundScale.setY(1 / Math.abs(newValue.doubleValue() - 1));
					
					scaleLabel.setText("S: " + ToDecimal.to(1, ((newValue.doubleValue() + 5) / (5 / 0.9) + 0.1)) + "x");
				}
			}
		});
		
		GridPane scaleGridPane = new GridPane();
		scaleGridPane.add(scaleSlider, 0, 0);
		scaleGridPane.add(scaleLabel, 0, 1);
		scaleGridPane.setPrefWidth(75);
		scaleGridPane.setAlignment(Pos.CENTER);
		GridPane.setVgrow(scaleSlider, Priority.ALWAYS);
		GridPane.setHalignment(scaleSlider, HPos.CENTER);
		
		xSlider.setMajorTickUnit(1);
		xSlider.setMinorTickCount(100);
		xSlider.setOrientation(Orientation.VERTICAL);
		xSlider.setRotate(180);
		
		Label xLabel = new Label("X: 0.0");
		xLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT, new Insets(2, 2, 2, 2))));
		xLabel.setPadding(new Insets(2, 2, 2, 2));
		xLabel.setCursor(Cursor.HAND);
		
		xLabel.setOnMouseClicked(e -> {
			xSlider.setValue(0);
		});
		
		xSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				foregroundPatternPane.setLayoutX(newValue.doubleValue());
				
				if(newValue.doubleValue() > 0) {
					xLabel.setText("X: +" + (((int) (newValue.doubleValue() * 10)) / 10.0));
				} else {
					xLabel.setText("X: " + (((int) (newValue.doubleValue() * 10)) / 10.0));
				}
			}
		});
		
		GridPane xGridPane = new GridPane();
		xGridPane.add(xSlider, 0, 0);
		xGridPane.add(xLabel, 0, 1);
		xGridPane.setPrefWidth(75);
		xGridPane.setAlignment(Pos.CENTER);
		GridPane.setVgrow(xSlider, Priority.ALWAYS);
		GridPane.setHalignment(xSlider, HPos.CENTER);
		
		ySlider.setMajorTickUnit(1);
		ySlider.setMinorTickCount(100);
		ySlider.setOrientation(Orientation.VERTICAL);
		ySlider.setRotate(180);
		
		Label yLabel = new Label("Y: 0.0");
		yLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT, new Insets(2, 2, 2, 2))));
		yLabel.setPadding(new Insets(2, 2, 2, 2));
		yLabel.setCursor(Cursor.HAND);
		
		yLabel.setOnMouseClicked(e -> {
			ySlider.setValue(0);
		});
		
		ySlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				foregroundPatternPane.setLayoutY(newValue.doubleValue());
				
				if(newValue.doubleValue() > 0) {
					yLabel.setText("Y: +" + (((int) (newValue.doubleValue() * 10)) / 10.0));
				} else {
					yLabel.setText("Y: " + (((int) (newValue.doubleValue() * 10)) / 10.0));
				}
			}
		});
		
		GridPane yGridPane = new GridPane();
		yGridPane.add(ySlider, 0, 0);
		yGridPane.add(yLabel, 0, 1);
		yGridPane.setPrefWidth(75);
		yGridPane.setAlignment(Pos.CENTER);
		GridPane.setVgrow(ySlider, Priority.ALWAYS);
		GridPane.setHalignment(ySlider, HPos.CENTER);
		
		Slider zoomSlider = new Slider(0, 1, 1);
		zoomSlider.setMajorTickUnit(0.01);
		zoomSlider.setMinorTickCount(100);
		zoomSlider.setOrientation(Orientation.VERTICAL);
		zoomSlider.setRotate(180);
		
		Label zoomLabel = new Label("Z: 1.0x");
		zoomLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT, new Insets(2, 2, 2, 2))));
		zoomLabel.setPadding(new Insets(2, 2, 2, 2));
		zoomLabel.setCursor(Cursor.HAND);
		
		zoomLabel.setOnMouseClicked(e -> {
			zoomSlider.setValue(1);
		});
		
		zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue) {
				zoomScale.setX(newValue.doubleValue());
				zoomScale.setY(newValue.doubleValue());
				
				zoomLabel.setText("Z: " + (((int) (newValue.doubleValue() * 10)) / 10.0) + "x");
			}
		});
		
		GridPane zoomGridPane = new GridPane();
		zoomGridPane.add(zoomSlider, 0, 0);
		zoomGridPane.add(zoomLabel, 0, 1);
		zoomGridPane.setPrefWidth(75);
		zoomGridPane.setAlignment(Pos.CENTER);
		GridPane.setVgrow(zoomSlider, Priority.ALWAYS);
		GridPane.setHalignment(zoomSlider, HPos.CENTER);
		
		HBox sliderHBox = new HBox(20, rotateGridPane, scaleGridPane, xGridPane, yGridPane, zoomGridPane);
		sliderHBox.setAlignment(Pos.CENTER);
		
		colorCheckBox.setOnAction(e -> {
			isColor = !isColor;
			
			correctColor();
		});
		
		lockRotateCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue) {
				if(newValue.booleanValue()) {
					foregroundRotate.pivotXProperty().bind(foregroundScale.pivotXProperty().subtract(foregroundPatternPane.layoutXProperty().multiply(Bindings.createDoubleBinding(() -> 1 / foregroundScale.getX(), foregroundScale.xProperty()))));
					foregroundRotate.pivotYProperty().bind(foregroundScale.pivotYProperty().subtract(foregroundPatternPane.layoutYProperty().multiply(Bindings.createDoubleBinding(() -> 1 / foregroundScale.getY(), foregroundScale.yProperty()))));
				} else {
					foregroundRotate.pivotXProperty().bind(foregroundPatternPane.widthProperty().divide(2));
					foregroundRotate.pivotYProperty().bind(foregroundPatternPane.heightProperty().divide(2));
				}
			}
		});
		
		HBox optionsHBox = new HBox(20, colorCheckBox, lockRotateCheckBox);
		optionsHBox.setAlignment(Pos.CENTER);
		
		VBox sliderVBox = new VBox(5, optionsHBox, sliderHBox);
		sliderVBox.setAlignment(Pos.CENTER);
		VBox.setVgrow(sliderHBox, Priority.ALWAYS);
		
		HBox mainHBox = new HBox(10, patternScrollPane, sliderVBox);
		HBox.setHgrow(sliderVBox, Priority.ALWAYS);
		
		VBox mainVBox = new VBox(menuBar, mainHBox);
		VBox.setVgrow(mainHBox, Priority.ALWAYS);
		
		Scene scene = new Scene(mainVBox, sceneWidth, sceneHeight);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Moire Patterns");
		primaryStage.getIcons().add(iconImage);
		primaryStage.show();
		
		//After primaryStage.show() bindings
		patternScrollPane.minWidthProperty().bind(scene.heightProperty().subtract(menuBar.getHeight()));
		patternScrollPane.minHeightProperty().bind(scene.heightProperty().subtract(menuBar.getHeight()));
		patternScrollPane.maxWidthProperty().bind(scene.heightProperty().subtract(menuBar.getHeight()));
		patternScrollPane.maxHeightProperty().bind(scene.heightProperty().subtract(menuBar.getHeight()));
		
		foregroundScale.pivotXProperty().bind(foregroundPatternPane.widthProperty().divide(2));
		foregroundScale.pivotYProperty().bind(foregroundPatternPane.heightProperty().divide(2));
		
		foregroundRotate.pivotXProperty().bind(foregroundPatternPane.widthProperty().divide(2));
		foregroundRotate.pivotYProperty().bind(foregroundPatternPane.heightProperty().divide(2));
		
		zoomScale.pivotXProperty().bind(patternScrollPane.maxHeightProperty().divide(2));
		zoomScale.pivotYProperty().bind(patternScrollPane.maxHeightProperty().divide(2));

		foregroundPatternPane.translateXProperty().bind((patternScrollPane.maxHeightProperty().subtract(foregroundPatternPane.widthProperty()).subtract(8)).divide(2));
		foregroundPatternPane.translateYProperty().bind((patternScrollPane.maxHeightProperty().subtract(foregroundPatternPane.heightProperty()).subtract(16)).divide(2));

		backgroundPatternPane.translateXProperty().bind((patternScrollPane.maxHeightProperty().subtract(backgroundPatternPane.widthProperty()).subtract(8)).divide(2));
		backgroundPatternPane.translateYProperty().bind((patternScrollPane.maxHeightProperty().subtract(backgroundPatternPane.heightProperty()).subtract(16)).divide(2));
	}
	
	private void createPattern() {
		backgroundPatternPane.getChildren().clear();
		foregroundPatternPane.getChildren().clear();
		
		switch(shape) {
			case "stripes":
				for(int i = 0; i < rectCount; i++) {
					backgroundPatternPane.getChildren().add(new Rectangle(i * backgroundStripeGap * 2, 0, backgroundStripeWidth, (backgroundStripeGap + backgroundStripeWidth) * rectCount));
					foregroundPatternPane.getChildren().add(new Rectangle((i * foregroundStripeGap * 2), 0, foregroundStripeWidth, (backgroundStripeGap + backgroundStripeWidth) * rectCount));
				}
				
				break;
			
			case "circles":
				for(int i = 0; i < circleCount; i++) {
					Circle circle1 = new Circle(i * backgroundCircleGap * 2);
					circle1.setFill(Color.TRANSPARENT);
					circle1.setStroke(Color.BLACK);
					circle1.setStrokeWidth(backgroundCircleRadius);
					circle1.centerXProperty().bind(foregroundPatternPane.widthProperty().divide(2));
					circle1.centerYProperty().bind(foregroundPatternPane.widthProperty().divide(2));
					
					Circle circle2 = new Circle(i * foregroundCircleGap * 2);
					circle2.setFill(Color.TRANSPARENT);
					circle2.setStroke(Color.BLACK);
					circle2.setStrokeWidth(foregroundCircleRadius);
					circle2.centerXProperty().bind(foregroundPatternPane.widthProperty().divide(2));
					circle2.centerYProperty().bind(foregroundPatternPane.widthProperty().divide(2));
					
					backgroundPatternPane.getChildren().add(circle1);
					foregroundPatternPane.getChildren().add(circle2);
				}
				
				break;
			
			case "grid":
				for(int i = 0; i < gridCount; i++) {
					backgroundPatternPane.getChildren().add(new Rectangle(0, i * backgroundGridGap * 2, (gridCount * backgroundGridGap * 2) - (backgroundGridGap * 2), backgroundGridWidth));
					backgroundPatternPane.getChildren().add(new Rectangle(i * backgroundGridGap * 2, 0, backgroundGridWidth, (gridCount * backgroundGridGap * 2) - (backgroundGridGap * 2)));
					
					foregroundPatternPane.getChildren().add(new Rectangle((i * foregroundGridGap * 2), 0, foregroundGridWidth, (gridCount * foregroundGridGap * 2) - (backgroundGridGap * 2)));
					foregroundPatternPane.getChildren().add(new Rectangle(0, i * foregroundGridGap * 2, (gridCount * foregroundGridGap * 2) - (backgroundGridGap * 2), foregroundGridWidth));
				}
				
				break;
				
			case "circTriangles":
				for(int i = 0; i < circTriangleCount; i++) {
					Polygon triangle1 = new Polygon();
					triangle1.getPoints().addAll((double) backgroundCircTriangleRadius, (double) backgroundCircTriangleRadius);
					triangle1.getPoints().addAll(backgroundCircTriangleRadius + (backgroundCircTriangleRadius * Math.cos(Math.toRadians((360.0 / circTriangleCount) * i))), backgroundCircTriangleRadius + (backgroundCircTriangleRadius * Math.sin(Math.toRadians((360.0 / circTriangleCount) * i))));
					triangle1.getPoints().addAll(backgroundCircTriangleRadius + (backgroundCircTriangleRadius * Math.cos(Math.toRadians(((360.0 / circTriangleCount) * i) + backgroundCircTriangleWidth))), backgroundCircTriangleRadius + (backgroundCircTriangleRadius * Math.sin(Math.toRadians(((360.0 / circTriangleCount) * i) + backgroundCircTriangleWidth))));
					
					Polygon triangle2 = new Polygon();
					triangle2.getPoints().addAll((double) foregroundCircTriangleRadius, (double) foregroundCircTriangleRadius);
					triangle2.getPoints().addAll(foregroundCircTriangleRadius + (foregroundCircTriangleRadius * Math.cos(Math.toRadians((360.0 / circTriangleCount) * i))), foregroundCircTriangleRadius + (foregroundCircTriangleRadius * Math.sin(Math.toRadians((360.0 / circTriangleCount) * i))));
					triangle2.getPoints().addAll(foregroundCircTriangleRadius + (foregroundCircTriangleRadius * Math.cos(Math.toRadians(((360.0 / circTriangleCount) * i) + foregroundCircTriangleWidth))), foregroundCircTriangleRadius + (foregroundCircTriangleRadius * Math.sin(Math.toRadians(((360.0 / circTriangleCount) * i) + foregroundCircTriangleWidth))));
					
					backgroundPatternPane.getChildren().add(triangle1);
					foregroundPatternPane.getChildren().add(triangle2);
				}
				
				break;
				
			case "squares":
				for(int i = 0; i < squareCount; i++) {
					Rectangle rectangle1 = new Rectangle();
					rectangle1.setWidth(i * backgroundSquareGap * backgroundSquareWidth);
					rectangle1.setHeight(i * backgroundSquareGap * backgroundSquareWidth);
					rectangle1.setX((squareCount * backgroundSquareGap * backgroundSquareWidth * 0.5) - (rectangle1.getWidth() / 2) - backgroundSquareWidth);
					rectangle1.setY((squareCount * backgroundSquareGap * backgroundSquareWidth * 0.5) - (rectangle1.getHeight() / 2) - backgroundSquareWidth);
					rectangle1.setFill(Color.TRANSPARENT);
					rectangle1.setStroke(Color.BLACK);
					rectangle1.setStrokeWidth(backgroundSquareWidth);
					
					Rectangle rectangle2 = new Rectangle();
					rectangle2.setWidth(i * foregroundSquareGap * foregroundSquareWidth);
					rectangle2.setHeight(i * foregroundSquareGap * foregroundSquareWidth);
					//-1.9 used as a correction constant for misaligned layers
					rectangle2.setX((squareCount * foregroundSquareGap * foregroundSquareWidth * 0.5) - (rectangle2.getWidth() / 2) - foregroundSquareWidth -1.9);
					//-1.9 used as a correction constant for misaligned layers
					rectangle2.setY((squareCount * foregroundSquareGap * foregroundSquareWidth * 0.5) - (rectangle2.getHeight() / 2) - foregroundSquareWidth -1.9);
					rectangle2.setFill(Color.TRANSPARENT);
					rectangle2.setStroke(Color.BLACK);
					rectangle2.setStrokeWidth(foregroundSquareWidth);
					
					backgroundPatternPane.getChildren().add(rectangle1);
					foregroundPatternPane.getChildren().add(rectangle2);
				}
				
				break;
				
			case "triangles": {
				double v = Math.sqrt(3) / 2.0;
				
				for(double y = 5; y < triangleCount; y += backgroundTriangleGap * Math.sqrt(3)) {
					for(double x = 2, dy = y; x < triangleCount; x += (3.0 / 2.0) * backgroundTriangleGap) {
						Polygon triangle1 = new Polygon();
						triangle1.getPoints().addAll(new Double[]{
							x, dy + 10,
							x + backgroundTriangleGap * (3.0 / 2.0), dy + backgroundTriangleGap * v + 10,
							x, dy + backgroundTriangleGap * Math.sqrt(3) + 10,
						});
						triangle1.setFill(Color.TRANSPARENT);
						triangle1.setStrokeWidth(backgroundTriangleWidth);
						triangle1.setStroke(Color.BLACK);
						
						backgroundPatternPane.getChildren().add(triangle1);
						
						dy = dy == y ? dy + backgroundTriangleGap * v : y;
					}
				}
				
				for(double y = 5; y < triangleCount; y += foregroundTriangleGap * Math.sqrt(3)) {
					for(double x = 2, dy = y; x < triangleCount; x += (3.0 / 2.0) * foregroundTriangleGap) {
						Polygon triangle1 = new Polygon();
						triangle1.getPoints().addAll(new Double[]{
							x, dy + 10,
							x + foregroundTriangleGap * (3.0 / 2.0), dy + foregroundTriangleGap * v + 10,
							x, dy + foregroundTriangleGap * Math.sqrt(3) + 10,
						});
						triangle1.setFill(Color.TRANSPARENT);
						triangle1.setStrokeWidth(foregroundTriangleWidth);
						triangle1.setStroke(Color.BLACK);
						
						foregroundPatternPane.getChildren().add(triangle1);
						
						dy = dy == y ? dy + foregroundTriangleGap * v : y;
					}
				}
				
				break;
			}
				
			case "hexagons": {
				double v = Math.sqrt(3) / 2.0;
				
				for(double y = 4; y < hexagonCount; y += backgroundHexagonGap * Math.sqrt(3)) {
					for(double x = -2, dy = y; x < hexagonCount; x += (3.0 / 2.0) * backgroundHexagonGap) {
						Polygon hexagon1 = new Polygon();
						hexagon1.getPoints().addAll(new Double[]{
							x, dy + 10,
							x + backgroundHexagonGap, dy + 10,
							x + backgroundHexagonGap * (3.0 / 2.0), dy + backgroundHexagonGap * v + 10,
							x + backgroundHexagonGap, dy + backgroundHexagonGap * Math.sqrt(3) + 10,
							x, dy + backgroundHexagonGap * Math.sqrt(3) + 10,
							x - (backgroundHexagonGap / 2.0), dy + backgroundHexagonGap * v + 10
						});
						hexagon1.setFill(Color.TRANSPARENT);
						hexagon1.setStrokeWidth(backgroundHexagonWidth);
						hexagon1.setStroke(Color.BLACK);
						
						backgroundPatternPane.getChildren().add(hexagon1);
						
						dy = dy == y ? dy + backgroundHexagonGap * v : y;
					}
				}

				for(double y = 4; y < hexagonCount; y += backgroundHexagonGap * Math.sqrt(3)) {
					for(double x = -2, dy = y; x < hexagonCount; x += (3.0 / 2.0) * backgroundHexagonGap) {
						Polygon hexagon2 = new Polygon();
						hexagon2.getPoints().addAll(new Double[]{
							x, dy + 10,
							x + foregroundHexagonGap, dy + 10,
							x + foregroundHexagonGap * (3.0 / 2.0), dy + foregroundHexagonGap * v + 10,
							x + foregroundHexagonGap, dy + foregroundHexagonGap * Math.sqrt(3) + 10,
							x, dy + foregroundHexagonGap * Math.sqrt(3) + 10,
							x - (foregroundHexagonGap / 2.0), dy + foregroundHexagonGap * v + 10
						});
						hexagon2.setFill(Color.TRANSPARENT);
						hexagon2.setStrokeWidth(foregroundHexagonWidth);
						hexagon2.setStroke(Color.BLACK);
						
						foregroundPatternPane.getChildren().add(hexagon2);
						
						dy = dy == y ? dy + foregroundHexagonGap * v : y;
					}
				}
				
				break;
			}
		}
		
		if(isColor) {
			correctColor();
		}
	}
	
	private void correctColor() {
		if(isColor) {
			for(int i = 0; i < foregroundPatternPane.getChildren().size(); i++) {
				switch(shape) {
					case "stripes":
					case "grid":
					case "circTriangles":
						((Shape) foregroundPatternPane.getChildren().get(i)).setFill(Color.hsb((360.0 / foregroundPatternPane.getChildren().size()) * i, 1, 1));
						((Shape) backgroundPatternPane.getChildren().get(i)).setFill(Color.hsb((360.0 / foregroundPatternPane.getChildren().size()) * i, 1, 1));
						
						break;
						
					case "circles":
					case "squares":
						((Shape) foregroundPatternPane.getChildren().get(i)).setStroke(Color.hsb((360.0 / foregroundPatternPane.getChildren().size()) * i, 1, 1));
						((Shape) backgroundPatternPane.getChildren().get(i)).setStroke(Color.hsb((360.0 / foregroundPatternPane.getChildren().size()) * i, 1, 1));
						
						break;

					case "triangles":
					case "hexagons":
						Polygon foregroundPolygon = ((Polygon) foregroundPatternPane.getChildren().get(i));
						Polygon backgroundPolygon = ((Polygon) backgroundPatternPane.getChildren().get(i));
						
						double distanceFromCenter = Math.sqrt(Math.pow(foregroundPolygon.getPoints().get(0) - ((Polygon) foregroundPatternPane.getChildren().get(foregroundPatternPane.getChildren().size() / 2)).getPoints().get(0), 2) + Math.pow(foregroundPolygon.getPoints().get(1) - ((Polygon) foregroundPatternPane.getChildren().get(foregroundPatternPane.getChildren().size() / 2)).getPoints().get(1), 2));
						
						foregroundPolygon.setStroke(Color.hsb(distanceFromCenter, 1, 1));
						backgroundPolygon.setStroke(Color.hsb(distanceFromCenter, 1, 1));
						
						break;
				}
			}
		} else {
			for(int i = 0; i < foregroundPatternPane.getChildren().size(); i++) {
				switch(shape) {
					case "stripes":
					case "grid":
					case "circTriangles":
						((Shape) foregroundPatternPane.getChildren().get(i)).setFill(Color.BLACK);
						((Shape) backgroundPatternPane.getChildren().get(i)).setFill(Color.BLACK);
						
						break;
						
					case "circles":
					case "squares":
					case "triangles":
					case "hexagons":
						((Shape) foregroundPatternPane.getChildren().get(i)).setStroke(Color.BLACK);
						((Shape) backgroundPatternPane.getChildren().get(i)).setStroke(Color.BLACK);
						
						break;
				}
			}
		}
	}
	
	private void importConfiguration(File openFile) {
		try {
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(openFile));
			
			shape = (String) jsonObject.get("shape");
			
			isColor = (Boolean) jsonObject.get("isColor");
			colorCheckBox.setSelected(isColor);
			
			lockRotateCheckBox.setSelected((Boolean) jsonObject.get("lockRotate"));
			
			createPattern();
			
			@SuppressWarnings("unchecked")
			Map<String, Number> defaultMap = (Map<String, Number>) jsonObject.get("default");
			
			rotateSlider.setValue(defaultMap.get("rotate").doubleValue());
			scaleSlider.setValue(defaultMap.get("scale").doubleValue());
			xSlider.setValue(defaultMap.get("x").doubleValue());
			ySlider.setValue(defaultMap.get("y").doubleValue());
			
			@SuppressWarnings("unchecked")
			Map<String, Map<String, String>> animateMap = (Map<String, Map<String, String>>) jsonObject.get("animate");
			
			Map<String, String> colorAnimateMap = animateMap.get("color");
			
			colorAnimationType = colorAnimateMap.get("type");
			colorAnimationSpeed = Double.parseDouble(colorAnimateMap.get("speed"));
			
			if(!colorAnimationType.equals("None")) {
				colorAnimation.start();
			}
			
			Map<String, String> rotateAnimateMap = animateMap.get("rotate");
			
			rotateAnimationType = rotateAnimateMap.get("type");
			rotateAnimationSpeed = Double.parseDouble(rotateAnimateMap.get("speed"));
			rotateAnimationDirection = rotateAnimateMap.get("direction");
			rotateAnimationReverse = rotateAnimateMap.get("reverse");
			
			if(!rotateAnimationType.equals("None")) {
				rotateAnimation.start();
			}
			
			Map<String, String> scaleAnimateMap = animateMap.get("scale");
			
			scaleAnimationType = scaleAnimateMap.get("type");
			scaleAnimationSpeed = Double.parseDouble(scaleAnimateMap.get("speed"));
			scaleAnimationDirection = scaleAnimateMap.get("direction");
			scaleAnimationReverse = scaleAnimateMap.get("reverse");
			scaleAnimationLowerBound = Double.parseDouble(scaleAnimateMap.get("lowerBound"));
			scaleAnimationUpperBound = Double.parseDouble(scaleAnimateMap.get("upperBound"));
			
			if(!scaleAnimationType.equals("None")) {
				scaleAnimation.start();
			}
			
			Map<String, String> xAnimateMap = animateMap.get("x");
			
			xAnimationType = xAnimateMap.get("type");
			xAnimationSpeed = Double.parseDouble(xAnimateMap.get("speed"));
			xAnimationDirection = xAnimateMap.get("direction");
			xAnimationReverse = xAnimateMap.get("reverse");
			xAnimationLowerBound = Double.parseDouble(xAnimateMap.get("lowerBound"));
			xAnimationUpperBound = Double.parseDouble(xAnimateMap.get("upperBound"));
			
			if(!xAnimationType.equals("None")) {
				xAnimation.start();
			}
			
			Map<String, String> yAnimateMap = animateMap.get("y");
			
			yAnimationType = yAnimateMap.get("type");
			yAnimationSpeed = Double.parseDouble(yAnimateMap.get("speed"));
			yAnimationDirection = yAnimateMap.get("direction");
			yAnimationReverse = yAnimateMap.get("reverse");
			yAnimationLowerBound = Double.parseDouble(yAnimateMap.get("lowerBound"));
			yAnimationUpperBound = Double.parseDouble(yAnimateMap.get("upperBound"));
			
			if(!yAnimationType.equals("None")) {
				yAnimation.start();
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Number> parametersMap = (Map<String, Number>) jsonObject.get("parameters");
			
			switch(shape) {
				case "stripes":
					backgroundStripeWidth = parametersMap.get("backgroundStripeWidth").intValue();
					backgroundStripeGap = parametersMap.get("backgroundStripeGap").intValue();
					foregroundStripeWidth = parametersMap.get("foregroundStripeWidth").intValue();
					foregroundStripeGap = parametersMap.get("foregroundStripeGap").intValue();
					
					break;
					
				case "circles":
					backgroundCircleRadius = parametersMap.get("backgroundCircleRadius").intValue();
					backgroundCircleGap = parametersMap.get("backgroundCircleGap").intValue();
					foregroundCircleRadius = parametersMap.get("foregroundCircleRadius").intValue();
					foregroundCircleGap = parametersMap.get("foregroundCircleGap").intValue();
					
					break;
					
				case "grid":
					backgroundGridWidth = parametersMap.get("backgroundGridWidth").intValue();
					backgroundGridGap = parametersMap.get("backgroundGridGap").intValue();
					foregroundGridWidth = parametersMap.get("foregroundGridWidth").intValue();
					foregroundGridGap = parametersMap.get("foregroundGridGap").intValue();
					
					break;
					
				case "circTriangles":
					backgroundCircTriangleRadius = parametersMap.get("backgroundCircTriangleRadius").intValue();
					backgroundCircTriangleWidth = parametersMap.get("backgroundCircTriangleWidth").intValue();
					foregroundCircTriangleRadius = parametersMap.get("foregroundCircTriangleRadius").intValue();
					foregroundCircTriangleWidth = parametersMap.get("foregroundCircTriangleWidth").intValue();
					
					break;
					
				case "squares":
					backgroundSquareWidth = parametersMap.get("backgroundSquareWidth").intValue();
					backgroundSquareGap = parametersMap.get("backgroundSquareGap").intValue();
					foregroundSquareWidth = parametersMap.get("foregroundSquareWidth").intValue();
					foregroundSquareGap = parametersMap.get("foregroundSquareGap").intValue();
					
					break;
			}
		} catch(Exception ex) {
			DebugInfoFX.show(ex);
		}
	}
	
	private void reset() {
		colorAnimationType = "None";
		colorAnimationSpeed = 1;
		colorAngle = defaultColorAngle;
	
		rotateAnimationType = "None";
		rotateAnimationSpeed = 1;
		rotateAnimationAngle = defaultRotateAnimationAngle;
		rotateAnimationDirection = "Forward";
		rotateAnimationReverse = "Reset";
	
		scaleAnimationType = "None";
		scaleAnimationSpeed = 1;
		scaleAnimationValue = defaultScaleAnimationValue;
		scaleAnimationAngle = defaultScaleAnimationAngle;
		scaleAnimationDirection = "Forward";
		scaleAnimationReverse = "Reset";
		scaleAnimationLowerBound = defaultScaleAnimationLowerBound;
		scaleAnimationUpperBound = defaultScaleAnimationUpperBound;
		scaleAnimationAdjustedLowerBound = defaultScaleAnimationAdjustedLowerBound;
		scaleAnimationAdjustedUpperBound = defaultScaleAnimationAdjustedUpperBound;
	
		xAnimationType = "None";
		xAnimationSpeed = 1;
		xAnimationValue = defaultXAnimationValue;
		xAnimationAngle = defaultXAnimationAngle;
		xAnimationDirection = "Forward";
		xAnimationReverse = "Reset";
		xAnimationLowerBound = defaultXAnimationLowerBound;
		xAnimationUpperBound = defaultXAnimationUpperBound;
	
		yAnimationType = "None";
		yAnimationSpeed = 1;
		yAnimationValue = defaultYAnimationValue;
		yAnimationAngle = defaultYAnimationAngle;
		yAnimationDirection = "Forward";
		yAnimationReverse = "Reset";
		yAnimationLowerBound = defaultYAnimationLowerBound;
		yAnimationUpperBound = defaultYAnimationUpperBound;
		
		backgroundStripeWidth = 4;
		backgroundStripeGap = 4;
		foregroundStripeWidth = 4;
		foregroundStripeGap = 4;
		
		backgroundCircleRadius = 4;
		backgroundCircleGap = 4;
		foregroundCircleRadius = 4;
		foregroundCircleGap = 4;
		
		backgroundGridWidth = 4;
		backgroundGridGap = 6;
		foregroundGridWidth = 4;
		foregroundGridGap = 6;
		
		backgroundCircTriangleRadius = 500;
		backgroundCircTriangleWidth = 2;
		foregroundCircTriangleRadius = 500;
		foregroundCircTriangleWidth = 2;
		
		backgroundSquareWidth = 4;
		backgroundSquareGap = 4;
		foregroundSquareWidth = 4;
		foregroundSquareGap = 4;
	}
}
