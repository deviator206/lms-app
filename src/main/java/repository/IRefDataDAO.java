package repository;

import java.util.List;

import repository.entity.RefDataEntity;

public interface IRefDataDAO {
	RefDataEntity insertRefData(RefDataEntity refData);

	boolean deleteRefDataFromType(String type);
	boolean deleteRefDataFromCode(String type, String code);

	List<RefDataEntity> getRefDataFromType(List<String> type);

	List<RefDataEntity> getAllRefData();
}
