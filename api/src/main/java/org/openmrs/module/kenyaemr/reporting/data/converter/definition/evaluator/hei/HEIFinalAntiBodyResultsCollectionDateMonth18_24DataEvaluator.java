package org.openmrs.module.kenyaemr.reporting.data.converter.definition.evaluator.hei;

import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEIFinalAntiBodyResultsCollectionDateMonth18_24DataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.hei.HEISerialNumberDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a PersonDataDefinition
 */
@Handler(supports= HEIFinalAntiBodyResultsCollectionDateMonth18_24DataDefinition.class, order=50)
public class HEIFinalAntiBodyResultsCollectionDateMonth18_24DataEvaluator implements PersonDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        String qry = "select\n" +
                "       f.patient_id,\n" +
                "       f.final_antibody_result_date as 24_months_results_collection_date\n" +
                "from  kenyaemr_etl.etl_hei_follow_up_visit f\n" +
                "        INNER JOIN kenyaemr_etl.etl_patient_demographics d ON\n" +
                "        f.patient_id = d.patient_id\n" +
                "WHERE timestampdiff(month,d.DOB,f.visit_date) BETWEEN 18 AND 24\n" +
                "GROUP BY f.patient_id;";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}