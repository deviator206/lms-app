package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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

import repository.entity.RootLeadEntity;
import repository.mapper.RootLeadRowMapper;

@Repository
@Scope("prototype")
public class RootLeadDAOImpl implements IRootLeadDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Override
	public RootLeadEntity getRootLead(Long id) {
		String sql = "SELECT ID, SOURCE,CUST_NAME,DESCRIPTION,CONTACT_ID,DELETED,CREATION_DATE,CREATOR_ID,BUDGET,CURRENCY,SELF_APPROVED,TENURE, SOURCE_INFO FROM ROOT_LEAD WHERE ID = ?";
		RowMapper<RootLeadEntity> rowMapper = new RootLeadRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { id });
	}

	@Override
	public Long insertRootLead(RootLeadEntity rootLeadEntity) {
		TransactionStatus ts = transactionManager.getTransaction(new DefaultTransactionDefinition());
		Long id = null;
		try {
			String sql = "INSERT INTO ROOT_LEAD (SOURCE,CUST_NAME,DESCRIPTION,CONTACT_ID,CREATION_DATE,CREATOR_ID,SELF_APPROVED,BUDGET,CURRENCY,TENURE,SALES_REP, SOURCE_INFO) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, rootLeadEntity.getSource());
					ps.setString(2, rootLeadEntity.getCustName());
					ps.setString(3, rootLeadEntity.getDescription());

					if (rootLeadEntity.getContactId() != null) {
						ps.setLong(4, rootLeadEntity.getContactId());
					} else {
						ps.setNull(4, Types.FLOAT);
					}

					ps.setDate(5, rootLeadEntity.getCreationDate());

					if (rootLeadEntity.getCreatorId() != null) {
						ps.setLong(6, rootLeadEntity.getCreatorId());
					} else {
						ps.setNull(6, Types.FLOAT);
					}

					ps.setBoolean(7, rootLeadEntity.isSelfApproved());
					ps.setFloat(8, rootLeadEntity.getBudget());
					ps.setString(9, rootLeadEntity.getCurrency());
					ps.setString(10, rootLeadEntity.getTenure());
					ps.setString(11, rootLeadEntity.getSalesRep());
					ps.setString(12, rootLeadEntity.getSourceInfo());
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
	public boolean deleteRootLead(Long id) {
		String sql = "DELETE FROM ROOT_LEAD WHERE ID = ?";
		jdbcTemplate.update(sql, id);
		return true;
	}

	@Override
	public void updateRootLead(RootLeadEntity rootLeadEntity) {
		String sql = "UPDATE ROOT_LEAD SET CONTACT_ID = ?, WHERE ID = ?;";
		jdbcTemplate.update(sql, rootLeadEntity.getContactId(), rootLeadEntity.getId());
	}
}
