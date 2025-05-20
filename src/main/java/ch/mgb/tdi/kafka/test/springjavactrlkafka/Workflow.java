package ch.mgb.tdi.kafka.test.springjavactrlkafka;

import io.github.javactrl.kafka.WorkflowProcessorSupplier;
import io.github.javactrl.rt.CThrowable;
import io.github.javactrl.rt.Ctrl;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static io.github.javactrl.kafka.Workflow.*;
import static org.apache.kafka.streams.StreamsConfig.*;

@Ctrl
@Component
public class Workflow {
    private KafkaStreams kafkaStreams;
    private static final Serde<String> STRING_SERDE = Serdes.String();

    public static void workflow(final String parameterString) throws CThrowable {
        final var stepVar1 = newVar("step1");
        final var inputString1 = await(stepVar1);
        final var inputInt1 = Integer.parseInt(inputString1);
        final var resultInt1 = inputInt1 + 1;
        System.out.println("This code is only run once per Workflow execution :-");
        //
        final var stepVar2 = newVar("step2");
        await(stepVar2);
        final var resultInt2 = resultInt1 * 2;
        //
        forward("result", String.valueOf(resultInt2));
    }

    @Bean
    public KafkaStreams kafkaStreams() {
        var topology = createTopology();
        System.out.println(topology.describe());

        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(APPLICATION_ID_CONFIG, "minimal-app");
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass().getName());
        props.put(STATE_DIR_CONFIG, "/tmp/kafka-streams");

        this.kafkaStreams = new KafkaStreams(topology, props);
        this.kafkaStreams.start();
        return this.kafkaStreams;
    }

    private Topology createTopology() {
        final var topology = new Topology();
        topology.addSource("Loop", "minimal-resume")
                .addProcessor("Process", new WorkflowProcessorSupplier(Workflow::workflow), "Loop")
                .addSink("result", "minimal-result", "Process");
        return topology;
    }

    public ReadOnlyKeyValueStore<String, Long> getReadOnlyKeyValueStore() {
        return this.kafkaStreams.store(StoreQueryParameters.fromNameAndType("store", QueryableStoreTypes.keyValueStore()));
    }

    @EventListener(ContextClosedEvent.class)
    public void stopKafkaStreams() {
        if (this.kafkaStreams != null) {
            this.kafkaStreams.close();
        }
    }
}
