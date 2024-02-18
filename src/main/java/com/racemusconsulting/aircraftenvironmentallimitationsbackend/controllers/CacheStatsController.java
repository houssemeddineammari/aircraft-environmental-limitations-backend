package com.racemusconsulting.aircraftenvironmentallimitationsbackend.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/cache")
public class CacheStatsController {

	private final CacheManager cacheManager;

	@Autowired
	public CacheStatsController(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@GetMapping("/stats")
	public Map<String, Object> getCacheStats() {
		Map<String, Object> stats = new HashMap<>();
		CaffeineCache cache = (CaffeineCache) cacheManager.getCache("temperatureDeviations");
		if (cache != null) {
			com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = cache.getNativeCache();
			stats.put("temperatureDeviationsStats", nativeCache.stats().toString());
		}
		return stats;
	}
}
