package com.nhat.modpackassistant.model;

import javafx.collections.ObservableList;

public class BountyList {
    //bounty list singleton
    private static BountyList instance;
    private final ObservableList<Bounty> bounties;

    private BountyList() {
        bounties = null;
    }

    public static BountyList getInstance() {
        if (instance == null) {
            instance = new BountyList();
        }
        return instance;
    }

    public void addBounty(Bounty bounty) {
        if (bounties == null) {
            System.out.println("bounties is null");
            return;
        }

        System.out.println("bounties is not null" + bounty.toString());
        bounties.add(bounty);
    }

    public void removeBounty(Bounty bounty) {
        if (bounties == null) {
            return;
        }

        bounties.remove(bounty);
    }

    public ObservableList<Bounty> getBounties() {
        return bounties;
    }
}
