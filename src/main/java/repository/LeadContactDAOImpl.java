package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import repository.entity.LeadContactEntity;
import repository.mapper.LeadContactRowMapper;

@Repository
@Scope("prototype")
public class LeadContactDAOImpl implements ILeadContactDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public Long insertLeadContact(LeadContactEntity leadContactEntity) {
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Long id = null;
		try {
			String sql = "INSERT INTO LEAD_CONTACT (NAME,EMAIL,PHONE,COUNTRY,STATE, DESIGNATION) VALUES (?,?,?,?,?,?);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, leadContactEntity.getName());
					ps.setString(2, leadContactEntity.getEmail());
					ps.setString(3, leadContactEntity.getPhoneNumber());
					ps.setString(4, leadContactEntity.getCountry());
					ps.setString(5, leadContactEntity.getState());
					ps.setString(6, leadContactEntity.getDesignation());
					return ps;
				}
			}, keyHolder);
			transactionManager.commit(ts);
			id = keyHolder.getKey().longValue();
		} catch (Exception e) {
			transactionManager.rollback(ts);
			throw new RuntimeException(e);
		}

		return id;
	}

	@Override
	public LeadContactEntity getLeadContact(Long id) {
		String sql = "SELECT ID, NAME,EMAIL,PHONE,COUNTRY,STATE,DESIGNATION FROM LEAD_CONTACT WHERE ID = ?";
		RowMapper<LeadContactEntity> rowMapper = new LeadContactRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { id });
	}

	@Override
	public boolean deleteLeadContact(Long id) {
		String sql = "DELETE FROM LEAD_CONTACT WHERE ID = ?";
		jdbcTemplate.update(sql, id);
		return true;
	}
	
	@Override
	public boolean updateLeadContact(LeadContactEntity leadContactEntity) {
		String sql = "UPDATE LEAD_CONTACT SET ATTACHMENT = ? WHERE ID = ?;";
		int affectedRows = jdbcTemplate.update(sql, leadContactEntity.getAttachment(), leadContactEntity.getId());
		return affectedRows == 0 ? false : true;
	}

}
