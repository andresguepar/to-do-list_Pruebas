package co.edu.unihumboldt.to_do_list.restTest;

import static io.restassured.RestAssured.*;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ApiRestTest {

    @Test
    public void testStatsTask(){
        baseURI ="http://localhost:8080/api/tasks";

        given()
                .when()
                .get("/stats")
                .then()
                    .statusCode(200)
                    .body("totalTasks", equalTo(7))
                    .body("completedTasks", equalTo(3))
                    .body("pendingTasks", equalTo(4))
                    .body("highPriorityTasks", equalTo(3))
                    .body("mediumPriorityTasks", equalTo(0))
                    .body("lowPriorityTasks", equalTo(0))
                    .body("completionRate", equalTo(0.42857142857142855f))
                    .body("taskCompletedOnTime", equalTo(2))
                    .body("overDueTasks", equalTo(4))
                    .body("averageRewardLevel", equalTo(1.7142857142857142f));

    }

    @Test
    public void testSortedByPriorityTask(){
        baseURI = "http://localhost:8080/api/tasks";


        List<Map<String, Object>> tasks = get("/sorted-by-priority").as(new TypeRef<List<Map<String, Object>>>() {});

        assertThat(tasks, hasSize(7));
        assertThat(tasks.get(0).get("id"), equalTo(1));
        assertThat(tasks.get(1).get("id"), equalTo(2));
        assertThat(tasks.get(2).get("id"), equalTo(10));
        assertThat(tasks.get(3).get("id"), equalTo(3));
        assertThat(tasks.get(4).get("id"), equalTo(4));
        assertThat(tasks.get(5).get("id"), equalTo(5));
        assertThat(tasks.get(6).get("id"), equalTo(9));

    }

    @Test
    public void testSortedByCompletionTask() {
        baseURI = "http://localhost:8080/api/tasks";

        List<Map<String, Object>> tasks = get("/sorted-by-completion-status").as(new TypeRef<List<Map<String, Object>>>() {});

        assertThat(tasks, hasSize(7));
        assertThat(tasks.get(0).get("id"), equalTo(1));
        assertThat(tasks.get(1).get("id"), equalTo(10));
        assertThat(tasks.get(2).get("id"), equalTo(3));
        assertThat(tasks.get(3).get("id"), equalTo(9));
        assertThat(tasks.get(4).get("id"), equalTo(2));
        assertThat(tasks.get(5).get("id"), equalTo(4));
        assertThat(tasks.get(6).get("id"), equalTo(5));
    }

    @Test
    public void testSortedByDueDateTask() {
        baseURI = "http://localhost:8080/api/tasks";

        List<Map<String, Object>> tasks = get("/sorted-by-due-date").as(new TypeRef<List<Map<String, Object>>>() {});

        assertThat(tasks, hasSize(7));
        assertThat(tasks.get(0).get("id"), equalTo(3));
        assertThat(tasks.get(1).get("id"), equalTo(1));
        assertThat(tasks.get(2).get("id"), equalTo(2));
        assertThat(tasks.get(3).get("id"), equalTo(10));
        assertThat(tasks.get(4).get("id"), equalTo(4));
        assertThat(tasks.get(5).get("id"), equalTo(5));
        assertThat(tasks.get(6).get("id"), equalTo(9));
    }

    @Test
    public void testCreateTask() {
        baseURI = "http://localhost:8080/api";

        Map<String, Object> newTask = Map.of(
                "title", "Nueva Tarea",
                "description", "Descripción de la nueva tarea",
                "priority", "HIGH",
                "creationDate", "2024-10-20",
                "limitDate", "2024-10-25",
                "completed", false,
                "rewardLevel", 3
        );

        given()
                .contentType("application/json")
                .body(newTask)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201)
                .body("title", equalTo("Nueva Tarea"))
                .body("description", equalTo("Descripción de la nueva tarea"))
                .body("priority", equalTo("HIGH"))
                .body("completed", equalTo(false))
                .body("rewardLevel", equalTo(3));
    }

}
