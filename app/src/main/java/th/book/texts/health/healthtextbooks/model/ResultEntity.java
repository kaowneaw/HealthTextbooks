package th.book.texts.health.healthtextbooks.model;

import java.util.List;

/**
 * Created by KaowNeaw on 12/27/2015.
 */
public class ResultEntity {

    private List<Refrigerator> resultsRefrigerator;
    private List<Matirial> resultsMatirial;
    private List<Order> resultsOrder;
    private List<String> results;
    boolean status;


    public List<Refrigerator> getResultsRefrigerator() {
        return resultsRefrigerator;
    }

    public void setResultsRefrigerator(List<Refrigerator> resultsRefrigerator) {
        this.resultsRefrigerator = resultsRefrigerator;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public List<Matirial> getResultsMatirial() {
        return resultsMatirial;
    }

    public void setResultsMatirial(List<Matirial> resultsMatirial) {
        this.resultsMatirial = resultsMatirial;
    }

    public List<Order> getResultsOrder() {
        return resultsOrder;
    }

    public void setResultsOrder(List<Order> resultsOrder) {
        this.resultsOrder = resultsOrder;
    }
}
