package com.abdisalam.efleague;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
class EfleagueApplicationTests {

    @Test
    void contextLoads() {
    }

//    @InjectMocks
//    private TeamService teamService;
//
//    @Mock
//    private TeamRepository teamRepository;
//
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSaveTeam(){
//        Team team = new Team();
//        team.setName("Eagles");
//
//        when(teamRepository.save(team)).thenReturn(team);
//
//        Team savedTeam = teamService.saveTeam(team);
//
//        assertNotNull(savedTeam);
//        assertEquals("Eagles", savedTeam.getName());
//        verify(teamRepository, times(1)).save(team);
//    }

}
