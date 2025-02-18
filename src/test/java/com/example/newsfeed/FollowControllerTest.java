//package com.example.newsfeed;
//
//import com.example.newsfeed.controller.follow.FollowController;
//import com.example.newsfeed.service.follow.FollowService;
//import com.example.newsfeed.common.consts.Const;
//import com.example.newsfeed.dto.follow.responseDto.FollowResponseDto;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class) // MockitoExtension 추가
//public class FollowControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private FollowService followService; // 모킹된 서비스
//
//    @InjectMocks
//    private FollowController followController; // 실제 컨트롤러
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(followController)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }
//
//    @Test
//    void testFollowUser() throws Exception {
//        Long userId = 1L;
//        Long followerId = 2L;
//
//        // followService의 반환값을 "팔로우 성공" 대신 "1"로 설정
//        when(followService.followUser(any(), any())).thenReturn("1");
//
//        mockMvc.perform(post("/follows/{followerId}", followerId)
//                        .sessionAttr(Const.LOGIN_USER, userId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("1"))
//                .andDo(result -> System.out.println("Response Content: " + result.getResponse().getContentAsString()));
//
//        verify(followService, times(1)).followUser(userId, followerId);
//    }
//
//    @Test
//    void testGetFollowers() throws Exception {
//        Long userId = 1L;
//        FollowResponseDto followResponseDto = new FollowResponseDto(1L, "user2");
//        when(followService.getFollowers(userId)).thenReturn(Collections.singletonList(followResponseDto));
//
//        mockMvc.perform(get("/follows")
//                        .sessionAttr(Const.LOGIN_USER, userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].followerName").value("user2"));
//
//        verify(followService, times(1)).getFollowers(userId);
//    }
//
//    @Test
//    void testAcceptFollower() throws Exception {
//        Long userId = 1L;
//        Long followerId = 2L;
//        doNothing().when(followService).acceptFollower(userId, followerId);
//
//        mockMvc.perform(patch("/follows/{followerId}/accept", followerId)
//                        .sessionAttr(Const.LOGIN_USER, userId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("요청이 수락되었습니다."));
//
//        verify(followService, times(1)).acceptFollower(userId, followerId);
//    }
//
//    @Test
//    void testRejectFollower() throws Exception {
//        Long userId = 1L;
//        Long followerId = 2L;
//
//        // followService의 메서드가 정상적으로 실행된다고 가정
//        doNothing().when(followService).rejectFollower(userId, followerId);
//
//        mockMvc.perform(patch("/follows/{followerId}/reject", followerId)
//                        .sessionAttr(Const.LOGIN_USER, userId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("요청이 거절되었습니다."));
//
//        verify(followService, times(1)).rejectFollower(userId, followerId);
//    }
//
//    @Test
//    void testUnfollowUser() throws Exception {
//        Long userId = 1L;
//        Long followerId = 2L;
//
//        doNothing().when(followService).unfollowUser(userId, followerId);
//
//        mockMvc.perform(delete("/follows/{followerId}", followerId)
//                        .sessionAttr(Const.LOGIN_USER, userId))
//                .andExpect(status().isOk())
//                .andExpect(content().string("삭제되었습니다."));
//
//        verify(followService, times(1)).unfollowUser(userId, followerId);
//    }
//}
