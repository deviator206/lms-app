package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
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

import consts.LeadManagementConstants;
import model.CreateRootLeadRes;
import model.DownloadFileRes;
import model.FilterLeadRes;
import model.LeadRes;
import model.LeadStatistictsRes;
import model.Pagination;
import model.RootLeadRes;
import model.UploadFileRes;
import security.JwtTokenReader;
import service.FileServices;
import service.ILeadDetailService;
import service.INotificationService;

@RestController
@Scope("prototype")
public class LeadDetailController {
	@Autowired
	public ILeadDetailService leadDetailService;

	@Autowired
	FileServices fileServices;

	@Autowired
	private JwtTokenReader jwtTokenReader;

	@Autowired
	INotificationService notificationService;

	@Value("${app.pushNotification.lead.create}")
	private Boolean leadCreateNotification;

	@Value("${app.pushNotification.lead.update}")
	private Boolean leadUpdateNotification;

	@GetMapping("/rootlead/{id}")
	public RootLeadRes getRootLead(@PathVariable("id") Long id) {
		return leadDetailService.getRootLead(id);
	}

	@PostMapping("/rootlead")
	public ResponseEntity<CreateRootLeadRes> addRootLead(@RequestHeader("Authorization") String autorizationHeader,
			@RequestBody RootLeadRes rootLeadRes) {
		CreateRootLeadRes createRootLeadRes = leadDetailService.createRootLead(rootLeadRes);
		if (leadCreateNotification) {
			Long userId = jwtTokenReader.getUserIdFromAuthHeader(autorizationHeader);
			notificationService.sendNotificationAfterLeadCreation(userId, createRootLeadRes.getLeads());
		}
		return new ResponseEntity<CreateRootLeadRes>(createRootLeadRes, HttpStatus.CREATED);
	}

	@GetMapping("/lead/{id}")
	public LeadRes getLead(@PathVariable("id") Long id) {
		return leadDetailService.getLead(id);
	}

	@GetMapping("/leads")
	public List<LeadRes> getLeads(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "desc", required = false) String description,
			@RequestParam(value = "leadtype", required = false, defaultValue = LeadManagementConstants.LEAD_TYPE_ALL) String leadType,
			@RequestParam(value = "userid", required = false) Long userid,
			@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "pagesize", required = false) Integer pageSize) {
		if ((name != null) && (!name.isEmpty()) || (description != null) && (!description.isEmpty())) {
			return leadDetailService.searchLeads(name, description);
		} else {
			if (start != null && pageSize != null) {
				return leadDetailService.getLeads(leadType, userid, new Pagination(start, pageSize));
			}
			return leadDetailService.getLeads(leadType, userid, null);
		}
	}

	@PutMapping("/lead/{id}")
	public Long updateLead(@RequestHeader("Authorization") String autorizationHeader, @PathVariable("id") Long id,
			@RequestBody LeadRes leadRes) {
		if (id != leadRes.getId()) {
			throw new RuntimeException("Bad Request. Ids in path and Resourse are not matching");
		}
		if (leadUpdateNotification) {
			Long userId = jwtTokenReader.getUserIdFromAuthHeader(autorizationHeader);
			notificationService.sendNotificationAfterLeadUpdate(userId, id);
		}
		return leadDetailService.updateLead(leadRes);
	}

	@PostMapping("/search/leads")
	public List<LeadRes> filterLeads(@RequestParam(value = "start", required = false) Integer start,
			@RequestParam(value = "pagesize", required = false) Integer pageSize,
			@RequestBody FilterLeadRes filterLeadRes) {

		if (start != null && pageSize != null) {
			return leadDetailService.filterLeads(filterLeadRes, new Pagination(start, pageSize));
		}
		return leadDetailService.filterLeads(filterLeadRes, null);
	}

	@PostMapping("/statistics/lead")
	public LeadStatistictsRes getLeadStatistics(
			@RequestParam(value = "busummary", required = false, defaultValue = "false") Boolean busummary,
			@RequestParam(value = "userid", required = false) Long userId, @RequestBody FilterLeadRes filterLeadRes) {
		return leadDetailService.getLeadStatistics(filterLeadRes, busummary, userId);
	}

	@PostMapping(value = "/report/lead", produces = "application/vnd.ms-excel")
	public ResponseEntity<InputStreamResource> getLeadStatisticsReport(
			@RequestParam(value = "busummary", required = false, defaultValue = "false") Boolean busummary,
			@RequestParam(value = "userid", required = false) Long userId, @RequestBody FilterLeadRes filterLeadRes)
			throws IOException {
		ByteArrayInputStream in = leadDetailService.getLeadStatisticsReport(filterLeadRes, busummary, userId);

		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();
		// headers.add("Content-Disposition", "attachment; filename=customers.xlsx");
		// change the file name
		headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/ms-excel"))
				.body(new InputStreamResource(in));
	}

	/*
	 * @GetMapping(value = "/report/file", produces = "application/vnd.ms-excel")
	 * public ResponseEntity<InputStreamResource> downloadFile() {
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.add("Content-Disposition",
	 * "attachment; filename=customers.xlsx");
	 * 
	 * return ResponseEntity.ok().headers(headers).body(new
	 * InputStreamResource(fileServices.loadFile())); }
	 */

	@PostMapping("/lead/attachment/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,
			@RequestParam(value = "userid", required = true) Long userId,
			@RequestParam(value = "leadid", required = true) List<Long> leadid) throws IOException {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity("You must select a file!", HttpStatus.OK);
		}
		UploadFileRes uploadFileRes = leadDetailService.uploadLeadAttachments(leadid, userId,
				Arrays.asList(uploadfile));

		return new ResponseEntity("Successfully uploaded - " + uploadFileRes.getFileName(), new HttpHeaders(),
				HttpStatus.OK);

	}

	@PostMapping("/lead/contact/upload")
	public ResponseEntity<?> uploadContactAttachment(@RequestParam("file") MultipartFile uploadfile,
			@RequestParam(value = "rootleadid", required = true) Long rootLeadId) throws IOException {
		if (uploadfile.isEmpty()) {
			return new ResponseEntity("You must select a file!", HttpStatus.OK);
		}
		UploadFileRes uploadFileRes = leadDetailService.uploadRootLeadContactAttachment(rootLeadId,
				Arrays.asList(uploadfile));

		return new ResponseEntity("Successfully uploaded - " + uploadFileRes.getFileName(), new HttpHeaders(),
				HttpStatus.OK);

	}

	@RequestMapping(path = "/lead/attachment/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "leadid", required = true) Long leadid) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		DownloadFileRes downloadFileRes = leadDetailService.downloadLeadAttachment(leadid, name);
		return ResponseEntity.ok().headers(headers).contentLength(downloadFileRes.getContentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(downloadFileRes.getFileResource());
	}

	@RequestMapping(path = "/lead/contact/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadContactAttachment(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "leadid", required = true) Long leadid) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		DownloadFileRes downloadFileRes = leadDetailService.downloadContactAttachment(leadid, name);
		return ResponseEntity.ok().headers(headers).contentLength(downloadFileRes.getContentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(downloadFileRes.getFileResource());
	}
}
