package es.marcosaguirre.videogame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.marcosaguirre.videogame.model.VideoGame;

public interface IVideoGameRepo extends JpaRepository<VideoGame, Long>{

}
