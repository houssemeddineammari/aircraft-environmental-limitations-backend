package com.racemusconsulting.aircraftenvironmentallimitationsbackend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CacheStatsController.class)
public class CacheStatsControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        CaffeineCache cache = Mockito.mock(CaffeineCache.class);
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = Mockito.mock(com.github.benmanes.caffeine.cache.Cache.class);
        
        when(cache.getNativeCache()).thenReturn(nativeCache);
        when(cacheManager.getCache("temperatureDeviations")).thenReturn(cache);
        when(nativeCache.stats()).thenReturn(com.github.benmanes.caffeine.cache.stats.CacheStats.of(
            1L, // hitCount
            2L, // missCount
            3L, // loadSuccessCount
            4L, // loadFailureCount
            5L, // totalLoadTime
            6L, // evictionCount
            7L  // evictionWeight
        ));
    }

    @Test
    public void getCacheStats_ShouldReturnCacheStatistics() throws Exception {
        mockMvc.perform(get("/admin/cache/stats"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.temperatureDeviationsStats").value(
                   "CacheStats{hitCount=1, missCount=2, loadSuccessCount=3, loadFailureCount=4, " +
                   "totalLoadTime=5, evictionCount=6, evictionWeight=7}"));
    }
}