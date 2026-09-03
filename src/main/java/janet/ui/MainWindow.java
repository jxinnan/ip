package janet.ui;

import janet.Janet;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controls Janet's main FXML view.
 */
public class MainWindow {
    /** Delay that lets the goodbye response remain visible before exit. */
    private static final Duration EXIT_DELAY = Duration.seconds(1.0);

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    /** Janet instance used to process commands. */
    private Janet janet;

    /**
     * Adds the greeting and keeps the newest dialog visible.
     */
    @FXML
    public void initialize() {
        dialogContainer.getChildren().add(DialogBox.getJanetDialog(
                "Hi! I'm Janet. Type a command below, and I'll help manage your tasks."));
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> scrollPane.setVvalue(1.0));
    }

    /**
     * Supplies the Janet instance used to process commands.
     *
     * @param janet chatbot instance
     */
    public void setJanet(Janet janet) {
        this.janet = janet;
        userInput.requestFocus();
    }

    /**
     * Submits the current input, displays both sides of the exchange, and handles exit.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (!input.isBlank()) {
            dialogContainer.getChildren().add(DialogBox.getUserDialog(input));
        }
        dialogContainer.getChildren().add(DialogBox.getJanetDialog(janet.getResponse(input)));
        userInput.clear();

        if (input.equals("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition exitPause = new PauseTransition(EXIT_DELAY);
            exitPause.setOnFinished(event -> Platform.exit());
            exitPause.play();
        }
    }
}
