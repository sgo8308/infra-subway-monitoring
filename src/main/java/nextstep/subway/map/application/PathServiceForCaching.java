package nextstep.subway.map.application;

import nextstep.subway.map.application.PathService;
import nextstep.subway.map.domain.SectionEdge;
import nextstep.subway.map.domain.SubwayGraph;
import nextstep.subway.map.domain.SubwayPath;
import nextstep.subway.map.dto.PathResponse;
import nextstep.subway.map.dto.PathResponseAssembler;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PathServiceForCaching {

    @Autowired
    PathService pathService;

    @Cacheable(cacheNames = "path", key = "#source.id + ',' + #target.id")
    public PathResponse findPath(SubwayGraph graph, Station source, Station target){
        // 다익스트라 최단 경로 찾기
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath<Station, SectionEdge> path = dijkstraShortestPath.getPath(source, target);

        SubwayPath subwayPath = pathService.convertSubwayPath(path);

        return PathResponseAssembler.assemble(subwayPath);
    }
}
