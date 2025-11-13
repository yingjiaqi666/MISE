package com.example.game.service;

import com.example.game.entity.Option;
import com.example.game.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    public Option save(Option option) {
        return optionRepository.save(option);
    }

    public java.util.List<Option> findBySceneId(Long sceneId) {
        return optionRepository.findBySceneId(sceneId);
    }

    public Option findById(Long id) {
        return optionRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        optionRepository.deleteById(id);
    }

    public Option updateOption(Long id, Option optionDetails) {
        Option option = findById(id);
        if (option != null) {
            option.setText(optionDetails.getText());
            option.setType(optionDetails.getType());
            option.setDc(optionDetails.getDc());
            option.setSuccess(optionDetails.getSuccess());
            option.setFail(optionDetails.getFail());
            return optionRepository.save(option);
        }
        return null;
    }
}
