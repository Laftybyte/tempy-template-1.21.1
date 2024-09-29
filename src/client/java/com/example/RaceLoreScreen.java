package com.example;

import net.minecraft.client.gui.screen.Screen; // For Screen class
import net.minecraft.client.gui.DrawContext; // For DrawContext class
import net.minecraft.client.gui.widget.ButtonWidget; // For ButtonWidget class
import net.minecraft.text.Text; // For Text class
import net.minecraft.util.Formatting; // For Formatting class

public class RaceLoreScreen extends Screen {

    private static final int TITLE_MARGIN_LEFT = 50; // Margin for the title
    private static final int TITLE_MARGIN_TOP = 10; // Vertical position for the title
    private static final int RETURN_BUTTON_WIDTH = 50; // Width for the return button
    private static final int RETURN_BUTTON_HEIGHT = 30; // Height for the return button
    private final String raceName; // Name of the selected race

    public RaceLoreScreen(Text title, String raceName) {
        super(title);
        this.raceName = raceName; // Store the name of the selected race
    }

    @Override
    protected void init() {
        // Calculate button positions based on the layout of GodsScreen
        int buttonSpacingX = (width - 2 * 10 - 100 * 4) / (4 + 1); // 10 is the MARGIN_LEFT, and 100 is BUTTON_WIDTH
        int topButtonY = 20 + TITLE_MARGIN_TOP + 30; // Using MARGIN_TOP and BUTTON_HEIGHT for the Y position of the last row
        int returnButtonX = 10 + buttonSpacingX + (4 - 1) * (100 + buttonSpacingX) + 100 - RETURN_BUTTON_WIDTH; // Align with the last button
        int returnButtonY = topButtonY - (RETURN_BUTTON_HEIGHT + 15); // Position above the last button

        // Add the "Return" button in the calculated position
        this.addDrawableChild(ButtonWidget.builder(Text.of("Return"), btn -> {
            if (this.client != null) {
                this.client.setScreen(new LoreScreen(Text.of("Gods"))); // Return to the Gods screen
            }
        }).dimensions(returnButtonX, returnButtonY, RETURN_BUTTON_WIDTH, RETURN_BUTTON_HEIGHT).build());
    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // Fully opaque white
        super.render(contents, mouseX, mouseY, delta);

        // Draw the title at the adjusted position
        String text = raceName; // Title text
        int textX = TITLE_MARGIN_LEFT; // Left margin for the title
        int textY = TITLE_MARGIN_TOP; // Use the same top margin as before

        // Draw the title
        contents.drawCenteredTextWithShadow(textRenderer, Text.literal(text).styled(style -> style.withBold(true).withColor(Formatting.BLUE)), textX, textY, 0xFFFFFFFF);

        // Draw the box for race details (placeholder background)
        int boxX = 50; // X position of the box
        int boxY = 50; // Y position of the box
        int boxWidth = width - 100; // Width of the box
        int boxHeight = height - 100; // Height of the box
        contents.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xFFAAAAAA); // Gray background for the box
    }
}
