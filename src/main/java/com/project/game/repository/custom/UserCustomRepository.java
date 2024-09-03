package com.project.game.repository.custom;

import com.project.game.dto.response.member.admin.AdminUserListResponseDto;
import com.project.game.dto.response.member.user.RecentViewListResponseDto;
import com.project.game.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCustomRepository {


    List<RecentViewListResponseDto> findMyPageRecentProductViewList(List<Integer> recentViewList);

    Page<UserEntity> findUserAll(Pageable pageable, String searchType, String searchKeyword);
}
