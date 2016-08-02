package com.nago.Prompt;

import com.nago.logic.Organizer;
import com.nago.model.Player;
import com.nago.model.Team;

import java.util.*;

public class Prompter {
    private Scanner scanner = new Scanner(System.in);
    private Organizer organizer;
    private List<Player> players = new ArrayList<>();
    private Map<Integer, List<String>> playersByHeight;
    private int maxTeams;
    private boolean exit;

    public void start() {
        organizer = new Organizer();
        players.addAll(Arrays.asList(organizer.loadPlayers()));
        maxTeams = players.size() / 11;
        exit = false;
        promptWelcome();
        do {
            promptMainMenu();
        } while (!exit);
    }

    private void message(String message, Object... objects) {
        System.out.printf(message, objects);
    }

    private void promptWelcome() {
        message("Welcome to \033[1mSoccer League Organizer\033[0m!!!");
    }

    private void promptMainMenu() {
        message("%n0----||===============================>%nTo chose enter a NUMBER of an option.%n1. Show all AVAILABLE players %n2. Show all TEAMS%n3. Create a TEAM.%n4. Add a PLAYER to a TEAM%n5. Remove PLAYER from the TEAM%n6. Show HEIGHT ratio of AVAILABLE players%n7. Show HEIGHT report for a TEAM%n8. Experience Report%n9. Print ROSTER for COACH%n10. Exit%n0----||===============================>%n");
        checkForInt();
        switch (scanner.nextInt()) {
            case 1:
                promptHowManyAvailablePlayers();
                break;
            case 2:
                promptTeamList();
                break;
            case 3:
                promptToCreateTeam();
                break;
            case 4:
                promptToAddPlayerToATeam();
                break;
            case 5:
                promptToRemovePlayerFromTheTeam();
                break;
            case 6:
                promptAvailablePlayersByHeight();
                break;
            case 7:
                promptTeamPlayersByHeight();
                break;
            case 8:
                promptExpReport();
                break;
            case 9:
                promptToPrintRosterForCoach();
                break;
            case 10:
                exit();
                break;
            default:
                message("There is NO such an option");
                break;
        }
    }

    private void exit() {
        message("Soccer League Organizer made by Mykola Nagorskyi.\nExiting...\nHave a good day!!!");
        exit = true;
    }

    private void promptTeamList() {
        if (organizer.getTeamList().isEmpty()) {
            message("There is NO teams.");
        } else {
            message("Team's List:");
            int number = 0;
            for (Team team : organizer.getTeamList()) {
                number++;
                message("%n%d. \033[1m%s\033[0m with coach \033[1m%s\033[0m", number, team.getTeamName(), team.getCoachName());
            }
        }
    }

    private void promptToAddPlayerToATeam() {
        if (!organizer.getTeamList().isEmpty()) {
            scanner.nextLine();
            message("Add Player to A Team%nEnter a team name:");
            String teamName = teamMatchingInput();
            addPlayer(teamName);
        } else {
            message("There is no teams.");
        }
    }

    private void addPlayer(String teamName) {
        if (organizer.getTeamFromTeamList(teamName).getTeamPlayers().size() != 11) {
            promptHowManyAvailablePlayers();
            message("Enter an index of a Player you want to add to the %s", teamName);
            checkForInt();
            int playerIndex = scanner.nextInt();
            message("Player %s %s was added to the %s team successfully! ",
                    players.get(playerIndex).getFirstName(),
                    players.get(playerIndex).getLastName(),
                    teamName);
            organizer.addPlayerToTheTeam(teamName, players, playerIndex);
        } else {
            message("Team limit of 11 players is reached.%nPress ENTER to continue");
        }
    }

    private void promptToRemovePlayerFromTheTeam() {
        if (!organizer.getTeamList().isEmpty()) {
            scanner.nextLine();
            message("Remove Player from the Team%nEnter a team name:");
            String teamName = teamMatchingInput();
            checkForPlayersToDelete(teamName);
        } else {
            message("There is no teams.");
        }
    }

    private void checkForPlayersToDelete(String teamName) {
        if (promptHowManyPlayersInTheTeam(teamName, true) != -1) {
            message("Enter an index of a Player you want to remove from the %s", teamName);
            checkForInt();
            int playerIndex = scanner.nextInt();
            message("Player %s %s was DELETED from the %s team successfully! ",
                    players.get(playerIndex).getFirstName(),
                    players.get(playerIndex).getLastName(),
                    teamName);
            deletePlayer(teamName, playerIndex);
        } else {
            message("There is no players in %s", teamName);
        }
    }

    private void deletePlayer(String teamName, int playerIndex) {
        if (organizer.getTeamFromTeamList(teamName).getTeamPlayers().size() > playerIndex && playerIndex >= 0) {
            organizer.removePlayerFromTheTeam(teamName, players, playerIndex);
        } else {
            message("There is no player with an index %d. Try again", playerIndex);
        }
    }

    private String teamMatchingInput() {
        String input;
        boolean matching = false;
        do {
            input = scanner.nextLine();
            for (Team team : organizer.getTeamList()) {
                if (team.getTeamName().equalsIgnoreCase(input)) {
                    matching = true;
                    break;
                }
            }
            if (!matching) {
                message("No such a team. Try again: ");
            }
        } while (!matching);
        return input;
    }

    private void promptToCreateTeam() {
        if (organizer.getTeamList().size() < maxTeams) {
            scanner.nextLine();
            message("Enter team name: ");
            String teamName = notEmptyInput();
            message("Enter coach name for %s: ", teamName);
            String coachName = notEmptyInput();
            organizer.addTeam(teamName, coachName);
            message("The team \033[1m%s\033[0m with coach \033[1m%s\033[0m was added \033[1msuccessfully!\033[0m%nPress ENTER to coninue...", teamName, coachName);
        } else {
            message("Each team must have 11 players. Team limit %d is reached", maxTeams);
        }
    }

    private String notEmptyInput() {
        String input;
        do {
            input = scanner.nextLine();
            if (input.isEmpty() || input.matches("\\s+")) {
                message("Can't be empty. Try again:");
            }
        }
        while (input.isEmpty() || input.matches("\\s+"));
        return input;
    }

    private void promptHowManyAvailablePlayers() {
        message("There are currently %d registered players.%n", players.size());
        Collections.sort(players);
        int playerIndex = 0;
        for (Player player : players) {
            message("%d. %s", playerIndex, player.toString());
            playerIndex++;
        }
    }

    private boolean isTeamHasPlayers(String team) {
        return !organizer.getTeamFromTeamList(team).getTeamPlayers().isEmpty();
    }

    private Integer promptHowManyPlayersInTheTeam(String team, boolean showList) {
        int playerIndex = 0;
        if (isTeamHasPlayers(team) && showList) {
            for (Player player : organizer.getTeamFromTeamList(team).getTeamPlayers()) {
                message("%d. %s", playerIndex, player.toString());
                playerIndex++;
            }
            return 1;
        } else if (isTeamHasPlayers(team) && !showList) {
            return 1;
        }
        return -1;
    }

    private void promptAvailablePlayersByHeight() {
        playersByHeight = organizer.populatePlayersByHeightMap(players);
        message("Report By Height of Available Players (NOT on the TEAM):\n" + formattedString(playersByHeight.toString()));
    }

    private void promptTeamPlayersByHeight() {
        if (!organizer.getTeamList().isEmpty()) {
            scanner.nextLine();
            message("Enter Team Name: ");
            String teamName = teamMatchingInput();
            populateAndShowMapForHeight(teamName);
        } else {
            message("There is no teams.");
        }
    }

    private void populateAndShowMapForHeight(String teamName) {
        if (promptHowManyPlayersInTheTeam(teamName, false) != -1) {
            playersByHeight = organizer.populatePlayersByHeightMap(organizer.getTeamFromTeamList(teamName).getTeamPlayers());
            compareToOtherTeams(playersByHeight, teamName);
        } else {
            message("There is no players in %s", teamName);
        }
    }

    private void compareToOtherTeams(Map<Integer, List<String>> playersByHeight, String teamName) {
        Map<String, Map<Integer, Integer>> otherTeam = organizer.otherTeamsWithSamePlayersHeight(playersByHeight, teamName);
        message("Report By Height of a %s Players:%n %s%n", teamName, formattedString(playersByHeight.toString()));
        for (Map.Entry<String, Map<Integer, Integer>> entry : otherTeam.entrySet()) {
            String otherTeamName = entry.getKey();
            message("Team %s has:%n", otherTeamName);
            for (Map.Entry<Integer, Integer> subEntry : otherTeam.get(otherTeamName).entrySet()) {
                message("%d players with height %d%n", subEntry.getValue(), subEntry.getKey());
            }
        }
    }

    private void promptExpReport() {
        message("Experience Report: %n %s", formattedString(organizer.experienceRatio().toString()));
    }

    private void promptToPrintRosterForCoach() {
        message("Enter Coach name: ");
        String coachName = scanner.next();
        organizer.getTeamList().stream().filter(team -> team.getCoachName().equalsIgnoreCase(coachName)).forEach(team -> {
            Collections.sort(team.getTeamPlayers());
            message("List of players for %s with coach %s:%n %s", team.getTeamName(), coachName, formattedString(team.getTeamPlayers().toString()));
        });
    }

    private String formattedString(String stringToFormat) {
        return stringToFormat.replaceAll("[\\[\\],{}]", "")
                .replace("=", ":\n ")
                .trim();
    }

    private void checkForInt() {
        while (!scanner.hasNextInt()) {
            message("That is NOT a number, please try again:");
            scanner.next();
        }
    }
}
