package daintiness.patterns;

import java.util.List;

import daintiness.clustering.Phase;
import daintiness.clustering.measurements.ChartGroupPhaseMeasurement;
import daintiness.models.PatternData;
import javafx.collections.ObservableList;


public interface IPatternManager {

	List<PatternData> getPatterns(ObservableList<ChartGroupPhaseMeasurement> totalValues, List<Phase> phases);
}
