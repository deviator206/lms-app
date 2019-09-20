package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mapper.ModelMappers;
import model.RefDataRes;
import repository.entity.RefDataEntity;
import service.IRefDataService;

@RestController
public class RefDataController {
	@Autowired
	public IRefDataService refDataService;

	@GetMapping("/refdata")
	public List<RefDataRes> getRefData(@RequestParam(value = "type", required = false) String type) {
		List<RefDataEntity> refDataEtityLst = null;
		if (type != null && !type.isEmpty()) {
			List<String> typeList = Arrays.asList(type.split(",")); 
			refDataEtityLst = refDataService.getRefDataFromType(typeList);
		} else {
			refDataEtityLst = refDataService.getAllRefData();
		}
		List<RefDataRes> userResList = new ArrayList<RefDataRes>();
		for (RefDataEntity refDataEntity : refDataEtityLst) {
			RefDataRes refDataRes = new RefDataRes();
			ModelMappers.mapRefEntityToRefRes(refDataEntity, refDataRes);
			userResList.add(refDataRes);
		}
		return userResList;
	}

	@PostMapping("/refdata")
	public void addRefData(@RequestBody RefDataRes refDataRes) {
		RefDataEntity refDataEntity = new RefDataEntity();
		ModelMappers.mapRefResToRefEntity(refDataRes, refDataEntity);
		refDataService.createRefData(refDataEntity);
	}
}
