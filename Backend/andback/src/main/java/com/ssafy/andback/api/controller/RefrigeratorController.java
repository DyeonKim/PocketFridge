package com.ssafy.andback.api.controller;

import com.ssafy.andback.api.constant.ErrorCode;
import com.ssafy.andback.api.dto.request.InsertRefrigeratorRequestDto;
import com.ssafy.andback.api.dto.request.InsertShareMemberRequestDto;
import com.ssafy.andback.api.dto.response.*;
import com.ssafy.andback.api.exception.CustomException;
import com.ssafy.andback.api.service.RefrigeratorService;
import com.ssafy.andback.core.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


import java.util.List;

/**
 * RefrigeratorController
 * 냉장공 API 컨트롤러
 *
 * @author hoony
 * @version 1.0.0
 * 생성일 2022-04-25
 * 마지막 수정일 2022-04-25
 **/

@Api(tags = {"03. 냉장고 API"})
@RestController()
@RequestMapping(value = "/refrigerator")
@RequiredArgsConstructor
public class RefrigeratorController {

    private final RefrigeratorService refrigeratorService;

    @ApiOperation(value = "냉장고 조회", notes = "사용자의 JWT 토큰값을 받아 냉장고 리스트를 보여준다")
    @GetMapping
    public ResponseEntity<ListResponseDto<RefrigeratorResponseDto>> findRefrigeratorList(@ApiIgnore Authentication authentication) {

        if (authentication == null) {
            throw new CustomException(ErrorCode.NOT_AUTH_TOKEN);
        }

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }


        List<RefrigeratorResponseDto> response = refrigeratorService.findAllRefrigeratorByUser(user);

        return ResponseEntity.ok(new ListResponseDto<RefrigeratorResponseDto>(200, "success", response));
    }

    @ApiOperation(value = "냉장고 생성", notes = "사용자의 새로운 냉장고를 생성한다")
    @PostMapping("/{refrigeratorName}")
    public ResponseEntity<BaseResponseDto> insertRefrigerator(@ApiIgnore Authentication authentication, @PathVariable String refrigeratorName) {

        if (authentication == null) {
            throw new CustomException(ErrorCode.NOT_AUTH_TOKEN);
        }

        User user = (User) authentication.getPrincipal();

        String response = refrigeratorService.insertRefrigerator(user, refrigeratorName);


        if (response.equals("fail")) {
            return ResponseEntity.status(401).body(BaseResponseDto.of(401, "실패"));
        }

        return ResponseEntity.ok(BaseResponseDto.of(200, "success"));
    }

    @ApiOperation(value = "공유 냉장고 생성", notes = "공유 냉장고를 생성한다.")
    @PostMapping("/share/{refrigeratorId}")
    public ResponseEntity<BaseResponseDto> createShareGroup(@ApiIgnore Authentication authentication, @PathVariable Long refrigeratorId) {

        User user = (User) authentication.getPrincipal();

        String result = refrigeratorService.createShareGroup(user, refrigeratorId);

        if (result.equals("success")) {
            return ResponseEntity.ok().body(BaseResponseDto.of(200, "냉장고 공유 그룹 생성 성공"));
        }

        throw new CustomException(ErrorCode.FAIL_SHARE_GROUP);
    }

    @ApiOperation(value = "공유 냉장고 그릅원 초대를 위한 냉장고 아이디 전달", notes = "공유 냉장고 그룹원 초대를 위해 해당 냉장고 아이디를 넘겨준다.")
    @GetMapping("/share/{refrigeratorId}")
    public ResponseEntity<SingleResponseDto<InviteShareMemberResponseDto>> inviteShareMember(@PathVariable Long refrigeratorId) {

        InviteShareMemberResponseDto inviteShareMemberResponseDto = InviteShareMemberResponseDto.builder()
                .refrigeratorId(refrigeratorId)
                .build();

        return ResponseEntity.ok(new SingleResponseDto<InviteShareMemberResponseDto>(200, "초대 요청 성공", inviteShareMemberResponseDto));
    }

    @ApiOperation(value = "공유 그룹원 초대 수락", notes = "공유 그릅원을 추가한다.")
    @PostMapping("/share/insertmember")
    public ResponseEntity<BaseResponseDto> insertShareMember(@ApiIgnore Authentication authentication, @RequestBody InsertShareMemberRequestDto insertShareMemberRequestDto) {

        User user = (User) authentication.getPrincipal();

        String result = refrigeratorService.createShareGroup(user, insertShareMemberRequestDto);

        if (result.equals("success")) {
            return ResponseEntity.ok().body(BaseResponseDto.of(200, "냉장고 공유 그룹원 추가 성공"));
        }

        throw new CustomException(ErrorCode.FAIL_SHARE_GROUP);
    }

}
