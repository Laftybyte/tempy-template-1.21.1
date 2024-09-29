package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JsonReader {

    // Method to read JSON using MinecraftClient's ResourceManager
    public static List<RaceData> readJson(String filename) {
        Gson gson = new Gson();
        Identifier id = Identifier.of("tempy", filename);

        try {
            Optional<Resource> optionalResource = MinecraftClient.getInstance().getResourceManager().getResource(id);

            if (optionalResource.isPresent()) {
                Resource resource = optionalResource.get();
                try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                    RaceData[] raceDataArray = gson.fromJson(reader, RaceData[].class);
                    System.out.println("<<<<<Json Loaded!");
                    return List.of(raceDataArray);
                }
            } else {
                System.out.println("Resource not found: " + id);
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Define RaceData class to map JSON structure rawrr
    public static class RaceData {
        private String race_name;
        private String background_description;
        private PhysicalAttributes physical_attributes;
        private List<TraitData> racial_traits; // Field for racial traits

        public String getRaceName() {
            return race_name;
        }

        public String getBackgroundDescription() {
            return background_description;
        }

        public PhysicalAttributes getPhysicalAttributes() {
            return physical_attributes;
        }

        public List<TraitData> getRacialTraits() {
            return racial_traits; // Getter for racial traits
        }

        // Method to return a list of trait names
        public List<String> getTraitNames() {
            return racial_traits.stream()
                    .map(TraitData::getTraitName)
                    .collect(Collectors.toList());
        }

        public static class PhysicalAttributes {
            private Attribute height;
            private Attribute health;

            public Attribute getHeight() {
                return height;
            }

            public Attribute getHealth() {
                return health;
            }

            public static class Attribute {
                private String text;

                public String getText() {
                    return text;
                }
            }
        }

        // Class for TraitData
        public static class TraitData {
            private String trait_name;
            private String trait_description; // Optional field

            public String getTraitName() {
                return trait_name;
            }

            public String getTraitDescription() {
                return trait_description; // Optional, but can be added
            }

        }
    }
}
