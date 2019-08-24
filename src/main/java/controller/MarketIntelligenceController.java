package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.FilterMarketIntelligenceRes;
import model.MarketIntelligenceRes;
import service.IMarketIntelligenceService;

@RestController
public class MarketIntelligenceController {
	@Autowired
	public IMarketIntelligenceService marketIntelligenceService;

	@GetMapping("/marketIntelligence")
	public List<MarketIntelligenceRes> getMarketIntelligence() {
		List<MarketIntelligenceRes> marketIntelligenceLst = marketIntelligenceService.getMarketIntelligence();
		return marketIntelligenceLst;
	}

	@GetMapping("/marketIntelligence/{id}")
	public MarketIntelligenceRes getMarketIntelligenceById(@PathVariable("id") Long id) {
		return marketIntelligenceService.getMarkByIdetIntelligenceById(id);
	}

	@PostMapping("/marketIntelligence/{id}")
	public Long updateMarketIntelligence(@PathVariable("id") Long id,
			@RequestBody MarketIntelligenceRes marketIntelligenceRes) {
		return marketIntelligenceService.updateMarketIntelligence(marketIntelligenceRes);
	}

	@PostMapping("/marketIntelligence")
	public Long addMarketIntelligence(@RequestBody MarketIntelligenceRes marketIntelligenceRes) {
		return marketIntelligenceService.addMarketIntelligence(marketIntelligenceRes);
	}

	@PostMapping("/search/marketIntelligence")
	public List<MarketIntelligenceRes> filterMarketIntelligence(@RequestBody FilterMarketIntelligenceRes filterMarketIntelligence) {
		return marketIntelligenceService.filterMarketIntelligence(filterMarketIntelligence);
	}
	
}
