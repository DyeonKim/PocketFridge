package com.ssafy.andback.api.service;


import com.ssafy.andback.api.dto.response.RecipeResponseDto;

import java.util.List;

/**
 * RecipeService
 * 레시피 서비스 인터페이스
 *
 * @author hoony
 * @version 1.0.0
 * 생성일 2022-05-02
 * 마지막 수정일 2022-05-02
 **/

public interface RecipeService {

    List<RecipeResponseDto> findAllRecipe();

}
