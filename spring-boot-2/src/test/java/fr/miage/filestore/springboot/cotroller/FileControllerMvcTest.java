package fr.miage.filestore.springboot.cotroller;

import fr.miage.filestore.springboot.controller.FileController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.springframework.util.Assert.notNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerMvcTest {

    @Autowired
    private FileController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void hello() throws Exception {
        notNull(controller);

        final ResponseEntity<String> helloResponse =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/johndoe/content",
                        String.class);

        Assert.assertTrue(
                helloResponse
                        .getBody()
                        .toLowerCase()
        .startsWith("hello"));
    }

}