package com.example.final_task_mobile.repository.callback;

import com.example.final_task_mobile.models.Videos;

import java.util.List;

public interface OnVideosCallback {
    void onSuccess(List<Videos> videos);
    void onFailure(String message);
}
