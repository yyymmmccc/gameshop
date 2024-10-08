package com.project.game.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.game.dto.response.ResponseDto;
import com.project.game.entity.*;
import com.project.game.global.code.ResponseCode;
import com.project.game.global.handler.CustomException;
import com.project.game.repository.*;
import com.project.game.service.SteamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamServiceImpl implements SteamService {

    private final GameCategoryRepository gameCategoryRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameCategoryMappingRepository gameCategoryMappingRepository;
    private final GameImageRepository gameImageRepository;
    private final GameSpecificationsRepository gameSpecificationsRepository;

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
                System.out.println(data);

                if(data == null)
                    return ResponseDto.success("게임등록에 실패하였습니다.");

                String gameName = (String) data.get("name");
                Boolean gameCheck = gameRepository.existsByGameName(gameName);
                if(gameCheck)
                    return ResponseDto.success("이미 등록된 게임입니다.");

                // 게임 설명
                String gameDc = (String) data.get("about_the_game");

                // 게임 배급사
                List<String> list = (List<String>) data.get("publishers");

                List<String> gameThumbnailList = new ArrayList<>();
                // 게임 썸네일 이미지
                String gameImage = (String) data.get("header_image");
                gameThumbnailList.add(gameImage);

                // 게임 썸네일 동영상
                List<Object> movieList = (List<Object>)data.get("movies");
                if(!movieList.isEmpty()){
                    Map<String, Object> movie = (Map<String, Object>) movieList.get(0);
                    Map<String, Object> webm = (Map<String, Object>)movie.get("webm");
                    String maxMovie = (String)webm.get("max");
                    System.out.println(maxMovie);
                    gameThumbnailList.add(maxMovie);
                }

                // 게임 출시일
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

                List<Integer> categoryIdList = new ArrayList<>();

                for(Map<String, Object> category : genres){
                    String categoryName = (String)category.get("description");

                    switch (categoryName) {
                        case String name when name.contains("무료"):
                            categoryId = 1;
                            break;
                        case String name when name.contains("액"):
                            categoryId = 2;
                            break;
                        case String name when name.contains("어드"):
                            categoryId = 3;
                            break;
                        case String name when name.contains("RPG"):
                            categoryId = 4;
                            break;
                        case String name when name.contains("MMO"):
                            categoryId = 5;
                            break;
                        case String name when name.contains("레이싱"):
                            categoryId = 6;
                            break;
                        case String name when name.contains("스포츠"):
                            categoryId = 7;
                            break;
                        case String name when name.contains("전략"):
                            categoryId = 8;
                            break;
                        case String name when name.contains("시뮬레이션"):
                            categoryId = 9;
                            break;
                        case String name when name.contains("인디"):
                            categoryId = 10;
                            break;
                        case String name when name.contains("캐주얼"):
                            categoryId = 11;
                            break;
                        default:
                            log.info("category_not_found: " + categoryName);
                            continue;
                    }
                    categoryIdList.add(categoryId);
                }

                List<GameCategoryEntity> gameCategoryEntityList = gameCategoryRepository.findByCategoryIdIn(categoryIdList);

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

                Map<String, Object> platforms = (Map<String, Object>)data.get("platforms");

                String winMinSpecifications = null;
                String winMaxSpecifications = null;
                String macMinSpecifications = null;
                String macMaxSpecifications = null;
                String linuxMinSpecifications = null;
                String linuxMaxSpecifications = null;

                if((boolean) platforms.get("windows")){

                    Map<String, Object> specifications = (Map<String, Object>)data.get("pc_requirements");
                    winMinSpecifications = (String)specifications.get("minimum");
                    winMaxSpecifications = (String)specifications.get("recommended");
                }

                if((boolean) platforms.get("mac")){

                    Map<String, Object> specifications = (Map<String, Object>)data.get("mac_requirements");
                    macMinSpecifications = (String)specifications.get("minimum");
                    macMaxSpecifications = (String)specifications.get("recommended");
                }

                if((boolean) platforms.get("linux")){

                    Map<String, Object> specifications = (Map<String, Object>)data.get("linux_requirements");
                    linuxMinSpecifications = (String)specifications.get("minimum");
                    linuxMaxSpecifications = (String)specifications.get("recommended");
                }

                GameEntity gameEntity = GameEntity.builder()
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

                // 게임 사양 저장하기
                GameSpecificationsEntity gameSpecificationsEntity =
                        GameSpecificationsEntity.builder()
                                .gameEntity(game)
                                .windowsMinSpecifications(winMinSpecifications)
                                .windowsMaxSpecifications(winMaxSpecifications)
                                .macMinSpecifications(macMinSpecifications)
                                .macMaxSpecifications(macMaxSpecifications)
                                .linuxMinSpecifications(linuxMinSpecifications)
                                .linuxMaxSpecifications(linuxMaxSpecifications)
                                .build();

                gameSpecificationsRepository.save(gameSpecificationsEntity);

                // 게임 : 게임카테고리 매핑 테이블 저장하기
                List<GameCategoryMappingEntity> gameCategoryMappingEntityList = new ArrayList<>();
                for(GameCategoryEntity gameCategoryEntity : gameCategoryEntityList){
                    GameCategoryMappingEntity gameCategoryMappingEntity =
                            GameCategoryMappingEntity.builder()
                                    .gameEntity(game)
                                    .gameCategoryEntity(gameCategoryEntity)
                                    .build();
                    gameCategoryMappingEntityList.add(gameCategoryMappingEntity);
                }
                gameCategoryMappingRepository.saveAll(gameCategoryMappingEntityList);

                // 게임이미지 repository 에 저장하기
                List<GameImageEntity> gameImageEntityList = new ArrayList<>();
                for (int i = 0; i < gameThumbnailList.size(); i++) {
                    String url = gameThumbnailList.get(i);
                    String thumbnailFlag = (i == 0) ? "Y" : "N";

                    GameImageEntity gameImageEntity =
                            GameImageEntity.builder()
                                    .gameEntity(game)
                                    .gameImageUrl(url)
                                    .thumbnail(thumbnailFlag)
                                    .build();

                    gameImageEntityList.add(gameImageEntity);
                }

                gameImageRepository.saveAll(gameImageEntityList);
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
