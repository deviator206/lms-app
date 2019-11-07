package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.EncodeDecode;
import service.IUtilityService;

@RestController
@Scope("prototype")
public class UtilityServicesController {
	@Autowired
	public IUtilityService utilityService;

	@PostMapping("/encode")
	public String encode(@RequestBody EncodeDecode encodeDecode) {
		return utilityService.encode(encodeDecode.getDecoded());
	}

	// @PostMapping("/decode")
	public String decode(@RequestBody EncodeDecode encodeDecode) {
		return utilityService.decode(encodeDecode.getEncoded());
	}
}
