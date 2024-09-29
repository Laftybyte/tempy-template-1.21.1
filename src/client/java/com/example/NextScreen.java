package com.example;
import java.util.Random;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class NextScreen extends Screen {

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
    private Formatting randomColor; // Instance variable for random color

    public NextScreen(Text title) {
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



        // Define an array of colors
        Formatting[] colors = new Formatting[] {
                Formatting.RED,
                Formatting.GOLD,
                Formatting.YELLOW,
                Formatting.GREEN,
                Formatting.AQUA,
                Formatting.DARK_AQUA,
                Formatting.BLUE,
                Formatting.DARK_BLUE,
                Formatting.LIGHT_PURPLE,
                Formatting.DARK_PURPLE,
                Formatting.WHITE,
                Formatting.GRAY,
                Formatting.DARK_GRAY
        };

        // Generate a random index to select a color
        Random random = new Random();
        randomColor = colors[random.nextInt(colors.length)]; // Set the random color



        // Calculate the grid dimensions
        int gridWidth = width - 2 * MARGIN_LEFT;
        int gridHeight = height - MARGIN_TOP - MARGIN_BOTTOM;

        int buttonSpacingX = (gridWidth - BUTTON_WIDTH * GRID_COLUMNS) / (GRID_COLUMNS + 1);
        int buttonSpacingY = (gridHeight - BUTTON_HEIGHT * GRID_ROWS) / (GRID_ROWS + 1);
        List<JsonReader.RaceData> raceDataList = JsonReader.readJson("races.json");
        // Create buttons for the grid
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                int index = row * GRID_COLUMNS + col;
                if (index < buttonNames.size()) { // Ensure we don't exceed the list size
                    int x = MARGIN_LEFT + buttonSpacingX + col * (BUTTON_WIDTH + buttonSpacingX);
                    int y = MARGIN_TOP + buttonSpacingY + row * (BUTTON_HEIGHT + buttonSpacingY);

                    JsonReader.RaceData raceData = raceDataList.get(index); // Get the RaceData object
                    String buttonText = buttonNames.get(index);

                    ButtonWidget button = ButtonWidget.builder(Text.of(buttonText), btn -> {
                        // Button click logic
                        if (this.client != null) {
                            this.client.setScreen(new RaceDetailScreen(Text.of(buttonText), this, raceData, buttonText));
                            //this.client.player.sendMessage(Text.of(buttonText + " clicked!"), false);
                        }
                    }).dimensions(x, y, BUTTON_WIDTH, BUTTON_HEIGHT).build();

                    this.addDrawableChild(button);
                }
            }
        }

        // Add the Lore button at the top-right of the screen
        int loreButtonX = width - MARGIN_RIGHT - LORE_BUTTON_WIDTH;
        int loreButtonY = TITLE_MARGIN;
        this.addDrawableChild(ButtonWidget.builder(Text.of("Lore"), btn -> {
            if (this.client != null) {
                this.client.player.sendMessage(Text.of("Lore button clicked!"), false);
            }
        }).dimensions(loreButtonX, loreButtonY, LORE_BUTTON_WIDTH, LORE_BUTTON_HEIGHT).build());

        // Add pagination buttons
        int paginationButtonX = width - MARGIN_RIGHT - 2 * PAGINATION_BUTTON_WIDTH - 5;
        int paginationButtonY = height - MARGIN_BOTTOM - PAGINATION_BUTTON_HEIGHT;
        this.addDrawableChild(ButtonWidget.builder(Text.of(" < "), btn -> {
            // Pagination left button click logic
            if (this.client != null) {
                this.client.setScreen(new CustomScreen(Text.of("Custom Screen")));
            }
        }).dimensions(paginationButtonX, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of(" / "), btn -> {
            // Pagination right button click logic
            if (this.client != null) {
                this.client.player.sendMessage(Text.of("Pagination right clicked!"), false);
            }
        }).dimensions(paginationButtonX + PAGINATION_BUTTON_WIDTH + 5, paginationButtonY, PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT).build());
    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // Fully opaque white
        super.render(contents, mouseX, mouseY, delta);

        // Draw the "Next Screen" text at the top-left corner
        String text = "Race Selection";
        int textX = TITLE_MARGIN_LEFT;
        int textY = TITLE_MARGIN;


        // Create the styled text with a random color
        Text styledText = Text.literal(text)
                .styled(style -> style.withBold(true).withColor(randomColor));
        contents.drawCenteredTextWithShadow(textRenderer, styledText, textX, textY, 0xFFFFFFFF);
    }
}
