package cn.cuiot.dmp.system.application.service;

/**
 * @author shixh
 * @version 1.0
 * @date 2022/11/24 10:42
 */
@FunctionalInterface
public interface DbExecution {
	/**
	 * 未加锁的删除会导致redis数据错误，所以去掉了这里的删除，必须要加锁后删除，取消双删操作。
	 */
	void execute();
}
