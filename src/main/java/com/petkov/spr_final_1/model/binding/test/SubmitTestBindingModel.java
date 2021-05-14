package com.petkov.spr_final_1.model.binding.test;

import java.util.LinkedHashMap;
import java.util.Map;

public class SubmitTestBindingModel {

    private Map<String, String> askedQuestionMatrix = new LinkedHashMap<>();
    private Map<String, String> givenAnswerMatrix = new LinkedHashMap<>();

    public SubmitTestBindingModel() {
    }


    public Map<String, String> getAskedQuestionMatrix() {
        return askedQuestionMatrix;
    }

    public void setAskedQuestionMatrix(Map<String, String> askedQuestionMatrix) {
        this.askedQuestionMatrix = askedQuestionMatrix;
    }

    public Map<String, String> getGivenAnswerMatrix() {
        return givenAnswerMatrix;
    }

    public void setGivenAnswerMatrix(Map<String, String> givenAnswerMatrix) {
        this.givenAnswerMatrix = givenAnswerMatrix;
    }
}
