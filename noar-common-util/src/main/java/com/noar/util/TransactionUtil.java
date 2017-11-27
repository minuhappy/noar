/**
 * 
 */
package com.noar.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.minu.base.ConfigConstants;
import com.minu.exception.SystemCodeException;
import com.minu.exception.SystemErrorException;

/**
 * @author Minu Ʈ����� ������ ���� Util
 */
public class TransactionUtil {
	private static PlatformTransactionManager manager;
	private static PlatformTransactionManager managerReadOnly;
	private static PlatformTransactionManager managerBiOlap;
	private static PlatformTransactionManager managerQa;
	private static Integer timeout;
	public static PlatformTransactionManager getManager() {
		if (manager == null) {
			String name = PropertyUtil.getProperty(ConfigConstants.TRANSACTION_MANAGER, "businessTxManager");
			manager = BeanUtil.get(name, PlatformTransactionManager.class);
		}
		TransactionInfo info = getInfo();
		if (info == null) {
				return manager;
		}
		return info.getManager();
	}
	public static int getTimeout() {
		if (timeout == null) {
			timeout = CastingUtil.toInteger(PropertyUtil.getProperty(ConfigConstants.TRANSACTION_TIMEOUT), 180); // in
																											// seconds
		}
		TransactionDefinition definition = getDefinition();
		return definition == null ? timeout : definition.getTimeout();
	}

	/**
	 * <pre>
	 * ó������ : Transaction ������ �����մϴ�.
	 * </pre>
	 * 
	 * @Method Name : doScope
	 * @param name
	 * @param closure
	 * @return
	 * @throws Exception
	 */
	public static Object doScope(String name, IClosure closure) throws Exception {
		return doScope(name, TransactionDefinition.PROPAGATION_REQUIRED, getTimeout(), closure);
	}

	/**
	 * <pre>
	 * ó������ : xxx�� ó���ϴ� �޼ҵ�
	 * </pre>
	 * 
	 * @Method Name : doNewScope
	 * @param name
	 * @param closure
	 * @return
	 * @throws Exception
	 */
	public static Object doNewScope(String name, IClosure closure) throws Exception {
		return doScope(name, TransactionDefinition.PROPAGATION_REQUIRES_NEW, getTimeout(), closure);
	}

	/**
	 * <pre>
	 * ó������ : Sub Transaction ������ �����մϴ�.
	 * �ݵ�� �ٸ� Transaction �ȿ��� ����Ǿ�� �մϴ�.
	 * </pre>
	 * 
	 * @Method Name : doSubScope
	 * @param name
	 * @param closure
	 * @return
	 * @throws Exception
	 */
	public static Object doSubScope(String name, IClosure closure) throws Exception {
		if (!isScope()) {
			throw new SystemCodeException("TransactionUtil.doSubScope(name, closure) must be invoked in another Transaction Scope.");
		}
		return doScope(name, getPropagationBehavior(), getTimeout(), closure);
	}

	/**
	 * <pre>
	 * ó������ : Transaction ������ �����մϴ�.
	 * </pre>
	 * 
	 * @Method Name : doScope
	 * @param name
	 * @param tran
	 * @param closure
	 * @return
	 * @throws Exception
	 */
	public static Object doScope(String name, Transactional tran, IClosure closure) throws Exception {
		int propagation = TransactionDefinition.PROPAGATION_REQUIRED;
		int timeout = TransactionUtil.getTimeout();
		if (tran != null) {
			propagation = tran.propagation().value();
			if (tran.timeout() > 0) {
				timeout = tran.timeout();
			}
		}
		return doScope(name, propagation, timeout, closure);
	}

	/**
	 * <pre>
	 * ó������ : Transaction ������ �����մϴ�.
	 * </pre>
	 * 
	 * @Method Name : doScope
	 * @param name
	 * @param propagation
	 * @param timeout
	 * @param closure
	 * @return
	 * @throws Exception
	 */
	private static Object doScope(final String name, final int propagation, final int timeout, final IClosure closure)
			throws Exception {
		try {
			// Method ������ ���, Transaction ������ �����մϴ�.
			if (MethodUtil.isScope()) {
				return doTranScope(name, propagation, timeout, closure);
			}

			// Method ������ �ƴ� ���, Method ���� �ȿ��� Transaction ������ �����մϴ�.
			return MethodUtil.doScope(name, new IClosure() {
				@Override
				public Object execute() throws Throwable {
					return doTranScope(name, propagation, timeout, closure);
				}
			});
		} catch (Exception e) {
			throw e;
		} catch (Throwable t) {
			throw ValueUtil.toGmes2Exception(t);
		}
	}
	private static Object doTranScope(String name, int propagation, int timeout, IClosure closure) throws Throwable {
		PlatformTransactionManager manager = getManager();

		/**
		 * Transaction ����
		 */
		TransactionStatus status = begin(manager, name, propagation, timeout);

		Object res = null;
		try {
			res = closure.execute();

			AnyframeDVO anyframeDVO = AdapterUtil.getAnyframeDVO();
			if (anyframeDVO != null && anyframeDVO.getRollbackYn() != null && "Y".equalsIgnoreCase(anyframeDVO.getRollbackYn())) {
				rollback(manager, status);
			} else {
				/**
				 * Transaction �Ϸ�
				 */
				commit(manager, status);
			}
		}
		/**
		 * ���� �� Transaction �ѹ�
		 */
		catch (Throwable t) {
			rollback(manager, status);
			throw t;
		}
		return res;
	}

	private static TransactionStatus begin(PlatformTransactionManager manager, DefaultTransactionDefinition definition)
			throws TransactionException {
		TransactionStatus status = manager.getTransaction(definition);
		put(manager, status, definition);
		return status;
	}
	private static TransactionStatus begin(PlatformTransactionManager manager, String name, int propagation, int timeout)
			throws TransactionException {
		if (manager == null) {
			throw new Gmes2BugException("manager is null.");
		}
		return begin(manager, getDefinition(name, propagation, timeout));
	}
	private static DefaultTransactionDefinition getDefinition(String name, int propagation, int timeout) {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setName(name);
		definition.setPropagationBehavior(propagation);
		// set readOnly
		if (ValueUtil.isNotEmpty(name)) {
			String methodName = name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : name;
			for (String prefix : MethodUtil.PREFIX_READONLY_LIST) {
				if (methodName.startsWith(prefix)) {
					definition.setReadOnly(true);
					break;
				}
			}
		}
		// set timeout
		definition.setTimeout(timeout);
		return definition;
	}
	private static void commit(PlatformTransactionManager manager, TransactionStatus status) throws Exception {
		// ���� ������ �� Transaction(���� commit �Ǳ� ����)�� �����̸�,
		// �� Ʈ����ǿ� ����� History ������ Batch �Է�
		if (isNewScope()) {
			HistoryUtil.insertBatch();
		}

		try {
			manager.commit(status);
		} finally {
			if (status.isCompleted() && status.equals(getStatus())) {
				pop();
			}
		}
	}
	private static void rollback(PlatformTransactionManager manager, TransactionStatus status) throws Exception {
		try {
			if (status.isCompleted()) {
				return;
			}
			manager.rollback(status);
		} finally {
			if (status.equals(getStatus())) {
				// ���� ������ �� Transaction(���� rollback �Ǳ� ����)�� �����̸�,
				// �� Ʈ����ǿ� ����� History ������ ����
				if (isNewScope()) {
					try {
						HistoryUtil.removeBatch();
					} catch (Exception e) {
					}
				}

				pop();
			}
		}
	}

	/**
	 * Transaction ID Stack �� ThreadProperty key
	 */
	private static final String PROP_ID_STACK = "TransactionUtil.idStack";
	/**
	 * Transaction Information Stack �� ThreadProperty key
	 */
	private static final String PROP_INFO_STACK = "TransactionUtil.infoStack";
	/**
	 * <pre>
	 * ó������ : Ʈ����� �������� ����
	 * </pre>
	 * 
	 * @Method Name : isTranScope
	 * @return
	 */
	public static boolean isScope() {
		return ValueUtil.isNotEmpty(ThreadPropertyUtil.get(PROP_INFO_STACK));
	}
	/**
	 * <pre>
	 * ó������ : ���� ������ Ʈ����� ���̵� ��ȯ�մϴ�.
	 * </pre>
	 * 
	 * @Method Name : getId
	 * @return
	 */
	public static String getId() {
		List<String> idStack = getIdStack();
		if (ValueUtil.isEmpty(idStack)) {
			return null;
		}
		return idStack.get(idStack.size() - 1);
	}
	@SuppressWarnings("unchecked")
	private static List<String> getIdStack() {
		if (!ThreadPropertyUtil.contains(PROP_ID_STACK)) {
			return null;
		}
		return (List<String>) ThreadPropertyUtil.get(PROP_ID_STACK);
	}
	@SuppressWarnings("unchecked")
	private static List<TransactionInfo> getInfoStack() {
		if (!ThreadPropertyUtil.contains(PROP_INFO_STACK)) {
			return null;
		}
		return (List<TransactionInfo>) ThreadPropertyUtil.get(PROP_INFO_STACK);
	}
	private static TransactionStatus getStatus() {
		TransactionInfo info = getInfo();
		return info == null ? null : info.getStatus();
	}
	private static TransactionDefinition getDefinition() {
		TransactionInfo info = getInfo();
		return info == null ? null : info.getDefinition();
	}
	private static TransactionInfo getInfo() {
		List<TransactionInfo> stack = getInfoStack();
		if (ValueUtil.isEmpty(stack)) {
			return null;
		}
		return stack.get(stack.size() - 1);
	}
	/**
	 * <pre>
	 * ó������ : ������ Ʈ����� ������ �� Ʈ����� ������ ��ȯ�մϴ�.
	 * </pre>
	 * 
	 * @Method Name : isNewTransaction
	 * @return
	 */
	private static boolean isNewScope() {
		TransactionInfo info = getInfo();
		return info == null || info.isNewTransaction() || getInfoStack().size() == 1;
	}
	/**
	 * <pre>
	 * ó������ : Ʈ����� ���� ������ ��ȯ�մϴ�.
	 * </pre>
	 * 
	 * @Method Name : getPropagationBehavior
	 * @return
	 */
	private static int getPropagationBehavior() {
		TransactionDefinition definition = getDefinition();
		return definition == null ? TransactionDefinition.PROPAGATION_REQUIRED : definition.getPropagationBehavior();
	}
	/**
	 * Transaction ID: ���� Ʈ����� ���̵� �ƴ϶� ���������� �����ϱ� ���� �ĺ��� �Դϴ�.
	 */
	private static long tranId;
	@SuppressWarnings("unchecked")
	private static void put(PlatformTransactionManager manager, TransactionStatus status, TransactionDefinition definition) {
		TransactionInfo info = new TransactionInfo();
		info.setManager(manager);
		info.setStatus(status);
		info.setDefinition(definition);
		List<TransactionInfo> stack = null;
		if (ThreadPropertyUtil.contains(PROP_INFO_STACK)) {
			stack = (List<TransactionInfo>) ThreadPropertyUtil.get(PROP_INFO_STACK);
		} else {
			stack = new ArrayList<TransactionInfo>();
			ThreadPropertyUtil.put(PROP_INFO_STACK, stack);
		}
		info.setNewTransaction(status.isNewTransaction() || stack.isEmpty());
		// �� Ʈ������� ������ Transaction ID �� put �ϹǷ� infoStack �� ��ĥ �� �����ϴ�.
		if (info.isNewTransaction()) {
			List<String> tranIdStack = null;
			if (ThreadPropertyUtil.contains(PROP_ID_STACK)) {
				tranIdStack = (List<String>) ThreadPropertyUtil.get(PROP_ID_STACK);
			} else {
				tranIdStack = new ArrayList<String>();
				ThreadPropertyUtil.put(PROP_ID_STACK, tranIdStack);
			}
			tranIdStack.add(tranId++ + "");
		}
		stack.add(info);
	}
	private static void pop() {
		List<TransactionInfo> stack = getInfoStack();
		if (ValueUtil.isEmpty(stack)) {
			return;
		}
		TransactionInfo info = stack.remove(stack.size() - 1);
		List<String> idStack = getIdStack();
		if (info.isNewTransaction() && ValueUtil.isNotEmpty(idStack)) {
			idStack.remove(idStack.size() - 1);
		}
	}
}

/**
 * <pre>
 * Ŭ���� ���� : Ʈ����� ����
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