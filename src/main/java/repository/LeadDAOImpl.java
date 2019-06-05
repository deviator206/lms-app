package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import repository.entity.LeadEntity;
import repository.mapper.LeadRowMapper;

@Repository
public class LeadDAOImpl implements ILeadDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public LeadEntity getLead(Long id) {
		String sql = "SELECT ID, BU,SALES_REP,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID FROM LEADS WHERE ID = ?";
		RowMapper<LeadEntity> rowMapper = new LeadRowMapper();
		return this.jdbcTemplate.queryForObject(sql, rowMapper, new Object[] { id });
	}

	@Override
	public Long insertLead(LeadEntity leadEntity) {
		Long id = null;
		String sql = "INSERT INTO LEADS (BU,STATUS,ROOT_ID,DELETED,CREATION_DATE,CREATOR_ID,UPDATE_DATE,UPDATOR_ID,SALES_REP) \r\n"
				+ "VALUES (?,?,?,?,?,?,?,?,?);";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, leadEntity.getBusinessUnit());
				ps.setString(2, leadEntity.getStatus());
				ps.setLong(3, leadEntity.getRootLeadId());
				ps.setBoolean(4, leadEntity.isDeleted());
				ps.setDate(5, leadEntity.getCreationDate());
				ps.setLong(6, leadEntity.getCreatorId());
				ps.setDate(7, leadEntity.getUpdateDate());
				ps.setLong(8, leadEntity.getUpdatorId());
				ps.setString(9, leadEntity.getSalesRep());
				return ps;
			}
		}, keyHolder);
		id = keyHolder.getKey().longValue();
		return id;
	}

	@Override
	public boolean deleteLead(Long id) {
		String sql = "DELETE FROM LEADS WHERE ID = ?";
		int affectedRows = jdbcTemplate.update(sql, id);
		return affectedRows == 0 ? false : true;
	}

	@Override
	public boolean updateLead(LeadEntity leadEntity) {
		String sql = "UPDATE LEADS SET STATUS = ? WHERE ID= ?;";
		int affectedRows = jdbcTemplate.update(sql, leadEntity.getStatus(), leadEntity.getId());
		return affectedRows == 0 ? false : true;
	}

}
