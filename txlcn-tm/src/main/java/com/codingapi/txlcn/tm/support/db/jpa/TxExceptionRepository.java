package com.codingapi.txlcn.tm.support.db.jpa;

import com.codingapi.txlcn.tm.support.db.domain.TxException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author meetzy 2019-02-14 12:09
 */
@Repository
public interface TxExceptionRepository extends JpaRepository<TxException, Long> , JpaSpecificationExecutor<TxException> {
    
    /**
     * find TxException by groupId and unitId
     * @param groupId groupId
     * @param unitId unitId
     * @return TxException
     */
    TxException findByGroupIdAndUnitId(String groupId, String unitId);
    
    /**
     * update exSate
     * @param id id
     * @param state state
     */
    @Query(value = "update t_tx_exception set ex_state=?2 where id=?1",nativeQuery = true)
    @Modifying
    void changeExState(Long id, short state);
    
    /**
     * find status
     * @param page page need one
     * @param groupId groupId
     * @return list get(0)
     */
    @Query(value = "select transaction_state from t_tx_exception where group_id=?1",nativeQuery = true)
    List<Integer> getTransactionStateByGroupId(String groupId,Pageable page);
}
