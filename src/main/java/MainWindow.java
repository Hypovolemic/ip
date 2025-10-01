import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private FengWei fengWei;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/TheUser.png"));
    private Image fengWeiImage = new Image(this.getClass().getResourceAsStream("/images/TheFengWei.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the FengWei instance */
    public void setFengWei(FengWei f) {
        fengWei = f;
        // Show FengWei's introduction when the GUI starts, just like the CLI version
        String welcomeMessage = fengWei.getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getFengWeiDialog(welcomeMessage, fengWeiImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing FengWei's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = fengWei.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getFengWeiDialog(response, fengWeiImage)
        );
        userInput.clear();

        // Check if the user entered a bye command and close the application
        if (fengWei.isByeCommand(input)) {
            // Use Platform.runLater to allow the goodbye message to be displayed first
            Platform.runLater(() -> {
                try {
                    Thread.sleep(1500); // Wait 1.5 seconds to let user see the goodbye message
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Platform.exit(); // Close the JavaFX application
                System.exit(0); // Ensure complete shutdown
            });
        }
    }
}
