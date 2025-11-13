package com.example.game.repository;

import com.example.game.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
	List<Option> findBySceneId(Long sceneId);
}
