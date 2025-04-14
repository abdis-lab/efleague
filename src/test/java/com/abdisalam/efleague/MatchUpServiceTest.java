package com.abdisalam.efleague;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.MatchUpRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.services.MatchUpService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.discovery.SelectorResolver;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MatchUpServiceTest {

//    private MatchUpService matchUpService;
//    private TeamRepository teamRepository;
//    private MatchUpRepository matchUpRepository;
//
//
//
//    @BeforeEach
//    void setUp() {
//
//        //Mock Repositories
//        matchUpRepository = Mockito.mock(MatchUpRepository.class);
//        teamRepository = Mockito.mock(TeamRepository.class);
//
//
//        // Initialize service with mocked repositories
//        matchUpService = new MatchUpService(matchUpRepository, teamRepository);
//    }
//
//
//    @Test
//    void testGenerateRoundRobinSchedule(){
//        //Create mock teams (less than 12)
//        List<Team> teams = new ArrayList<>();
//        for(int i = 0; i < 4; i++){//Create 4 Teams
//            Team team = new Team();
//            team.setId((long) i);
//            team.setName("Team " + i);
//            teams.add(team);
//        }
//
//
//        //Assert that teams are properly initialized
//        assertEquals(4, teams.size());
//
//        //Mock the findAll method of the team repository
//        when(teamRepository.findAll()).thenReturn(teams);
//
//
//        //create a mock season
//        Season season = new Season();
//        season.setId(1L);
//        season.setName("Season 1");
//        season.isActive();
//
//
//        //Mock the matchUpRepository saveAll to just return the same list of matchups
//        //List<Matchup> savedMatchups = new ArrayList<>();
//        when(matchUpRepository.saveAll(Mockito.anyList())).thenAnswer(invocation -> {
//            return (List<Matchup>) invocation.getArguments()[0];
//        });
//
//
//        // Call the method to generate matchups
//        List<Matchup> matchups = matchUpService.generateRoundRobinSchedule(LocalDateTime.now(), false, season);
//
//        //Validate the matchups are generated correctly
//        assertNotNull(matchups);
//        assertEquals(6, matchups.size());// For 4 teams, round-robin creates 6 matchups
//
//
//        //Ensure matchups are scheduled on Saturdays
//        for(Matchup matchup : matchups){
//            assertEquals(6, matchup.getGameDateTime().getDayOfWeek().getValue());
//        }
//
//    }
//
//
//
//    @Test
//    void testMaxTeamsLimit(){
//        //Create mock teams (more than 12 to trigger exception)
//        List<Team> teams = new ArrayList<>();
//        for(int i = 0; i < 13; i++){
//            Team team = new Team();
//            team.setId((long) i);
//            team.setName("Team " + i);
//            teams.add(team);
//        }
//
//
//        //Mock the findAll method of the team Repository
//        when(teamRepository.findAll()).thenReturn(teams);
//
//
//        //Create a mock season
//        Season season = new Season();
//        season.setId(1L);
//        season.setName("Season 1");
//        season.setActive(true);
//
//
//        IllegalStateException exception  = assertThrows(IllegalStateException.class, () -> {
//            matchUpService.generateRoundRobinSchedule(LocalDateTime.now(), false, season);
//        });
//
//
//        //validate the error message
//        assertEquals("Maximum number of teams allowed is 12.", exception.getMessage());
//    }
//
//
//
//    @Test
//    void TestGamesPerTeamLimit(){
//        //Create Mock teams (4 teams)
//        List<Team> teams = new ArrayList<>();
//        for(int i = 0; i < 4; i++){
//            Team team = new Team();
//            team.setId((long) i);
//            team.setName("Team " + i);
//            teams.add(team);
//        }
//
//
//        //Mock the findAll method of the team repository
//        when(teamRepository.findAll()).thenReturn(teams);
//
//
//        //Creat a Mock Season
//        Season season = new Season();
//        season.setId(1L);
//        season.setName("Season 2");
//        season.setActive(false);
//
//
//
//        //Mock the matchUpRepository saveAll to just return the same list of matchups
//        List<Matchup> savedMatchups = new ArrayList<>();//Define the matchups list to be returned
//        when(matchUpRepository.saveAll(Mockito.anyList())).thenReturn(savedMatchups);
//
//
//
//        //Call the method generate matchups
//        List<Matchup> matchups = matchUpService.generateRoundRobinSchedule(LocalDateTime.now(), false, season);
//
//
//
//        //Validate that no team has more than 8 games (in this case each team should have 3 matchups since there are 4 teams)
//        int[] gamesPlayed = new int[teams.size()];
//        for(Matchup matchup : matchups){
//            //Convert Long to int safely
//            int homeTeamIndex = Math.toIntExact(matchup.getHomeTeam().getId());
//            int awayTeamIndex = Math.toIntExact(matchup.getAwayTeam().getId());
//
//
//            gamesPlayed[homeTeamIndex]++;
//            gamesPlayed[awayTeamIndex]++;
//        }
//
//
//        //Ensure no team exceeds 8 games
//        for(int games : gamesPlayed){
//            assertTrue(games <= 8);
//        }
//
//
//
//    }
//


}
