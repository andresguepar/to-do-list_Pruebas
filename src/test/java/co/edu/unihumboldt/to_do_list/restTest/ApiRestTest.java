package co.edu.unihumboldt.to_do_list.restTest;

import static io.restassured.RestAssured.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ApiRestTest {

        @BeforeAll
        public static void setup() {
            baseURI = "http://localhost:8080/api/tasks";
        }

        @Test
        public void testStatsTask() {
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
        public void testGetTasks() {
            List<Map<String, Object>> tasks = get("").as(new TypeRef<List<Map<String, Object>>>() {});

            assertThat(tasks, hasSize(7));

            assertThat(tasks, containsInAnyOrder(
                    allOf(hasEntry("title", "Leer Libro")),
                    allOf(hasEntry("title", "Hacer Tarea")),
                    allOf(hasEntry("title", "Something")),
                    allOf(hasEntry("title", "Tas")),
                    allOf(hasEntry("title", "Estudiar ")),
                    allOf(hasEntry("title", "Hola")),
                    allOf(hasEntry("title", "Si"))
            ));
        }

        @Test
        public void testSortedByPriorityTask() {
            List<Map<String, Object>> tasks = get("/sorted-by-priority").as(new TypeRef<List<Map<String, Object>>>() {});

            assertThat(tasks, hasSize(7));
            assertThat(tasks, containsInAnyOrder(
                    allOf(hasEntry("title", "Leer Libro"), hasEntry("priority", "HIGH")),
                    allOf(hasEntry("title", "Hacer Tarea"), hasEntry("priority", "HIGH")),
                    allOf(hasEntry("title", "Si"), hasEntry("priority", "HIGH")),
                    allOf(hasEntry("title", "Something"), hasEntry("priority", "MEDIUM")),
                    allOf(hasEntry("title", "Tas"), hasEntry("priority", "MEDIUM")),
                    allOf(hasEntry("title", "Estudiar "), hasEntry("priority", "MEDIUM")),
                    allOf(hasEntry("title", "Hola"), hasEntry("priority", "MEDIUM"))
            ));
        }

    @Test
    public void testSortedByCompletionTask() {
        List<Map<String, Object>> tasks = get("/sorted-by-completion-status").as(new TypeRef<List<Map<String, Object>>>() {});

        assertThat(tasks, hasSize(7));

        List<Map<String, Object>> filteredTasks = tasks.stream()
                .map(task -> Map.of(
                        "title", task.get("title"),
                        "completed", task.get("completed")
                ))
                .collect(Collectors.toList());

        assertThat(filteredTasks, containsInAnyOrder(
                Map.of("title", "Leer Libro", "completed", false),
                Map.of("title", "Si", "completed", false),
                Map.of("title", "Something", "completed", false),
                Map.of("title", "Hola", "completed", false),
                Map.of("title", "Hacer Tarea", "completed", true),
                Map.of("title", "Tas", "completed", true),
                Map.of("title", "Estudiar ", "completed", true)
        ));
    }
    @Test
    public void testSortedByDueDateTask() {
        List<Map<String, Object>> tasks = get("/sorted-by-due-date").as(new TypeRef<List<Map<String, Object>>>() {});

        assertThat(tasks, hasSize(7));

        List<Map<String, Object>> filteredTasksDate = tasks.stream()
                .map(task -> Map.of(
                        "title", task.get("title")
                ))
                .collect(Collectors.toList());

        assertThat(filteredTasksDate, containsInAnyOrder(
                Map.of("title", "Something"),
                Map.of("title", "Leer Libro"),
                Map.of("title", "Hacer Tarea"),
                Map.of("title", "Si"),
                Map.of("title", "Tas"),
                Map.of("title", "Estudiar "),
                Map.of("title", "Hola")
        ));
    }
    }


