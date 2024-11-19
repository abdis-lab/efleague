package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.repositories.SeasonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;


    public SeasonService(SeasonRepository seasonRepository){
        this.seasonRepository = seasonRepository;
    }



    public Season saveSeason(Season season){
        return seasonRepository.save(season);
    }


    public Optional<Season> findSeasonById(Long id){
        return seasonRepository.findById(id);
    }


}
