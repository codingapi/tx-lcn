package com.codingapi.tx.framework.task;

import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;

/**
 * create by lorne on 2017/8/22
 */
public class TxTask extends Task{

    private Task task;


    public TxTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean isNotify() {
        return task.isNotify();
    }

    @Override
    public boolean isRemove() {
        return task.isRemove();
    }

    @Override
    public boolean isAwait() {
        return task.isAwait();
    }

    @Override
    public int getState() {
        return task.getState();
    }

    @Override
    public void setState(int state) {
        task.setState(state);
    }

    @Override
    public String getKey() {
        return task.getKey();
    }

    @Override
    public void setKey(String key) {
        task.setKey(key);
    }

    @Override
    public IBack getBack() {
        return task.getBack();
    }

    @Override
    public void setBack(IBack back) {
        task.setBack(back);
    }

    @Override
    public Object execute(IBack back) {
        return task.execute(back);
    }

    @Override
    public void remove() {
        task.remove();

        boolean hasData = true;//true没有，false有

        String groupKey = getKey().split("_")[1];
        TaskGroup taskGroup =  TaskGroupManager.getInstance().getTaskGroup(groupKey);
        for(TxTask task: taskGroup.getTasks()){
            if(!task.isRemove()){
                hasData = false;
            }
        }

        if(hasData){
            TaskGroupManager.getInstance().removeKey(groupKey);
        }



    }

    @Override
    public void signalTask() {
        task.signalTask();
    }

    @Override
    public void signalTask(IBack back) {
        task.signalTask(back);
    }

    @Override
    public void awaitTask() {
        task.awaitTask();
    }

    @Override
    public void awaitTask(IBack back) {
        task.awaitTask(back);
    }
}
