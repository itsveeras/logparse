package com.ef.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ef.entity.LogEntry;

/**
 * @author Veeramani S
 *
 */
@Repository
@Transactional
public class LogJpaRepository  {

	// connect to the database
	@PersistenceContext
	EntityManager entityManager;
	
	@Value("${db.persist.flush.size}")
	private int flushSize;
	
	@Autowired
	private EntityManagerFactory emf;
	
	public LogEntry getTheLatestEntry(){
		TypedQuery<LogEntry> namedQuery = entityManager.createNamedQuery("find_latest_log", LogEntry.class);
		List<LogEntry> result = namedQuery.getResultList();
		if(result.isEmpty()){
			return null;
		} else {
		return result.get(0);
		}
	}

	public LogEntry insert(LogEntry logEntry) {
		return entityManager.merge(logEntry);
	}

	/**
	 * 
	 * @param logEntries
	 */
	public void save(List<LogEntry> logEntries) {
		for (LogEntry log : logEntries) {
			entityManager.persist(log);
		}
	}

	public List<String> findIpsReachedThreshold(String startTime, String endTime, long threshold) {
		try {
		TypedQuery<String> namedQuery = entityManager.createNamedQuery("find_ips_reached_threshold", String.class);
		namedQuery.setParameter("startTime", startTime);
		namedQuery.setParameter("endTime", endTime);
		namedQuery.setParameter("threshold", threshold);
		return namedQuery.getResultList();
		} catch (Exception e){
			System.out.println((e.getStackTrace()));
		}
		return null;
	}

}