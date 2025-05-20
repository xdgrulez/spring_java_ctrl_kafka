package ch.mgb.tdi.kafka.test.springjavactrlkafka;

import io.github.javactrl.rt.Ctrl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    private final Workflow workflow;

    public Controller(Workflow workflow) {
        this.workflow = workflow;
    }

    @GetMapping("/workflows")
    public List<String> getWorkflows() {
        var readOnlyKeyValueStore = this.workflow.getReadOnlyKeyValueStore();
        var keyValueIterator =  readOnlyKeyValueStore.all();
        var keyStringList = new ArrayList<String>();
        while (keyValueIterator.hasNext()) {
            keyStringList.add(keyValueIterator.next().key);
        }
        return keyStringList;
    }

    @GetMapping("/workflows/{id}")
    public Long getWorkflow(@PathVariable String id) {
        var readOnlyKeyValueStore = this.workflow.getReadOnlyKeyValueStore();
        return readOnlyKeyValueStore.get(id);
    }
}
