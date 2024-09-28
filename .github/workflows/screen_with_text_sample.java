NextScreenpackage com.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NextScreen extends Screen {

    public NextScreen(Text title) {
        super(title);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        // Draw your content for the next screen here
        // Draw the styled text
        context.drawCenteredTextWithShadow(textRenderer, "hiiiiii",55, 55, 0xFFFFFFFF); // Fully opaque white shadow

    }
}
