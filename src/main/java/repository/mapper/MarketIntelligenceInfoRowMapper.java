package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.MarketIntelligenceInfoEntity;

public class MarketIntelligenceInfoRowMapper implements RowMapper<MarketIntelligenceInfoEntity> {

	@Override
	public MarketIntelligenceInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MarketIntelligenceInfoEntity marketingIntelligenceEntity = new MarketIntelligenceInfoEntity();
		marketingIntelligenceEntity.setId(rs.getLong("ID"));
		marketingIntelligenceEntity.setMiId(rs.getLong("MI_ID"));
		marketingIntelligenceEntity.setInfo(rs.getString("INFO"));
		marketingIntelligenceEntity.setCreationDate(rs.getDate("CREATION_DATE"));
		return marketingIntelligenceEntity;
	}

}
