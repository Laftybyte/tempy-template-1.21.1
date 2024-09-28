package com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CustomScreen extends Screen {
    private static final int GRID_COLUMNS = 4;
    private static final int GRID_ROWS = 3;
    private static final int MARGIN_TOP = 20; // 20% from top of screen
    private static final int MARGIN_BOTTOM = 10; // 10% from bottom of screen
    private static final int MARGIN_LEFT = 10; // 10% from left of screen
    private static final int MARGIN_RIGHT = 20; // 20% from right of screen
    private static final int BUTTON_WIDTH = 100; // Width of each button
    private static final int BUTTON_HEIGHT = 30; // Height of each button
    private static final int TITLE_MARGIN = 10; // Margin for the title from the top-left corner
    private static final int LORE_BUTTON_WIDTH = (int) (80 * 0.8); // Width of the Lore button, 20% smaller
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
        int loreButtonX = width - MARGIN_RIGHT - LORE_BUTTON_WIDTH;
        int loreButtonY = TITLE_MARGIN;
        this.addDrawableChild(ButtonWidget.builder(Text.of("Lore"), btn -> {
                    // Lore button click logic
                    if (this.client != null) {
                        this.client.player.sendMessage(Text.of("Lore button clicked!"), false);
                    }
                }).dimensions(loreButtonX, loreButtonY, LORE_BUTTON_WIDTH, LORE_BUTTON_HEIGHT).build()
        );

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
                    }
                }).dimensions(paginationButtonX + PAGINATION_BUTTON_WIDTH + 5, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build()
        );

    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        // Render the default background first
        super.render(contents, mouseX, mouseY, delta);

        // Draw a white-filled rectangle as background
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // White background

        // Render the screen's components
        super.render(contents, mouseX, mouseY, delta);
    }
}
