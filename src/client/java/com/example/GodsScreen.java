package com.example;

import net.minecraft.client.gui.screen.Screen; // For Screen class
import net.minecraft.client.gui.DrawContext; // For DrawContext class
import net.minecraft.client.gui.widget.ButtonWidget; // For ButtonWidget class
import net.minecraft.text.Text; // For Text class
import net.minecraft.util.Formatting; // For Formatting class

import java.util.ArrayList;
import java.util.List;

public class GodsScreen extends Screen {

    private static final int TITLE_MARGIN_LEFT = 50; // Adjusted to move title left
    private static final int TITLE_MARGIN_TOP = 10; // Adjusted for vertical position
    private static final int GRID_COLUMNS = 4;
    private static final int MARGIN_TOP = 20;
    private static final int MARGIN_BOTTOM = 10;
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 20;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;
    private static final int RETURN_BUTTON_WIDTH = 50; // 50% of the original width
    private static final int RETURN_BUTTON_HEIGHT = 30;

    private final List<String> topButtonNames;
    private final List<String> bottomButtonNames;

    public GodsScreen(Text title) {
        super(title);
        // Initialize button names for both rows
        topButtonNames = new ArrayList<>(List.of(
                "God 1",
                "God 2",
                "God 3",
                "God 4"
        ));

        bottomButtonNames = new ArrayList<>(List.of(
                "God 5",
                "God 6",
                "God 7",
                "God 8"
        ));
    }

    @Override
    protected void init() {
        // Calculate button positions
        int buttonSpacingX = (width - 2 * MARGIN_LEFT - BUTTON_WIDTH * GRID_COLUMNS) / (GRID_COLUMNS + 1);
        int topButtonY = MARGIN_TOP + TITLE_MARGIN_TOP + BUTTON_HEIGHT; // Adjust y position for the top row
        int bottomButtonY = topButtonY + BUTTON_HEIGHT + MARGIN_BOTTOM + BUTTON_HEIGHT + MARGIN_BOTTOM; // Position for the bottom row
        int buttonY = MARGIN_TOP + TITLE_MARGIN_TOP + BUTTON_HEIGHT; // Adjust y position below the title
        // Create buttons for the top row
        for (int col = 0; col < GRID_COLUMNS; col++) {
            int index = col;
            if (index < topButtonNames.size()) { // Ensure we don't exceed the list size
                int x = MARGIN_LEFT + buttonSpacingX + col * (BUTTON_WIDTH + buttonSpacingX);

                String buttonText = topButtonNames.get(index);

                ButtonWidget button = ButtonWidget.builder(Text.of(buttonText), btn -> {
                    // Button click logic for top row gods
                }).dimensions(x, topButtonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();

                this.addDrawableChild(button);
            }
        }

        // Create buttons for the bottom row
        for (int col = 0; col < GRID_COLUMNS; col++) {
            int index = col;
            if (index < bottomButtonNames.size()) { // Ensure we don't exceed the list size
                int x = MARGIN_LEFT + buttonSpacingX + col * (BUTTON_WIDTH + buttonSpacingX);

                String buttonText = bottomButtonNames.get(index);

                ButtonWidget button = ButtonWidget.builder(Text.of(buttonText), btn -> {
                    // Button click logic for bottom row gods
                }).dimensions(x, bottomButtonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();

                this.addDrawableChild(button);
            }
        }

        // Add the "Return" button above the last button in the button row
        int returnButtonX = MARGIN_LEFT + buttonSpacingX + (GRID_COLUMNS - 1) * (BUTTON_WIDTH + buttonSpacingX) + BUTTON_WIDTH - RETURN_BUTTON_WIDTH; // Align with the last button
        int returnButtonY = buttonY - (RETURN_BUTTON_HEIGHT + 15); // Position higher above the last button
        this.addDrawableChild(ButtonWidget.builder(Text.of("Return"), btn -> {
            if (this.client != null) {
                this.client.setScreen(new CustomScreen(Text.of("Previous Screen"))); // Goes back to main screen
            }
        }).dimensions(returnButtonX, returnButtonY, RETURN_BUTTON_WIDTH, RETURN_BUTTON_HEIGHT).build());
    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // Fully opaque white
        super.render(contents, mouseX, mouseY, delta);

        // Draw the title at the adjusted position
        String text = "Gods"; // Title text
        int textX = TITLE_MARGIN_LEFT; // Adjusted left
        int textY = TITLE_MARGIN_TOP; // Use the same top margin as before

        // Draw the title
        contents.drawCenteredTextWithShadow(textRenderer, Text.literal(text).styled(style -> style.withBold(true).withColor(Formatting.DARK_AQUA)), textX, textY, 0xFFFFFFFF);
    }
}
