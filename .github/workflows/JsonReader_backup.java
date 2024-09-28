package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JsonReader {

    // Method to read JSON using MinecraftClient's ResourceManager
    public static List<RaceData> readJson() {
        Gson gson = new Gson();
        // Create the Identifier with your mod ID and the path to the JSON file
        Identifier id = Identifier.of("tempy", "races.json"); // Ensure "tempy" is your mod ID and path is correct

        try {
            // Use MinecraftClient's ResourceManager to access the file, handling the Optional<Resource> properly
            Optional<Resource> optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(id);

            // Check if the resource is present
            if (optionalResource.isPresent()) {
                Resource resource = optionalResource.get();
                try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                    // Parse the JSON into the RaceData array
                    return List.of(gson.fromJson(reader, RaceData[].class));
                }
            } else {
                System.out.println("Resource not found: " + id);
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Define RaceData class to map JSON structure
    public static class RaceData {
        private String name;
        private Attributes attributes;

        public String getName() {
            return name;
        }

        public Attributes getAttributes() {
            return attributes;
        }

        public static class Attributes {
            private int speed;
            private int strength;

            public int getSpeed() {
                return speed;
            }

            public int getStrength() {
                return strength;
            }
        }
    }
}
