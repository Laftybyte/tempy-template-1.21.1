package com.example;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class RaceDetailScreen extends Screen {

    private final Screen previousScreen;
    private final List<JsonReader.RaceData> raceDataList;
    private JsonReader.RaceData currentRaceData;

    public RaceDetailScreen(Text title, Screen previousScreen, JsonReader.RaceData raceData,String buttonText) {
        super(title);
        this.previousScreen = previousScreen;
        this.currentRaceData = raceData; // Set currentRaceData to the passed race data

        // Read JSON data
        this.raceDataList = JsonReader.readJson("races.json");



        // Print the button text
        System.out.println("Button text: " + buttonText);
        System.out.println(raceData); // [00:08:28] [Render thread/INFO] (Minecraft) [STDOUT]: com.example.JsonReader$RaceData@2e91f25b
        System.out.println("Loaded races: " + raceDataList.size());






        // Iterate through the raceDataList to find the race matching the buttonText
        for (JsonReader.RaceData race : raceDataList) {
            if (race.getRaceName().equals(buttonText)) {
                this.currentRaceData = race; // Set currentRaceData to the matching race
                break;
            }
        }


        this.currentRaceData = (this.currentRaceData == null && !raceDataList.isEmpty()) ? raceDataList.getFirst() : this.currentRaceData;



        //this.currentRaceData = raceDataList.isEmpty() ? null : raceDataList.get(1); // Default to the first race
    }

    @Override
    protected void init() {
        // Add a "Go Back" button at the bottom left
        this.addDrawableChild(ButtonWidget.builder(Text.of("Go Back"), btn -> {
            if (this.client != null) {
                this.client.setScreen(previousScreen);
            }
        }).dimensions(10, height - 30, 100, 20).build());

        // Add a "Confirm" button at the bottom right
        this.addDrawableChild(ButtonWidget.builder(Text.of("Confirm"), btn -> {
            // Define the action for the Confirm button here
        }).dimensions(width - 110, height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        contents.fill(0, 0, width, height, 0xFFFFFFFF);
        super.render(contents, mouseX, mouseY, delta);

        // Draw title text
        String raceName = currentRaceData != null ? currentRaceData.getRaceName() : "No Race Selected";
        int textX = 10;
        int textY = 10;

        Formatting fixedColor = Formatting.WHITE;
        Text styledText = Text.literal(raceName).styled(style -> style.withBold(true).withColor(fixedColor.getColorValue()));
        contents.drawTextWithShadow(textRenderer, styledText, textX, textY, 0xFF000000);

        // Draw left area
        int leftWidth = width * 7 / 10;
        int leftHeight = height - 80;
        contents.fill(0, 30, leftWidth, 30 + leftHeight, 0xFFCCCCCC);

        // Draw the background description
        String leftyText = currentRaceData != null ? currentRaceData.getBackgroundDescription() : "No description available.";
        int leftyTextX = 10;
        int leftyTextY = 40;

        // Split text into lines that fit within the left area
        List<String> wrappedLines = wrapText(leftyText, leftWidth - 20); // 20 is for padding
        int descriptionBoxHeight = wrappedLines.size() * textRenderer.fontHeight + 10; // 10 for padding
        contents.fill(leftyTextX - 2, leftyTextY - 2, leftWidth - 10, leftyTextY + descriptionBoxHeight + 2, 0x88000000);

        // Render each line of the description
        for (String line : wrappedLines) {
            contents.drawTextWithShadow(textRenderer, Text.literal(line), leftyTextX, leftyTextY, 0xFFFFFFFF);
            leftyTextY += textRenderer.fontHeight; // Move down for the next line
        }

        // Draw right area
        int rightWidth = width * 3 / 10;
        contents.fill(leftWidth, 30, width, 30 + leftHeight, 0xFFAAAAAA);

        // Display physical attributes
        if (currentRaceData != null) {
            String heightText = currentRaceData.getPhysicalAttributes().getHeight().getText();
            String healthText = currentRaceData.getPhysicalAttributes().getHealth().getText();

            // Split the attributes into an array for rendering line by line
            String[] rightLines = { heightText, healthText };
            int rightTextX = leftWidth + 10;
            int rightTextY = 40;

            // Background for the right area
            int rightBoxHeight = (rightLines.length * textRenderer.fontHeight) + 10; // 10 for padding
            contents.fill(rightTextX - 2, rightTextY - 2, width - 10, rightTextY + rightBoxHeight + 2, 0x88000000);

            // Render each line separately
            for (String line : rightLines) {
                contents.drawTextWithShadow(textRenderer, Text.literal(line), rightTextX, rightTextY, 0xFFFFFFFF);
                rightTextY += textRenderer.fontHeight; // Move down for the next line
            }

            // Draw racial traits below the physical attributes
            int traitsTextX = leftWidth + 10;
            int traitsTextY = rightTextY + 20; // Space below physical attributes

            // Get the trait names and render them
            List<String> traitNames = currentRaceData.getTraitNames(); // Fetch trait names
            for (String traitName : traitNames) {
                contents.drawTextWithShadow(textRenderer, Text.literal(traitName), traitsTextX, traitsTextY, 0xFFFFFFFF);
                traitsTextY += textRenderer.fontHeight; // Move down for the next trait
            }
        }
    }

    // Method to wrap text to fit within a specified width
    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (textRenderer.getWidth(currentLine + word) <= maxWidth) {
                currentLine.append(word).append(" ");
            } else {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder(word + " ");
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }

}
