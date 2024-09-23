package com.project.game.controller.swagger;

import com.project.game.dto.request.member.admin.AdminPatchUserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 - 유저관리", description = "관리자 유저 조회, 권한 수정, 삭제 API")
public interface SwaggerAdminUserApi {

    @Operation(summary = "유저 목록 조회", description = "페이지 및 검색 조건을 이용하여 유저 목록을 조회하는 API")
    @ApiResponse(responseCode = "200", description = "유저 목록 조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @GetMapping("/list")
    ResponseEntity<?> getUserList(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "") String searchType,
                                  @RequestParam(defaultValue = "") String searchKeyword);

    @Operation(summary = "유저 정보 수정", description = "유저의 정보를 수정하는 API")
    @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    @PatchMapping("")
    ResponseEntity<?> patchUser(@RequestBody @Valid AdminPatchUserRequestDto dto);

    @Operation(summary = "유저 삭제", description = "유저의 이메일로 계정을 삭제하는 API")
    @ApiResponse(responseCode = "200", description = "유저 삭제 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    @DeleteMapping("")
    ResponseEntity<?> deleteUser(@RequestParam("email") String userEmail);
}
