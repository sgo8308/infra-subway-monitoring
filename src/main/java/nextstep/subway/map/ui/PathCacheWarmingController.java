package nextstep.subway.map.ui;

import nextstep.subway.map.application.PathCacheWarmingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathCacheWarmingController {

    @Autowired
    PathCacheWarmingService pathCacheWarmingService;

    public PathCacheWarmingController(PathCacheWarmingService pathCacheWarmingService) {
        this.pathCacheWarmingService = pathCacheWarmingService;
    }

    @GetMapping("/warm-path-cache")
    public void cacheMulti2(@RequestParam int threadCount) throws InterruptedException {
        pathCacheWarmingService.warmCache(threadCount);
    }
}


