package service;

import java.util.List;

import model.RefDataRes;
import repository.entity.RefDataEntity;

public interface IRefDataService {
	void createRefData(RefDataEntity refData);

	boolean deleteRefDataFromType(String type);

	boolean deleteRefDataFromCode(String type, String code);

	List<RefDataEntity> getRefDataFromType(List<String> type);

	List<RefDataEntity> getAllRefData();
}
