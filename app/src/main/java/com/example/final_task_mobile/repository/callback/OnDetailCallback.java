package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.DetailModel;

public interface OnDetailCallback {
    void onSuccess(DetailModel model, String message);

    void onFailure(String message);
}
