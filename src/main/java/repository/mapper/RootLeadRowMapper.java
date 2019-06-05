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
		rootLeadEntity.setContactId(rs.getLong("CONTACT_ID"));
		rootLeadEntity.setDeleted(rs.getBoolean("DELETED"));
		rootLeadEntity.setCreationDate(rs.getDate("CREATION_DATE"));
		rootLeadEntity.setCreatorId(rs.getLong("CREATOR_ID"));
		return rootLeadEntity;
	}

}
