package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.RootLeadEntity;

public class RootLeadRowMapper implements RowMapper<RootLeadEntity> {

	@Override
	public RootLeadEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RootLeadEntity rootLeadEntity = new RootLeadEntity();
		rootLeadEntity.setId(rs.getLong("ID"));
		rootLeadEntity.setSource(rs.getString("SOURCE"));
		rootLeadEntity.setCustName(rs.getString("CUST_NAME"));
		rootLeadEntity.setDescription(rs.getString("DESCRIPTION"));
		
		if (rs.getObject("CONTACT_ID") != null) {
			rootLeadEntity.setContactId(rs.getLong("CONTACT_ID"));
		}
		
		rootLeadEntity.setDeleted(rs.getBoolean("DELETED"));
		rootLeadEntity.setCreationDate(rs.getDate("CREATION_DATE"));
		
		if (rs.getObject("CREATOR_ID") != null) {
			rootLeadEntity.setCreatorId(rs.getLong("CREATOR_ID"));
		}				
		
		if (rs.getObject("BUDGET") != null) {
			rootLeadEntity.setBudget(rs.getLong("BUDGET"));
		}		
	
		rootLeadEntity.setCurrency(rs.getString("CURRENCY"));
		rootLeadEntity.setSelfApproved(rs.getBoolean("SELF_APPROVED"));
		rootLeadEntity.setTenure(rs.getString("TENURE"));
		rootLeadEntity.setSourceInfo(rs.getString("SOURCE_INFO"));
		return rootLeadEntity;
	}

}
