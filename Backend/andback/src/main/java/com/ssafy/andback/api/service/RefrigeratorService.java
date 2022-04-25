package com.ssafy.andback.api.service;

import com.ssafy.andback.api.dto.request.InsertRefrigeratorRequestDto;
import com.ssafy.andback.api.dto.response.RefrigeratorResponseDto;

import java.util.List;

/**
 * RefrigeratorService
 * 냉장고 서비스 인터페이스
 *
 * @author hoony
 * @version 1.0.0
 * 생성일 2022-04-25
 * 마지막 수정일 2022-04-25
 **/

public interface RefrigeratorService {

    public String insertRefrigerator(InsertRefrigeratorRequestDto reqDto);

    public List<RefrigeratorResponseDto> findAllRefrigeratorByUser(String userEmail);

}