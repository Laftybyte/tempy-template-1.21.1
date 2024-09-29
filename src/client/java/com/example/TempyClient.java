package com.example;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

// ... other imports

//agwa test
// HRRRM
public class TempyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client)
                -> {
            if (client.mouse != null) {
                client.mouse.unlockCursor();
                MinecraftClient.getInstance().setScreen(new CustomScreen(Text.empty()));
            }
        });
    }
}


/*

@Environment(EnvType.CLIENT)
public class TempyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {


        // Register client-side events and screens here
        System.out.println("TempyClient initialized!");
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // This will run once when the player joins the world
            // Schedule the screen opening after the game has fully initialized


            if (client.player != null) {
                client.player.sendMessage(Text.literal("Hello, world!"), false);
                MinecraftClient.getInstance().execute(CustomScreen::showCustomMessageScreen);
            }
        });


    }
}
cakee?

 */
