package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import model.DownloadFileRes;
import model.FilterMarketIntelligenceRes;
import model.MarketIntelligenceInfoRes;
import model.MarketIntelligenceReq;
import model.MarketIntelligenceRes;
import model.Pagination;
import model.UploadFileRes;
import security.JwtTokenReader;
import service.IFileStorageService;
import service.IMarketIntelligenceService;
import service.INotificationService;

@RestController
@Scope("prototype")
public class MarketIntelligenceController {
	@Autowired
	public IMarketIntelligenceService marketIntelligenceService;
	
	@Autowired
	private JwtTokenReader jwtTokenReader;

	@Autowired
	INotificationService notificationService;

	@Autowired
	IFileStorageService fileStorageService;

	@GetMapping("/marketIntelligence")
	public List<MarketIntelligenceRes> getMarketIntelligence(
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "pagesize", required = false) Integer pageSize) {
		if (start != null && pageSize != null) {
			return marketIntelligenceService.getMarketIntelligence(new Pagination(start, pageSize));
		}
		return marketIntelligenceService.getMarketIntelligence(null);
	}

	@GetMapping("/marketIntelligence/{id}")
	public MarketIntelligenceRes getMarketIntelligenceById(@PathVariable("id") Long id,@RequestParam(value = "infosize", required = false) Integer infoSize) {		
		return marketIntelligenceService.getMarkByIdetIntelligenceById(id,new Pagination(1, infoSize));
	}

	@GetMapping("/marketIntelligence/{id}/marketIntelligenceInfo")
	public List<MarketIntelligenceInfoRes> getMarketIntelligenceInfoByMiId(@PathVariable("id") Long miId,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "pagesize", required = false) Integer pageSize) {
		if (start != null && pageSize != null) {
			return marketIntelligenceService.getMarketIntelligenceInfo(miId,new Pagination(start, pageSize));
		}
		return marketIntelligenceService.getMarketIntelligenceInfo(miId,null);
		
	}

	@ApiOperation(value = "Update Market Intelligence. Only adding Market Intelligence infos is supported ", response = Long.class)
	@PutMapping("/marketIntelligence/{id}")
	public Long updateMarketIntelligence(@RequestHeader("Authorization") String autorizationHeader, @PathVariable("id") Long id,
			@RequestBody MarketIntelligenceReq marketIntelligenceRes) {
		Long miId = marketIntelligenceService.updateMarketIntelligence(marketIntelligenceRes);
		
		
		if(marketIntelligenceRes.getRootLeadId() != null) {
			Long userId = jwtTokenReader.getUserIdFromAuthHeader(autorizationHeader);
			notificationService.sendNotificationAfterMiToLeadCreation(userId, marketIntelligenceRes.getRootLeadId());
		}
		
		return miId;
	}

	@PostMapping("/marketIntelligence")
	public Long addMarketIntelligence(@RequestBody MarketIntelligenceReq marketIntelligenceReq) {
		return marketIntelligenceService.addMarketIntelligence(marketIntelligenceReq);
	}

	@PostMapping("/search/marketIntelligence")
	public List<MarketIntelligenceRes> filterMarketIntelligence(
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "pagesize", required = false) Integer pageSize,
			@RequestBody FilterMarketIntelligenceRes filterMarketIntelligence) {
		if (start != null && pageSize != null) {
			return marketIntelligenceService.filterMarketIntelligence(filterMarketIntelligence,
					new Pagination(start, pageSize));
		}
		return marketIntelligenceService.filterMarketIntelligence(filterMarketIntelligence, null);
	}

	@PostMapping("/marketIntelligence/attachment/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,
			@RequestParam(value = "userid", required = true) Long userId,
			@RequestParam(value = "id", required = true) Long id) {// TODO change id to miid
		if (uploadfile.isEmpty()) {
			return new ResponseEntity("You must select a file!", HttpStatus.OK);
		}
		try {

			UploadFileRes uploadFileRes = marketIntelligenceService.uploadMiAttachment(id, userId,
					Arrays.asList(uploadfile));

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity("Successfully uploaded - " + uploadfile.getOriginalFilename(), new HttpHeaders(),
				HttpStatus.OK);

	}

	@RequestMapping(path = "/marketIntelligence/attachment/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "miid", required = true) Long miId) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		DownloadFileRes downloadFileRes = marketIntelligenceService.downloadMiAttachment(miId, name);
		return ResponseEntity.ok().headers(headers).contentLength(downloadFileRes.getContentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(downloadFileRes.getFileResource());
	}

}
