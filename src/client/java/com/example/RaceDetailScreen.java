package com.example;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Formatting;
import java.util.ArrayList;
import java.util.List;

public class RaceDetailScreen extends Screen {

    private final Screen previousScreen;
    private final List<JsonReader.RaceData> raceDataList;
    private JsonReader.RaceData currentRaceData;

    public RaceDetailScreen(Text title, Screen previousScreen, JsonReader.RaceData raceData, String buttonText) {
        super(title);
        this.previousScreen = previousScreen;
        this.currentRaceData = raceData;

        // Read JSON data
        this.raceDataList = JsonReader.readJson("races.json");

        System.out.println(this.currentRaceData.getPhysicalAttributes().getHealth());
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

        this.currentRaceData = (this.currentRaceData == null && !raceDataList.isEmpty()) ? raceDataList.get(0) : this.currentRaceData;
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
            if (this.client != null) {
                // Get the selected race's data
                String raceName = currentRaceData.getRaceName();

                // Assuming PhysicalAttributes is a custom class with a getHealth() method
                String healthString = currentRaceData.getPhysicalAttributes().getHealth().getText();

                // Strip out non-numeric characters (like "Health: ")
                String healthValueString = healthString.replaceAll("[^0-9.]", "");  // Keeps only digits and period

                // Convert the health string to a float (or int)
                float healthValue = 0.0f; // Default value in case parsing fails
                try {
                    healthValue = Float.parseFloat(healthValueString); // Use Float.parseFloat to convert
                } catch (NumberFormatException e) {
                    System.out.println("Invalid health value: " + healthString);
                }

                // Modify player's attack damage here
                PlayerEntity player = this.client.player;
                if (player != null) {
                    // Set attack damage based on the race or other factors
                    float damageMultiplier = 1.0f; // Default damage multiplier
                    if (raceName.equals("Orc")) { // Example: Orc race gives bonus damage
                        damageMultiplier = 1.5f; // Increase damage by 50%
                    } else if (raceName.equals("Elf")) { // Example: Elf race has less damage
                        damageMultiplier = 0.8f; // Decrease damage by 20%
                    }

                    // Set the attack damage modifier
                    // Use the correct operation (MULTIPLY_BASE or ADDITION)
                    //player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(
                     //       new EntityAttributeModifier("Race Attack Damage Modifier", damageMultiplier - 1.0, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    //);
                }

                // Send a message to the in-game chat
                this.client.player.sendMessage(
                        Text.of("You selected: " + raceName + " with " + healthString + " health and modified damage."),
                        false // Set to true for system messages
                );

                // Log the selection to the console
                System.out.println("Player selected: " + raceName + " | Health: " + healthString);
//fs
                // Set the player's health (assuming health is in the correct format)
                this.client.player.setHealth(healthValue / 2.0f); // Dividing by 2 to match Minecraft's half-heart system

                // Close the selection screen
                this.close();
            }
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
        // Set left area background color
        contents.fill(0, 30, leftWidth, 30 + leftHeight, 0xFFCCCCCC); // Left area background

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
        // Calculate the Y position for attributes starting from the middle of the lefty text box
        int attributesYPosition = 30 + leftHeight / 2; // Start at 50% height of lefty box

        // Background for the racial traits
        int traitsBackgroundHeight = 50; // Height of the traits background box
        contents.fill(leftyTextX - 2, attributesYPosition - 10, leftWidth - 10, attributesYPosition + traitsBackgroundHeight, 0x88000000);

        // Display physical attributes
        // Draw racial traits in the bottom left of the lefty text box
        if (currentRaceData != null) {
            // Get the trait names and render them
            List<String> traitNames = currentRaceData.getTraitNames(); // Fetch trait names
            int attributesX = leftyTextX + 10; // Padding from the left

            // Render each trait name
            for (String traitName : traitNames) {
                contents.drawTextWithShadow(textRenderer, Text.literal(traitName), attributesX, attributesYPosition, 0xFFFFFFFF);
                attributesYPosition += textRenderer.fontHeight; // Move down for the next trait
                // Check if the Y position exceeds the bottom of the lefty box
                if (attributesYPosition > (30 + leftHeight - 10)) { // 10 for padding
                    break; // Stop rendering if it exceeds the box
                }
            }
        }

        // Display physical attributes in the right area
        if (currentRaceData != null) {
            String heightText = currentRaceData.getPhysicalAttributes().getHeight().getText();
            String healthText = currentRaceData.getPhysicalAttributes().getHealth().getText();

            // Split the attributes into an array for rendering line by line
            String[] rightLines = { heightText, healthText };
            int rightTextX = leftWidth + 10;
            int rightTextY = 40;

            // Background for the right area
            contents.fill(leftWidth, 30, width, 30 + leftHeight, 0xFFAAAAAA); // Right area background (different color)

            // Background for the right box
            int rightBoxHeight = (rightLines.length * textRenderer.fontHeight) + 10; // 10 for padding
            contents.fill(rightTextX - 2, rightTextY - 2, width - 10, rightTextY + rightBoxHeight + 2, 0x88000000);
            contents.fill(rightTextX - 2, rightTextY - 2, width - 10, rightTextY + rightBoxHeight + 2, 0x88000000); // Background for right box

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

