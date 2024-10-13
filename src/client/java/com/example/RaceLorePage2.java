package com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaceLorePage2 extends Screen {

    private static final int TITLE_MARGIN_LEFT = 100;
    private static final int GRID_COLUMNS = 4;
    private static final int GRID_ROWS = 3;
    private static final int MARGIN_TOP = 20;
    private static final int MARGIN_BOTTOM = 10;
    private static final int MARGIN_LEFT = 10;
    private static final int MARGIN_RIGHT = 20;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 30;
    private static final int TITLE_MARGIN = 10;
    private static final int LORE_BUTTON_WIDTH = (int) (80 * 0.8);
    private static final int LORE_BUTTON_HEIGHT = 20;
    private static final int PAGINATION_BUTTON_WIDTH = 20;
    private static final int PAGINATION_BUTTON_HEIGHT = 20;

    private final List<String> buttonNames;
    private Formatting randomColor;

    public RaceLorePage2(Text title) {
        super(title);
        // Initialize button names with race names
        buttonNames = new ArrayList<>(List.of(
                "Tiefling",
                "Dragonborn",
                "Sheepfolk",
                "Allic",
                "Blazekin",
                "Endling",
                "Arachnikin",
                "Piglin",
                "Withered",
                "Illager",
                "Ghastling",
                "Merkin"
        ));
    }

    @Override
    protected void init() {
        Formatting[] colors = new Formatting[]{
                Formatting.RED, Formatting.GOLD, Formatting.YELLOW,
                Formatting.GREEN, Formatting.AQUA, Formatting.DARK_AQUA,
                Formatting.BLUE, Formatting.LIGHT_PURPLE, Formatting.DARK_PURPLE
        };
        randomColor = colors[new Random().nextInt(colors.length)];

        int gridWidth = width - 2 * MARGIN_LEFT;
        int gridHeight = height - MARGIN_TOP - MARGIN_BOTTOM;
        int buttonSpacingX = (gridWidth - BUTTON_WIDTH * GRID_COLUMNS) / (GRID_COLUMNS + 1);
        int buttonSpacingY = (gridHeight - BUTTON_HEIGHT * GRID_ROWS) / (GRID_ROWS + 1);

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                int index = row * GRID_COLUMNS + col;
                if (index < buttonNames.size()) {
                    int x = MARGIN_LEFT + buttonSpacingX + col * (BUTTON_WIDTH + buttonSpacingX);
                    int y = MARGIN_TOP + buttonSpacingY + row * (BUTTON_HEIGHT + buttonSpacingY);

                    String buttonText = buttonNames.get(index);

                    ButtonWidget button = ButtonWidget.builder(Text.of(buttonText), btn -> {
                        if (this.client != null) {
                            this.client.setScreen(new RaceLoreScreen(Text.of(buttonText), buttonText));
                            //this.client.setScreen(new RaceDetailScreen(Text.of(buttonText), this, null, buttonText));
                        }
                    }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build();

                    this.addDrawableChild(button);
                }
            }
        }

        int loreButtonX = width - MARGIN_RIGHT - LORE_BUTTON_WIDTH;
        int loreButtonY = TITLE_MARGIN;
        this.addDrawableChild(ButtonWidget.builder(Text.of("Lore"), btn -> {
            if (this.client != null) {
                this.client.player.sendMessage(Text.of("Lore button clicked!"), false);
                this.client.setScreen(new LoreScreen(Text.of("Lore Selection"))); // Pass the title for the Lore screen
            }
        }).dimensions(loreButtonX, loreButtonY, LORE_BUTTON_WIDTH, LORE_BUTTON_HEIGHT).build());

        int paginationButtonX = width - MARGIN_RIGHT - 2 * PAGINATION_BUTTON_WIDTH - 5;
        int paginationButtonY = height - MARGIN_BOTTOM - PAGINATION_BUTTON_HEIGHT;
        this.addDrawableChild(ButtonWidget.builder(Text.of(" < "), btn -> {
            if (this.client != null) {
                this.client.setScreen(new RaceLorePage1(Text.of("Race Lore Page 1")));
            }
        }).dimensions(paginationButtonX, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of(" / "), btn -> {
            if (this.client != null) {
                this.client.player.sendMessage(Text.of("Pagination right clicked!"), false);
            }
        }).dimensions(paginationButtonX + PAGINATION_BUTTON_WIDTH + 5, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build());
    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // White background
        super.render(contents, mouseX, mouseY, delta);

        String titleText = "Race Information uw0";
        int textX = TITLE_MARGIN_LEFT;
        int textY = TITLE_MARGIN;

        // Create the styled text with the random color
        Text styledText = Text.literal(titleText)
                .styled(style -> style.withBold(true).withColor(randomColor)); // Use the instance variable

        // Draw the styled text
        contents.drawCenteredTextWithShadow(textRenderer, styledText, textX, textY, 0xFFFFFFFF);
    }
}
