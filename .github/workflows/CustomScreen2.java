package com.example;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.item.MinecartItem;
import net.minecraft.text.Text;

public class CustomScreen extends Screen {
    public CustomScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Hello World"), (btn) -> {
            if (this.client != null) {
                this.client.getToastManager().add(
                        SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("Hello World!"), Text.of("This is a toast."))
                );
            }
        }).dimensions(40, 40, 120, 20).build();

        this.addDrawableChild(buttonWidget);




    }

    @Override
    public void render(DrawContext contents, int mouseX, int mouseY, float delta) {
        // Fill the entire screen with a white background
        contents.fill(0, 0, width, height, 0xFFFFFFFF); // Fully opaque white

        // Render the default background first
        super.render(contents, mouseX, mouseY, delta);

        // Draw a white-filled rectangle in the center with correct alpha for visibility
        contents.fill(width / 2, height / 2, width / 2 + 100, height / 2 + 100, 0xFFFFFFFF); // Fully opaque white

        // Draw a red border at the specified area
        contents.drawBorder(10, 10, 100, 100, 0xFFFF0000); // Corrected color to fully opaque red

        // Draw a vertical white line
        contents.drawVerticalLine(10, 10, height, 0xFFFFFFFF); // Fully opaque white line

        // Draw centered text
        contents.drawCenteredTextWithShadow(textRenderer, Text.literal(" ribbit - sleepy lafty"), width / 2, height / 2, 0xFFFFFFFF); // Fully opaque white text

        // Draw a small white square
        contents.fill(10, 10, 20, 20, 0xFFFFFFFF); // Fully opaque white

        // Draw a border on the right side of the screen extending halfway
        int margin = 10; // Margin from the right edge
        int borderWidth = (width / 2) - margin; // Border width extending halfway across the screen
        int borderX = width - borderWidth - margin; // X position for the right border
        contents.drawBorder(borderX, 10, borderWidth, height - 20, 0xFF00FF00); // Green border filling halfway across the right side

        // Draw text inside the right border
        int textX = borderX + 10; // 10-pixel padding inside the border
        int textY = 20; // Start text near the top of the border
        float scale = 0.7f; // Adjust this value to change the font size (1.0f is normal size, less than 1.0f reduces size)

        // Apply scaling transformation for smaller text
        contents.getMatrices().push(); // Save the current transformation state
        contents.getMatrices().scale(scale, scale, 1.0f); // Scale down both x and y

        // Adjust textX and textY to fit scaling (compensate for the scale factor)
        int scaledTextX = (int) (textX / scale);
        int scaledTextY = (int) (textY / scale);

        // A poem about loving code
        String poem = "Lines of code, I write with glee,\n"
                + "A dance of logic, wild and free.\n"
                + "Through loops and turns, the patterns show,\n"
                + "The secrets only coders know.\n"
                + "\n"
                + "From ifs to whiles, and then some more,\n"
                + "Each function opens up a door.\n"
                + "In classes deep, and methods wide,\n"
                + "I find my joy and take great pride.\n"
                + "\n"
                + "To debug lines, to see them through,\n"
                + "Is like a puzzle built for few.\n"
                + "But when it runs, and works just right,\n"
                + "It feels like magic in the night.\n"
                + "\n"
                + "So here's to code, my heart's delight,\n"
                + "I'll craft my dreams with every byte.";

        // Split the poem into lines for rendering
        String[] lines = poem.split("\n");
        for (String line : lines) {
            // Updated drawText method call to include scaling
            contents.drawText(textRenderer, Text.literal(line), scaledTextX, scaledTextY, 0xFFFFFFFF, false); // Fully opaque white text without shadow
            scaledTextY += 12 / scale; // Adjust line spacing to accommodate scaling
        }

        // Restore the transformation state to undo scaling
        contents.getMatrices().pop();
    }




}
