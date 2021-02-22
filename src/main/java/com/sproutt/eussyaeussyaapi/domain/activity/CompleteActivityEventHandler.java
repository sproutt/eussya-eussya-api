package com.sproutt.eussyaeussyaapi.domain.activity;

import com.sproutt.eussyaeussyaapi.domain.grass.Grass;
import com.sproutt.eussyaeussyaapi.domain.grass.GrassRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompleteActivityEventHandler{

    private GrassRepository grassRepository;

    public CompleteActivityEventHandler(GrassRepository grassRepository) {
        this.grassRepository = grassRepository;
    }

    @Async
    @Transactional
    @EventListener
    public void onApplicationEvent(CompleteActivityEvent event) {
        Grass grass = Grass.generate(event.getActivity());
        grassRepository.save(grass);
    }
}
