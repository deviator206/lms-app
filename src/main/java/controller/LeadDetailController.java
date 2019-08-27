package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import consts.LeadManagementConstants;
import model.FilterLeadRes;
import model.LeadRes;
import model.LeadStatistictsRes;
import model.RootLeadRes;
import service.ILeadDetailService;

@RestController
public class LeadDetailController {
	@Autowired
	public ILeadDetailService leadDetailService;

	@GetMapping("/rootlead/{id}")
	public RootLeadRes getRootLead(@PathVariable("id") Long id) {
		return leadDetailService.getRootLead(id);
	}

	@PostMapping("/rootlead")
	public Long addRootLead(@RequestBody RootLeadRes rootLeadRes) {
		return leadDetailService.createRootLead(rootLeadRes);
	}

	@GetMapping("/lead/{id}")
	public LeadRes getLead(@PathVariable("id") Long id) {
		return leadDetailService.getLead(id);
	}

	@GetMapping("/leads")
	public List<LeadRes> getLeads(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "desc", required = false) String description,
			@RequestParam(value = "leadtype", required = false, defaultValue = LeadManagementConstants.LEAD_TYPE_ALL) String leadType,
			@RequestParam(value = "userid", required = false) Long userid) {
		if ((name != null) && (!name.isEmpty()) || (description != null) && (!description.isEmpty())) {
			return leadDetailService.searchLeads(name, description);
		} else {
			return leadDetailService.getLeads(leadType, userid);
		}
	}

	@PutMapping("/lead/{id}")
	public Long updateLead(@PathVariable("id") Long id, @RequestBody LeadRes leadRes) {
		if (id != leadRes.getId()) {
			throw new RuntimeException("Bad Request. Ids in path and Resourse are not matching");
		}
		return leadDetailService.updateLead(leadRes);
	}

	@PostMapping("/search/leads")
	public List<LeadRes> filterLeads(@RequestBody FilterLeadRes filterLeadRes) {
		return leadDetailService.filterLeads(filterLeadRes);
	}

	@PostMapping("/statistics/lead")
	public LeadStatistictsRes getLeadStatistics(
			@RequestParam(value = "busummary", required = false, defaultValue = "false") Boolean busummary,
			@RequestParam(value = "userid", required = false) Long userId, @RequestBody FilterLeadRes filterLeadRes) {
		return leadDetailService.getLeadStatistics(filterLeadRes,busummary, userId);
	}
}
