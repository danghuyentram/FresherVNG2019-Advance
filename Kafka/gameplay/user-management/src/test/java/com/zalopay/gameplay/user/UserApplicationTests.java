package com.zalopay.gameplay.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zalopay.gameplay.user.entity.User;
import com.zalopay.gameplay.user.model.GameAnnounce;
import com.zalopay.gameplay.user.service.Producer;
import com.zalopay.gameplay.user.service.UserGameService;
import com.zalopay.gameplay.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserApplicationTests {

	@Autowired
	UserService userService;
	@Autowired
	Producer producer;
	@Autowired
	UserGameService userGameService;

	@Test
	public void saveUser() {
		User user = new User();
		user.setName("Pham Nhat Hao");
		user.setUsername("haopn");
		userService.save(user);
	}

	@Test
	public void saveUserGame() {
		userGameService.saveUserGamePlay("thucnc", "New");
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
