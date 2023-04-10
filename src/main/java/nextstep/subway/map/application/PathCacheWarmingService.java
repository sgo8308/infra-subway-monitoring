package nextstep.subway.map.application;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.map.domain.SectionEdge;
import nextstep.subway.map.domain.SubwayGraph;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathCacheWarmingService {

    @Autowired
    LineService lineService;

    @Autowired
    PathServiceForCaching pathServiceForCaching;

    @Autowired
    StationRepository stationRepository;

    private static final Logger log = LoggerFactory.getLogger("file");

    public void warmCache(int threadCount) throws InterruptedException {
        List<Station> stations = stationRepository.findAll();

        int chunk = stations.size() / threadCount;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int num = i;
            Thread t = new Thread(()->{doCaching(stations, chunk, num);});
            threads.add(t);
            t.start();
        }

        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }

        log.info("Finish Path cache warming");
    }

    private void doCaching(List<Station> stations, int chunk, int num){
        int start = chunk * num;
        int end = chunk * (num + 1);

        int left = stations.size() - end;

        if(left < chunk){
            end = stations.size();
        }

        SubwayGraph graph = createGraph(lineService.findLines());

        for (int i = 0; i < stations.size(); i++) {
            for (int j = start; j < end; j++) {
                pathServiceForCaching.findPath(graph, stations.get(i), stations.get(j));
            }
        }
    }

    private SubwayGraph createGraph(List<Line> lines) {
        SubwayGraph graph = new SubwayGraph(SectionEdge.class);
        graph.addVertexWith(lines);
        graph.addEdge(lines);

        return graph;
    }
}
