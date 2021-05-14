package com.petkov.spr_final_1.DEVTools;

import com.petkov.spr_final_1.model.app_entity.QuestionEntity;
import com.petkov.spr_final_1.model.app_entity.TestEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.ArticleEntity;
import com.petkov.spr_final_1.model.enumeration.TestStatusEnum;
import com.petkov.spr_final_1.repository.ArticleRepository;
import com.petkov.spr_final_1.repository.TestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;


@Component
public class AppController implements CommandLineRunner {

    private final TestRepository testRepository;
    private final ArticleRepository articleRepository;

    public AppController(TestRepository testRepository, ArticleRepository articleRepository) {
        this.testRepository = testRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        //number of tests in db
        System.out.printf("tests: %d%n", this.testRepository.count());

       //  testRepository.saveAndFlush(generateDummyTestWithQuestions());

        printTests();


    }

    private TestEntity generateDummyTestWithQuestions() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Engines");
        testEntity.setTestStatus(TestStatusEnum.ACTIVE);
        testEntity.setDateCreated(LocalDate.now());
        testEntity.setDueDate(LocalDate.now());


        testEntity.setQuestions(List.of(
                generateDummyQuestion(
                        "Starter N2 limitation",
                        "What is the maximum N2 for starter engagement (NEO)?",
                        "20%",
                        "10%", "12%", "25%", "8%",
                        generateStarterDummyArticle()),
                generateDummyQuestion(
                        "Oil temperature limitation",
                        "What is the maximum OIL temperature for the  NEO aircraft (C)?",
                        "151",
                        "165", "135", "160", "152",
                        generateOilDummyArticle())));

        return testEntity;
    }

    private ArticleEntity generateStarterDummyArticle() {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle("STARTER");
        articleEntity.setFullReferencePath("LIM-ENG P 4/8");
        articleEntity.setArticleText("‐ A standard automatic start that includes up to two start attempts, is considered one cycle\n" +
                "‐ For ground starts (automatic or manual), a 35 s pause is required between successive cycles\n" +
                "‐ A 15 min cooling period is required, subsequent to three failed cycles, or 15 min of continuous or\n" +
                "cumulated crank\n" +
                "‐ The starter must not be run when N2 is above 20 %.");

        return articleEntity;
    }

    private ArticleEntity generateOilDummyArticle() {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle("OIL");
        articleEntity.setFullReferencePath("LIM-ENG P 3/8");
        articleEntity.setArticleText("""
                OIL TEMPERATURE
                Maximum continuous temperature......................................................................................... 155 °C
                Maximum transient temperature (15 min).............................................................................. 165 °C
                Minimum starting temperature................................................................................................ -40 °C
                Minimum temperature before IDLE is exceeded..................................................................... -10 °C
                Minimum temperature before takeoff....................................................................................... 50 °C
                OIL QUANTITY
                Minimum oil quantity..................... Refer to PRO-NOR-SOP-04 Before Walkaround - ECAM Pages
                MINIMUM OIL PRESSURE
                Minimum oil pressure............................................................................................................. 60 PSI""");

        return articleEntity;
    }

    private QuestionEntity generateDummyQuestion(
            String name,
            String question,
            String correctAnswer,
            String altAnswer1,
            String altAnswer2,
            String altAnswer3,
            String altAnswer4,
            ArticleEntity articleEntity) {

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setName(name);
        questionEntity.setQuestion(question);
        questionEntity.setCorrectAnswer(correctAnswer);
        questionEntity.setArticle(articleEntity);
        questionEntity.setAltAnswer1(altAnswer1);
        questionEntity.setAltAnswer2(altAnswer2);
        questionEntity.setAltAnswer3(altAnswer3);
        questionEntity.setAltAnswer4(altAnswer4);

        return questionEntity;
    }

    private void printTests() {
        this.testRepository.findAll(Sort.by("name")).forEach(test -> {

            System.out.printf("Test: %s%n", test.getName());

            test.getQuestions()
                    .forEach(question -> {
                        System.out.printf("   Name: %s%n", question.getName());
                        System.out.printf("      Question: %s%n", question.getQuestion());
                        System.out.printf("          correct Answer: %s%n", question.getCorrectAnswer());
                        System.out.printf("          alt Answer1:     %s%n", question.getAltAnswer1());
                        System.out.printf("          alt Answer2:     %s%n", question.getAltAnswer2());
                        System.out.printf("          alt Answer3:     %s%n", question.getAltAnswer3());
                        System.out.printf("          alt Answer4:     %s%n", question.getAltAnswer4());
                    });

        });

    }


}
