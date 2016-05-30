package com.fruit.client.object.event;

import java.util.ArrayList;

/**
 * Created by qianyx on 16-5-9.
 */
public class EventResult {
    private ArrayList<Event> list;

    public ArrayList<Event> getResult() {
        return list;
    }

    public void setResult(ArrayList<Event> result) {
        this.list = result;
    }
}
