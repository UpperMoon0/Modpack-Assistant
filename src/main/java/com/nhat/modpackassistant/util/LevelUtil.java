package com.nhat.modpackassistant.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhat.modpackassistant.model.MaxLevel;
import com.nhat.modpackassistant.model.Project;

import java.io.IOException;
import java.nio.file.Paths;

public class LevelUtil {
    private static LevelUtil instance;
    private final ObjectMapper objectMapper;

    private LevelUtil() {
        objectMapper = new ObjectMapper();
    }

    public static LevelUtil getInstance() {
        if (instance == null) {
            instance = new LevelUtil();
        }
        return instance;
    }

    public void saveLevels() throws IOException {
        objectMapper.writeValue(Paths.get(Project.getInstance().getPath(), "levels.json").toFile(), MaxLevel.getInstance().getLevel());
    }

    public void loadLevels() throws IOException {
        MaxLevel.getInstance().setLevel(objectMapper.readValue(Paths.get(Project.getInstance().getPath(), "levels.json").toFile(), Integer.class));
    }

    public boolean levelValid(String level) {
        return StringUtil.isInteger(level) && Integer.parseInt(level) > 0;
    }
}