package com.codingapi.tx.framework.task;

import com.lorne.core.framework.utils.task.ConditionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by lorne on 2017/8/22
 */
public class TaskGroupManager {

    private Map<String, TaskGroup> taskMap = new ConcurrentHashMap<String, TaskGroup>();

    private static TaskGroupManager instance = null;

    private TaskGroupManager(){}

    public static TaskGroupManager getInstance() {
        if (instance == null) {
            synchronized (TaskGroupManager.class) {
                if(instance==null){
                    instance = new TaskGroupManager();
                }
            }
        }
        return instance;
    }

    public TaskGroup createTask(String key,String type) {
        TaskGroup taskGroup = getTaskGroup(key);
        if(taskGroup==null){
            taskGroup = new TaskGroup();
        }
        taskGroup.setKey(key);

        String taskKey = type+"_"+key;

        TxTask task =  new TxTask(ConditionUtils.getInstance().createTask(taskKey));
        taskGroup.setCurrent(task);
        taskGroup.addTask(task);
        taskMap.put(key, taskGroup);
        return taskGroup;
    }

    public TaskGroup getTaskGroup(String key) {
        return taskMap.get(key);
    }


    public TxTask getTask(String key,String type) {
        String taskKey = type+"_"+key;
        TaskGroup txGroup =  taskMap.get(key);
        if(txGroup!=null){
            for(TxTask task:txGroup.getTasks()){
                if(taskKey.equals(task.getKey())){
                    return task;
                }
            }
        }
        return null;
    }


    public void removeKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            taskMap.remove(key);
        }
    }



}
