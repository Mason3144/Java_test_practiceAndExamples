package com.codestates.sliceTest.slice.controller;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.stamp.Stamp;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//    @WebMvcTest(controllers = MemberController.class)
//    @MockBean(JpaMetamodelMappingContext.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @Autowired
    private MemberRepository memberRepository;
    private ResultActions postResultAction;
    private MemberDto.Post post;
    private long memberId;

    @BeforeEach
    public void init() throws Exception {
        // given
        post = new MemberDto.Post("mason@test.com",
                "Mason",
                "010-1111-1111");
        String content = gson.toJson(post);
        // when
        postResultAction =
                mockMvc.perform(
                        post("/v11/members")
                                .accept(MediaType.APPLICATION_JSON) // http 메서드에따라 설정해줘야됨
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        String location = postResultAction.andReturn().getResponse().getHeader("Location"); // "/v11/members/1"
        memberId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }

    @Test
    void postMemberTest() throws Exception {
        postResultAction
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v11/members/"))));
    }

    @Test
    void getMemberTest() throws Exception {

        mockMvc.perform(
                        get("/v11/members/" + memberId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(post.getEmail()))
                .andExpect(jsonPath("$.data.name").value(post.getName()))
                .andExpect(jsonPath("$.data.phone").value(post.getPhone()));
    }

    @Test
    void patchMemberTest() throws Exception{
        MemberDto.Patch patchDto = MemberDto.Patch.builder().phone("010-1111-1111").build();
        String content = gson.toJson(patchDto);

        mockMvc.perform(
                patch("/v11/members/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.phone").value(patchDto.getPhone())) // 변경된 값 확인
                .andExpect(jsonPath("$.data.memberId").value(memberId))
                .andExpect(jsonPath("$.data.name").value(post.getName()))
                .andExpect(jsonPath("$.data.email").value(post.getEmail()))
                .andExpect(jsonPath("$.data.memberStatus").value(Member.MemberStatus.MEMBER_ACTIVE.getStatus()));
    }

    @Test
    void deleteMemberTest() throws Exception{
        mockMvc.perform(
                delete("/v11/members/"+memberId)
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void getMembersTest() throws Exception{
        Member newMember = new Member("mason2@test.com","Mason2","010-2222-2222");
        newMember.setStamp(new Stamp());
        memberRepository.save(newMember);

        String page = "1";
        String size = "10";
        MultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page",page);
        queryParams.add("size",size);

        ResultActions actions = mockMvc.perform(
                get("/v11/members")
                        .params(queryParams)
        );

        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andReturn();

        List<Member> list = JsonPath.parse(result.getResponse().getContentAsString()).read("$.data");

        assertThat(list.size(),is(2));
    }
}

