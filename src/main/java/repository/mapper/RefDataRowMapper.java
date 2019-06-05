package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.RefDataEntity;

public class RefDataRowMapper implements RowMapper<RefDataEntity> {

	@Override
	public RefDataEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RefDataEntity refData = new RefDataEntity();
		refData.setCode(rs.getString("CODE"));
		refData.setName(rs.getString("NAME"));
		refData.setType(rs.getString("TYPE"));
		return refData;
	}

}
