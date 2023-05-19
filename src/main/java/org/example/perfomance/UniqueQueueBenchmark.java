package org.example.perfomance;

import org.example.collections.UniqueQueue;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10)
public class UniqueQueueBenchmark {
    @State(Scope.Thread)
    public static class MyState {
        Queue<Employee> employees = new UniqueQueue<>();
        long iterations = 1000;
        Employee employee = new Employee(100L, "Harry");

        @Setup(Level.Trial)
        public void setUp() {
            for (long i = 0; i < iterations; i++) {
                employees.add(new Employee(i, "John"));
            }
        }
    }

    @Benchmark
    public boolean testAdd(UniqueQueueBenchmark.MyState state) {
        return state.employees.add(state.employee);
    }

    @Benchmark
    public Boolean testContains(UniqueQueueBenchmark.MyState state) {
        return state.employees.contains(state.employee);
    }

    @Benchmark
    public boolean testRemoveSpecificObject(UniqueQueueBenchmark.MyState state) {
        return state.employees.remove(state.employee);
    }

    @Benchmark
    public boolean testPoll(UniqueQueueBenchmark.MyState state) {
        return state.employees.poll() != null;
    }


    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
            .include(UniqueQueueBenchmark.class.getSimpleName()).threads(1)
            .forks(1).shouldFailOnError(true)
            .shouldDoGC(true)
            .jvmArgs("-server").build();
        new Runner(options).run();
    }
}
