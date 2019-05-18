package ppztw.AdvertBoard.View;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatsView {
    Map<Integer, Integer> monthAdvertReportsCount;
    Map<Integer, Integer> monthReportedAdvertsCount;
    Integer allReportedAdvertsCount;
    Integer todayAdvertReportsCount;
    Integer todayReportedAdvertsCount;
}
