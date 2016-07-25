package com.nago.logic;

import com.nago.model.Player;
import com.nago.model.Players;
import com.nago.model.Team;

import java.util.*;

/**
 * Created by Mykola
 * 7/19/2016.
 */
public class Organizer {

    private List<Team> teamList = new ArrayList<>();

    public void addTeam(String teamName, String coachName){
        teamList.add(new Team(teamName, coachName));
    }

    public Player[] loadPlayers(){
        return Players.load();
    }

    public List<Team> getTeamList() {
        Collections.sort(teamList);
        return teamList;
    }

    public Team getTeamFromTeamList(String teamName) throws NullPointerException{
        Team result = null;
        for (Team team : teamList){
            if(team.getTeamName().equals(teamName)){
                result = team;
                return result;
            }
        }
        return result;
    }

    public void addPlayerToTheTeam(String teamName, List<Player> playerList, int player){
        if(!playerList.isEmpty() && playerList.size()>player){
            getTeamFromTeamList(teamName).getTeamPlayers().add(playerList.get(player));
            playerList.remove(player);
        }
    }

    public void removePlayerFromTheTeam(String teamName, List<Player> playerList, int player){
        if(!playerList.isEmpty() && playerList.size()>player){
            playerList.add(getTeamFromTeamList(teamName).getTeamPlayers().get(player));
            getTeamFromTeamList(teamName).getTeamPlayers().remove(player);
        }
    }

    public TreeMap<Integer, List<String>> populatePlayersByHeightMap(List<Player> playerList){
        TreeMap<Integer, List<String>> result = new TreeMap<>();
        for (Player player : playerList){
            List<String> namesList = result.get(player.getHeightInInches());
            if(namesList == null){
                namesList = new ArrayList<>();
                result.put(player.getHeightInInches(), namesList);
            }
            namesList.add(player.getFirstName()+ " " + player.getLastName()+",\n");
        }
        return result;
    }

    public TreeMap<String, String> experienceRatio(){
        TreeMap<String, String> result = new TreeMap<>();
        for (Team team : teamList){
            int expCount = 0;
            int notExpCount = 0;
                    for(Player player : team.getTeamPlayers()){
                        if(player.isPreviousExperience()){
                            expCount++;
                        }else{
                            notExpCount++;
                        }
                    }
            String expRatio = String.format("Experienced - %d, NOT Experienced - %d%n", expCount, notExpCount);
           result.put(team.getTeamName(), expRatio);
        }
        return result;
    }

}
