package service;

import java.util.List;

import repository.entity.RefDataEntity;

public interface IRefDataService {
	RefDataEntity createRefData(RefDataEntity refData);

	boolean deleteRefDataFromType(String type);

	boolean deleteRefDataFromCode(String type, String code);

	List<RefDataEntity> getRefDataFromType(List<String> type);

	List<RefDataEntity> getAllRefData();
}
