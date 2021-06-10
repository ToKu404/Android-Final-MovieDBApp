package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.CreditModel;

public interface OnCastCallback {
    void onSuccess(CreditModel creditModel, String message);
    void onFailure(String message);
}
