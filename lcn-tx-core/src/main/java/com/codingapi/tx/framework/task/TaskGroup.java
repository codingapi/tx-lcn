package com.codingapi.tx.framework.task;


import java.util.ArrayList;
import java.util.List;

/**
 * create by lorne on 2017/8/22
 */
public class TaskGroup {

    private String key;

    private List<TxTask> tasks;

    private TxTask current;

    private int state;


    public TxTask getCurrent() {
        return current;
    }

    public void setCurrent(TxTask current) {
        this.current = current;
    }

    public String getKey() {
        return key;
    }


    public TaskGroup() {
        tasks = new ArrayList<>();
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<TxTask> getTasks() {
        return tasks;
    }

    public void addTask(TxTask task) {
        tasks.add(task);
    }


    public boolean isAwait(){
        for(TxTask task: getTasks()){
            if(!task.isAwait()){
                return false;
            }
        }
        return true;
    }

    public boolean isRemove(){
        for(TxTask task: getTasks()){
            if(!task.isRemove()){
                return false;
            }
        }
        return true;
    }



    public void signalTask(){
        for(TxTask task: getTasks()){
            task.setState(state);
            task.signalTask();
        }
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
