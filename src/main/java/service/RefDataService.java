package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import mapper.ModelMappers;
import model.RefDataRes;
import repository.IRefDataDAO;
import repository.entity.RefDataEntity;

@Service
@Scope("prototype")
public class RefDataService implements IRefDataService {

	@Autowired
	private IRefDataDAO refDataDAO;

	@Override
	public void createRefData(RefDataEntity refData) {
		refDataDAO.insertRefData(refData);
	}

	@Override
	public void createRefDataLst(List<RefDataRes> refDataLst) {
		RefDataEntity refDataEntity = null;
		for (RefDataRes RefDataRes : refDataLst) {
			refDataEntity = new RefDataEntity();
			ModelMappers.mapRefResToRefEntity(RefDataRes, refDataEntity);
			refDataDAO.insertRefData(refDataEntity);
		}
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
	public List<RefDataEntity> getRefDataFromType(List<String> type) {
		return refDataDAO.getRefDataFromType(type);
	}

	@Override
	public List<RefDataEntity> getAllRefData() {
		return refDataDAO.getAllRefData();
	}

}
