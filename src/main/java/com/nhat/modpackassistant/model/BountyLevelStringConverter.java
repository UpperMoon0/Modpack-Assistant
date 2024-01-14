package com.nhat.modpackassistant.model;

import javafx.util.StringConverter;

import java.util.Set;
import java.util.stream.Collectors;

public class BountyLevelStringConverter extends StringConverter<Set<Integer>> {
    public BountyLevelStringConverter() {
    }
    @Override
    public String toString(Set<Integer> integers) {
        if (integers == null) {
            return "";
        }
        return integers.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }

    @Override
    public Set<Integer> fromString(String s) {
        if (!Bounties.isValidBountyLevels(s)) {
            return null;
        }

        return Bounties.parseBountyLevels(s);
    }
}
