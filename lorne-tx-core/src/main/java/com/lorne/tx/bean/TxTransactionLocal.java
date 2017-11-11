package com.lorne.tx.bean;

//import com.lorne.tx.compensate.model.TransactionRecover;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分布式事务远程调用控制对象
 * Created by lorne on 2017/6/5.
 */
public class TxTransactionLocal {

    private final static ThreadLocal<TxTransactionLocal> currentLocal = new ThreadLocal<TxTransactionLocal>();

  //  private boolean hasCompensate = false;

    private String groupId;

    private int maxTimeOut;

    private boolean hasIsGroup = false;

    private boolean hasStart = false;

    private String kid;

   // private TransactionRecover recover;

    private String type;

    private boolean readOnly;

    private Transactional transactional;

//    public TransactionRecover getRecover() {
//        return recover;
//    }

//    public void setRecover(TransactionRecover recover) {
//        this.recover = recover;
//    }

    public boolean isHasIsGroup() {
        return hasIsGroup;
    }

    public void setHasIsGroup(boolean hasIsGroup) {
        this.hasIsGroup = hasIsGroup;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public boolean isHasStart() {
        return hasStart;
    }

    public void setHasStart(boolean hasStart) {
        this.hasStart = hasStart;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


//    public boolean isHasCompensate() {
//        return hasCompensate;
//    }
//
//    public void setHasCompensate(boolean hasCompensate) {
//        this.hasCompensate = hasCompensate;
//    }

    public TxTransactionLocal() {

    }

    public int getMaxTimeOut() {
        return maxTimeOut;
    }

    public void setMaxTimeOut(int maxTimeOut) {
        this.maxTimeOut = maxTimeOut;
    }


    public static TxTransactionLocal current() {
        return currentLocal.get();
    }

    public static void setCurrent(TxTransactionLocal current) {
        currentLocal.set(current);
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }



    public boolean isReadOnly() {
        return readOnly;
    }

    public Transactional getTransactional() {
        return transactional;
    }

    public void setTransactional(Transactional transactional) {
        this.transactional = transactional;

        //set readOnly
        if(transactional==null){
            //没有配置事务注解的时候当做只读来处理
            readOnly = true;
        }else{
            readOnly = transactional.readOnly();
        }
    }
}
