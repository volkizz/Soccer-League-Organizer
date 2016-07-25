package com.nago.model;

import java.util.ArrayList;
import java.util.List;

public class Team implements Comparable<Team>{
    private String teamName;
    private String coachName;
    private List<Player> teamPlayers;

    public Team(String teamName, String coachName) {
        this.teamName = teamName;
        this.coachName = coachName;
        teamPlayers = new ArrayList<>();
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCoachName() {
        return coachName;
    }

    public List<Player> getTeamPlayers() {
        return teamPlayers;
    }

    @Override
    public int compareTo(Team other) {
        return this.teamName.compareTo(other.teamName);
    }

}
