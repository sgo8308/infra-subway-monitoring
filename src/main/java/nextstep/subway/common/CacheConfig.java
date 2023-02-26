package nextstep.subway.common;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    private long refreshTime = 7200L;

    @Bean
    public CacheManager centralCache() {
        SimpleCacheManager manager = new SimpleCacheManager();
        List<CaffeineCache> caches = caffeineCaches();
        manager.setCaches(caches);
        return manager;
    }

    private List<CaffeineCache> caffeineCaches() {
        List<CaffeineCache> caches = new ArrayList<>();
        caches.add(new CaffeineCache("lines",
                Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(refreshTime, TimeUnit.SECONDS)
                        .build()));

        caches.add(new CaffeineCache("station",
                Caffeine.newBuilder()
                        .recordStats()
                        .expireAfterWrite(refreshTime, TimeUnit.SECONDS)
                        .build()));
        return caches;
    }

}
