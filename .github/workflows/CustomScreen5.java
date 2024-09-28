package com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class CustomScreen extends Screen {

    private static final int TITLE_MARGIN_LEFT = 100;
    private static final int GRID_COLUMNS = 4;
    private static final int GRID_ROWS = 3;
    private static final int MARGIN_TOP = 20; // 20% from top of screen
    private static final int MARGIN_BOTTOM = 10; // 10% from bottom of screen
    private static final int MARGIN_LEFT = 10; // 20% from left of screen
    private static final int MARGIN_RIGHT = 20; // 20% from right of screen
    private static final int BUTTON_WIDTH = 100; // Width of each button
    private static final int BUTTON_HEIGHT = 30; // Height of each button
    private static final int TITLE_MARGIN = 10; // Margin for the title from the top-left corner
    private static final int LORE_BUTTON_WIDTH = (int) (80 * 0.8); // Width of the Lore button, 20% smaller Ya totally not written  by ai pfsht.
    private static final int LORE_BUTTON_HEIGHT = 20; // Height of the Lore button
    private static final int PAGINATION_BUTTON_WIDTH = 20; // Width of pagination buttons
    private static final int PAGINATION_BUTTON_HEIGHT = 20; // Height of pagination buttons

    public CustomScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        // Calculate the grid dimensions
        int gridWidth = width - 2 * MARGIN_LEFT;
        int gridHeight = height - MARGIN_TOP - MARGIN_BOTTOM;

        int buttonSpacingX = (gridWidth - BUTTON_WIDTH * GRID_COLUMNS) / (GRID_COLUMNS + 1);
        int buttonSpacingY = (gridHeight - BUTTON_HEIGHT * GRID_ROWS) / (GRID_ROWS + 1);

        // Create buttons for the grid
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                int x = MARGIN_LEFT + buttonSpacingX + col * (BUTTON_WIDTH + buttonSpacingX);
                int y = MARGIN_TOP + buttonSpacingY + row * (BUTTON_HEIGHT + buttonSpacingY);

                String buttonText = "Button " + (row * GRID_COLUMNS + col + 1);
                ButtonWidget button = ButtonWidget.builder(Text.of(buttonText), btn -> {
                    // Button click logic
                    if (this.client != null) {
                        this.client.player.sendMessage(Text.of(buttonText + " clicked!"), false);
                    }
                }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build();

                this.addDrawableChild(button);
            }
        }

// Add the Lore button at the top-right of the screen
// Add the Lore button at the top-right of the screen
        int loreButtonX = width - MARGIN_RIGHT - LORE_BUTTON_WIDTH;
        int loreButtonY = TITLE_MARGIN;
        this.addDrawableChild(ButtonWidget.builder(Text.of("Lore"), btn -> {
            // Lore button click logic
            if (this.client != null) {
                // Read data from JSON
                List<JsonReader.RaceData> races = JsonReader.readJson("races.json");

                // Check if races were read successfully
                if (races != null && !races.isEmpty()) {
                    // Display the name of the first race
                    String raceName = races.get(0).getName(); // Display other data as needed
                    this.client.player.sendMessage(Text.of("Race: " + raceName), false);
                } else {
                    this.client.player.sendMessage(Text.of("Failed to read race data!"), false);
                }
            }
        }).dimensions(loreButtonX, loreButtonY, LORE_BUTTON_WIDTH, LORE_BUTTON_HEIGHT).build());

        // Add pagination buttons at the bottom-right of the screen, side by side
        int paginationButtonX = width - MARGIN_RIGHT - 2 * PAGINATION_BUTTON_WIDTH - 5; // 5-pixel gap between buttons
        int paginationButtonY = height - MARGIN_BOTTOM - PAGINATION_BUTTON_HEIGHT;
        this.addDrawableChild(ButtonWidget.builder(Text.of(" / "), btn -> {
                    // Pagination left button click logic
                    if (this.client != null) {
                        this.client.player.sendMessage(Text.of("Pagination left clicked!"), false);
                    }
                }).dimensions(paginationButtonX, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build()
        );

        this.addDrawableChild(ButtonWidget.builder(Text.of(" > "), btn -> {
                    // Pagination right button click logic
                    if (this.client != null) {
                        this.client.player.sendMessage(Text.of("Pagination right clicked!"), false);
                        this.client.setScreen(new NextScreen(Text.of("Next Screen")));
                    }
                }).dimensions(paginationButtonX + PAGINATION_BUTTON_WIDTH + 5, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build()
        );

    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        // Fill the entire screen with a white background
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // Fully opaque white

        // Render the default background first
        super.render(contents, mouseX, mouseY, delta);

        // Draw the "Race Selection" text at the top-left corner
        String text = "Race Selection";
        int textX = TITLE_MARGIN_LEFT; // X position from the left margin
        int textY = TITLE_MARGIN; // Y position from the top margin


        // Create a styled text component with bold and shadow
        Text styledText = Text.literal(text)
                .styled(style -> style.withBold(true) // Make text bold
                        .withColor(Formatting.DARK_PURPLE));// Change text color


        // Draw the styled text
        contents.drawCenteredTextWithShadow(textRenderer, styledText, textX, textY, 0xFFFFFFFF); // Fully opaque white shadow

        // Draw the text with white color
        //contents.drawCenteredTextWithShadow(textRenderer, Text.literal(text), textX, textY, 0xFFFFFFFF); // Fully opaque white text
    }
}
