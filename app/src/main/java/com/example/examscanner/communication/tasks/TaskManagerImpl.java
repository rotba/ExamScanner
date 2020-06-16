package com.example.examscanner.communication.tasks;

import java.util.HashMap;

import io.reactivex.Completable;

class TaskManagerImpl implements TasksManager {

    private HashMap<String,Task> map = new HashMap<>();
    @Override
    public void post(Completable comp, String id, String desc) {
        map.put(id, new Task(comp,desc));
    }

    @Override
    public Task get(String id) {
        return map.get(id);
    }
}
