package com.trackswiftly.vehicle_service.tests.web;




import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackswiftly.client_service.dtos.GroupRequest;
import com.trackswiftly.client_service.dtos.GroupResponse;
import com.trackswiftly.client_service.services.GroupService;
import com.trackswiftly.client_service.web.GroupController;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@DisabledInNativeImage
@ExtendWith(MockitoExtension.class)
class GroupControllerTest {


    @InjectMocks
    private GroupController groupController;

    @Mock
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }




    @Test
    void testCreateGroups() throws Exception {
        List<GroupRequest> groupRequests = Arrays.asList(
                GroupRequest.builder().name("Group 1").build(),
                GroupRequest.builder().name("Group 2").build()
        );
        List<GroupResponse> groupResponses = Arrays.asList(
                GroupResponse.builder().id(1L).name("Group 1").build(),
                GroupResponse.builder().id(2L).name("Group 2").build()
        );

        when(groupService.createEntities(groupRequests)).thenReturn(groupResponses);

        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequests)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Group 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Group 2"));

        verify(groupService, times(1)).createEntities(groupRequests);
    }




    @Test
    void testDeleteGroups() throws Exception {
        List<Long> groupIds = Arrays.asList(1L, 2L);
        OperationResult result = OperationResult.of(2);

        when(groupService.deleteEntities(groupIds)).thenReturn(result);

        mockMvc.perform(delete("/groups/1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.affectedRecords").value(2));

        verify(groupService, times(1)).deleteEntities(groupIds);
    }




    @Test
    void testFindGroups() throws Exception {
        List<Long> groupIds = Arrays.asList(1L, 2L);
        List<GroupResponse> groupResponses = Arrays.asList(
                GroupResponse.builder().id(1L).name("Group 1").build(),
                GroupResponse.builder().id(2L).name("Group 2").build()
        );

        when(groupService.findEntities(groupIds)).thenReturn(groupResponses);

        mockMvc.perform(get("/groups/1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Group 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Group 2"));

        verify(groupService, times(1)).findEntities(groupIds);
    }





    @Test
    void testGetGroupsWithPagination() throws Exception {
        PageDTO<GroupResponse> pageDTO = new PageDTO<>(Arrays.asList(
                GroupResponse.builder().id(1L).name("Group 1").build()
        ), 0, 1, 10L, 10);

        when(groupService.pageEntities(0, 1)).thenReturn(pageDTO);

        mockMvc.perform(get("/groups?page=0&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(10))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Group 1"));

        verify(groupService, times(1)).pageEntities(0, 1);
    }




    @Test
    void testUpdateGroupsInBatch() throws Exception {
        List<Long> groupIds = Arrays.asList(1L, 2L);
        GroupRequest groupRequest = GroupRequest.builder()
                .name("Updated Group")
                .build();
        OperationResult result = OperationResult.of(2);

        when(groupService.updateEntities(groupIds, groupRequest)).thenReturn(result);

        mockMvc.perform(put("/groups/1,2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.affectedRecords").value(2));

        verify(groupService, times(1)).updateEntities(groupIds, groupRequest);
    }

    
}
