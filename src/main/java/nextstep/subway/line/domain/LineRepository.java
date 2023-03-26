package nextstep.subway.line.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Override
    @Query("select distinct l from Line l join fetch l.sections s join fetch s.upStation join fetch s.downStation")
    List<Line> findAll();
}
