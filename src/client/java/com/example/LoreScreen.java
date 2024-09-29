package com.example;

import net.minecraft.client.gui.screen.Screen; // For Screen class
import net.minecraft.client.gui.DrawContext; // For DrawContext class
import net.minecraft.client.gui.widget.ButtonWidget; // For ButtonWidget class
import net.minecraft.text.Text; // For Text class
import net.minecraft.util.Formatting; // For Formatting class

import java.util.ArrayList;
import java.util.List;

public class LoreScreen extends Screen {

    private static final int TITLE_MARGIN_LEFT = 50; // Moved title closer to the left
    private static final int TITLE_MARGIN_TOP = 10; // Moved title closer to the top
    private static final int GRID_COLUMNS = 4;
    private static final int MARGIN_TOP = 20;
    private static final int MARGIN_BOTTOM = 10;
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 20;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;
    private static final int RETURN_BUTTON_WIDTH = 50; // 50% of the original width
    private static final int RETURN_BUTTON_HEIGHT = 30;

    private final List<String> buttonNames;

    public LoreScreen(Text title) {
        super(title);
        // Initialize button names
        buttonNames = new ArrayList<>(List.of(
                "Races",
                "Gods",
                "", // Blank button
                ""  // Blank button
        ));
    }

    @Override
    protected void init() {
        // Calculate button positions
        int buttonSpacingX = (width - 2 * MARGIN_LEFT - BUTTON_WIDTH * GRID_COLUMNS) / (GRID_COLUMNS + 1);
        int buttonY = MARGIN_TOP + TITLE_MARGIN_TOP + BUTTON_HEIGHT; // Adjust y position below the title

        // Create buttons for the top row
        for (int col = 0; col < GRID_COLUMNS; col++) {
            int index = col;
            if (index < buttonNames.size()) { // Ensure we don't exceed the list size
                int x = MARGIN_LEFT + buttonSpacingX + col * (BUTTON_WIDTH + buttonSpacingX);

                String buttonText = buttonNames.get(index);

                ButtonWidget button = ButtonWidget.builder(Text.of(buttonText), btn -> {
                    // Button click logic for Lore
                    if (this.client != null) {
                        if ("Races".equals(buttonText)) {


                            this.client.setScreen(new RaceLorePage1(Text.of("Race Lore Page 1"))); // Set the screen to RaceLorePage1
                        } else if ("Gods".equals(buttonText)) {
                            this.client.setScreen(new GodsScreen(Text.of("Gods Lore")));
                        }
                        // Add actions for blank buttons if needed
                    }
                }).dimensions(x, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT).build();

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

        // Draw the title at the adjusted position with increased size
        String text = "Lore"; // Changed to just "Lore"
        int textX = TITLE_MARGIN_LEFT; // Adjusted left
        int textY = TITLE_MARGIN_TOP; // Use the same top margin as before

        // Draw the title with increased size
        contents.drawCenteredTextWithShadow(textRenderer, Text.literal(text).styled(style -> style.withBold(true).withColor(Formatting.DARK_AQUA)), textX, textY, 0xFFFFFFFF);
    }
}
