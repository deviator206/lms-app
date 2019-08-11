package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.LeadEntity;

public class LeadRowMapper implements RowMapper<LeadEntity> {

	@Override
	public LeadEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		LeadEntity leadEntity = new LeadEntity();
		leadEntity.setId(rs.getLong("ID"));
		leadEntity.setBusinessUnit(rs.getString("BU"));
		leadEntity.setStatus(rs.getString("STATUS"));
		leadEntity.setSalesRep(rs.getString("SALES_REP"));
		leadEntity.setRootLeadId(rs.getLong("ROOT_ID"));
		leadEntity.setDeleted(rs.getBoolean("DELETED"));
		leadEntity.setCreationDate(rs.getDate("CREATION_DATE"));
		leadEntity.setUpdateDate(rs.getDate("UPDATE_DATE"));
		leadEntity.setCreatorId(rs.getLong("CREATOR_ID"));
		leadEntity.setUpdatorId(rs.getLong("UPDATOR_ID"));
		leadEntity.setBudget(rs.getLong("BUDGET"));
		leadEntity.setCurrency(rs.getString("CURRENCY"));
		leadEntity.setMessage(rs.getString("MESSAGE"));
		return leadEntity;
	}

}
