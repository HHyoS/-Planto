package com.ssafy.plant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.plant.domain.*;
import com.ssafy.plant.repository.AchievementRepository;
import com.ssafy.plant.repository.PlantRepository;
import com.ssafy.plant.repository.PotRepository;
import com.ssafy.plant.repository.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MachinePlantService {

    @Autowired
    PlantRepository plantRepository;
    @Autowired
    PotRepository potRepository;
    @Autowired
    AchievementRepository achievementRepository;
    @Autowired
    UserAchievementRepository userAchievementRepository;
    @Autowired
    LikingService likingService;

    public void getPlant(Object payload) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map = objectMapper.readValue(payload.toString(), map.getClass());

        long serialNo = Long.parseLong(map.get("device_number"));
        int touch_status = Integer.parseInt(map.get("touch"));
        int attack = Integer.parseInt(map.get("attack"));

        PotEntity pot = potRepository.findByPotId(serialNo);

        Plant plant = pot.getPlant();
        int touch = plant.getTouch();
        attack += plant.getAttack();
        int liking = plant.getLiking();

        if (touch_status == 1){
            touch += 1;
        } else if(touch_status == 2){
            attack += 1;
        }

        if (touch != 0 && touch % 10 == 0 && liking < 100){
            liking += 1;
            touch += 1;
        } else if (attack != 0 && attack % 20 == 0 && liking > 0){
            attack += 1;
            liking -= 1;
        }

        User user = pot.getUser();

        List<UserAchievementEntity> userAchievementEntity = userAchievementRepository.findByUser(user);

        // 1??? ?????? ?????????
        if (touch >= 3){
            saveAchievement(user, userAchievementEntity, 1);
        }
        // 2??? ???????????? ?????? ????????????.
        if (liking <= 30){
            saveAchievement(user, userAchievementEntity, 2);
            if (liking >= 25) {
                // ????????? ?????????
                likingService.sendLiking(serialNo, 2);
            }
        }
        // 3??? ???????????? ????????? ?????? ???????????????.
        if (attack >= 50){
            saveAchievement(user, userAchievementEntity, 3);
        }
        // 4??? ????????????.... ???????????????
        if (touch >= 100){
            saveAchievement(user, userAchievementEntity, 4);
        }
        // 5??? ???????????? ?????? ????????? ?????????????????????.
        if (attack >= 100){
            saveAchievement(user, userAchievementEntity, 5);
        }

        // 9??? ???????????? ?????????????????????
        if (liking >= 70){
            saveAchievement(user, userAchievementEntity, 9);
            if (liking < 75) {
                // ???????????? ?????????
                likingService.sendLiking(serialNo, 1);
            }
        }

        // ?????? ?????????
        if ((liking > 30 && liking < 35) || (liking < 70 && liking > 65)){
            likingService.sendLiking(serialNo, 0);
        }

        plant.setTemperature((int)Double.parseDouble(map.get("temperature")));
        plant.setHumidity((int)Double.parseDouble(map.get("humidity")));
        plant.setSun((int)Double.parseDouble(map.get("light")));
        plant.setTouch(touch);
        plant.setAttack(attack);
        plant.setLiking(liking);
        plant.setSoilMoisture((int)Double.parseDouble(map.get("soil_moisture")));

        System.out.println(plant);
        plantRepository.save(plant);
    }

    public void saveAchievement(User user, List<UserAchievementEntity> userAchievementEntity, int achievement_id){
        for (UserAchievementEntity userAchievement : userAchievementEntity){
            if (userAchievement.getAchievement().getAchievement_id() == achievement_id){
                return;
            }
        }

        AchievementEntity achievement = achievementRepository.findById(achievement_id).get();

        UserAchievementEntity userAchievement = UserAchievementEntity.builder().user(user).achievement(achievement).build();
        userAchievementRepository.save(userAchievement);

    }
}
