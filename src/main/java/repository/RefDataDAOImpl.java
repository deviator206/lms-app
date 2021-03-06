package repository;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import repository.entity.RefDataEntity;
import repository.mapper.RefDataRowMapper;

@Repository
@Scope("prototype")
public class RefDataDAOImpl implements IRefDataDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public RefDataEntity insertRefData(RefDataEntity refData) {
		String sql = "INSERT INTO REF_DATA (CODE,NAME,TYPE) VALUES (?,?,?)";
		jdbcTemplate.update(sql, refData.getCode(), refData.getName(),refData.getType());
		return refData;
	}

	@Override
	public boolean deleteRefDataFromType(String type) {
		String sql = "DELETE FROM REF_DATA WHERE TYPE = ?";
		jdbcTemplate.update(sql, type);
		return true;
	}

	@Override
	public boolean deleteRefDataFromCode(String type, String code) {
		String sql = "DELETE FROM REF_DATA WHERE TYPE = ? AND CODE = ?";
		jdbcTemplate.update(sql, type, code);
		return true;
	}

	@Override
	public List<RefDataEntity> getRefDataFromType(List<String> type) {
		
		RowMapper<RefDataEntity> rowMapper = new RefDataRowMapper();
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
	    String sql = "SELECT CODE, NAME, TYPE FROM REF_DATA WHERE TYPE IN(:params)";
	    List<RefDataEntity> result = namedParameterJdbcTemplate.query(sql, Collections.singletonMap("params", type), rowMapper);
	    return result;
	}

	@Override
	public List<RefDataEntity> getAllRefData() {
		String sql = "SELECT CODE, NAME, TYPE FROM REF_DATA";
		RowMapper<RefDataEntity> rowMapper = new RefDataRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}
}
