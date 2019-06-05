package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.IRefDataDAO;
import repository.entity.RefDataEntity;

@Service
public class RefDataService implements IRefDataService {

	@Autowired
	private IRefDataDAO refDataDAO;

	@Override
	public RefDataEntity createRefData(RefDataEntity refData) {
		return refDataDAO.insertRefData(refData);
	}

	@Override
	public boolean deleteRefDataFromType(String type) {
		return refDataDAO.deleteRefDataFromType(type);
	}

	@Override
	public boolean deleteRefDataFromCode(String type, String code) {
		return refDataDAO.deleteRefDataFromCode(type, code);
	}

	@Override
	public List<RefDataEntity> getRefDataFromType(String type) {
		return refDataDAO.getRefDataFromType(type);
	}

	@Override
	public List<RefDataEntity> getAllRefData() {
		return refDataDAO.getAllRefData();
	}

}