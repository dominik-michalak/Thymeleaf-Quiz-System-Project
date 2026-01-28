package Controllers;


import Entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @GetMapping
    public ResponseEntity<List<QuizEntity>> getAllQuizzes() {
        return ResponseEntity.ok(getAllQuizzes().getBody());
    }

}
