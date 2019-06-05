package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.LeadContactEntity;

public class LeadContactRowMapper implements RowMapper<LeadContactEntity> {

	@Override
	public LeadContactEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		LeadContactEntity leadContactEntity = new LeadContactEntity();
		leadContactEntity.setId(rs.getLong("ID"));
		leadContactEntity.setName(rs.getString("NAME"));
		leadContactEntity.setEmail(rs.getString("EMAIL"));
		leadContactEntity.setPhoneNumber(rs.getString("PHONE"));
		leadContactEntity.setCountry(rs.getString("COUNTRY"));
		leadContactEntity.setState(rs.getString("STATE"));
		return leadContactEntity;
	}

}
