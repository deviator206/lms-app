package repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import repository.entity.MarketIntelligenceEntity;

public class MarketIntelligenceRowMapper implements RowMapper<MarketIntelligenceEntity> {

	@Override
	public MarketIntelligenceEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MarketIntelligenceEntity marketingIntelligenceEntity = new MarketIntelligenceEntity();
		marketingIntelligenceEntity.setId(rs.getLong("ID"));
		marketingIntelligenceEntity.setType(rs.getString("TYPE"));
		marketingIntelligenceEntity.setStatus(rs.getString("STATUS"));
		marketingIntelligenceEntity.setName(rs.getString("NAME"));
		if (rs.getObject("LEAD_ID") != null) {
			marketingIntelligenceEntity.setLeadId(rs.getLong("LEAD_ID"));
		}

		if (rs.getObject("INVESTMENT") != null) {
			marketingIntelligenceEntity.setInvestment(rs.getDouble("INVESTMENT"));
		}
		marketingIntelligenceEntity.setDescription(rs.getString("DESCRIPTION"));
		marketingIntelligenceEntity.setCreationDate(rs.getDate("CREATION_DATE"));
		if (rs.getObject("CREATOR_ID") != null) {
			marketingIntelligenceEntity.setCreatorId(rs.getLong("CREATOR_ID"));
		}
		marketingIntelligenceEntity.setUpdateDate(rs.getDate("UPDATE_DATE"));
		if (rs.getObject("UPDATOR_ID") != null) {
			marketingIntelligenceEntity.setUpdatorId(rs.getLong("UPDATOR_ID"));
		}
		marketingIntelligenceEntity.setAttachment(rs.getString("ATTACHMENT"));
		return marketingIntelligenceEntity;
	}

}
