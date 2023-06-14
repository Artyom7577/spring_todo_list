package com.example.spring_todo_list;

import com.example.spring_todo_list.dto.TaskRequest;
import com.example.spring_todo_list.repositories.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class SpringTodoListApplicationTests {
    private TaskRepository taskRepository;
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskRequest taskRequest = getTaskRequest();
        String taskRequestString = objectMapper.writeValueAsString(taskRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, taskRepository.findAll().size());
    }
    private TaskRequest getTaskRequest() {
        return TaskRequest.builder()
                .taskDescription("taskDescription")
                .startDate(LocalDate.of(2023, 11, 6))
                .endDate(LocalDate.of(2023, 11, 24))
                .completed(false)
                .build();
    }

}
