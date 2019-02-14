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
 * @author meetzy
 * @date 2019-02-14 12:09
 */
@Repository
public interface TxExceptionRepository extends JpaRepository<TxException, Long> , JpaSpecificationExecutor<TxException> {
    
    TxException findByGroupIdAndUnitId(String groupId, String unitId);
    
//    List<TxException> findByRegistrar(Pageable pageable,short registrar);
//
//    List<TxException> findByExState(Pageable pageable,short exState);
//
//    List<TxException> findByExStateAndRegistrar(Pageable pageable,short exState, short registrar);
    
    @Query(value = "update t_tx_exception set ex_state=?2 where id=?1",nativeQuery = true)
    @Modifying
    void changeExState(Long id, short state);
    
    @Query(value = "select transaction_state from t_tx_exception where group_id=?1",nativeQuery = true)
    List<Integer> getTransactionStateByGroupId(Pageable page, String groupId);
}
