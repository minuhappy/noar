package com.noar.core.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.noar.common.util.BeanUtil;
import com.noar.common.util.IScope;
import com.noar.common.util.ValueUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.exception.ServerException;

/**
 * @author Minu.Kim
 */
public class TransactionUtil {

	private static Integer timeout;
	private static PlatformTransactionManager manager;

	public static PlatformTransactionManager getManager() {
		return ValueUtil.checkValue(manager, BeanUtil.get("transactionManager", PlatformTransactionManager.class));
	}

	public static int getTimeout() {
		return ValueUtil.checkValue(timeout, ConfigConstants.TX_TIMEOUT);
	}

	/**
	 * 처리내용 : Transaction 영역을 수행합니다.
	 * 
	 * @Method Name : doScope
	 * @param name
	 * @param scope
	 * @return
	 * @throws Throwable
	 */
	public static Object doScope(String name, IScope scope) throws Throwable {
		return doScope(name, TransactionDefinition.PROPAGATION_REQUIRED, getTimeout(), scope);
	}

	/**
	 * 처리내용 : 새로운 트랜잭션 스콥을 생성하는 메소드
	 * 
	 * @Method Name : doNewScope
	 * @param name
	 * @param scope
	 * @return
	 * @throws Throwable
	 */
	public static Object doNewScope(String name, IScope scope) throws Throwable {
		return doScope(name, TransactionDefinition.PROPAGATION_REQUIRES_NEW, getTimeout(), scope);
	}

	/**
	 * <pre>
	 * 처리내용 : Transaction 영역을 수행합니다.
	 * </pre>
	 * 
	 * @Method Name : doScope
	 * @param name
	 * @param tran
	 * @param scope
	 * @return
	 * @throws Throwable
	 */
	public static Object doScope(String name, Transactional tran, IScope scope) throws Throwable {
		int propagation = TransactionDefinition.PROPAGATION_REQUIRED;
		int timeout = getTimeout();
		if (tran != null) {
			propagation = tran.propagation().value();
			if (tran.timeout() > 0) {
				timeout = tran.timeout();
			}
		}
		return doScope(name, propagation, timeout, scope);
	}

	/**
	 * <pre>
	 * 처리내용 : Transaction 영역을 수행합니다.
	 * </pre>
	 * 
	 * @Method Name : doScope
	 * @param name
	 * @param propagation
	 * @param timeout
	 * @param scope
	 * @return
	 * @throws Throwable
	 */
	private static Object doScope(final String name, final int propagation, final int timeout, final IScope scope) throws Throwable {
		return doTranScope(name, propagation, timeout, scope);
	}

	private static Object doTranScope(String name, int propagation, int timeout, IScope scope) throws Throwable {
		PlatformTransactionManager manager = getManager();

		/**
		 * Transaction 시작
		 */
		TransactionStatus status = begin(manager, name, propagation, timeout);

		Object res = null;
		try {
			res = scope.execute();
			/**
			 * Transaction 완료
			 */
			commit(manager, status);
		}
		/**
		 * 에러 시 Transaction 롤백
		 */
		catch (Throwable t) {
			rollback(manager, status);
			throw t;
		}
		return res;
	}

	private static TransactionStatus begin(PlatformTransactionManager manager, DefaultTransactionDefinition definition) throws TransactionException {
		TransactionStatus status = manager.getTransaction(definition);
		return status;
	}

	private static TransactionStatus begin(PlatformTransactionManager manager, String name, int propagation, int timeout) throws TransactionException, ServerException {
		if (manager == null) {
			// FIXME Exception 형식 변경 필요
			throw new ServerException("Transaction Manager is null.");
		}
		return begin(manager, getDefinition(name, propagation, timeout));
	}

	private static DefaultTransactionDefinition getDefinition(String name, int propagation, int timeout) {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setName(name);
		definition.setPropagationBehavior(propagation);
		// FIXME Spring Version Error
		// definition.setTimeout(timeout);
		return definition;
	}

	private static void commit(PlatformTransactionManager manager, TransactionStatus status) throws Exception {
		// 현재 영역이 새 Transaction(실제 commit 되기 직전)인 시점이면,
		// 이 트랜잭션에 예약된 History 내역을 Batch 입력
		manager.commit(status);
	}

	private static void rollback(PlatformTransactionManager manager, TransactionStatus status) throws Exception {
		if (status.isCompleted()) {
			return;
		}
		manager.rollback(status);
	}
}

/**
 * <pre>
 * 클래스 개요 : 트랜잭션 정보
 * </pre>
 */
class TransactionInfo {
	private boolean newTransaction = false;
	private PlatformTransactionManager manager;
	private TransactionStatus status;
	private TransactionDefinition definition;

	public boolean isNewTransaction() {
		return newTransaction;
	}

	public void setNewTransaction(boolean newTransaction) {
		this.newTransaction = newTransaction;
	}

	public PlatformTransactionManager getManager() {
		return manager;
	}

	public void setManager(PlatformTransactionManager manager) {
		this.manager = manager;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public TransactionDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(TransactionDefinition definition) {
		this.definition = definition;
	}
}