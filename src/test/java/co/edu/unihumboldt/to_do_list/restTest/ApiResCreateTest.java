package co.edu.unihumboldt.to_do_list.restTest;

import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

public class ApiResCreateTest {


    @BeforeAll
    public static void setup() {
        baseURI = "http://localhost:8080/api/tasks";
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
                .body("rewardLevel", equalTo(3))
                .extract()
                .path("id");
    }
}

