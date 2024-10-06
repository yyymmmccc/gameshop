package com.project.game.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.game.dto.response.ResponseDto;
import com.project.game.entity.GameCategoryEntity;
import com.project.game.entity.GameEntity;
import com.project.game.entity.GameImageEntity;
import com.project.game.entity.UserEntity;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.GameCategoryRepository;
import com.project.game.repository.GameImageRepository;
import com.project.game.repository.GameRepository;
import com.project.game.repository.UserRepository;
import com.project.game.service.SteamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamServiceImpl implements SteamService {

    private final GameCategoryRepository gameCategoryRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameImageRepository gameImageRepository;

    @Override
    public ResponseEntity<?> regSteamGame(long gameId) {

        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        UserEntity userEntity = userRepository.findByEmail("admin").orElseThrow(()
                -> new CustomException(ResponseCode.USER_NOT_FOUND));

        final String apiUrl = "https://store.steampowered.com/api/appdetails?appids=" + gameId + "&l=korean";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String jsonString = response.getBody();
                Map<String, Object> jsonMap = objectMapper.readValue(jsonString, new TypeReference<>() {});

                Map<String, Object> gameData = (Map<String, Object>) jsonMap.get(String.valueOf(gameId));
                Map<String, Object> data = (Map<String, Object>) gameData.get("data");

                String gameName = (String) data.get("name");
                Boolean gameCheck = gameRepository.existsByGameName(gameName);
                if(gameCheck){
                    return ResponseDto.success("이미 존재하는 게임입니다.");
                }

                String gameDc = (String) data.get("about_the_game");
                String gameImage = (String) data.get("header_image");
                List<String> list = (List<String>) data.get("publishers");

                Map<String, Object> release_date = (Map<String, Object>) data.get("release_date");
                String date = (String) release_date.get("date");
                date = date.replace("년", "-").replace("월", "-").replace("일", "");
                // 공백 제거 후 최종 날짜 반환
                date = date.replaceAll(" ", "").trim();
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                String formattedDate = String.format("%d-%02d-%02d", year, month, day);

                int categoryId;
                List<Map<String, Object>> genres = (List<Map<String, Object>>) data.get("genres");
                String categoryName = (String)genres.get(0).get("description");
                if(categoryName.contains("무")) categoryId = 1;
                else if(categoryName.contains("액")) categoryId = 2;
                else if(categoryName.contains("어드")) categoryId = 3;
                else if(categoryName.contains("RPG")) categoryId = 4;
                else if(categoryName.contains("MMO")) categoryId = 5;
                else if(categoryName.contains("레이싱")) categoryId = 6;
                else if(categoryName.contains("스포츠")) categoryId = 7;
                else if(categoryName.contains("전략")) categoryId = 8;
                else if(categoryName.contains("시뮬레이션")) categoryId = 9;
                else if(categoryName.contains("인디")) categoryId = 10;
                else if(categoryName.contains("캐주얼")) categoryId = 11;
                else{
                    log.info("category_not_found" + categoryName);
                    return null;
                }

                GameCategoryEntity gameCategoryEntity = gameCategoryRepository.findById(categoryId).orElseThrow(()
                        -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));

                int originalAmount = 0;
                int finalAmount = 0;
                int discountPercentage = 0;

                Boolean freeCheck = (boolean)data.get("is_free");
                if(freeCheck) log.info("free" + true);

                else{
                    Map<String, Object> price_overview = (Map<String, Object>) data.get("price_overview");
                    String finalPrice = (String)price_overview.get("final_formatted");
                    String originalPrice = (String)price_overview.get("initial_formatted");

                    if(originalPrice == null || originalPrice.isEmpty()) {
                        originalPrice = finalPrice;
                    }
                    String discount = String.valueOf(price_overview.get("discount_percent"));

                    originalPrice = originalPrice.replace("₩", "");
                    finalPrice = finalPrice.replace("₩", "");

                    originalPrice = originalPrice.replace(",", "").trim();
                    finalPrice = finalPrice.replace(",", "").trim();

                    originalAmount = Integer.parseInt(originalPrice);
                    finalAmount = Integer.parseInt(finalPrice);
                    discountPercentage = Integer.parseInt(discount);
                }

                GameEntity gameEntity = GameEntity.builder()
                        .gameCategoryEntity(gameCategoryEntity)
                        .gameName(gameName)
                        .gameDc(gameDc)
                        .publisher(list.get(0).trim())
                        .originalPrice(originalAmount)
                        .discountPrice(finalAmount)
                        .discountPercentage(discountPercentage)
                        .reviewCount(0)
                        .purchaseCount(0)
                        .releaseDate(formattedDate)
                        .userEntity(userEntity)
                        .build();

                GameEntity game = gameRepository.save(gameEntity);

                GameImageEntity gameImageEntity = GameImageEntity.builder()
                        .gameEntity(game)
                        .gameImageUrl(gameImage)
                        .thumbnail("Y")
                        .build();

                gameImageRepository.save(gameImageEntity);
            }

            else {
                log.info("게임 응답에 실패하였습니다.");
                return (ResponseEntity<?>) ResponseEntity.badRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDto.success("게임이 등록되었습니다.");
    }
}
