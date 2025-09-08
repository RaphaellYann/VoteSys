package com.senac.votesys.services;

import com.senac.votesys.model.Campanhas;
import com.senac.votesys.repository.CampanhasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampanhasService {

    @Autowired
    private CampanhasRepository campanhasRepository;


}
