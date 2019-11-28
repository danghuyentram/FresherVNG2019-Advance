package com.zalopay.gameplay.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zalopay.gameplay.user.entity.Game;
import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.entity.UserGame;
import com.zalopay.gameplay.user.model.GameAnnounce;
import com.zalopay.gameplay.user.service.GameService;
import com.zalopay.gameplay.user.service.Producer;
import com.zalopay.gameplay.user.service.UserGameService;
import com.zalopay.gameplay.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserApplicationTests {

	@Autowired
	UserService userService;
	@Autowired
	GameService gameService;
	@Autowired
	UserGameService userGameService;
	@Autowired
	Producer producer;

	@Test
	public void saveUser() {
		User user = new User();
		user.setName("Pham Nhat Hao");
		user.setUsername("haopn");
		userService.save(user);
	}

	@Test
	public void saveGame() {
		Game game = new Game();
		game.setName("123Play");
		gameService.save(game);
	}

	@Test
	public void saveUserGame() {
		userGameService.saveUserGamePlay("nhanbv", "123Play", 0);
	}

	@Test
	@Transactional
	public void findUserGame() {
		User user = userService.findUserByUsername("haopn");
		Game game = gameService.findGameByName("abc");
		List<UserGame> games = userGameService.findByUserAndGame(user, game);
		System.out.println(games.size());
	}

	@Test
	public void pushAnnounce() throws JsonProcessingException {
		GameAnnounce gameAnnounce = new GameAnnounce();
		gameAnnounce.setResult(0);
		gameAnnounce.setBotStep(1);
		gameAnnounce.setUserStep(1);
		gameAnnounce.setUserName("xyz");
		producer.sendResultToAnnounce(gameAnnounce);
	}

}
